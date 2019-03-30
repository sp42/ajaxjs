package com.ajaxjs.net.http;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.Map;

import com.ajaxjs.util.MapTool;

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

	/**
	 * 域名是否已经注册
	 * 
	 * @param domain 域名
	 * @return true=可以注册
	 * @throws IOException
	 */
	public static boolean isDomianRegisterAvailable(String domain) throws IOException {
		String url = "http://panda.www.net.cn/cgi-bin/check.cgi?area_domain=" + domain;
		String xml = NetUtil.simpleGET(url);
		Map<String, String> map = MapTool.xmlToMap(xml);

		if ("200".equals(map.get("returncode"))) {
			if (map.get("original").startsWith("210")) {
				return true; // original=210 : Domain name is available 表示域名可以注册
			} else if (map.get("original").startsWith("211")) {
				return false;// original=211 : Domain name is not available 表示域名已经注册
			} else if (map.get("original").startsWith("212")) {
				throw new IOException("域名参数传输错误");
			} else {
				throw new IOException("未知错误！ " + map);
			}
		} else {
			throw new IOException("接口返回不成功 " + map);
		}
	}

	/**
	 * 域名 whois 查询 https://www.nowapi.com/api/domain.whois
	 * 
	 * @param domain 域名
	 * @return 域名详情
	 * @throws IOException
	 */
	public static Map<String, String> getWhois(String domain) throws IOException {
		String url = "http://api.k780.com/?app=domain.whois&appkey=10003&sign=b59bc3ef6191eb9f747dd4e83c99f2a4&format=xml&domain=" + domain;
		String xml = NetUtil.simpleGET(url);
		Map<String, String> map = MapTool.xmlToMap(xml);

		if ("1".equals(map.get("success"))) {
			return map;
		} else {
			throw new IOException("接口返回不成功 " + map);
		}
	}
}
