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
package com.ajaxjs.web.security.filter;

import javax.servlet.http.HttpServletRequest;

/**
 * 利用来路 Referer 请求头阻止"盗链"。true 表示为同域
 * 
 * @author Frank
 *
 */
public class RefererFilter extends SimpleFilter {
	/**
	 * 本站站名，应以 http 开头
	 */
	public String serverName;

	@Override
	public boolean check(HttpServletRequest request) {
		String referer = request.getHeader("referer");

		if (isEmptyStr(referer))
			return false; // 请求没有 referer 字段不通过

		if (serverName == null)
			serverName = request.getServerName();

		return referer.startsWith(serverName);
	}

}
