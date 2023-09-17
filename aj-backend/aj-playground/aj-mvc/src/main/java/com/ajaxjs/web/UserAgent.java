/**
 * Copyright Sp42 frank@ajaxjs.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ajaxjs.web;

import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

/**
 * 浏览器 UA 检测
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class UserAgent {
	/**
	 * 送入一个请求，得到 UA 标识字符串
	 * 
	 * @param req 请求对象
	 */
	public UserAgent(HttpServletRequest req) {
		String ua = req.getHeader("User-Agent");
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
	 * 是否为手机
	 * 
	 * @return true 表示为手机
	 */
	public boolean isPhone() {
		return isAndroid() || isIOS();
	}

	/**
	 * 是否微信内置浏览器
	 * 
	 * @return
	 */
	public boolean isWeixin() {
		return ua.contains("micromessenger");
	}
}