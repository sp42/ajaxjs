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
package com.ajaxjs.security.web;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.springframework.beans.factory.annotation.Autowired;

import com.ajaxjs.security.web.checker.CRLF;
import com.ajaxjs.security.web.checker.Xss;
import com.ajaxjs.util.config.EasyConfig;

/**
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class SecurityResponse extends HttpServletResponseWrapper {
	@Autowired
	private EasyConfig config;

	@Autowired
	private CRLF crlfChecker;

	@Autowired
	private Xss xxsChecker;

	private final static String IS_ENABLE_CRLF = "webSecurity.isCRLF_Filter";

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
		if (config.getBol(IS_ENABLE_CRLF)) {
			String name = cookie.getName(), value = cookie.getValue();
			Cookie newCookie = new Cookie(crlfChecker.clean(name), crlfChecker.clean(value));

			if (cookie.getDomain() != null)
				newCookie.setDomain(cookie.getDomain());

			newCookie.setComment(cookie.getComment());
			newCookie.setHttpOnly(cookie.isHttpOnly());
			newCookie.setMaxAge(cookie.getMaxAge());
			newCookie.setPath(cookie.getPath());
			newCookie.setSecure(cookie.getSecure());
			newCookie.setVersion(cookie.getVersion());

			/*
			 * 检查 Cookie 容量大小和是否在白名单中。
			 */
			if (config.getBol("security.isCookiesSizeCheck") && (cookie.getValue().length() > MAX_COOKIE_SIZE))
				throw new SecurityException("超出 Cookie 允许容量：" + MAX_COOKIE_SIZE);

//		if (!delegate.isInWhiteList(cookie.getName()))
//			throw new SecurityException("cookie: " + cookie.getName() + " 不在白名单中，添加无效！");

			super.addCookie(newCookie);
		} else
			super.addCookie(cookie);
	}

	/**
	 * Cookie 最大容量
	 */
	private static final int MAX_COOKIE_SIZE = 4 * 1024;

	@Override
	public void setDateHeader(String name, long date) {
		if (config.getBol(IS_ENABLE_CRLF))
			name = crlfChecker.clean(name);

		super.setDateHeader(name, date);
	}

	@Override
	public void setIntHeader(String name, int value) {
		if (config.getBol(IS_ENABLE_CRLF))
			name = crlfChecker.clean(name);

		super.setIntHeader(name, value);
	}

	@Override
	public void addHeader(String name, String value) {
		if (config.getBol(SecurityRequest.IS_ENABLE_XSS))
			value = xxsChecker.clean(value);

		if (config.getBol(IS_ENABLE_CRLF)) {
			name = crlfChecker.clean(name);
			value = crlfChecker.clean(value);
		}

		super.addHeader(name, value);
	}

	@Override
	public void setHeader(String name, String value) {
		if (config.getBol(SecurityRequest.IS_ENABLE_XSS))
			value = xxsChecker.clean(value);

		if (config.getBol(IS_ENABLE_CRLF)) {
			name = crlfChecker.clean(name);
			value = crlfChecker.clean(value);
		}

		super.setHeader(name, value);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void setStatus(int sc, String value) {
		if (config.getBol(SecurityRequest.IS_ENABLE_XSS))
			value = crlfChecker.clean(value);

		super.setStatus(sc, value);
	}

	@Override
	public void sendError(int sc, String value) throws IOException {
		if (config.getBol(SecurityRequest.IS_ENABLE_XSS))
			value = crlfChecker.clean(value);

		super.sendError(sc, value);
	}
}
