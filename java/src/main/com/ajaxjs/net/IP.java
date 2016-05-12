/**
 * Copyright 2015 Frank Cheung
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
package com.ajaxjs.net;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;

import com.ajaxjs.util.LogHelper;
import com.ajaxjs.util.StringUtil;
import com.ajaxjs.util.Util;

/**
 * IP 工具类
 */
public class IP {
	private static final LogHelper LOGGER = LogHelper.getLog(IP.class);

	/**
	 * 获取请求的 IP 地址
	 * 
	 * @param request
	 *            请求对象
	 * @return IP 地址
	 */
	public static String getIP(HttpServletRequest request) {
		String s = request.getHeader("X-Forwarded-For");
		if (StringUtil.isEmptyString(s) || "unknown".equalsIgnoreCase(s))
			s = request.getHeader("Proxy-Client-IP");
		if (StringUtil.isEmptyString(s) || "unknown".equalsIgnoreCase(s))
			s = request.getHeader("WL-Proxy-Client-IP");
		if (StringUtil.isEmptyString(s) || "unknown".equalsIgnoreCase(s))
			s = request.getHeader("HTTP_CLIENT_IP");
		if (StringUtil.isEmptyString(s) || "unknown".equalsIgnoreCase(s))
			s = request.getHeader("HTTP_X_FORWARDED_FOR");
		if (StringUtil.isEmptyString(s) || "unknown".equalsIgnoreCase(s))
			s = request.getRemoteAddr(); // REMOTE_ADDR

		return s;
	}

	/**
	 * 返回本地主机 InetAddress 对象
	 * 
	 * @return
	 */
	private static InetAddress getLocalhost() {
		try {
			return InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			LOGGER.warning("返回本地主机失败！", e);
			return null;
		}
	}

	/**
	 * 获取本机的IP 地址字符串
	 * 
	 * @return
	 */
	public static String getLocalHostIP() {
		return getLocalhost().getHostAddress();
	}

	/**
	 * 获取本机主机名
	 * 
	 * @return
	 */
	public static String getLocalHostName() {
		return getLocalhost().getHostName();
	}

	/**
	 * 用于局域网内的测试机器
	 * 
	 * @cache
	 */
	private static String localIp = null;

	/**
	 * 获取本机 ip 地址
	 * 
	 * @return
	 */
	public static String getLocalIp() {
		if (localIp == null) {
			localIp = getReal_LAN_IP();
			if (localIp == null)
				localIp = "localhost";// 还是 null，那就本机的……没开网卡？
		}

		return localIp;
	}

	/**
	 * 获得本地所有的IP地址
	 * 
	 * @return
	 */

	public static String[] getAllLocalHostIP() {
		String[] ips = null;

		String hostName = getLocalHostName();// 获得主机名
		InetAddress[] addrs = null;

		try {
			// 在给定主机名的情况下，根据系统上配置的名称服务返回其 IP 地址所组成的数组。
			addrs = InetAddress.getAllByName(hostName);
		} catch (UnknownHostException e) {
			LOGGER.warning("未知主机：" + hostName, e);
		}

		if (Util.isNotNull(addrs)) {
			ips = new String[addrs.length];
			int i = 0;
			for (InetAddress addr : addrs)
				ips[i++] = addr.getHostAddress();
		}

		return ips;
	}

	/**
	 * 以 192.168.x.x 开头的都是局域网内的 ip
	 * 
	 * @return
	 */
	public static String getReal_LAN_IP() {
		for (String ip : getAllLocalHostIP()) {
			if (ip.startsWith("192.168."))
				return ip;
		}

		return null;
	}

	/**
	 * 输入主机名，返回其 ip，相当于cmd 的 ping
	 * 
	 * @param hostname
	 *            例如 www.baidu.com
	 * @return
	 */
	public static String getIpByHostName(String hostname) {
		InetAddress ia = null;

		try {
			ia = InetAddress.getByName("www.baidu.com");
		} catch (UnknownHostException e) {
			LOGGER.warning("获取 ip 失败！" + hostname, e);
		}

		return ia == null ? null : ia.getHostAddress();
	}

}