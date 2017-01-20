package com.ajaxjs.web.security;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import com.ajaxjs.web.security.filter.CLRF;
import com.ajaxjs.web.security.filter.XSS;

/**
 * @author weijian.zhongwj
 *
 */
public class SecurityResponse extends HttpServletResponseWrapper {
	private static final int MAX_COOKIE_SIZE = 4 * 1024;

	private int length = 0;

	public SecurityResponse(HttpServletResponse response) {
		super(response);
	}

	@Override
	public void addCookie(Cookie cookie) {
		if (length + cookie.getValue().length() > MAX_COOKIE_SIZE)
			return;

		if (!isInWhiteList(cookie))
			throw new RuntimeException("cookie:" + cookie.getName() + " is not in whitelist,not valid.");

		try {
			super.addCookie(CLRF.checkCookie(cookie));
			length += cookie.getValue().length();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setDateHeader(String name, long date) {
		super.setDateHeader(CLRF.filterCLRF(name), date);
	}

	@Override
	public void setIntHeader(String name, int value) {
		super.setIntHeader(CLRF.filterCLRF(name), value);
	}

	@Override
	public void addHeader(String name, String value) {
		super.addHeader(CLRF.filterCLRF(name), XSS.xssFilter(CLRF.filterCLRF(value), null));
	}

	@Override
	public void setHeader(String name, String value) {
		super.setHeader(CLRF.filterCLRF(name), XSS.xssFilter(CLRF.filterCLRF(value), null));
	}

	@Override
	public void sendRedirect(String location) throws IOException {
		if (!checkRedirectValid(location)) {
			throw new RuntimeException("跳转地址 " + location + " 非法，因为不在白名单中。");
		}
		super.sendRedirect(location);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void setStatus(int sc, String sm) {
		super.setStatus(sc, XSS.xssFilter(sm, null));
	}

	/**
	 * Cookie 名称是否在白名单中
	 * @param cookie
	 * @return
	 */
	private static boolean isInWhiteList(Cookie cookie) {
		if (cookie == null || cookie.getName() == null) {
			return false;
		}
		for (String name : SecurityFilter.cookieWhiteList) {
			if (name.equalsIgnoreCase(cookie.getName())) {
				return true;
			}
		}
		return false;
	}

	private static boolean checkRedirectValid(String location) {
		if (location == null || location.isEmpty())
			return false;

		for (Pattern pattern : SecurityFilter.redirectLocationWhiteList) {
			if (pattern.matcher(location).find())
				return true;
		}

		return false;
	}
}
