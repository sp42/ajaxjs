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

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
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
	static {
		String t =
				"     ___       _       ___  __    __      _   _____        _          __  _____   _____  \n"+
				"     /   |     | |     /   | \\ \\  / /     | | /  ___/      | |        / / | ____| |  _  \\ \n"+
				"    / /| |     | |    / /| |  \\ \\/ /      | | | |___       | |  __   / /  | |__   | |_| |  \n"+
				"   / / | |  _  | |   / / | |   }  {    _  | | \\___  \\      | | /  | / /   |  __|  |  _  {  \n"+
				"  / /  | | | |_| |  / /  | |  / /\\ \\  | |_| |  ___| |      | |/   |/ /    | |___  | |_| |  \n"+
				" /_/   |_| \\_____/ /_/   |_| /_/  \\_\\ \\_____/ /_____/      |___/|___/     |_____| |_____/ \n";
		
		System.out.println(t);
		
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
		String absolute = cxt.getRealPath(relativePath);

		if (absolute != null)
			absolute = absolute.replace('\\', '/');
		return absolute;
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
	 * 返回协议+主机名+端口（如果为 80 端口的话就默认不写 80）
	 * 
	 * @param request
	 *            请求对象
	 * @return 网站名称
	 */
	public static String getBasePath(HttpServletRequest request) {
		String prefix = request.getScheme() + "://" + request.getServerName();

		int port = request.getServerPort();
		if (port != 80)
			prefix += ":" + port;

		return prefix + "/" + request.getContextPath();
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
	 * 
	 * @param request
	 *            请求对象
	 * @return
	 */
	public static String remoteConsole(HttpServletRequest request) {
		String msg = "";
		Map<String, String> requestMap = new MapHelper().setParameterMapRaw(request.getParameterMap()).toMap().ignoreField("url").ignoreField("action").getParameterMap_String();

		if (requestMap.size() > 0) {
			msg = com.ajaxjs.util.collection.MapHelper.join(requestMap);
		} else {
			msg = "没有参数";
		}

		// 可能是 jsonp
		return String.format("(function(){console.log('%s');})();", msg);
	}

	/**
	 * http 代理，请谨慎外显，为了不带来不必要的流量和运算
	 * 
	 * @param request
	 *            请求对象
	 * @return
	 */
	public static String httpProxy(HttpServletRequest request) {
		String url = request.getParameter("url");
		new MapHelper().setParameterMapRaw(request.getParameterMap()).ignoreField("url");
		// TODO
		String params = MapHelper.join(new MapHelper().setParameterMapRaw(request.getParameterMap()).toMap().ignoreField("url").getParameterMap_String(), "&"); // 不要 url 参数
		return url + '?' + params;
	}
	
	/**
	 * 获得本地所有的 IP 地址
	 * 
	 * @return 本机所有 IP
	 */
	public static String[] getAllLocalHostIP() {
		InetAddress[] addrs = null;

		try {
			String hostName = InetAddress.getLocalHost().getHostName();// 获得主机名
			// 在给定主机名的情况下，根据系统上配置的名称服务返回其 IP 地址所组成的数组。
			addrs = InetAddress.getAllByName(hostName);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		String[] ips = new String[addrs.length];
		for (int i = 0; i < addrs.length; i++)
			ips[i] = addrs[i].getHostAddress();
		
		return ips;
	}

	/**
	 * 输入主机名，返回其 ip，相当于 cmd 的 ping 所得到的域名
	 * 
	 * @param hostname
	 *            例如 www.baidu.com
	 * @return 主机对应的 ip
	 */
	public static String getIpByHostName(String hostname) {
		try {
			return InetAddress.getByName(hostname).getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 模拟ping ping("192.168.0.113");
	 * 
	 * @param ip
	 *            IP 地址
	 */
	public static boolean ping(String ip) {
		try {
			return InetAddress.getByName(ip).isReachable(5000); // 设定超时时间，返回结果表示是否连上
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/*
	 * 根据请求 ip 显示所在区域 https://www.ipip.net/
	 * http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=js&ip=123.123.
	 * 123.12
	 */
	//String json = Client.GET("http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=js&ip=" + MvcRequest.getIp(request));
	//out.println(json.replace("var remote_ip_info = ", "").replaceAll(";$", ""));
}
