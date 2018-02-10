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

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import com.ajaxjs.config.ConfigService;
import com.ajaxjs.config.SiteStruService;
import com.ajaxjs.util.collection.MapHelper;

/**
 * 该类对应 tagfile：head.tag
 * 
 * @author Sp42 frank@ajaxjs.com
 */
public class HtmlHead {
	private UserAgent ua;

	HttpServletRequest request;

	Map<String, Object> node;

	public void init(HttpServletRequest request) {
		this.request = request;
		setUa(new UserAgent(request));

		// 设置页面 node
		node = SiteStruService.getPageNode(request.getRequestURI(), request.getContextPath());
	}

	public Map<String, Object> getNode() {
		return node;
	}

	public String getCssUrl(String lessPath) {
		boolean isDebug;
		if (ConfigService.config != null && ConfigService.config.get("isDebug") != null)
			isDebug = ConfigService.config.get("isDebug") == null ? false
					: (boolean) ConfigService.config.get("isDebug");
		else
			isDebug = true;

		return getCssUrl(lessPath, isDebug);
	}

	/**
	 * 返回样式文件
	 * 
	 * @param lessPath
	 *            LESS 路径，如果 lessPath = null，表示不需要 <link href=...> 样式
	 * @return CSS 地址
	 */
	public String getCssUrl(String lessPath, boolean isDebug) {
		if (lessPath == null)
			lessPath = "/asset/less/main.less"; // 默认 less 路径

		if (isDebug) {// 设置参数
			Map<String, String> params = new HashMap<>();
			params.put("lessFile", Mappath(request, lessPath));
			params.put("ns", Mappath(request, lessPath.replaceAll("\\/[\\w\\.]*$", ""))); // 去掉文件名，保留路径，也就是文件夹名称
			params.put("picPath", getBasePath(request) + "/asset");// 返回 CSS 背景图所用的图片
			params.put("MainDomain", "");
			params.put("isdebug", "true");

			return "http://" + IP.getLocalIp() + ":83/lessService/?" + MapHelper.join(params, "&");
		} else {
			return request.getContextPath() + lessPath.replace("/less", "/css").replace(".less", ".css");
		}
	}

	/**
	 * 获取磁盘真實地址。
	 * 
	 * @param cxt
	 *            Web 上下文
	 * @param relativePath
	 *            相对地址
	 * @return 绝对地址
	 */
	public static String Mappath(ServletContext cxt, String relativePath) {
		String absoluteAddress = cxt.getRealPath(relativePath); // 绝对地址

		if (absoluteAddress != null)
			absoluteAddress = absoluteAddress.replace('\\', '/');
		return absoluteAddress;
	}

	/**
	 * 输入一个相对地址，补充成为绝对地址 相对地址转换为绝对地址，并转换斜杠
	 * 
	 * @param relativePath
	 *            相对地址
	 * @return 绝对地址
	 */
	public static String Mappath(HttpServletRequest request, String relativePath) {
		return Mappath(request.getServletContext(), relativePath);
	}

	/**
	 * 协议+主机名+端口（如果为 80 端口的话就默认不写 80）
	 * 
	 * @return 网站名称
	 */
	public static String getBasePath(HttpServletRequest request) {
		String prefix = request.getScheme() + "://" + request.getServerName();

		int port = request.getServerPort();
		if (port != 80)
			prefix += ":" + port;

		return prefix + request.getContextPath();
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
}
