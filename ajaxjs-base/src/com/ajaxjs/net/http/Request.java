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
import java.nio.charset.StandardCharsets;
import java.util.Map;

import com.ajaxjs.util.io.StreamChain;

/**
 * 携带请求信息的 Bean
 * @author frank
 *
 */
public abstract class Request<T> extends StreamChain<T> {
	/**
	 * 请求目标地址
	 */
	private String url;

	/**
	 * 请求方法,默认 GET
	 */
	private String method = "GET";

	/**
	 * 超时，单位:秒
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
	 * 自定义 http 头
	 */
	private Map<String, String> headers;
	
	/**
	 * 是否启动 GZip 请求，默认为 false
	 */
	private boolean isEnableGzip;

	/**
	 * 设置响应内容的编码
	 */
	private String encoding = StandardCharsets.UTF_8.toString();

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
	private Callback<T> callback;
	
	/**
	 * 请求是否ok，是否确定已经发出去了（不管如何响应）
	 */
	private boolean isDone;

	/**
	 * 
	 * @author Frank Cheung frank@ajaxjs.com
	 * @version 2017年7月17日 上午10:37:48 
	 * @param <T>
	 */
	public static interface Callback<T> {
		/**
		 * 当有数据进入时……
		 * 
		 * @param is
		 *            请求回来的响应流
		 */
		public void onDataLoad(InputStream is);
	}
	
	public String getUrl() {
		return url;
	}

	@SuppressWarnings("unchecked")
	public T setUrl(String url) {
		this.url = url;
		return (T) this;
	}

	public String getMethod() {
		return method;
	}

	@SuppressWarnings("unchecked")
	public T setMethod(String method) {
		this.method = method;
		return (T) this;
	}

	public int getTimeout() {
		return timeout;
	}

	@SuppressWarnings("unchecked")
	public T setTimeout(int timeout) {
		this.timeout = timeout;
		return (T) this;
	}

	public String getHost() {
		return host;
	}

	@SuppressWarnings("unchecked")
	public T setHost(String host) {
		this.host = host;
		return (T) this;
	}

	public String getUserAgent() {
		return UserAgent;
	}

	@SuppressWarnings("unchecked")
	public T setUserAgent(String userAgent) {
		UserAgent = userAgent;
		return (T) this;
	}

	public Map<String, String> getCookies() {
		return cookies;
	}

	@SuppressWarnings("unchecked")
	public T setCookies(Map<String, String> cookies) {
		this.cookies = cookies;
		return (T) this;
	}

	public String getEncoding() {
		return encoding;
	}

	@SuppressWarnings("unchecked")
	public T setEncoding(String encoding) {
		this.encoding = encoding;
		return (T) this;
	}

	public String[] getBasicAuthorization() {
		return basicAuthorization;
	}

	@SuppressWarnings("unchecked")
	public T setBasicAuthorization(String[] basicAuthorization) {
		this.basicAuthorization = basicAuthorization;
		return (T) this;
	}

	public boolean isTextResponse() {
		return isTextResponse;
	}

	@SuppressWarnings("unchecked")
	public T setTextResponse(boolean isTextResponse) {
		this.isTextResponse = isTextResponse;
		return (T) this;
	}

	public Callback<T> getCallback() {
		return callback;
	}

	@SuppressWarnings("unchecked")
	public T setCallback(Callback<T> callback) {
		this.callback = callback;
		return (T) this;
	}

	public boolean isDone() {
		return isDone;
	}

	@SuppressWarnings("unchecked")
	public T setDone(boolean isDone) {
		this.isDone = isDone;
		return (T) this;
	}

	/**
	 * @return {@link #headers}
	 */
	public Map<String, String> getHeaders() {
		return headers;
	}

	/**
	 * @param headers {@link #headers}
	 */
	@SuppressWarnings("unchecked")
	public T setHeaders(Map<String, String> headers) {
		this.headers = headers;
		return (T) this;
	}

	/**
	 * @return {@link #isEnableGzip}
	 */
	public boolean isEnableGzip() {
		return isEnableGzip;
	}

	/**
	 * @param isEnableGzip {@link #isEnableGzip}
	 */
	@SuppressWarnings("unchecked")
	public T setEnableGzip(boolean isEnableGzip) {
		this.isEnableGzip = isEnableGzip;
		return (T) this;
	}
}
