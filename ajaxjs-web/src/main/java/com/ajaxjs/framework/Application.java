package com.ajaxjs.framework;

import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ajaxjs.Version;
import com.ajaxjs.framework.config.ConfigService;
import com.ajaxjs.framework.config.ServletStartUp;
import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.util.ReflectUtil;
import com.ajaxjs.util.ioc.ComponentMgr;
import com.ajaxjs.util.logger.LogHelper;

@WebListener
@WebFilter(urlPatterns = "/*")
public class Application implements ServletContextListener, Filter {
	private static final LogHelper LOGGER = LogHelper.getLog(Application.class);

	public static List<Consumer<ServletContext>> onServletStartUp = new ArrayList<>();

	@Override
	public void contextInitialized(ServletContextEvent e) {
		ServletContext ctx = e.getServletContext();
		Version.tomcatVersionDetect(ctx.getServerInfo());

		// 加载配置
		ConfigService.load(ctx.getRealPath("/META-INF/site_config.json"));

		if (ConfigService.CONFIG != null && ConfigService.isLoaded)
			ctx.setAttribute("aj_allConfig", ConfigService.CONFIG); // 所有配置保存在这里

		onStartUp(ctx);

		String scanPackage = ConfigService.get("System.scanPackage");

		if (!CommonUtil.isEmptyString(scanPackage))
			ComponentMgr.scan(CommonUtil.split(ConfigService.get("System.scanPackage")));
		else
			LOGGER.warning("配置文件中每发现任何要扫描的包名，请检查配置文件");

		onServletStartUp.forEach(action -> action.accept(ctx));
	}

	/**
	 * Startup callback，外界可调用改方法进行刷新
	 */
	private static void onStartUp(ServletContext cxt) {
		String startUp_Class = ConfigService.getValueAsString("startUp_Class");

		if (!CommonUtil.isEmptyString(startUp_Class)) {
			LOGGER.info("执行 Servlet 初始化，启动回调[{0}]", startUp_Class);

			try {
				Class<ServletStartUp> clz = ReflectUtil.getClassByName(startUp_Class, ServletStartUp.class);

				if (clz != null) {
					ServletStartUp startUp = ReflectUtil.newInstance(clz);
					startUp.onStartUp(cxt);
				}
			} catch (Throwable e) {
				if (e instanceof UndeclaredThrowableException) {
					Throwable _e = ReflectUtil.getUnderLayerErr(e);
					LOGGER.warning(_e);
				}

				throw e;
			}
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}

	/*
	 * ------------------------ FILTER ---------------------------------
	 */
	public static List<BiFunction<HttpServletRequest, HttpServletResponse, Boolean>> onRequest = new ArrayList<>();
	public static List<BiFunction<HttpServletRequest, FilterChain, Boolean>> onRequest2 = new ArrayList<>();

	@Override
	public void doFilter(ServletRequest _req, ServletResponse _resp, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) _req;
		HttpServletResponse resp = (HttpServletResponse) _resp;

		for (int i = 0; i < onRequest2.size(); i++) {
			if (onRequest2.get(i).apply(req, chain)) {
				chain.doFilter(req, resp);
				return;
			}
		}

		for (int i = 0; i < onRequest.size(); i++) {
			if (!onRequest.get(i).apply(req, resp)) {
				return;
			}
		}

		chain.doFilter(req, resp);
	}

	@Override
	public void init(FilterConfig fConfig) {
	}

	@Override
	public void destroy() {
	}
}
