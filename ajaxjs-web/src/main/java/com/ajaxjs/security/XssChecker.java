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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ajaxjs.util.CommonUtil;

public class XssChecker {

	private static String xssType = "<script[^>]*?>.*?</script>";

	private static Pattern xssPattern = Pattern.compile(xssType);

	private static String type_ESCAPSE = "escapse";

	public static String type_DELETE = "delete";

	/**
	 * 默认转义
	 * 
	 * @param input 输入内容
	 * @return
	 */
	public static String clean(String input) {
		return clean(input, type_ESCAPSE);
	}

	/**
	 * XSS 过滤器
	 * 
	 * @param input 输入内容
	 * @param filterType 过滤器类型
	 * @return
	 */
	public static String clean(String input, String filterType) {
		if (CommonUtil.isEmptyString(input))
			return input;

		if (filterType.equals(type_ESCAPSE)) {
			Matcher matcher = xssPattern.matcher(input);

			if (matcher.find())
				return matcher.group().replace("<", "&lt;").replace(">", "&gt;");
		} else if (filterType.equals(type_DELETE))
			return input.replaceAll(xssType, "");

		return input;
	}

	/**
	 * 过滤 for 数组
	 * 
	 * @param value
	 * @return
	 */
	public static String[] clean(String[] value) {
		if (CommonUtil.isNull(value))
			return null;

		String[] cleaned = new String[value.length];

		for (int i = 0; i < value.length; i++)
			cleaned[i] = clean(value[i]);

		return cleaned;
	}
}
