/**
 * Copyright Sp42 frank@ajaxjs.com Licensed under the Apache License, Version
 * 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package com.ajaxjs.net.tools;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.URL;
import java.util.Enumeration;
import java.util.regex.Pattern;

public class Tools {
	private static Pattern INNER_IP_REG = null;

	/**
	 * 判断 IP 是否内网
	 * 
	 * @param ip IP 地址
	 * @return IP 是否内网
	 */
	public static boolean isInnerIP(String ip) {
		if (INNER_IP_REG == null)
			INNER_IP_REG = Pattern.compile(
					"^(127\\.0\\.0\\.1)|(localhost)|(10\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})|(172\\.((1[6-9])|(2\\d)|(3[01]))\\.\\d{1,3}\\.\\d{1,3})|(192\\.168\\.\\d{1,3}\\.\\d{1,3})$");

		return INNER_IP_REG.matcher(ip).find();
	}

	/**
	 * 顶级域名
	 * 
	 * @param url 域名
	 * @return 顶级域名
	 */
	public static String getDomainName(String url) {
		URL fullUrl = null;

		try {
			fullUrl = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		assert fullUrl != null;
		String host = fullUrl.getHost(); // 域名
		String[] levels = host.split("\\.");
//		if (levels.length > 1)

		return levels[levels.length - 2] + "." + levels[levels.length - 1];
	}

	public static String ip; // 本地 ip 地址缓存

	/**
	 * 获取本机 ip，带缓存的
	 * 
	 * @return 本地 ip 地址
	 */
	public static String getIp() {
		if (ip == null)
			ip = getLocalHostLANAddress().getHostAddress();

		return ip;
	}

	/**
	 * 获取本机局域网地址
	 * 
	 * @return 本机局域网地址对象
	 */
	public static InetAddress getLocalHostLANAddress() {
		InetAddress candidateAddress = null;

		try {
			// 遍历所有的网络接口
			for (Enumeration<?> ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements();) {
				NetworkInterface iface = (NetworkInterface) ifaces.nextElement();

				// 在所有的接口下再遍历 IP
				for (Enumeration<?> inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements();) {
					InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();

					if (!inetAddr.isLoopbackAddress()) {// 排除 loopback 类型地址
						if (inetAddr.isSiteLocalAddress())
							return inetAddr;// 如果是 site-local 地址，就是它了
						else if (candidateAddress == null)
							candidateAddress = inetAddr;// site-local 类型的地址未被发现，先记录候选地址
					}
				}
			}

			if (candidateAddress != null)
				candidateAddress = InetAddress.getLocalHost();// 如果没有发现 non-loopback 地址.只能用最次选的方案
		} catch (Exception e) {
//			LOGGER.warning(e);
		}

		return candidateAddress;
	}

	/**
	 * 如果 getLocalHostLANAddress() 放在不能连接公网的环境，那个方法就不适用了，可以使用这个方法
	 * 
	 * @return 本地 ip 地址
	 */
	public static String getLocalIp() {
		try (Socket socket = new Socket()) {
			socket.connect(new InetSocketAddress("baidu.com", 80));
			return socket.getLocalAddress().getHostAddress();
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * 第二种方法
	 * 
	 * @return 本地 ip 地址
	 */
	public static String getLocalIp2() {
		try (DatagramSocket socket = new DatagramSocket()) {
			socket.connect(InetAddress.getByName("114.114.114.114"), 10002);
			return socket.getLocalAddress().getHostAddress();
		} catch (IOException e) {
			return null;
		}
	}

}
