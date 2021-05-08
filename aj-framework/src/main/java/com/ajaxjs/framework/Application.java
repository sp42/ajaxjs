/**
 * 版权所有 2017 Sp42 frank@ajaxjs.com 根据 2.0 版本 Apache 许可证("许可证")授权；
 * 根据本许可证，用户可以不使用此文件。 用户可从下列网址获得许可证副本：
 * http://www.apache.org/licenses/LICENSE-2.0
 * 除非因适用法律需要或书面同意，根据许可证分发的软件是基于"按原样"基础提供，
 * 无任何明示的或暗示的保证或条件。详见根据许可证许可下，特定语言的管辖权限和限制。
 */
package com.ajaxjs.framework;

import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.regex.Pattern;

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
import com.ajaxjs.framework.config.SetStartupCtx;
import com.ajaxjs.framework.config.SiteStruService;
import com.ajaxjs.user.filter.UserAdminFilter;
import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.util.ReflectUtil;
import com.ajaxjs.util.ioc.ComponentMgr;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.web.mvc.MvcDispatcherBase;

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

		try { // 没配置的时候，忽略异常，否则启动报错
			ComponentMgr.inject();
		} catch (Throwable e1) {
			LOGGER.warning(e1);
		}

		onServletStartUp.add(SetStartupCtx.INITIALIZED);
		onServletStartUp.add(SiteStruService.INITIALIZED);
		onServletStartUp.add(MvcDispatcherBase::init);
		onServletStartUp.forEach(action -> action.accept(ctx));

		onRequest.add(UserAdminFilter.CHECK_ADMIN);
		onRequest.add(MvcDispatcherBase.DISPATCHER);
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

	/**
	 * 字符串判断是否静态文件
	 */
	private static final Pattern IS_STATIC = Pattern.compile("\\.jpg|\\.png|\\.gif|\\.js|\\.css|\\.less|\\.ico|\\.jpeg|\\.htm|\\.swf|\\.txt|\\.mp4|\\.flv");

	/**
	 * 检查是否静态资源。Check the url if there is static asset.
	 */
	private static final BiFunction<HttpServletRequest, FilterChain, Boolean> STATIC_RESOURCE = (req, chain) -> IS_STATIC.matcher(req.getRequestURI()).find();

	@Override
	public void doFilter(ServletRequest _req, ServletResponse _resp, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) _req;
		HttpServletResponse resp = (HttpServletResponse) _resp;

		if (STATIC_RESOURCE.apply(req, chain)) {
			chain.doFilter(req, resp);
			return;
		}

		for (int i = 0; i < onRequest.size(); i++) {
			if (!onRequest.get(i).apply(req, resp))
				return;
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
