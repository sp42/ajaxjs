package com.ajaxjs.user.common.util;

import java.util.Random;

/**
 * 生成指定位数的随机复杂密码（至少包含一个大写字母、小写字母、特殊字符、数字）
 * 
 * @author https://www.zifangsky.cn/1540.html
 *
 */
public class MakePassword {
	private static final String SPECIAL_CHARS = "!@#$%^&*_=+-/";

	/**
	 * 查找一个char数组中还没有填充字符的位置
	 */
	private static int nextIndex(char[] chars, Random rnd) {
		int index = rnd.nextInt(chars.length);
		while (chars[index] != 0)
			index = rnd.nextInt(chars.length);

		return index;
	}

	/**
	 * 返回一个随机的特殊字符
	 */
	private static char nextSpecialChar(Random rnd) {
		return SPECIAL_CHARS.charAt(rnd.nextInt(SPECIAL_CHARS.length()));
	}

	/**
	 * 返回一个随机的大写字母
	 */
	private static char nextUpperLetter(Random rnd) {
		return (char) ('A' + rnd.nextInt(26));
	}

	/**
	 * 返回一个随机的小写字母
	 */
	private static char nextLowerLetter(Random rnd) {
		return (char) ('a' + rnd.nextInt(26));
	}

	/**
	 * 返回一个随机的数字
	 */
	private static char nextNumLetter(Random rnd) {
		return (char) ('0' + rnd.nextInt(10));
	}

	/**
	 * 返回一个随机的字符
	 */
	private static char nextChar(Random rnd) {
		switch (rnd.nextInt(4)) {
		case 0:
			return (char) ('a' + rnd.nextInt(26));
		case 1:
			return (char) ('A' + rnd.nextInt(26));
		case 2:
			return (char) ('0' + rnd.nextInt(10));
		default:
			return SPECIAL_CHARS.charAt(rnd.nextInt(SPECIAL_CHARS.length()));
		}
	}

	/**
	 * 生成指定位数的随机数
	 */
	public static String randomPassword(int length) {
		if (length < 4)
			return "";

		char[] chars = new char[length];
		Random rnd = new Random();

		// 1. 至少生成一个大写字母、小写字母、特殊字符、数字
		chars[nextIndex(chars, rnd)] = nextSpecialChar(rnd);
		chars[nextIndex(chars, rnd)] = nextUpperLetter(rnd);
		chars[nextIndex(chars, rnd)] = nextLowerLetter(rnd);
		chars[nextIndex(chars, rnd)] = nextNumLetter(rnd);

		// 2. 填补其他位置的字符
		for (int i = 0; i < length; i++) {
			if (chars[i] == 0) {
				chars[i] = nextChar(rnd);
			}
		}

		// 3. 返回结果
		return new String(chars);
	}
}
