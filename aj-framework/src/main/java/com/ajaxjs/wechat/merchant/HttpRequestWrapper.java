package com.ajaxjs.wechat.merchant;

/**
 * 
 * 请求接口的 HTTP 方法、URL 和 请求报文主体
 * 
 * @author Frank Cheung
 *
 */
public class HttpRequestWrapper {
	public String method;
	public String url;
	public String body = "";

	public HttpRequestWrapper() {
	}

	public HttpRequestWrapper(String method, String url) {
		this.method = method;
		this.url = url;
	}

	public HttpRequestWrapper(String method, String url, String body) {
		this(method, url);
		this.body = body;
	}
}
