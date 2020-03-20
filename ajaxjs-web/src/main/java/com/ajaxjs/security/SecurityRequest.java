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
package com.ajaxjs.security;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import com.ajaxjs.util.CommonUtil;

/**
 * 输入输出 Cookie 白名单验证过滤。 获取用户输入参数和参数值进行 XSS 过滤，对 Header 和 cookie value 值进行 XSS
 * 过滤（转码 Script 标签的< > 符号
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class SecurityRequest extends HttpServletRequestWrapper {
	public static SecurityFilter delegate = new SecurityFilter();

	/**
	 * 
	 * @param request
	 */
	public SecurityRequest(HttpServletRequest request) {
		super(request);
	}

	@Override
	public Cookie[] getCookies() {
		Cookie[] cookies = super.getCookies();

		if (CommonUtil.isNull(cookies))
			return null;

		List<Cookie> cookieList = new ArrayList<>();
		
		for (Cookie cookie : cookies) {
			// for (String name : SecurityConfigLoader.cookieWhiteList) {
			// if (name.equalsIgnoreCase(cookie.getName()))
			// cookieList.add(cookie);
			// }

			if (delegate.isInWhiteList(cookie.getName()))
				cookieList.add(cookie);
		}

		return cookieList.toArray(new Cookie[cookieList.size()]);
	}

	/**
	 * 按照名称获取 Cookie 的值
	 * 
	 * @param name Cookie 的名称
	 * @return Cookie 的值
	 */
	public String getCookieByName(String name) {
		Cookie[] cookies = getCookies();

		for (Cookie cookie : cookies) {
			if (name.equals(cookie.getName()))
				return cookie.getValue();
		}

		return null;
	}

	@Override
	public String getParameter(String key) {
		key = clean(key, TYPE_DELETE);
		return clean(super.getParameter(key));
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		Map<String, String[]> _map = super.getParameterMap();
		if (_map == null)
			return null;

		Map<String, String[]> map = new HashMap<>();
		_map.forEach((k, v) -> map.put(clean(k, TYPE_DELETE), clean(v)));

		return map;
	}

	@Override
	public Enumeration<String> getParameterNames() {
		Enumeration<String> enums = super.getParameterNames();
		Vector<String> vec = new Vector<>();

		while (enums.hasMoreElements()) {
			String value = enums.nextElement();
			vec.add(clean(value));
		}

		return vec.elements();
	}

	@Override
	public String[] getParameterValues(String key) {
		return clean(super.getParameterValues(key));
	}

	private static String XSS_Type = "<script[^>]*?>.*?</script>";

	private static Pattern XSS_Pattern = Pattern.compile(XSS_Type);

	private static String TYPE_ESCAPSE = "escapse";

	public static String TYPE_DELETE = "delete";

	/**
	 * 默认转义
	 * 
	 * @param input 输入内容
	 * @return 转义文字
	 */
	public static String clean(String input) {
		return clean(input, TYPE_ESCAPSE);
	}

	/**
	 * XSS 过滤器
	 * 
	 * @param input      输入内容
	 * @param filterType 过滤器类型
	 * @return 转义文字
	 */
	public static String clean(String input, String filterType) {
		if (CommonUtil.isEmptyString(input))
			return input;

		if (filterType.equals(TYPE_ESCAPSE)) {
			Matcher matcher = XSS_Pattern.matcher(input);

			if (matcher.find())
				return matcher.group().replace("<", "&lt;").replace(">", "&gt;");
		} else if (filterType.equals(TYPE_DELETE))
			return input.replaceAll(XSS_Type, "");

		return input;
	}

	/**
	 * 过滤 for 数组
	 * 
	 * @param value
	 * @return 转义文字
	 */
	private static String[] clean(String[] value) {
		if (CommonUtil.isNull(value))
			return null;

		for (int i = 0; i < value.length; i++)
			value[i] = clean(value[i]);

		return value;
	}
}
