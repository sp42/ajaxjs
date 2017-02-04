/**
 * Copyright 2015 Frank Cheung
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ajaxjs.web.security.in_out_filter;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import com.ajaxjs.web.security.SecurityConfigLoader;

/**
 * 检测 CLRF 的过滤器
 * @author Frank
 *
 */
public class CLRF_Filter_Response extends HttpServletResponseWrapper {
	private static final int MAX_COOKIE_SIZE = 4 * 1024;

	private int length = 0;

	public CLRF_Filter_Response(HttpServletResponse response) {
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
		super.addHeader(CLRF.filterCLRF(name), CLRF.filterCLRF(value));
	}

	@Override
	public void setHeader(String name, String value) {
		super.setHeader(CLRF.filterCLRF(name), CLRF.filterCLRF(value));
	}

	@Override
	public void sendRedirect(String location) throws IOException {
		if (!checkRedirectValid(location)) {
			throw new RuntimeException("跳转地址 " + location + " 非法，因为不在白名单中。");
		}
		super.sendRedirect(location);
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
		for (String name : SecurityConfigLoader.cookieWhiteList) {
			if (name.equalsIgnoreCase(cookie.getName())) {
				return true;
			}
		}
		return false;
	}

	private static boolean checkRedirectValid(String location) {
		if (location == null || location.isEmpty())
			return false;

		for (Pattern pattern : SecurityConfigLoader.redirectLocationWhiteList) {
			if (pattern.matcher(location).find())
				return true;
		}

		return false;
	}
	
	/**
	 * 如果 cookie 名称包含 CLRF 则抛出异常；接着过滤 cookie 内容
	 * @param inputCookie
	 * @return
	 */
	public static Cookie checkCookie(Cookie inputCookie) {
		if (inputCookie == null)
			return null;
	
		String name = inputCookie.getName(), value = inputCookie.getValue();
	
		if (containCLRF(name))
			throw new SecurityException("Cookie 名称不能包含 CLRF 字符，该 cookie 是 ：" + name);

		// 重新创建 cookie
		Cookie newCookie = new Cookie(name, filterCLRF(value));// 已经过滤好的 Cookie value
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

	/**
	 * 过滤  \r 、\n之类的换行符
	 * @param value
	 * @return
	 */
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

	/**
	 * 是否包含 \r 、\n之类的换行符
	 * @param name
	 * @return
	 */
	private static boolean containCLRF(String name) {
		if (name == null || name.isEmpty())
			return false;
	
		for (int i = 0; i < name.length(); i++) {
			if (name.charAt(i) == '\r' || name.charAt(i) == '\n')
				return true;
		}
		
		return false;
	}
}
