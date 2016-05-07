package com.ajaxjs.web.security;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * @author weijian.zhongwj
 *
 */
public class SecurityHttpServletResponse extends HttpServletResponseWrapper {
	private static final int MAX_COOKIE_SIZE = 4 * 1024;

	private int length = 0;

	public SecurityHttpServletResponse(HttpServletResponse response) {
		super(response);
	}

	@Override
	public void addCookie(Cookie cookie) {
		if (length + cookie.getValue().length() > MAX_COOKIE_SIZE)
			return;

		if (!isInWhiteList(cookie))
			throw new RuntimeException("cookie:" + cookie.getName() + " is not in whitelist,not valid.");

		super.addCookie(checkCookie(cookie));
		length += cookie.getValue().length();
	}

	@Override
	public void setDateHeader(String name, long date) {
		super.setDateHeader(filterCLRF(name), date);
	}

	@Override
	public void setIntHeader(String name, int value) {
		super.setIntHeader(filterCLRF(name), value);
	}

	@Override
	public void addHeader(String name, String value) {
		super.addHeader(filterCLRF(name), SecurityHttpServletRequest.xssFilter(filterCLRF(value), null));
	}

	@Override
	public void setHeader(String name, String value) {
		super.setHeader(filterCLRF(name), SecurityHttpServletRequest.xssFilter(filterCLRF(value), null));
	}

	@Override
	public void sendRedirect(String location) throws IOException {
		if (!checkRedirectValid(location)) {
			throw new RuntimeException("redirect location " + location + " is not valid.");
		}
		super.sendRedirect(location);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void setStatus(int sc, String sm) {
		super.setStatus(sc, SecurityHttpServletRequest.xssFilter(sm, null));
	}

	private boolean isInWhiteList(Cookie cookie) {
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

	public static Cookie checkCookie(Cookie inputCookie) {
		if (inputCookie == null)
			return null;

		String name = inputCookie.getName();
		String value = inputCookie.getValue();

		if (containCLRF(name))
			throw new RuntimeException("cookie name could not contain CLRF " + name);

		String newValue = filterCLRF(value);
		Cookie newCookie = new Cookie(name, newValue);
		newCookie.setComment(inputCookie.getComment());

		if (inputCookie.getDomain() != null)
			newCookie.setDomain(inputCookie.getDomain());

		newCookie.setHttpOnly(inputCookie.isHttpOnly());
		newCookie.setMaxAge(inputCookie.getMaxAge());
		newCookie.setPath(inputCookie.getPath());
		newCookie.setSecure(inputCookie.getSecure());
		newCookie.setVersion(inputCookie.getVersion());

		return newCookie;
	}

	private static boolean containCLRF(String name) {
		if (name == null || name.isEmpty())
			return false;

		for (int i = 0; i < name.length(); i++) {
			if (name.charAt(i) == '\r' || name.charAt(i) == '\n')
				return true;
		}
		return false;
	}

	public static String filterCLRF(String value) {
		if (value == null || value.isEmpty())
			return value;

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < value.length(); i++) {
			if (!(value.charAt(i) == '\r' || value.charAt(i) == '\n'))
				sb.append(value.charAt(i));
		}
		return sb.toString();
	}

	public static boolean checkRedirectValid(String location) {
		if (location == null || location.isEmpty())
			return false;

		for (Pattern pattern : SecurityFilter.redirectLocationWhiteList) {
			if (pattern.matcher(location).find())
				return true;
		}

		return false;
	}
}
