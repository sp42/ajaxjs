package com.ajaxjs.security.xss;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Xss {

	private static String xssType = "<script[^>]*?>.*?</script>";

	private static Pattern xssPattern = Pattern.compile(xssType);

	private static String type_ESCAPSE = "escapse";
	
	static String type_DELETE = "delete";

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
