/**
 * Copyright 2015 Sp42 frank@ajaxjs.com
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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.zip.GZIPInputStream;

import com.ajaxjs.keyvalue.MapHelper;
import com.ajaxjs.util.Encode;
import com.ajaxjs.util.logger.LogHelper;

/**
 * 远程连接
 * 
 * @author Sp42 frank@ajaxjs.com
 *
 * @param <T>
 *            因为泛型的缘故，不好直接使用 Stream，故特提供该泛型
 */
public class Connection<T> extends Request<T> {
	private static final LogHelper LOGGER = LogHelper.getLog(Connection.class);

	/**
	 * 连接对象，暴露出去以方便配置
	 */
	private HttpURLConnection connection;

	/**
	 * 创建一个远程连接对象
	 * 
	 * @param urlStr
	 *            URL 地址
	 */
	public Connection(String urlStr) {
		setUrl(urlStr);

		URL url = null;

		try {
			url = new URL(getUrl());
		} catch (MalformedURLException e) {
			LOGGER.warning(e, "初始化连接出错！URL {0} 格式不对！", getUrl());
			return;
		}

		try {
			connection = (HttpURLConnection) url.openConnection();
		} catch (IOException e) {
			LOGGER.warning(e, "初始化连接出错！URL {0}。", getUrl());
			return;
		}

		try {
			connection.setRequestMethod(getMethod());
		} catch (ProtocolException e) {
			LOGGER.warning(e, "初始化连接出错！方法出错，URL {0}。", getUrl());
			return;
		}

		connection.addRequestProperty("Referer", getUrl() == null ? url.getHost() : getUrl());
	}

	/**
	 * 配置请求参数
	 * 
	 * @param cc
	 *            配置请求参数
	 * @return 返回该对象，供链式调用
	 */
	@SuppressWarnings("unchecked")
	public T customConnection(CustomConnection cc) {
		cc.callback(connection);
		return (T) this;
	}

	/**
	 * 配置请求参数
	 * 
	 * @author Sp42 frank@ajaxjs.com
	 *
	 */
	public static interface CustomConnection {
		/**
		 * 回调函数
		 * 
		 * @param connection
		 *            HttpURLConnection 连接
		 */
		public void callback(HttpURLConnection connection);
	}

	/**
	 * 发起请求 send()
	 * 
	 * @return 返回该对象，供链式调用
	 * @throws ConnectException
	 */
	@SuppressWarnings("unchecked")
	public T connect() throws ConnectException {
		init();

		// 写入数据（POST/PUT ONLY， GET 不需要）
		if (getData() != null && !getMethod().equalsIgnoreCase("GET")) {
			try {
				setOut(connection.getOutputStream());
			} catch (IOException e) {
				throw new ConnectException("写入 post 数据时失败！");
			}
			outputWriteData();// 写入 POST 数据
		}

		// 接受响应
		try {
			setIn(connection.getInputStream());

			// 是否启动 GZip 请求
			// 有些网站强制加入 Content-Encoding:gzip，而不管之前的是否有 GZip 的请求
			boolean isGzip = isEnableGzip() || "gzip".equals(connection.getHeaderField("Content-Encoding"));

			ConnectException err = checkErrCode(isGzip);
			if (err != null)
				throw err;

			if (getCallback() != null) {
				getCallback().onDataLoad(getIn());
			}

			if (isTextResponse()) {
				if (isGzip)
					setIn(new GZIPInputStream(getIn()));

				byteStream2stringStream();
			}

		} catch (UnknownHostException e) {
			throw new ConnectException("未知地址！" + getUrl());
		} catch (FileNotFoundException e) {
			throw new ConnectException("404 地址！" + getUrl());
		} catch (SocketTimeoutException e) {
			throw new ConnectException("请求地址超时！" + getUrl());
		} catch (IOException e) {
			try {
				LOGGER.info("响应异常，响应代码：" + connection.getResponseCode());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			throw new ConnectException("请求地址 IO 异常！" + getUrl());
		} finally {
			close();// 自动关闭流
		}

		return (T) this;
	}

	/**
	 * 检查响应内容
	 * 
	 * @param isGzip
	 * @return 连接异常
	 * @throws IOException
	 */
	private ConnectException checkErrCode(boolean isGzip) throws IOException {
		if (connection.getResponseCode() >= 400) {// 如果返回的结果是400以上，那么就说明出问题了
			ConnectException e = null;

			if (connection.getResponseCode() < 500) {
				e = new ConnectException(connection.getResponseCode() + "：客户端请求参数错误！");
			} else {
				e = new ConnectException(connection.getResponseCode() + "：抱歉！我们服务端出错了！");
			}

			if (isGzip)
				setIn(new GZIPInputStream(getIn()));
			byteStream2stringStream();

			e.setFeedback(getContent());
		}

		return null;
	}

	/**
	 * 内部初始化
	 */
	private void init() {
		connection.addRequestProperty("User-Agent", getUserAgent());
		connection.setConnectTimeout(getTimeout() * 1000);// 设置超时
		// connection.setReadTimeout(30000);

		if (getCookies() != null) {
			String cookieStr = MapHelper.join(getCookies(), ";");
			connection.setRequestProperty("Cookie", cookieStr);
		}

		if (isEnableGzip()) // 是否启动 GZip 请求
			connection.addRequestProperty("Accept-Encoding", "gzip, deflate");

		if (getBasicAuthorization() != null) { // HTTP 用户认证
			String username = getBasicAuthorization()[0], password = getBasicAuthorization()[1];
			String encoding = Encode.base64Encode(username + ":" + password);
			connection.setRequestProperty("Authorization", "Basic " + encoding);
		}
	}

	/**
	 * 获取连接对象，暴露出去以方便配置
	 * 
	 * @return 连接对象，暴露出去以方便配置
	 */
	public HttpURLConnection getConnection() {
		return connection;
	}

	/**
	 * 设置连接对象
	 * 
	 * @param connection
	 *            连接对象
	 * @return 返回该对象，供链式调用
	 */
	@SuppressWarnings("unchecked")
	public T setConnection(HttpURLConnection connection) {
		this.connection = connection;

		return (T) this;
	}
}
