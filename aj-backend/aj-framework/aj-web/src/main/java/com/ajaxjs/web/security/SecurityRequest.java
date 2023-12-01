/**
 * Copyright 2015 Sp42 frank@ajaxjs.com
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ajaxjs.web.security;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * 输入输出 Cookie 白名单验证过滤。 
 * 获取用户输入参数名称和参数值进行 XSS 过滤，
 * 对 Header 和 cookie value 值进行 XSS 过滤（转码 Script 标签的 &lt;、&gt; 符号）
 *
 * @author sp42 frank@ajaxjs.com
 *
 */
public class SecurityRequest extends HttpServletRequestWrapper {
	private final boolean isXssCheck = true;

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

		if (isXssCheck) {
			return Filter.cleanXSS(v);
		} else
			return v;
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		Map<String, String[]> _map = super.getParameterMap();

		if (_map == null)
			return null;

		if (isXssCheck) {
			Map<String, String[]> map = new HashMap<>();
			_map.forEach((k, v) -> map.put(Filter.cleanXSS(k), clean(v)));

			return map;
		} else
			return _map;
	}

	public final static String IS_ENABLE_XSS = "webSecurity.isXXS_Filter";

	@Override
	public Enumeration<String> getParameterNames() {
		Enumeration<String> enums = super.getParameterNames();

		if (isXssCheck) {
			Vector<String> vec = new Vector<>();

			while (enums.hasMoreElements()) {
				String value = enums.nextElement();
				vec.add(Filter.cleanXSS(value));
			}

			return vec.elements();
		} else
			return enums;
	}

	@Override
	public String[] getParameterValues(String key) {
		String[] values = super.getParameterValues(key);

		if (isXssCheck)
			clean(values);

		return values;
	}

	/**
	 * 过滤 for 数组
	 *
	 * @param values 输入内容的数组
	 * @return 转义文字
	 */
	public String[] clean(String[] values) {
		if (values == null || values.length == 0)
			return null;

		for (int i = 0; i < values.length; i++)
			values[i] = Filter.cleanXSS(values[i]);

		return values;
	}

}
