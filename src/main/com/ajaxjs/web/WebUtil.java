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

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import com.ajaxjs.util.collection.MapHelper;

/**
 * Provides many usful util funcitons.
 * 
 * @author Sp42 frank@ajaxjs.com
 *
 */
public class WebUtil {

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
	 * 获取请求 ip
	 * 
	 * @param request
	 *            请求对象
	 * @return 客户端 ip
	 */
	public static String getIp(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		
		if (!"unknown".equalsIgnoreCase(ip) && ip != null && ip.length() != 0) {
			int index = ip.indexOf(",");
			if (index != -1)
				ip = ip.substring(0, index);
			
			return ip;
		}
		
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) 
			ip = request.getHeader("Proxy-Client-IP");
		
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) 
			ip = request.getHeader("WL-Proxy-Client-IP");
		
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) 
			ip = request.getHeader("X-Real-Ip");
		
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) 
			ip = request.getRemoteAddr();
		
		return ip;
	}
	
	/**
	 * 手机调试
	 * @param request
	 * @return
	 */
	public static String remoteConsole(HttpServletRequest request) {

		String msg = "";
		Map<String, String> requestMap = new MapHelper().setParameterMapRaw(request.getParameterMap()).toMap()
				.ignoreField("url").ignoreField("action").getParameterMap_String();

		if (requestMap.size() > 0) {
			msg = com.ajaxjs.util.collection.MapHelper.join(requestMap);
		} else {
			msg = "没有参数";
		}

		System.out.println("手机调试参数：" + msg);
		// 可能是 jsonp
		return String.format("(function(){console.log('%s');})();", msg);
	}
	
	/**
	 * http 代理，请谨慎外显，为了不带来不必要的流量和运算
	 * @param request
	 * @return
	 */
	public static String httpProxy(HttpServletRequest request) {
		String url = request.getParameter("url");
		new MapHelper().setParameterMapRaw(request.getParameterMap()).ignoreField("url");
		String params = MapHelper.join(new MapHelper().setParameterMapRaw(request.getParameterMap()).toMap()
				.ignoreField("url").getParameterMap_String(), "&"); // 不要 url 参数
		return url + '?' + params;
	}
}
