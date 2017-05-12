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
package com.ajaxjs.web;

import java.util.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import java.net.InetAddress;

/**
 * 该类对应 tagfile：head.tag
 * @author xinzhang
 *
 */
public class HtmlHead {
	/**
	 * ip 缓存
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
				if (ip.startsWith("192.168.")) { // 以 192.168.x.x 开头的都是局域网内的 IP
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
		} catch (Throwable e) {
			e.printStackTrace();
			return null;
		}

		String[] ips = null;
		if (addrs !=null ) {
			ips = new String[addrs.length];
			int i = 0;
			for (InetAddress addr : addrs)
				ips[i++] = addr.getHostAddress();
		}

		return ips;
	}
	
	// 默认 8080 端口
	private final static String picPath = "http://%s:8080/%s/asset/";

	/**
	 * 返回样式文件
	 * 
	 * @param lessPath
	 *            LESS 预编译文件路径
	 * @return CSS 地址
	 */
	public static String getCssUrl(ServletContext cxt, String lessPath, boolean isDebug) {
		String css = null;
		
		if (isDebug) {// 设置参数
			String ip = getLocalIp();
			Map<String, String> params = new HashMap<String, String>();
			params.put("lessFile", Mappath(cxt, lessPath)); // LESS 预编译文件完整的磁盘路径
			params.put("ns", Mappath(cxt, lessPath.replaceAll("\\/[\\w\\.]*$", ""))); // 去掉文件名，保留路径，也就是文件夹名称
			params.put("picPath", String.format(picPath, ip, cxt.getContextPath()));// 返回 CSS 背景图所用的图片
			params.put("MainDomain", "");
			params.put("isdebug", "true");

			css = "http://" + ip + "/lessService/?" + join(params);
		} else {
			css = cxt.getContextPath() + lessPath.replace("/less", "/css").replace(".less", ".css");
		}
		
		return css;
	}
	
	/**
	 * 也是 join，不过输入的参数不是数组而是 hash。
	 * 
	 * @param map
	 *            输入的 map
	 * @return 连续的字符串
	 */
	public static String join(Map<String, String> map) {
		String[] pairs = new String[map.size()];
		String div = "&";

		int i = 0;
		for (String key : map.keySet())
			pairs[i++] = key + "=" + map.get(key);

		/*
		 * 另外一种算法 // for (String key : pair.keySet()) cookieStr += key + "=" +
		 * pair.get(key) + ";"; // cookieStr = cookieStr.substring(0,
		 * cookieStr.length() - 1); // 删掉最后一个分号 // 另外一种算法 int i = 0; for(String
		 * key : hash.keySet()){ ... if(++i != size)buff.append(","); } //
		 * 另外一种算法，删除最后一个 , if (buff.length() > 1)buff =
		 * buff.deleteCharAt(buff.length() - 1); // 另外一种算法，删除最后一个 , ... if(i !=
		 * arr.length - 1)str += ",";
		 */
		return stringJoin(pairs, div);
	}
	
	/**
	 * Java String 有 split 却没有 join，这里实现一个
	 * 
	 * @param arr
	 *            输入的字符串数组
	 * @param join
	 *            分隔符
	 * @return 连接后的字符串
	 */
	public static String stringJoin(String[] arr, String join) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < arr.length; i++) {
			if (i == (arr.length - 1))
				sb.append(arr[i]);
			else
				sb.append(arr[i]).append(join);
		}

		return new String(sb);
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
	 * 返回样式文件
	 * 
	 * @param lessPath
	 *            LESS 路径
	 * @return CSS 地址
	 */
	public static String getCssUrl(HttpServletRequest request, String lessPath, boolean isDebug) {
		String css = null;
		
		if(lessPath == null)
			lessPath = "/asset/less/main.less"; // 默认 less 路径
		
		if (isDebug) {// 设置参数
			Map<String, String> params = new HashMap<String, String>();
			params.put("lessFile", Mappath(request, lessPath));
			params.put("ns", Mappath(request, lessPath.replaceAll("\\/[\\w\\.]*$", ""))); // 去掉文件名，保留路径，也就是文件夹名称
			params.put("picPath", getBasePath(request) + "/asset/images");// 返回 CSS 背景图所用的图片
			params.put("MainDomain", "");
			params.put("isdebug", "true");
			
			css = "http://" + getLocalIp() + "/lessService/?" + join(params);
		} else {
			css = request.getContextPath() + lessPath.replace("/less", "/css").replace(".less", ".css");
		}
		
		return css;
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
}
