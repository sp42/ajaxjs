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

import java.io.IOException;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import com.ajaxjs.web.security.ConfigLoader;

/**
 * @author Frank
 *
 */
public class XSS_Response extends HttpServletResponseWrapper {
	public XSS_Response(HttpServletResponse response) {
		super(response);
	}

	@Override
	public void addHeader(String name, String value) {
		super.addHeader(name, XSS_Request.xssFilter(value, null));
	}

	@Override
	public void setHeader(String name, String value) {
		super.setHeader(name, XSS_Request.xssFilter(value, null));
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
		super.setStatus(sc, XSS_Request.xssFilter(sm, null));
	}

	private static boolean checkRedirectValid(String location) {
		if (location == null || location.isEmpty())
			return false;

		for (Pattern pattern : ConfigLoader.redirectLocationWhiteList) {
			if (pattern.matcher(location).find())
				return true;
		}

		return false;
	}
}
