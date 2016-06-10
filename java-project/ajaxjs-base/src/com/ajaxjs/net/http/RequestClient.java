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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;

import com.ajaxjs.Constant;
import com.ajaxjs.util.LogHelper;
import com.ajaxjs.util.StringUtil;

import sun.misc.BASE64Encoder;

/**
 * 发起 HTTP 请求
 * @author frank
 *
 */
public class RequestClient {
	private static final LogHelper LOGGER = LogHelper.getLog(RequestClient.class);

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
			LOGGER.warning("初始化连接出错！" + request.getUrl(), e);
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

		if (request.getBasicAuthorization() != null) { // HTTP 用户认证
			String username = request.getBasicAuthorization()[0], password = request.getBasicAuthorization()[1];
			String encoding = new BASE64Encoder().encode((username + ":" + password).getBytes());
			connection.setRequestProperty("Authorization", "Basic " + encoding);
		}
	}

	/**
	 * 发起请求
	 * 
	 * @return
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
				LOGGER.warning(e);
				return false;
			}
		}

		// 接受响应
		try (InputStream is = connection.getInputStream();) {
			if (connection.getResponseCode() >= 400) {// 如果返回的结果是400以上，那么就说明出问题了
				ConnectException e = null;

				if (connection.getResponseCode() < 500) {
					e = new ConnectException(connection.getResponseCode() + "：客户端请求参数错误！");
				} else {
					e = new ConnectException(connection.getResponseCode() + "：抱歉！我们服务端出错了！");
				}

				String msg = stream2String(is, request.getEncoding());
				e.setFeedback(msg);

				if (request.isTextResponse())
					request.setFeedback(msg);
				throw e;
			}
			if (request.getCallback() != null) {
				request.getCallback().onDataLoad(is);
			}
			if (request.isTextResponse())
				request.setFeedback(stream2String(is, request.getEncoding()));

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

	/**
	 * 字节流转换为字符串。注意对于送入的流，执行完毕后会自动关闭。 input=读、output=写
	 * 
	 * @param is
	 *            流对象
	 * @param charset
	 *            字符集
	 * @return
	 */
	static String stream2String(InputStream is, String charset) {
		String line = null;
		StringBuilder result = new StringBuilder();

		// InputStreamReader从一个数据源读取字节，并自动将其转换成Unicode字符
		// OutputStreamWriter将字符的Unicode编码写到字节输出流
		try (
				// 指定编码
				InputStreamReader isReader = new InputStreamReader(is, charset);
				/*
				 * Decorator，装饰模式，又称为 Wrapper，使它具有了缓冲功能
				 * BufferedInputStream、BufferedOutputStream
				 * 只是在这之前动态的为它们加上一些功能（像是缓冲区功能）
				 */
				BufferedReader reader = new BufferedReader(isReader);) {
			while ((line = reader.readLine()) != null) { // 一次读入一行，直到读入null为文件结束
				// 指定编码集的另外一种方法 line = new String(line.getBytes(),
				// encodingSet);
				result.append(line);
				result.append(Constant.newline);
			}
		} catch (IOException e) {
			LOGGER.warning(e);
		}

		return result.toString();
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
