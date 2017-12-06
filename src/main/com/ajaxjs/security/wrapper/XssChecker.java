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
package com.ajaxjs.security.wrapper;

import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ajaxjs.security.SecurityInit;
import com.ajaxjs.util.aop.Aop;
import com.ajaxjs.util.aop.ReturnAsArg;

public class XssChecker extends Aop<SecurityInit> {
	@Override
	protected Object before(Method method, Object[] args) throws Throwable {
		if (method.getName().equals("initRequest")) {
			HttpServletRequest request = (HttpServletRequest) args[0];
			return new ReturnAsArg(new XssReqeust(request));
		}

		if (method.getName().equals("initResponse")) {
			HttpServletResponse response = (HttpServletResponse) args[0];
			return new ReturnAsArg(new XssResponse(response));
		}

		return null;
	}

	@Override
	protected void after(Method method, Object[] args, Object returnObj) {

	}

	private static String xssType = "<script[^>]*?>.*?</script>";

	private static Pattern xssPattern = Pattern.compile(xssType);

	private static String type_ESCAPSE = "escapse";

	public static String type_DELETE = "delete";

	/**
	 * 默认转义
	 * 
	 * @param input
	 *            输入内容
	 * @return
	 */
	public static String clean(String input) {
		return clean(input, type_ESCAPSE);
	}

	/**
	 * XSS 过滤器
	 * 
	 * @param input
	 *            输入内容
	 * @param filterType
	 *            过滤器类型
	 * @return
	 */
	public static String clean(String input, String filterType) {
		if (isEmptyString(input))
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
		if (isNull(value))
			return null;

		String[] cleaned = new String[value.length];

		for (int i = 0; i < value.length; i++)
			cleaned[i] = clean(value[i]);

		return cleaned;
	}

	/**
	 * 是否空字符串
	 * 
	 * @param str
	 *            要判断的字符串
	 * @return true 表示为为空字符串，否则不为空
	 */
	public static boolean isEmptyString(String str) {
		return str == null || str.isEmpty() || str.trim().isEmpty();
	}

	/**
	 * 判断数组是否为空
	 * 
	 * @param arr
	 *            输入的数组
	 * @return true 表示为素组不是为空，是有内容的，false 表示为数组为空数组，length = 0
	 */
	public static boolean isNull(Object[] arr) {
		return arr == null || arr.length == 0;
	}
}
