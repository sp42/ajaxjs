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
package com.ajaxjs.http;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.GZIPInputStream;

import com.ajaxjs.util.StringUtil;

import sun.misc.BASE64Encoder;

/**
 * 
 * @author frank
 *
 * @param <T>
 */
public class Connection<T> extends Request<T> {
	/**
	 * 连接对象
	 */
	private HttpURLConnection connection;
	
	/**
	 * 
	 */
	public Connection() {
		URL url = null;
		
		try {
			url = new URL(getUrl());
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod(getMethod());
		} catch (IOException e) {
			System.err.println("初始化连接出错！" + getUrl());
			e.printStackTrace();
		}
		
		connection.addRequestProperty("Referer", getUrl() == null ? url.getHost() : getUrl());
	}
	
	/**
	 * 配置请求参数
	 * @param cc
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public T customConnection(CustomConnection cc) {
		cc.callback(connection);
		return (T) this;
	}

	/**
	 * 配置请求参数
	 */
	public static interface CustomConnection {
		public void callback(HttpURLConnection connection);
	}
	
	/**
	 * 发起请求 send()
	 * @return
	 * @throws ConnectException
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public T connect() throws ConnectException, IOException {
		init();
		
		// 写入数据（POST/PUT ONLY， GET 不需要）
		if (getData() != null && !getMethod().equalsIgnoreCase("GET")) {
			setOut(connection.getOutputStream());
			outputWriteData();// 写入 POST 数据
		}
		
		// 接受响应
		setIn(connection.getInputStream());
		
		// 是否启动 GZip 请求
		// 有些网站强制加入 Content-Encoding:gzip，而不管之前的是否有 GZip 的请求
		boolean isGzip = isEnableGzip() || "gzip".equals(connection.getHeaderField("Content-Encoding"));
		
		ConnectException err = checkErrCode(isGzip);
		if(err != null)throw err;
		
		if (getCallback() != null) {
			getCallback().onDataLoad(getIn());
		}
		
		if (isTextResponse()){
			if(isGzip)
				setIn(new GZIPInputStream(getIn()));
			byteStream2stringStream();
		}
		
		close();// 自动关闭流
		
		return (T) this;
	}
	
	/**
	 * 检查响应内容
	 * @param isGzip
	 * @return
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
			
			if(isGzip)
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
			String cookieStr = StringUtil.HashJoin(getCookies(), ";");
			connection.setRequestProperty("Cookie", cookieStr);
		}
		
		if(isEnableGzip()) // 是否启动 GZip 请求
			connection.addRequestProperty("Accept-Encoding", "gzip, deflate"); 

		if (getBasicAuthorization() != null) { // HTTP 用户认证
			String username = getBasicAuthorization()[0], password = getBasicAuthorization()[1];
			String encoding = new BASE64Encoder().encode((username + ":" + password).getBytes());
			connection.setRequestProperty("Authorization", "Basic " + encoding);
		}
	}
}
