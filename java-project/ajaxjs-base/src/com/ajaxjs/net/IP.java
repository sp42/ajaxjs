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

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import com.ajaxjs.io.StreamUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.spi.HttpServerProvider;

/**
 * IP 工具类
 */
public class IP {
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
		} catch (UnknownHostException e) {
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

	/**
	 * 输入主机名，返回其 IP，相当于 cmd 的 ping 所得到的域名
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
	
	/**
	 * 启动 HTTP 服务，监听来自客户端的请求
	 * @throws IOException
	 */
	public static void httpServer() {
		HttpServer httpserver;
		
		try {
			// 监听端口8081,能同时接受100个请求
			httpserver = HttpServerProvider.provider().createHttpServer(new InetSocketAddress(8081), 100);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		httpserver.createContext("/myApp", new MyHttpHandler());
		httpserver.setExecutor(null);
		httpserver.start();
		
		System.out.println("server started");
	}

	/**
	 * Http请求处理类
	 *  * 自定义的http服务器
	 *  http://blog.csdn.net/maosijunzi/article/details/41045181
	 *   http://blog.163.com/web_promise/blog/static/1096316552011224101531794/
	 * @author frank
	 */
	static class MyHttpHandler implements HttpHandler {
		@Override
		public void handle(HttpExchange httpExchange) throws IOException {
			StreamUtil s = new StreamUtil();

			String result = s.setIn(httpExchange.getRequestBody()).byteStream2stringStream().getContent();
			System.out.println("客户端请求:" + result);

			String responseMsg = "ok"; // 响应信息
			httpExchange.sendResponseHeaders(200, responseMsg.length()); // 设置响应头属性及响应信息的长度

			s.setOut(httpExchange.getResponseBody()).setData(responseMsg.getBytes()).stringStream2output();
			s.close();

			httpExchange.close();
		}
	}
}