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
package com.ajaxjs.net.http;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.zip.GZIPInputStream;

import com.ajaxjs.util.FileUtil;
import com.ajaxjs.util.StringUtil;

import sun.misc.BASE64Encoder;

/**
 * 发起 HTTP 请求
 * @author frank
 *
 */
public class RequestClient {

	/**
	 * 请求目标地址
	 */
	private URL url;

	/**
	 * API 链接对象
	 */
	private HttpURLConnection connection;

	/**
	 * 携带请求信息的 Bean
	 */
	private Request request;

	/**
	 * 创建 HTTP 请求
	 * 
	 * @param request
	 *            请求信息对象
	 */
	public RequestClient(Request request) {
		this.request = request;
		try {
			url = new URL(request.getUrl());
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod(request.getMethod());
		} catch (IOException e) {
			System.err.println("初始化连接出错！" + request.getUrl());
			e.printStackTrace();
		}
	}

	/**
	 * 内部初始化
	 */
	private void init() {
		connection.addRequestProperty("User-Agent", request.getUserAgent());
		connection.addRequestProperty("Referer", request.getUrl() == null ? url.getHost() : request.getUrl());
		connection.setConnectTimeout(request.getTimeout() * 1000);// 设置超时
		// connection.setReadTimeout(30000);

		if (request.getCookies() != null) {
			String cookieStr = StringUtil.HashJoin(request.getCookies(), ";");
			connection.setRequestProperty("Cookie", cookieStr);
		}
		
		if(request.isEnableGzip()) // 是否启动 GZip 请求
			connection.addRequestProperty("Accept-Encoding", "gzip, deflate"); 

		if (request.getBasicAuthorization() != null) { // HTTP 用户认证
			String username = request.getBasicAuthorization()[0], password = request.getBasicAuthorization()[1];
			String encoding = new BASE64Encoder().encode((username + ":" + password).getBytes());
			connection.setRequestProperty("Authorization", "Basic " + encoding);
		}
	}

	/**
	 * 发起请求
	 * 
	 * @return true 表示为发起请求成功
	 * @throws ConnectException
	 */
	public boolean connect() throws ConnectException {
		init();

		// 写入数据（POST ONLY， GET 不需要）
		if (request.getWriteData() != null && !request.getMethod().equalsIgnoreCase("GET")) {
			// 写入 POST 数据
			try (OutputStream os = connection.getOutputStream()) {
				os.write(request.getWriteData());
				os.flush();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		
		// 接受响应
		try (InputStream is = connection.getInputStream();) {
			// 是否启动 GZip 请求
			// 有些网站强制加入 Content-Encoding:gzip，而不管之前的是否有 GZip 的请求
			boolean isGzip = request.isEnableGzip() || "gzip".equals(connection.getHeaderField("Content-Encoding"));
			
			if (connection.getResponseCode() >= 400) {// 如果返回的结果是400以上，那么就说明出问题了
				ConnectException e = null;

				if (connection.getResponseCode() < 500) {
					e = new ConnectException(connection.getResponseCode() + "：客户端请求参数错误！");
				} else {
					e = new ConnectException(connection.getResponseCode() + "：抱歉！我们服务端出错了！");
				}
				String msg = FileUtil.readText(isGzip ? new GZIPInputStream(is) : is);
				e.setFeedback(msg);

				if (request.isTextResponse())
					request.setFeedback(msg);
				throw e;
			}
			if (request.getCallback() != null) {
				request.getCallback().onDataLoad(is);
			}
			if (request.isTextResponse())
				request.setFeedback(FileUtil.readText(isGzip ? new GZIPInputStream(is) : is));

		} catch (UnknownHostException e) {
			throw new ConnectException("未知地址！" + request.getUrl());
		} catch (FileNotFoundException e) {
			throw new ConnectException("404 地址！" + request.getUrl());
		} catch (SocketTimeoutException e) {
			throw new ConnectException("请求地址超时！" + request.getUrl());
		} catch (IOException e) {
			try {
				System.out.println(connection.getResponseCode());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			throw new ConnectException("请求地址 IO 异常！" + request.getUrl());
		}

		return true;
	} 

	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}

	public Request getRequest() {
		return request;
	}

	public void setRequest(Request request) {
		this.request = request;
	}

	public HttpURLConnection getConnection() {
		return connection;
	}

	public void setConnection(HttpURLConnection connection) {
		this.connection = connection;
	}

}
