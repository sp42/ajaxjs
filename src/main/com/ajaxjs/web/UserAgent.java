package com.ajaxjs.web;

import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

/**
 * 浏览器 UA 检测
 * 
 * @author Sp42 frank@ajaxjs.com
 *
 */
public class UserAgent {
	/**
	 * 送入一个请求，得到  UA 标识字符串
	 * 
	 * @param request
	 *            请求对象
	 */
	public UserAgent(HttpServletRequest request) {
		String ua = request.getHeader("User-Agent");
		if (ua != null)
			this.ua = ua.toLowerCase();// 强制转换为小写字母
	}

	private String ua;

	/**
	 * 是否 IE 浏览器
	 * 
	 * @return true 表示为 IE 浏览器
	 */
	public boolean isIE() {
		return ua.contains("msie");
	}

	/**
	 * IE 8 或其以下的皆为旧版 IE
	 * 
	 * @return true 表示为旧 IE 浏览器
	 */
	public boolean isOldIE() {
		return isIE() && (ua.contains("msie 5.5") || ua.contains("msie 6.0") || ua.contains("msie 7.0") || ua.contains("msie 8.0"));
	}

	/**
	 * 是否为 Firefox 浏览器
	 * 
	 * @return true 表示为 Firefox 浏览器
	 */
	public boolean isFireFox() {
		return ua.contains("firefox");
	}

	/**
	 * 是否为 Chrome 浏览器
	 * 
	 * @return true 表示为 Chrome 浏览器
	 */
	public boolean isChrome() {
		return ua.contains("chrome");
	}

	/**
	 * 是否为安卓
	 * 
	 * @return true 表示为 Android 浏览器
	 */
	public boolean isAndroid() {
		return ua.contains("android");
	}

	/**
	 * 是否为安卓 7.x
	 * 
	 * @return true 表示为 Android 7.x 浏览器
	 */
	public boolean isAndroid_7() {
		boolean is7 = Pattern.compile("Android\\s7", Pattern.CASE_INSENSITIVE).matcher(ua).find();
		return isAndroid() && is7;
	}

	/**
	 * 是否为安卓 56.x
	 * 
	 * @return true 表示为 Android 6.x 浏览器
	 */
	public boolean isAndroid_6() {
		boolean is6 = Pattern.compile("Android\\s6", Pattern.CASE_INSENSITIVE).matcher(ua).find();
		return isAndroid() && is6;
	}

	/**
	 * 是否为安卓 5.x
	 * 
	 * @return true 表示为 Android 5.x 浏览器
	 */
	public boolean isAndroid_5() {
		boolean is5 = Pattern.compile("Android\\s5", Pattern.CASE_INSENSITIVE).matcher(ua).find();
		return isAndroid() && is5;
	}

	/**
	 * 是否为安卓 4.x
	 * 
	 * @return true 表示为 Android 4.x 浏览器
	 */
	public boolean isAndroid_4() {
		boolean is4 = Pattern.compile("Android\\s4", Pattern.CASE_INSENSITIVE).matcher(ua).find();
		return isAndroid() && is4;
	}

	/**
	 * 是否为安卓 2.x
	 * 
	 * @return true 表示为 Android 2.x 浏览器
	 */
	public boolean isAndroid_2() {
		boolean is2 = Pattern.compile("Android\\s2", Pattern.CASE_INSENSITIVE).matcher(ua).find();
		return isAndroid() && is2;
	}

	/**
	 * 是否为安卓 2.2.x
	 * 
	 * @return true 表示为 Android 2.2.x 浏览器
	 */
	public boolean isAndroid_2_2() {
		boolean is2_2 = Pattern.compile("Android\\s2\\.2", Pattern.CASE_INSENSITIVE).matcher(ua).find();
		return isAndroid() && is2_2;
	}

	/**
	 * 是否为 iPhone
	 * 
	 * @return true 表示为 iPhone Safari 浏览器
	 */
	public boolean isIPhone() {
		return ua.contains("iphone");
	}

	/**
	 * 是否为 iPad
	 * 
	 * @return true 表示为 iPad Safari 浏览器
	 */
	public boolean isIPad() {
		return ua.contains("ipad");
	}

	/**
	 * 是否为 iOS 系统
	 * 
	 * @return true 表示为 iOS 系统
	 */
	public boolean isIOS() {
		return isIPad() || isIPhone();
	}

	/**
	 * 是否为 WinPhone 手机
	 * 
	 * @return true 表示为 WinPhone 手机
	 */
	public boolean isWinPhone() {
		return ua.contains("windows phone");
	}

	/**
	 * 是否为手机
	 * 
	 * @return true 表示为手机
	 */
	public boolean isPhone() {
		return isAndroid() || isIOS() || isWinPhone();
	}
}