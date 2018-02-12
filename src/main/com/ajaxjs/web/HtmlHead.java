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

import javax.servlet.http.HttpServletRequest;

import com.ajaxjs.config.ConfigService;
import com.ajaxjs.config.SiteStruService;
import com.ajaxjs.simpleApp.Constant;
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

		request.setAttribute("commonAsset", request.getContextPath() + "/" + Constant.commonFolder); // 静态资源目录
		request.setAttribute("commonImage", request.getContextPath() + "/" + Constant.commonImage);
		request.setAttribute("commonAssetIcon", request.getContextPath() + "/" + Constant.commonIcon);
	}

	public Map<String, Object> getNode() {
		return node;
	}

	public String getCssUrl(String lessPath) {
		boolean isDebug = ConfigService.getValueAsBool("isDebug");
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
			params.put("lessFile", WebUtil.Mappath(request, lessPath));
			params.put("ns", WebUtil.Mappath(request, lessPath.replaceAll("\\/[\\w\\.]*$", ""))); // 去掉文件名，保留路径，也就是文件夹名称
			params.put("picPath", WebUtil.getBasePath(request) + "/asset");// 返回 CSS 背景图所用的图片
			params.put("MainDomain", "");
			params.put("isdebug", "true");

			return "http://" + IP.getLocalIp() + ":83/lessService/?" + MapHelper.join(params, "&");
		} else {
			return request.getContextPath() + lessPath.replace("/less", "/css").replace(".less", ".css");
		}
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
