package com.ajaxjs.web;

import java.util.List;

public class Utils {
	/**
	 * 是否空字符串
	 * 
	 * @param str 字符串
	 * @return true = 空的字符串
	 */
	public static boolean isEmtpyString(String str) {
		return str == null || str.isEmpty() || "".equals(str);
	}

	public static boolean isEmptyList(List<?> list) {
		return list == null || list.size() == 0;
	}
}
