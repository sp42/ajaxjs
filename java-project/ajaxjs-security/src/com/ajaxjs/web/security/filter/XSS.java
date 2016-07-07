package com.ajaxjs.web.security.filter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class XSS {
	private static String xssType = "<script[^>]*?>.*?</script>";

	private static Pattern xssPattern = Pattern.compile(xssType);

	/**
	 * XSS 过滤器
	 * 
	 * @param input
	 *            输入内容
	 * @param filterType
	 *            过滤器类型
	 * @return
	 */
	public static String xssFilter(String input, String filterType) {
		if (input == null || "".equals(input))
			return input;
	
		if (filterType == null || !XssFilterTypeEnum.checkValid(filterType))
			filterType = XssFilterTypeEnum.ESCAPSE.getValue(); // 默认转义
	
		if (filterType.equals(XssFilterTypeEnum.ESCAPSE.getValue())) {
			Matcher matcher = xssPattern.matcher(input);
	
			if (matcher.find())
				return matcher.group().replace("<", "&lt;").replace(">", "&gt;");
		}
		if (filterType.equals(XssFilterTypeEnum.DELETE.getValue()))
			return input.replaceAll(xssType, "");
	
		return input;
	}

	/**
	 * 过滤器类型
	 * 
	 * @author frank
	 *
	 */
	public static enum XssFilterTypeEnum {
		ESCAPSE("escapse"), NO("no"), DELETE("delete");

		private String value;

		/**
		 * 
		 * @param type
		 */
		private XssFilterTypeEnum(String type) {
			value = type;
		}

		/**
		 * 获取值
		 * 
		 * @return
		 */
		public String getValue() {
			return value;
		}

		/**
		 * 
		 * @param type
		 * @return
		 */
		public static boolean checkValid(String type) {
			if (type == null || type.equals("")) {
				return false;
			}
			return (ESCAPSE.getValue().equals(type) || NO.getValue().equals(type) || DELETE.getValue().equals(type));
		}
	}
}
