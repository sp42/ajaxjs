package com.ajaxjs.util.websecurity.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;


import com.ajaxjs.core.Util;
import com.ajaxjs.util.websecurity.ISecurityFilter;
import com.ajaxjs.util.websecurity.SecurityConstant;

/**
 * cookie白名单过滤,输入输出cookie白名单验证过滤
 * 
 * @author weijian.zhongwj
 * 
 */
public class CookieWhiteListFilter implements ISecurityFilter {
	@Override
	public void doFilterInvoke(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		request = new CookieWhiteFilterHttpServletRequest(request);
	}

	public class CookieWhiteFilterHttpServletRequest extends HttpServletRequestWrapper {
		public CookieWhiteFilterHttpServletRequest(HttpServletRequest request) {
			super(request);
		}

		@Override
		public Cookie[] getCookies() {
			return filter(super.getCookies());
		}
	}
	
	/**
	 * 返回符合白名单的 cookie
	 * @param cookies
	 * @return
	 */
	private Cookie[] filter(Cookie[] cookies) {
		if (!Util.isNotNull(cookies)) return null;
		
		List<Cookie> cookieList = new ArrayList<>();
		for (Cookie cookie : cookies) {
			if (isInWhiteList(cookie)) 
				cookieList.add(cookie);
		}
		
		return cookieList.toArray(new Cookie[cookieList.size()]);
	}

	private boolean isInWhiteList(Cookie cookie) {
		if (cookie == null || cookie.getName() == null) 
			return false;
		
		for (String name : SecurityConstant.cookieWhiteList) {
			if (name.equalsIgnoreCase(cookie.getName()))
				return true;
		}
		
		return false;
	}

}
