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

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import com.ajaxjs.util.CommonUtil;

/**
 * 输入输出 Cookie 白名单验证过滤。
 * 
 * @author Frank
 *
 */
public class SecurityRequest extends HttpServletRequestWrapper {
	public static ListControl delegate = new ListControl();

	/**
	 * 
	 * @param request
	 */
	public SecurityRequest(HttpServletRequest request) {
		super(request);
	}

	@Override
	public Cookie[] getCookies() {
		Cookie[] cookies = super.getCookies();

		if (CommonUtil.isNull(cookies))
			return null;

		List<Cookie> cookieList = new ArrayList<>();
		for (Cookie cookie : cookies) {
			// for (String name : SecurityConfigLoader.cookieWhiteList) {
			// if (name.equalsIgnoreCase(cookie.getName()))
			// cookieList.add(cookie);
			// }

			if (delegate.isInWhiteList(cookie.getName()))
				cookieList.add(cookie);
		}

		return cookieList.toArray(new Cookie[cookieList.size()]);
	}

	/**
	 * 按照名称获取 Cookie 的值
	 * 
	 * @param name Cookie 的名称
	 * @return Cookie 的值
	 */
	public String getCookieByName(String name) {
		Cookie[] cookies = getCookies();

		for (Cookie cookie : cookies) {
			if (name.equals(cookie.getName()))
				return cookie.getValue();
		}

		return null;
	}

	@Override
	public String getParameter(String key) {
		key = XssChecker.clean(key, XssChecker.type_DELETE);
		return XssChecker.clean(super.getParameter(key));
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		return XssChecker.getParameterMap(super.getParameterMap());
	}

	@Override
	public Enumeration<String> getParameterNames() {
		return XssChecker.getParameterNames(super.getParameterNames());
	}

	@Override
	public String[] getParameterValues(String key) {
		return XssChecker.clean(super.getParameterValues(key));
	}
}
