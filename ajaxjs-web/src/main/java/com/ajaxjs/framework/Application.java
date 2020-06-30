package com.ajaxjs.framework;

import java.io.IOException;
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

import com.ajaxjs.util.ReflectUtil;
import com.ajaxjs.util.ioc.EveryClass;
import com.ajaxjs.util.logger.LogHelper;

@WebListener
@WebFilter(urlPatterns = "/*")
public class Application implements ServletContextListener, Filter {
	private static final LogHelper LOGGER = LogHelper.getLog(Application.class);

	public static List<Consumer<ServletContext>> onServletStartUp = new ArrayList<>();

	@Override
	public void contextInitialized(ServletContextEvent e) {
		LOGGER.info("程序启动中");
		onServletStartUp.forEach(action -> action.accept(e.getServletContext()));
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

//		System.out.println("--------doFilter--------");
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
