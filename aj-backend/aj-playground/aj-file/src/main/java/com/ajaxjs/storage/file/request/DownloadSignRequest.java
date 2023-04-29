package com.ajaxjs.storage.file.request;

import java.util.Map;

/**
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public class DownloadSignRequest {
	/**
	 * 文件 ID
	 */
	private String fileId;

	/**
	 * 指定要从该存储下载
	 */
	private String storage;

	/**
	 * 指定签名有效时间 单位为秒
	 */
	private Integer expires;

	/**
	 * 要进行签要的 HTTP 请求头
	 */
	private Map<String, String> requestHeaders;

	/**
	 * 要进行签名的 HTTP QUERY 参数
	 */
	private Map<String, String> requestParameters;

	/**
	 * 重写默认的访问文件时的 HTTP 响应头
	 */
	private Map<String, String> responseHeaderOverrides;

	/**
	 * 
	 * @return
	 */
	public Long getFileIdAsLong() {
		return fileId == null ? null : Long.valueOf(fileId);
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getStorage() {
		return storage;
	}

	public void setStorage(String storage) {
		this.storage = storage;
	}

	public Integer getExpires() {
		return expires;
	}

	public void setExpires(Integer expires) {
		this.expires = expires;
	}

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

	public Map<String, String> getResponseHeaderOverrides() {
		return responseHeaderOverrides;
	}

	public void setResponseHeaderOverrides(Map<String, String> responseHeaderOverrides) {
		this.responseHeaderOverrides = responseHeaderOverrides;
	}

}
