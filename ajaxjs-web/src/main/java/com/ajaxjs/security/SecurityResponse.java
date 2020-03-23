/**
 * Copyright 2015 Sp42 frank@ajaxjs.com
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
package com.ajaxjs.security;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import com.ajaxjs.config.ConfigService;
import com.ajaxjs.util.CommonUtil;

/**
 * 检测 CRLF 的过滤器
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class SecurityResponse extends HttpServletResponseWrapper {
	public static SecurityFilter delegate = new SecurityFilter();

	/**
	 * 创建一个 SecurityResponse 实例。
	 * 
	 * @param response 响应对象
	 */
	public SecurityResponse(HttpServletResponse response) {
		super(response);
	}

	@Override
	public void addCookie(Cookie cookie) {
		// 如果 cookie 名称包含 CLRF 则抛出异常；接着过滤 cookie 内容
		String name = cookie.getName(), value = cookie.getValue();
		boolean isCRLF_Filter = ConfigService.getValueAsBool("security.isCRLF_Filter");

		if (isCRLF_Filter && containCLRF(name))
			throw new SecurityException("Cookie 名称不能包含 CLRF 字符，该 cookie 是 ：" + name);

		// 重新创建 cookie
		Cookie newCookie = new Cookie(name, isCRLF_Filter ? filterCLRF(value) : value);// 已经过滤好的 Cookie
																						// value
		newCookie.setComment(cookie.getComment());

		if (cookie.getDomain() != null)
			newCookie.setDomain(cookie.getDomain());

		newCookie.setHttpOnly(cookie.isHttpOnly());
		newCookie.setMaxAge(cookie.getMaxAge());
		newCookie.setPath(cookie.getPath());
		newCookie.setSecure(cookie.getSecure());
		newCookie.setVersion(cookie.getVersion());

		/*
		 * 检查 Cookie 容量大小和是否在白名单中。
		 */
		if (ConfigService.getValueAsBool("security.isCookiesSizeCheck")
				&& (cookie.getValue().length() > MAX_COOKIE_SIZE))
			throw new SecurityException("超出 Cookie 允许容量：" + MAX_COOKIE_SIZE);

		if (!delegate.isInWhiteList(cookie.getName()))
			throw new SecurityException("cookie: " + cookie.getName() + " 不在白名单中，添加无效！");

		super.addCookie(newCookie);
	}

	@Override
	public void setDateHeader(String name, long date) {
		if (ConfigService.getValueAsBool("security.isCRLF_Filter"))
			super.setDateHeader(filterCLRF(name), date);
		else
			super.setDateHeader(name, date);
	}

	@Override
	public void setIntHeader(String name, int value) {
		if (ConfigService.getValueAsBool("security.isCRLF_Filter"))
			super.setIntHeader(filterCLRF(name), value);
		else
			super.setIntHeader(name, value);
	}

	@Override
	public void addHeader(String name, String value) {
		if (ConfigService.getValueAsBool("security.isXXS_Filter"))
			value = SecurityRequest.clean(value);

		if (ConfigService.getValueAsBool("security.isCRLF_Filter"))
			super.addHeader(filterCLRF(name), filterCLRF(value));
		else
			super.addHeader(name, value);
	}

	@Override
	public void setHeader(String name, String value) {
		if (ConfigService.getValueAsBool("security.isXXS_Filter"))
			value = SecurityRequest.clean(value);

		if (ConfigService.getValueAsBool("security.isCRLF_Filter"))
			super.setHeader(filterCLRF(name), filterCLRF(value));
		else
			super.setHeader(name, value);
	}

	/**
	 * 过滤 \r 、\n之类的换行符
	 * 
	 * @param value
	 * @return
	 */
	private static String filterCLRF(String value) {
		if (CommonUtil.isEmptyString(value))
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
	 * 
	 * @param name
	 * @return
	 */
	private static boolean containCLRF(String name) {
		if (CommonUtil.isEmptyString(name))
			return false;

		for (int i = 0; i < name.length(); i++) {
			if (name.charAt(i) == '\r' || name.charAt(i) == '\n')
				return true;
		}

		return false;
	}

	/**
	 * Cookie 最大容量
	 */
	private static final int MAX_COOKIE_SIZE = 4 * 1024;

	// 对 Response 的 setStatus(int sc, String value) 方法 sm 错误信息进行 XSS 过滤；
	@SuppressWarnings("deprecation")
	@Override
	public void setStatus(int sc, String value) {
		if (ConfigService.getValueAsBool("security.isXXS_Filter"))
			super.setStatus(sc, SecurityRequest.clean(value));
		else
			super.setStatus(sc, value);
	}
}
