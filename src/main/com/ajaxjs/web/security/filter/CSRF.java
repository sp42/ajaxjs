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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.ajaxjs.web.security.ListControl;

/**
 * CSRF 攻击：采用 tokenID 防御支持
 * 对 POST 表单提交进行 CSRF token 验证； 使用 getCsrfTokenId() 然后放入表单还有 session 中，key 名称必须为 csrf_ 开头。
 * 
 * @author Frank
 *
 */
public class CSRF extends ListControl implements SecurityFilter {
	private static final String CSRFTOKEN_PREFIX = "csrf_";

	@Override
	public boolean check(HttpServletRequest request) {
		if (!"POST".equalsIgnoreCase(request.getMethod())) {
			return true;
		}

		String tokenKey = null;// 获取 csrf_ 开头的 key

//		Iterator<Entry<String, String[]>> iter = request.getParameterMap().entrySet().iterator();
//		while (iter.hasNext()) {
//			Entry<String, String[]> entry = iter.next();
//			if (entry.getKey().startsWith(CSRFTOKEN_PREFIX))
//				tokenKey = entry.getKey();
//		}
		
		for (String key : request.getParameterMap().keySet()) {
			if (key.startsWith(CSRFTOKEN_PREFIX)) {
				tokenKey = key;
				break;
			}
		}

		if (tokenKey == null) // 找不到
			return false;

		long csrfTokenId = (Long) request.getSession().getAttribute(tokenKey),
				paramCsrfToken = Long.parseLong(request.getParameter(tokenKey));

		if (csrfTokenId != paramCsrfToken)
			throw new SecurityException("post method csrf token not valid.");

		return false;
	}

	/**
	 * 生成 CSRF token
	 * 
	 * @param session
	 * @return
	 */
	public static String getCsrfTokenId(HttpSession session) {
		String str = session.getCreationTime() + session.getId();

		try {
			return new String(MessageDigest.getInstance("MD5").digest(str.getBytes()));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}
}
