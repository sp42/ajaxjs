/**
 * Copyright 2015 Sp42 frank@ajaxjs.com
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

import java.util.*;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import com.ajaxjs.Version;
//import com.ajaxjs.config.ConfigService;
import com.ajaxjs.config.SiteStruService;
import com.ajaxjs.util.collection.MapHelper;

/**
 * 该类对应 tagfile：head.tag
 * 
 * @author Sp42 frank@ajaxjs.com
 */
public class HtmlHead {
	HttpServletRequest request;
	private UserAgent ua;
	Map<String, Object> node;

	/**
	 * 
	 * @param request
	 *            请求对象
	 */
	public void init(HttpServletRequest request) {
		this.request = request;
		setUa(new UserAgent(request));

		// 设置页面 node
		node = SiteStruService.getPageNode(request.getRequestURI(), request.getContextPath());
	}

	public Map<String, Object> getNode() {
		return node;
	}

	/**
	 * @return the ua
	 */
	public UserAgent getUa() {
		return ua;
	}

	/**
	 * @param ua
	 *            the ua to set
	 */
	public void setUa(UserAgent ua) {
		this.ua = ua;
	}

	/**
	 * 
	 * @deprecated
	 * @param lessPath
	 * @return
	 */
	public String getCssUrl(String lessPath) {
		//boolean isDebug = ConfigService.getValueAsBool("isDebug");
		return getCssUrl(lessPath, Version.isDebug);
	}

	/**
	 * 返回样式文件
	 * 
	 * @deprecated
	 * @param lessPath
	 *            LESS 路径，如果 lessPath = null，表示不需要 <link href=...> 样式
	 * @return CSS 地址
	 */
	public String getCssUrl(String lessPath, boolean isDebug) {
		if (lessPath == null)
			lessPath = "/asset/less/main.less"; // 默认 less 路径

		if (isDebug) {// 设置参数
			Map<String, String> params = new HashMap<>();
			params.put("lessFile", WebUtil.Mappath(request, lessPath));
			params.put("ns", WebUtil.Mappath(request, lessPath.replaceAll("\\/[\\w\\.]*$", ""))); // 去掉文件名，保留路径，也就是文件夹名称
			params.put("picPath", WebUtil.getBasePath(request) + "/asset");// 返回 CSS 背景图所用的图片
			params.put("MainDomain", "");
			params.put("isdebug", "true");

			return "http://" + getLocalIp() + ":83/lessService/?" + MapHelper.join(params, "&");
		} else
			return request.getContextPath() + lessPath.replace("/less", "/css").replace(".less", ".css");
	}

	/**
	 * ip 缓存，保存起来方便下次使用
	 */
	private static String localIp = null;

	/**
	 * 获取本机 IP 地址 用于局域网内的测试机器
	 * 
	 * @return 本机 IP
	 */
	public static String getLocalIp() {
		if (localIp == null) { // 第一次访问
			for (String ip : WebUtil.getAllLocalHostIP()) {
				if (ip.startsWith("192.168.") || ip.startsWith("10.0.")) { // 以 192.168.x.x 开头的都是局域网内的 IP
					localIp = ip;
					break;
				}
			}

			if (localIp == null)
				localIp = "localhost";// 还是 null，那就本机的……没开网卡？
		}

		return localIp;
	}

	/**
	 * 是否老版本浏览器
	 * 
	 * @return
	 */
	public boolean isOldIE() {
		return getUa().isIE() && getUa().is_old_IE();
	}
	
	public boolean isDebug() {
		return Version.isDebug;
	}

	/**
	 * 浏览器 UA 检测
	 * 
	 * @author Sp42 frank@ajaxjs.com
	 *
	 */
	public static class UserAgent {
		/**
		 * 送入一个浏览器 UA 标识字符串
		 * 
		 * @param ua
		 *            UA 标识字符串
		 */
		public UserAgent(String ua) {
			this.ua = ua;
		}

		/**
		 * 送入一个请求
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
		public boolean is_old_IE() {
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
}
