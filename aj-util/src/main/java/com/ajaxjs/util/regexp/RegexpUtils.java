package com.ajaxjs.util.regexp;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则工具类
 * 
 * @author xinzhang
 *
 */
public class RegexpUtils {
	/**
	 * 判断输入字符串是否为科学计数法
	 */
	static Pattern kexuejisshu_pattern;

	/**
	 * 过滤输入字符串的科学计数法为真实数字
	 * 
	 * @param json
	 * @return
	 */
	public static String kexuejishu(String json) {
		// init
		if (kexuejisshu_pattern == null)
			kexuejisshu_pattern = Pattern.compile("(-?\\d+\\.?\\d*)[Ee]{1}[\\+-]?[0-9]*");

		return RegexpUtils.replaceAll(json, kexuejisshu_pattern, new ReplaceCallBack() {
			@Override
			public String replace(String text, int index, Matcher matcher) {
				return new BigDecimal(Double.toString(Double.parseDouble(text))).toPlainString();
			}
		});
	}

	/**
	 * 将 String 中的所有 regex 匹配的字串全部替换掉
	 * 
	 * @param string      代替换的字符串
	 * @param regex       替换查找的正则表达式
	 * @param replacement 替换函数
	 * @return
	 */
	public static String replaceAll(String string, String regex, ReplaceCallBack replacement) {
		return replaceAll(string, Pattern.compile(regex), replacement);
	}

	/**
	 * 将 String 中的所有 pattern 匹配的字串替换掉
	 * 
	 * @param string      代替换的字符串
	 * @param pattern     替换查找的正则表达式对象
	 * @param replacement 替换函数
	 * @return
	 */
	public static String replaceAll(String string, Pattern pattern, ReplaceCallBack replacement) {
		if (string == null)
			return null;

		Matcher m = pattern.matcher(string);

		if (m.find()) {
			StringBuffer sb = new StringBuffer();
			int index = 0;

			while (true) {
				m.appendReplacement(sb, replacement.replace(m.group(0), index++, m));
				if (!m.find())
					break;
			}

			m.appendTail(sb);

			return sb.toString();
		}

		return string;
	}

	/**
	 * 将 String 中的 regex 第一次匹配的字串替换掉
	 * 
	 * @param string      代替换的字符串
	 * @param regex       替换查找的正则表达式
	 * @param replacement 替换函数
	 * @return
	 */
	public static String replaceFirst(String string, String regex, ReplaceCallBack replacement) {
		return replaceFirst(string, Pattern.compile(regex), replacement);
	}

	/**
	 * 将 String 中的 pattern 第一次匹配的字串替换掉
	 * 
	 * @param string      代替换的字符串
	 * @param pattern     替换查找的正则表达式对象
	 * @param replacement 替换函数
	 * @return
	 */
	public static String replaceFirst(String string, Pattern pattern, ReplaceCallBack replacement) {
		if (string == null)
			return null;

		Matcher m = pattern.matcher(string);
		StringBuffer sb = new StringBuffer();

		if (m.find())
			m.appendReplacement(sb, replacement.replace(m.group(0), 0, m));

		m.appendTail(sb);

		return sb.toString();
	}
}
