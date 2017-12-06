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
package com.ajaxjs.security.wrapper;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * 检测 CLRF 的过滤器
 * @author Frank
 *
 */
public class CLRF_Response extends HttpServletResponseWrapper {
	public CLRF_Response(HttpServletResponse response) {
		super(response);
	}

	@Override
	public void addCookie(Cookie cookie) {
		// 如果 cookie 名称包含 CLRF 则抛出异常；接着过滤 cookie 内容
		String name = cookie.getName(), value = cookie.getValue();
	
		if (containCLRF(name))
			throw new SecurityException("Cookie 名称不能包含 CLRF 字符，该 cookie 是 ：" + name);

		// 重新创建 cookie
		Cookie newCookie = new Cookie(name, filterCLRF(value));// 已经过滤好的 Cookie value
		newCookie.setComment(cookie.getComment());
	
		if (cookie.getDomain() != null)
			newCookie.setDomain(cookie.getDomain());
	
		newCookie.setHttpOnly(cookie.isHttpOnly());
		newCookie.setMaxAge(cookie.getMaxAge());
		newCookie.setPath(cookie.getPath());
		newCookie.setSecure(cookie.getSecure());
		newCookie.setVersion(cookie.getVersion());
		
		super.addCookie(newCookie);
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
		super.addHeader(filterCLRF(name), filterCLRF(value));
	}

	@Override
	public void setHeader(String name, String value) {
		super.setHeader(filterCLRF(name), filterCLRF(value));
	}

	/**
	 * 过滤  \r 、\n之类的换行符
	 * @param value
	 * @return
	 */
	private static String filterCLRF(String value) {
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
