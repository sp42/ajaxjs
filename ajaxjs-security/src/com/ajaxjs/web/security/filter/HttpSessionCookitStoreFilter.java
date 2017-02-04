package com.ajaxjs.web.security.filter;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


/**
 * session存储在cookie中
 * HttpSessionCookitStoreFilter是session存储到cookie的支持，encryKey加密密钥；
 * 
 * @author weijian.zhongwj
 * 
 */
public class HttpSessionCookitStoreFilter implements Filter {
	
	private static final String sessionCookieName = "tmp_app";

	private static final char sep = (char) 1;
	private static final char sep2 = (char) 2;
	
	public static String key;

	@Override
	public void init(FilterConfig config) throws ServletException {
		key = config.getInitParameter("encryKey");
		if (key == null || key.length() != 16)
			throw new ServletException("encrykey(" + key + ") length must be 16");
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		deseriable(httpRequest, key);
		
		filterChain.doFilter(request, response);
		
		seriable(httpRequest, httpResponse, key);
	}
	
	private static void deseriable(HttpServletRequest request, String key) {
		Cookie[] cookies = request.getCookies();
		if (cookies == null || cookies.length == 0) {
			return;
		}

//		AesCipherService aesCipherService = new AesCipherService();
//		aesCipherService.setKeySize(64); // 设置key长度

		for (Cookie cookie : cookies) {// 遍历 cookie
			if (cookie.getName().equals(sessionCookieName)) {
				try {
					String value = aesCipherService.encrypt(cookie.getValue().getBytes(), key.getBytes()).toHex();
					String[] kvs = value.split(sep2 + "");
					if (kvs == null || kvs.length == 0) {
						return;
					}
					for (String kv : kvs) {
						String[] param = kv.split(sep + "");
						if (param == null || param.length == 0 || param.length != 2) {
							continue;
						}
//						session.setAttribute(param[0], param[1]);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 把 session 所有东西保存到 cookies
	 * @param session
	 */
	private static void seriable(HttpServletRequest request, HttpServletResponse response, String key) {
		StringBuilder sb = new StringBuilder();
		HttpSession session = request.getSession();
		Enumeration<String> enums = session.getAttributeNames();
		
		while (enums.hasMoreElements()) {
			String name = enums.nextElement();
			sb.append(name + sep + session.getAttribute(name) + sep2);
		}

//		AesCipherService aesCipherService = new AesCipherService();
//		aesCipherService.setKeySize(64); // 设置key长度
//		String encrypt = aesCipherService.encrypt(sb.toString().getBytes(), key.getBytes()).toHex();
		
//		response.addCookie(new Cookie(sessionCookieName, encrypt));
	}
	
	@Override
	public void destroy() {}
 
}
