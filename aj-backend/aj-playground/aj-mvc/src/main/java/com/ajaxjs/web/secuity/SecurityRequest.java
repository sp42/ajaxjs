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
package com.ajaxjs.web.secuity;

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

import com.ajaxjs.framework.config.ConfigService;
import com.ajaxjs.util.CommonUtil;

/**
 * 输入输出 Cookie 白名单验证过滤。 获取用户输入参数名称和参数值进行 XSS 过滤，对 Header 和 cookie value 值进行 XSS
 * 过滤（转码 Script 标签的 &lt;、&gt; 符号）
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class SecurityRequest extends HttpServletRequestWrapper {
	public static SecurityFilter delegate = new SecurityFilter();

	/**
	 * 创建一个 SecurityRequest 实例
	 * 
	 * @param request 请求对象
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
		String v = super.getParameter(key);

		if (ConfigService.getValueAsBool("security.isXXS_Filter")) {
			key = clean(key, TYPE_DELETE);
			return clean(v);
		} else
			return v;
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		Map<String, String[]> _map = super.getParameterMap();
		if (_map == null)
			return null;

		if (ConfigService.getValueAsBool("security.isXXS_Filter")) {
			Map<String, String[]> map = new HashMap<>();
			_map.forEach((k, v) -> map.put(clean(k, TYPE_DELETE), clean(v)));

			return map;
		} else
			return _map;
	}

	@Override
	public Enumeration<String> getParameterNames() {
		Enumeration<String> enums = super.getParameterNames();

		if (ConfigService.getValueAsBool("security.isXXS_Filter")) {
			Vector<String> vec = new Vector<>();

			while (enums.hasMoreElements()) {
				String value = enums.nextElement();
				vec.add(clean(value));
			}

			return vec.elements();
		} else
			return enums;
	}

	@Override
	public String[] getParameterValues(String key) {
		String[] arr = super.getParameterValues(key);

		if (ConfigService.getValueAsBool("security.isXXS_Filter"))
			return clean(arr);
		else
			return arr;
	}

	private static String XSS_SCRIPT = "<script[^>]*?>.*?</script>";

	private static Pattern XSS_Pattern = Pattern.compile(XSS_SCRIPT);

	private static String TYPE_ESCAPSE = "escapse";

	public static String TYPE_DELETE = "delete";

	/**
	 * 默认转义
	 * 
	 * @param input 输入内容
	 * @return 转义后的字符串
	 */
	public static String clean(String input) {
		return clean(input, TYPE_ESCAPSE);
	}

	/**
	 * XSS 过滤器
	 * 
	 * @param input 输入内容
	 * @param type  过滤器类型
	 * @return 转义后的字符串
	 */
	public static String clean(String input, String type) {
		if (CommonUtil.isEmptyString(input))
			return input;

		if (type.equals(TYPE_ESCAPSE)) {
			Matcher matcher = XSS_Pattern.matcher(input);

			if (matcher.find())
				return matcher.group().replace("<", "&lt;").replace(">", "&gt;");
		} else if (type.equals(TYPE_DELETE))
			return input.replaceAll(XSS_SCRIPT, "");

		return input;
	}

	/**
	 * 过滤 for 数组
	 * 
	 * @param value 输入内容的数组
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
