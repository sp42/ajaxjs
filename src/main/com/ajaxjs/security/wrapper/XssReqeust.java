package com.ajaxjs.security.wrapper;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
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
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * 获取用户输入参数和参数值进行 XSS 过滤，对 Header 和 cookie value 值进行 XSS 过滤（转码 Script 标签的< > 符号
 * 
 * @author Frank Cheung
 */
public class XssReqeust extends HttpServletRequestWrapper {

	public XssReqeust(HttpServletRequest request) {
		super(request);
	}

	@Override
	public String getParameter(String key) {
		key = XssChecker.clean(key, XssChecker.type_DELETE);
		System.out.println("dfdfdf:::::::" + key);
		return XssChecker.clean(super.getParameter(key));
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		Map<String, String[]> paramsMap = super.getParameterMap();
		if (paramsMap == null)
			return null;

		Map<String, String[]> map = new HashMap<>();
		for (String key : paramsMap.keySet())
			map.put(XssChecker.clean(key, XssChecker.type_DELETE), XssChecker.clean(paramsMap.get(key)));

		return map;
	}

	@Override
	public Enumeration<String> getParameterNames() {
		Enumeration<String> enums = super.getParameterNames();
		Vector<String> vec = new Vector<>();

		while (enums.hasMoreElements()) {
			String value = enums.nextElement();
			vec.add(XssChecker.clean(value));
		}

		return vec.elements();
	}

	@Override
	public String[] getParameterValues(String key) {
		return XssChecker.clean(super.getParameterValues(key));
	}
}
