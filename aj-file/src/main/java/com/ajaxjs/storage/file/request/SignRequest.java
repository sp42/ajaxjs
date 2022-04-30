package com.ajaxjs.storage.file.request;

import java.util.Map;

/**
 * 该对象主要用于文件的上传/下载签名，如果需要进行秒传，请设置 HTTP 头：Content-MD5
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public class SignRequest {
	/**
	 * 要进行签要的 HTTP 请求头
	 */
	private Map<String, String> requestHeaders;

	/**
	 * 要进行签名的 HTTP QUERY 参数
	 */
	private Map<String, String> requestParameters;

	public Map<String, String> getRequestHeaders() {
		return requestHeaders;
	}

	public void setRequestHeaders(Map<String, String> requestHeaders) {
		this.requestHeaders = requestHeaders;
	}

	public Map<String, String> getRequestParameters() {
		return requestParameters;
	}

	public void setRequestParameters(Map<String, String> requestParameters) {
		this.requestParameters = requestParameters;
	}

}
