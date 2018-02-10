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

import javax.servlet.http.HttpServletRequest;

import com.ajaxjs.util.collection.MapHelper;

/**
 * IP 工具类
 */
public class IP {
	/**
	 * ip 缓存，保存起来方便下次使用
	 */
	private static String localIp = null;
 
	/**
	 * 获取本机 IP 地址
	 * 用于局域网内的测试机器
	 * @return 本机 IP
	 */
	public static String getLocalIp() {
		if (localIp == null) { // 第一次访问
			for (String ip : getAllLocalHostIP()) {
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

		String[] ips = null;
		
		if (addrs !=null) {
			ips = new String[addrs.length];
			int i = 0;
			
			for (InetAddress addr : addrs)
				ips[i++] = addr.getHostAddress();
		}

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
			System.err.println("获取 ip 失败！" + hostname);
			e.printStackTrace();
		}
		
		return null;
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
		} 

		return false;
	}
	
	// 手机调试
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
	 * 
	 * @return
	 */
	public static String httpProxy(HttpServletRequest request) {
		String url = request.getParameter("url");
		new MapHelper().setParameterMapRaw(request.getParameterMap()).ignoreField("url");
		String params = MapHelper.join(new MapHelper().setParameterMapRaw(request.getParameterMap()).toMap()
				.ignoreField("url").getParameterMap_String(), "&"); // 不要 url 参数
		return url + '?' + params;
	}

	/*
	 * 根据请求 ip 显示所在区域 https://www.ipip.net/
	 * http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=js&ip=123.123.
	 * 123.12
	 */
	//String json = Client.GET("http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=js&ip=" + MvcRequest.getIp(request));
	//out.println(json.replace("var remote_ip_info = ", "").replaceAll(";$", ""));
}