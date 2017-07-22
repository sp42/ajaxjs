package com.ajaxjs.js.json;

import com.ajaxjs.js.json.syntax.FMS;

/**
 * 主要的方法入口 parse()。 还有其他的一些工具方法。
 */
public class JSONParser {
	/**
	 * 解析 JSON 为 Map 或 List
	 * 
	 * @param str
	 *            JSON 字符串
	 * @return Map 或 List
	 */
	public static Object parse(String str) {
		return new FMS(str).parse();
	}

	/**
	 * 是否空格字符
	 * 
	 * @param c
	 * @return
	 */
	public static boolean isSpace(char c) {
		return c == ' ' || c == '\t' || c == '\n';
	}

	/**
	 * 是否字符或者下划线
	 * 
	 * @param c
	 * @return
	 */
	public static boolean isLetterUnderline(char c) {
		return (c >= 'a' && c <= 'z') || c == '_';
	}

	/**
	 * 是否数字字符
	 * 
	 * @param c
	 * @return
	 */
	public static boolean isNum(char c) {
		return c >= '0' && c <= '9';
	}

	/**
	 * 是否数字字符或小数
	 * 
	 * @param c
	 * @return
	 */
	public static boolean isDecimal(char c) {
		return isNum(c) || c == '.';
	}

	/**
	 * 是否字符或者下划线或下划线
	 * 
	 * @param c
	 * @return
	 */
	public static boolean isNumLetterUnderline(char c) {
		return isLetterUnderline(c) || isNum(c) || c == '_';
	}
}
