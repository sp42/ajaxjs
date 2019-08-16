package com.ajaxjs.js.jsonparser.lexer;

public class BaseLexer {
	/**
	 * 是否空格字符
	 * 
	 * @param c 传入的字符
	 * @return 是否空格字符
	 */
	public static boolean isSpace(char c) {
		return c == ' ' || c == '\t' || c == '\n';
	}

	/**
	 * 是否字符或者下划线
	 * 
	 * @param c 传入的字符
	 * @return 是否字符或者下划线
	 */
	public static boolean isLetterUnderline(char c) {
		return (c >= 'a' && c <= 'z') || c == '_';
	}

	/**
	 * 是否数字字符
	 * 
	 * @param c 传入的字符
	 * @return 是否数字字符
	 */
	public static boolean isNum(char c) {
		return c >= '0' && c <= '9';
	}

	/**
	 * 是否数字字符或小数
	 * 
	 * @param c 传入的字符
	 * @return 是否数字字符或小数
	 */
	public static boolean isDecimal(char c) {
		return isNum(c) || c == '.';
	}

	/**
	 * 是否字符或者数字或下划线
	 * 
	 * @param c 传入的字符
	 * @return 是否字符或者数字或下划线
	 */
	public static boolean isNumLetterUnderline(char c) {
		return isLetterUnderline(c) || isNum(c) || c == '_';
	}

}
