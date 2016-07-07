package com.ajaxjs.web.security;

import java.io.IOException;
 
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author weijian.zhongwj
 *
 */
public class SecurityFilter implements Filter {
	public static final List<Pattern> redirectLocationWhiteList = new ArrayList<>();
	public static final List<String> whitefilePostFixList = new ArrayList<>();
	public static final List<String> onlyPostUrlList = new ArrayList<>();
	
	/**
	 * 白名单
	 */
	public static final List<String> cookieWhiteList = new ArrayList<>();
	
	/**
	 * 程序入口。启动过滤器，加载配置
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// 初始化过滤器白名单
		loadParams(filterConfig, "cookieWhiteList", 	 cookieWhiteList);
		loadParams(filterConfig, "whitefilePostFixList", whitefilePostFixList);
		loadParams(filterConfig, "onlyPostUrlList", 	 onlyPostUrlList);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		
		SecurityRequest security =  new SecurityRequest(httpRequest);
		try {
			security.formPostPermitCheck();
			security.checkCsrfToken();
		} catch (SecurityException e) {
			return;
		}
		
		filterChain.doFilter(security, new SecurityResponse(httpResponse));
	}

	/**
	 * 读取配置参数
	 * 
	 * @param filterConfig
	 *            过滤器全部配置，可以从 XML 配置文件中获得
	 * @param configName
	 *            指定哪个的配置
	 * @param list
	 *            配置值要保存到列表
	 */
	private void loadParams(FilterConfig filterConfig, String configName, List<String> list) {
		String paramList = filterConfig.getInitParameter(configName);

		if (paramList != null) {
			String[] arr = paramList.split(",");
			list.addAll(Arrays.asList(arr));
		}
	}

//	public void initRedictWhiteList(FilterConfig filterConfig) throws ServletException {
//		String list = filterConfig.getInitParameter("redirectWhiteList");
//		if (list == null || list.isEmpty()) {
//			return;
//		}
//		String[] redirectWhiteList = list.split(",");
//		List<Pattern> patterns = new ArrayList<>();
//		for (String str : redirectWhiteList) {
//			patterns.add(Pattern.compile(str));
//		}
//		SecurityConstant.redirectLocationWhiteList.addAll(patterns);
//	}
	 
	

	@Override
	public void destroy() {}
	
}
