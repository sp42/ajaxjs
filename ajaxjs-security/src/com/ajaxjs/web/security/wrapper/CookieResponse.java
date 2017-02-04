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
package com.ajaxjs.web.security.wrapper;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import com.ajaxjs.web.security.ListControl;

/**
 * 检查 Cookie 容量大小和是否在白名单中。
 * 
 * @author Frank
 *
 */
public class CookieResponse extends HttpServletResponseWrapper {
	public ListControl delegate = new ListControl();

	private static final int MAX_COOKIE_SIZE = 4 * 1024;

	private int length = 0;

	public CookieResponse(HttpServletResponse response, List<String> whiteList) {
		super(response);
		delegate.whiteList = whiteList;
	}

	@Override
	public void addCookie(Cookie cookie) {
		if (length + cookie.getValue().length() > MAX_COOKIE_SIZE)
			throw new SecurityException("超出 Cookie 允许容量：" + MAX_COOKIE_SIZE);

		if (!delegate.isInWhiteList(cookie.getName()))
			throw new SecurityException("cookie:" + cookie.getName() + " 不在白名单中，添加无效！");
	}
}
