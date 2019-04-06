package com.ajaxjs.weixin;

import java.util.Random;

public class CommonWxUtil {

	/**
	 * 获取一定长度的随机字符串
	 * 
	 * @param length 指定字符串长度
	 * @return 一定长度的字符串
	 */
	public static String getNonceStr(int length) {
		String base = "abcdefghijklmnopqrstuvwxyz0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
	
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
	
		return sb.toString();
	}

	/**
	 * 时间戳从1970年1月1日00:00:00至今的秒数,即当前的时间，例如 1490840662
	 * 
	 * @return
	 */
	public static String getTimeStamp() {
		return System.currentTimeMillis() / 1000 + "";
	}

	public static String getNonceStr() {
		return getNonceStr(10);
	}

}
