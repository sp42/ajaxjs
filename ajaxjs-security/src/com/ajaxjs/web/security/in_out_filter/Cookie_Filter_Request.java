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

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import com.ajaxjs.web.security.ListControl;

/**
 * 
 * @author Frank
 *
 */
public class Cookie_Filter_Request extends HttpServletRequestWrapper {
	private ListControl delegate = new ListControl();

	public Cookie_Filter_Request(HttpServletRequest request, List<String> whiteList) {
		super(request);
		
		delegate.whiteList = whiteList;
	}

	/**
	 * 返回符合白名单的 cookie 输入输出cookie白名单验证过滤
	 */
	@Override
	public Cookie[] getCookies() {
		Cookie[] cookies = super.getCookies();

		if (cookies == null || cookies.length == 0)
			return null;

		List<Cookie> cookieList = new ArrayList<>();
		for (Cookie cookie : cookies) {
//			for (String name : SecurityConfigLoader.cookieWhiteList) {
//				if (name.equalsIgnoreCase(cookie.getName()))
//					cookieList.add(cookie);
//			}
			
			if (delegate.isInWhiteList(cookie.getName())) {
				cookieList.add(cookie);
			}
		}

		return cookieList.toArray(new Cookie[cookieList.size()]);
	}
}
