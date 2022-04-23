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

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.springframework.beans.factory.annotation.Autowired;

import com.ajaxjs.security.web.checker.Xss;
import com.ajaxjs.util.config.EasyConfig;

/**
 * 输入输出 Cookie 白名单验证过滤。 获取用户输入参数名称和参数值进行 XSS 过滤，对 Header 和 cookie value 值进行 XSS
 * 过滤（转码 Script 标签的 &lt;、&gt; 符号）
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class SecurityRequest extends HttpServletRequestWrapper {
	@Autowired
	private EasyConfig config;

	@Autowired
	private Xss xxsChecker;

	/**
	 * 创建一个 SecurityRequest 实例
	 * 
	 * @param request 请求对象
	 */
	public SecurityRequest(HttpServletRequest request) {
		super(request);
	}

	@Override
	public String getParameter(String key) {
		String v = super.getParameter(key);

		if (config.getBol(IS_ENABLE_XSS)) {
			return xxsChecker.clean(v);
		} else
			return v;
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		Map<String, String[]> _map = super.getParameterMap();
		
		if (_map == null)
			return null;

		if (config.getBol(IS_ENABLE_XSS)) {
			Map<String, String[]> map = new HashMap<>();
			_map.forEach((k, v) -> map.put(xxsChecker.clean(k), xxsChecker.clean(v)));

			return map;
		} else
			return _map;
	}

	public final static String IS_ENABLE_XSS = "webSecurity.isXXS_Filter";

	@Override
	public Enumeration<String> getParameterNames() {
		Enumeration<String> enums = super.getParameterNames();

		if (config.getBol(IS_ENABLE_XSS)) {
			Vector<String> vec = new Vector<>();

			while (enums.hasMoreElements()) {
				String value = enums.nextElement();
				vec.add(xxsChecker.clean(value));
			}

			return vec.elements();
		} else
			return enums;
	}

	@Override
	public String[] getParameterValues(String key) {
		String[] arr = super.getParameterValues(key);

		if (config.getBol(IS_ENABLE_XSS))
			arr = xxsChecker.clean(arr);

		return arr;
	}

}