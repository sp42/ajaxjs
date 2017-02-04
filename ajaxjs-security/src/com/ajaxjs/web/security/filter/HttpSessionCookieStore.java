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

import java.util.Enumeration;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.crypto.AesCipherService;

/**
 * session存储在cookie中
 * HttpSessionCookitStoreFilter是session存储到cookie的支持，encryKey加密密钥；
 * 
 * @author Frank
 * 
 */
public class HttpSessionCookieStore {

	private static final String sessionCookieName = "tmp_app";

	private static final char sep = (char) 1;
	private static final char sep2 = (char) 2;

	private String key;

	private HttpServletRequest httpServletRequest;

	private HttpServletResponse httpServletResponse;


	/**
	 * 
	 * @param httpServletRequest
	 * @param httpServletResponse
	 * @param key
	 */
	public HttpSessionCookieStore(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, String key) {
		this.httpServletRequest = httpServletRequest;
		this.httpServletResponse = httpServletResponse;
		this.key = key;
	}

	/**
	 * 从 cookie 中取出值放在 session 中
	 * @param session
	 */
	public void deseriable(HttpSession session) {
		Cookie[] cookies = httpServletRequest.getCookies();
		if (cookies == null || cookies.length <= 0) 
			return;
		
		AesCipherService aesCipherService = new AesCipherService();
		aesCipherService.setKeySize(64); // 设置key长度

		for (Cookie cookie : cookies) {// 遍历 cookie
			if (cookie.getName().equals(sessionCookieName)) {
				try {
					String value = aesCipherService.encrypt(cookie.getValue().getBytes(), key.getBytes()).toHex();
					String[] kvs = value.split(sep2 + "");
					
					if (kvs == null || kvs.length <= 0) 
						return;
					
					for (String kv : kvs) {
						String[] param = kv.split(sep + "");
						if (param == null || param.length != 2) 
							continue;
						
						session.setAttribute(param[0], param[1]);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 把 session 所有东西保存到 cookies
	 * @param session
	 */
	public void seriable(HttpSession session) {
		StringBuilder sb = new StringBuilder();
		
		Enumeration<String> enums = session.getAttributeNames();
		while (enums.hasMoreElements()) {
			String name = enums.nextElement();
			sb.append(name + sep + session.getAttribute(name) + sep2);
		}

		AesCipherService aesCipherService = new AesCipherService();
		aesCipherService.setKeySize(64); // 设置key长度

		String str = aesCipherService.encrypt(sb.toString().getBytes(), key.getBytes()).toHex();
		httpServletResponse.addCookie(new Cookie(sessionCookieName, str));
	}

}
