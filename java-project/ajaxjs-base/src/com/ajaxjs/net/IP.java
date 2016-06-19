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

/**
 * IP 工具类
 */
public class IP {
	/**
	 * 返回本地主机 InetAddress 对象
	 * 
	 * @return InetAddress 对象
	 */
	private static InetAddress getLocalhost() {
		try {
			return InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			System.out.println("返回本地主机失败！");
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取本机的 IP 地址字符串
	 * 
	 * @return 本机 IP
	 */
	public static String getLocalHostIP() {
		return getLocalhost().getHostAddress();
	}

	/**
	 * 获取本机主机名
	 * 
	 * @return 本机主机名
	 */
	public static String getLocalHostName() {
		return getLocalhost().getHostName();
	}

	/**
	 * 用于局域网内的测试机器
	 */
	private static String localIp = null;

	/**
	 * 获取本机 IP 地址
	 * 
	 * @return 本机 IP
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
	 * 获得本地所有的 IP 地址
	 * 
	 * @return 本机所有 IP
	 */
	public static String[] getAllLocalHostIP() {
		String[] ips = null;

		String hostName = getLocalHostName();// 获得主机名
		InetAddress[] addrs = null;

		try {
			// 在给定主机名的情况下，根据系统上配置的名称服务返回其 IP 地址所组成的数组。
			addrs = InetAddress.getAllByName(hostName);
		} catch (UnknownHostException e) {
			System.out.println("未知主机：" + hostName);
			e.printStackTrace();
		}

		if (addrs !=null ) {
			ips = new String[addrs.length];
			int i = 0;
			for (InetAddress addr : addrs)
				ips[i++] = addr.getHostAddress();
		}

		return ips;
	}

	/**
	 * 以 192.168.x.x 开头的都是局域网内的 IP
	 * 
	 * @return 局域网内的 IP
	 */
	public static String getReal_LAN_IP() {
		for (String ip : getAllLocalHostIP()) {
			if (ip.startsWith("192.168."))
				return ip;
		}

		return null;
	}

	/**
	 * 输入主机名，返回其 IP，相当于 cmd 的 ping
	 * 
	 * @param hostname
	 *            例如 www.baidu.com
	 * @return 主机对应的 IP
	 */
	public static String getIpByHostName(String hostname) {
		InetAddress ia = null;

		try {
			ia = InetAddress.getByName("www.baidu.com");
		} catch (UnknownHostException e) {
			System.err.println("获取 ip 失败！" + hostname);
			e.printStackTrace();
		}

		return ia == null ? null : ia.getHostAddress();
	}

}