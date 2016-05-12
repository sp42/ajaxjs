package com.ajaxjs.javatools;

import java.io.UnsupportedEncodingException;

public class StringTools {
	/**
	 * 字符串首字大写
	 * 
	 * @param str
	 *            输入的字符串
	 * @return
	 */
	public static String firstBig(String str) {
		return str.replaceFirst(str.substring(0, 1), str.substring(0, 1).toUpperCase());
	}
	
	/**
	 * 对字符串重新编码
	 * 
	 * @param text
	 *            字符串
	 * @param resEncoding
	 *            源编码
	 * @param newEncoding
	 *            新编码
	 * @return 重新编码后的字符串
	 */
	public static String reEncoding(String text, String resEncoding, String newEncoding) {
		try {
			return new String(text.getBytes(resEncoding), newEncoding);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("读取文件为一个内存字符串失败，失败原因是使用了不支持的字符编码");
		}
	}

	/**
	 * 重新编码Unicode字符串
	 * 
	 * @param text
	 *            源字符串
	 * @param newEncoding
	 *            新的编码
	 * @return 指定编码的字符串
	 */
	public static String reEncoding(String text, String newEncoding) {
		try {
			return new String(text.getBytes(), newEncoding);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("读取文件为一个内存字符串失败，失败原因是使用了不支持的字符编码" + newEncoding);
		}
	}
}
