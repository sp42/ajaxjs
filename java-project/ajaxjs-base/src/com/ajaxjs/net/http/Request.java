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

import java.io.InputStream;
import java.util.Map;

/**
 * 携带请求信息的 Bean
 * @author frank
 *
 */
public class Request {
	/**
	 * 请求目标地址
	 */
	private String url;

	/**
	 * 请求方法
	 */
	private String method = "GET";

	/**
	 * 超时，单位秒
	 */
	private int timeout = 5;

	/**
	 * 用户标识
	 */
	private String UserAgent = "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2";

	/**
	 * 主机，用于 Referer
	 */
	private String host;

	private Map<String, String> cookies;

	/**
	 * 设置响应内容的编码
	 */
	private String encoding = "UTF-8";

	/**
	 * BASIC HTTP 认证，数组[0] = 用户名/数组[1] = 密码
	 */
	private String[] basicAuthorization;

	/**
	 * 服务端是否会返回文本信息？
	 */
	private boolean isTextResponse = true;
	
	/**
	 * 如何处理响应信息？
	 */
	private Callback callback;
	
	/**
	 * 请求是否ok，是否确定已经发出去了（不管如何响应）
	 */
	private boolean isDone;
	
	/**
	 * 响应的内容，文本的
	 */
	private String feedback;
	
	/**
	 * 要写入的数据
	 */
	private byte[] writeData;
	
	/**
	 * 
	 * @author frank
	 *
	 */
	public static interface Callback {
		/**
		 * 当有数据进入时……
		 * 
		 * @param is
		 *            请求回来的响应流
		 */
		void onDataLoad(InputStream is);
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getUserAgent() {
		return UserAgent;
	}

	public void setUserAgent(String userAgent) {
		UserAgent = userAgent;
	}

	public Map<String, String> getCookies() {
		return cookies;
	}

	public void setCookies(Map<String, String> cookies) {
		this.cookies = cookies;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public String[] getBasicAuthorization() {
		return basicAuthorization;
	}

	public void setBasicAuthorization(String[] basicAuthorization) {
		this.basicAuthorization = basicAuthorization;
	}

	public boolean isTextResponse() {
		return isTextResponse;
	}

	public void setTextResponse(boolean isTextResponse) {
		this.isTextResponse = isTextResponse;
	}

	public Callback getCallback() {
		return callback;
	}

	public void setCallback(Callback callback) {
		this.callback = callback;
	}

	public boolean isDone() {
		return isDone;
	}

	public void setDone(boolean isDone) {
		this.isDone = isDone;
	}

	public String getFeedback() {
		return feedback;
	}

	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}

	public byte[] getWriteData() {
		return writeData;
	}

	public void setWriteData(byte[] writeData) {
		this.writeData = writeData;
	}
}
