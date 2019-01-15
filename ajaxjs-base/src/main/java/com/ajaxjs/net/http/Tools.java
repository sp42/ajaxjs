package com.ajaxjs.net.http;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class Tools {
	static String ip;

	/**
	 * 获取本机 ip，带缓存的
	 * 
	 * @return
	 */
	public static String getIp() {
		if (ip == null)
			ip = getLocalHostLANAddress().getHostAddress();

		return ip;
	}

	public static InetAddress getLocalHostLANAddress() {
		InetAddress candidateAddress = null;

		try {

			// 遍历所有的网络接口
			for (Enumeration<?> ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements();) {
				NetworkInterface iface = (NetworkInterface) ifaces.nextElement();

				// 在所有的接口下再遍历IP
				for (Enumeration<?> inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements();) {
					InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();

					if (!inetAddr.isLoopbackAddress()) {// 排除loopback类型地址
						if (inetAddr.isSiteLocalAddress())
							return inetAddr;// 如果是site-local地址，就是它了
						else if (candidateAddress == null)
							candidateAddress = inetAddr;// site-local类型的地址未被发现，先记录候选地址

					}
				}
			}

			if (candidateAddress != null)
				candidateAddress = InetAddress.getLocalHost();// 如果没有发现 non-loopback地址.只能用最次选的方案
		} catch (Exception e) {
			System.err.println(e);
		}

		return candidateAddress;
	}

}
