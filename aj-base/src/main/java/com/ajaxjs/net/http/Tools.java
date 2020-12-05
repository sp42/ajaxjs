/**
 * Copyright sp42 frank@ajaxjs.com
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
package com.ajaxjs.net.http;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.util.map.JsonHelper;
import com.ajaxjs.util.map.MapTool;

/**
 * ip 工具类
 * 
 * @author sp42
 *
 */
public class Tools {
	private static final LogHelper LOGGER = LogHelper.getLog(Tools.class);

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
			LOGGER.warning(e);
		}

		return candidateAddress;
	}

	/**
	 * 如果 getLocalHostLANAddress() 放在不能连接公网的环境，那个方法就不适用了，可以使用这个方法
	 * 
	 * @return 本地 ip 地址
	 */
	public static String getLocalIp() {
		try (Socket socket = new Socket();) {
			socket.connect(new InetSocketAddress("baidu.com", 80));
			return socket.getLocalAddress().getHostAddress();
		} catch (IOException e) {
			LOGGER.warning(e);
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
			LOGGER.warning(e);
			return null;
		}
	}

	/**
	 * 域名是否已经注册
	 * 
	 * @param domain 域名
	 * @return true=可以注册
	 * @throws IOException 访问异常
	 */
	public static boolean isDomianRegisterAvailable(String domain) throws IOException {
		String url = "http://panda.www.net.cn/cgi-bin/check.cgi?area_domain=" + domain;
		String xml = NetUtil.simpleGET(url);
		Map<String, String> map = MapTool.xmlToMap(xml);

		if ("200".equals(map.get("returncode"))) {
			if (map.get("original").startsWith("210"))
				return true; // original=210 : Domain name is available 表示域名可以注册
			else if (map.get("original").startsWith("211"))
				return false;// original=211 : Domain name is not available 表示域名已经注册
			else if (map.get("original").startsWith("212"))
				throw new IOException("域名参数传输错误");
			else
				throw new IOException("未知错误！ " + map);
		} else
			throw new IOException("接口返回不成功 " + map);
	}

	/**
	 * 域名 whois 查询 https://www.nowapi.com/api/domain.whois
	 * 
	 * @param domain 域名
	 * @return 域名详情
	 * @throws IOException 访问异常
	 */
	public static Map<String, String> getWhois(String domain) throws IOException {
		String url = "http://api.k780.com/?app=domain.whois&appkey=10003&sign=b59bc3ef6191eb9f747dd4e83c99f2a4&format=xml&domain=" + domain;
		String xml = NetUtil.simpleGET(url);
		Map<String, String> map = MapTool.xmlToMap(xml);

		if ("1".equals(map.get("success")))
			return map;
		else
			throw new IOException("接口返回不成功 " + map);

	}

	/**
	 * http://ip.taobao.com/instructions.html http://blog.zhukunqian.com/?p=1998
	 * http://pv.sohu.com/cityjson?ie=utf-8 https://gitee.com/meaktsui/ChinaIpSearch
	 * 
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> getIpLocation(String ip) throws IOException {
		String url = "http://ip.taobao.com/service/getIpInfo.php?ip=" + ip;
		String xml = NetUtil.simpleGET(url);
		Map<String, Object> map = JsonHelper.parseMap(xml);

		if (map != null && map.get("code") != null && (0 == (int) map.get("code"))) {
			Object obj = map.get("data");

			return (Map<String, Object>) obj;
		} else
			throw new IOException("接口返回不成功 " + map);

	}

	public static String getIpLocation2(String ip) throws IOException {
		String url = "http://ip-api.com/json/" + ip + "?lang=zh-CN";
		String jsonStr = NetUtil.get(url);

		Map<String, Object> map = JsonHelper.parseMap(jsonStr);

		if (jsonStr != null && map != null && map.get("status").toString().equals("success")) {
			return "" + map.get("country") + map.get("regionName") + map.get("city");
		} else
			throw new IOException("接口返回不成功 " + jsonStr);
	}

	/**
	 * 判断IP是否内网
	 * 
	 * @param ip
	 * @return
	 */
	public static boolean innerIP(String ip) {
		Pattern reg = Pattern.compile(
				"^(127\\.0\\.0\\.1)|(localhost)|(10\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})|(172\\.((1[6-9])|(2\\d)|(3[01]))\\.\\d{1,3}\\.\\d{1,3})|(192\\.168\\.\\d{1,3}\\.\\d{1,3})$");
		Matcher match = reg.matcher(ip);

		return match.find();
	}
}
