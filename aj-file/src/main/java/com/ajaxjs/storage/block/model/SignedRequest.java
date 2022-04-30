package com.ajaxjs.storage.block.model;

import java.net.URL;
import java.util.Map;

/**
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public class SignedRequest {
	/**
	 * 要进行上传/下载的 URL，当下载时如果该值为空（可秒传），则表示存储服务已经有该文件的内容，无需再进去上传，直接提交即可
	 */
	private URL url;

	/**
	 * 上传/下载时采用的 HTTP METHOD
	 */
	private String method;

	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	/**
	 * 在进行上传/下载时需要设置的请求头，当上传时，如果要进行秒传判断，则需要设置 Content-MD5 HTTP 头
	 */
	private Map<String, String> headers;
}