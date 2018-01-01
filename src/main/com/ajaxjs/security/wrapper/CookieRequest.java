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
package com.ajaxjs.security.wrapper;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import com.ajaxjs.security.ListControl;

/**
 * 输入输出 Cookie 白名单验证过滤。
 * 
 * @author Frank
 *
 */
public class CookieRequest extends HttpServletRequestWrapper {
	public static ListControl delegate = new ListControl();

	public CookieRequest(HttpServletRequest request) {
		super(request);
	}
	@Override
	public String getParameter(String key) {
		return super.getParameter(key);
	}

	@Override
	public Cookie[] getCookies() {
		Cookie[] cookies = super.getCookies();

		if (cookies == null || cookies.length == 0)
			return null;

		List<Cookie> cookieList = new ArrayList<>();
		for (Cookie cookie : cookies) {
			// for (String name : SecurityConfigLoader.cookieWhiteList) {
			// if (name.equalsIgnoreCase(cookie.getName()))
			// cookieList.add(cookie);
			// }

			if (delegate.isInWhiteList(cookie.getName())) {
				cookieList.add(cookie);
			}
		}

		return cookieList.toArray(new Cookie[cookieList.size()]);
	}

	public String getCookieByName(String name) {
		Cookie[] cookies = getCookies();
		for (Cookie cookie : cookies) {
			if (name.equals(cookie.getName())) {
				return cookie.getValue();
			}
		}
		return null;
	}
}
