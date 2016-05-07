package com.ajaxjs.web.security;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ajaxjs.util.StringUtil;

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

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		
		formPostPermitCheck(httpRequest);
		checkCsrfToken(httpRequest);
		
		filterChain.doFilter(new SecurityHttpServletRequest(httpRequest), new SecurityHttpServletResponse(httpResponse));
	}

	/**
	 * 启动过滤器，加载配置
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// 初始化过滤器白名单
		loadParams(filterConfig, "cookieWhiteList", 	 cookieWhiteList);
		loadParams(filterConfig, "whitefilePostFixList", whitefilePostFixList);
		loadParams(filterConfig, "onlyPostUrlList", 	 onlyPostUrlList);
	}

	/**
	 * 读取配置参数
	 * 
	 * @param filterConfig
	 * @param param
	 * @param list
	 */
	private void loadParams(FilterConfig filterConfig, String param, List<String> list) {
		String paramList = filterConfig.getInitParameter(param);

		if (!StringUtil.isEmptyString(paramList)) {
			String[] cookieList = paramList.split(",");
			list.addAll(Arrays.asList(cookieList));
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

 
	//////////////////////////////////////////////////////////////////////////////
	
	/**
	 * 只允许 post 提交的 url 列表，需要配置 onlyPostUrlList 参数
	 * 
	 * @param request
	 */
	private void formPostPermitCheck(HttpServletRequest request) {
		String uri = request.getRequestURI(),
			   method = request.getRequestURI();
		
		if (!Valid(uri, method)) {
			throw new RuntimeException("this requestUrI : " + uri + " only permit post, but now is " + method);
		}
	}

	private boolean Valid(String requestURI, String method) {
		if (!"POST".equalsIgnoreCase(method)) {
			for (String patternUri : SecurityFilter.onlyPostUrlList) {
				if (Pattern.matches(patternUri, requestURI))
					return false;
			}
		}
		return true;
	}
	
	//////////////////////////////////////////////////////////////////////////////
	
	/*
	 *  对post表单提交进行csrf token验证；
	 *  使用CsrfTokenIdCreator生成csrf tokenid后放入表单还有session中，key名称必须为csrf_开头；
	 *  为了支持多个form表单
	 */
	private static final String CSRFTOKEN_PREFIX = "csrf_";

	private void checkCsrfToken(HttpServletRequest httpRequest) {
		if (httpRequest.getMethod().equals("POST")) {
			String csrfTokenKey = getTokenName(httpRequest);
			
			long csrfTokenId = (Long) httpRequest.getSession().getAttribute(csrfTokenKey);
			long paramCsrfToken = Long.parseLong(httpRequest.getParameter(csrfTokenKey));
			
			if (csrfTokenId != paramCsrfToken)
				throw new RuntimeException("post method csrf token not valid.");
		}
	}

	/**
	 * 获取 csrf_开头的 key
	 * @param httpRequest
	 * @return
	 */
	private String getTokenName(HttpServletRequest httpRequest) {
		Iterator<Entry<String, String[]>> iter = httpRequest.getParameterMap().entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, String[]> entry = iter.next();
			if (entry.getKey().startsWith(CSRFTOKEN_PREFIX))
				return entry.getKey();
		}

		return null;
	}

	/**
	 * 使用CsrfTokenIdCreator生成csrf tokenid后放入表单
	 * @param session
	 * @return
	 */
	public static String getCsrfTokenId(HttpSession session) {
		String str = session.getCreationTime() + session.getId();
		
		try {
			return new String(MessageDigest.getInstance("MD5").digest(str.getBytes()));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void destroy() {}
}
