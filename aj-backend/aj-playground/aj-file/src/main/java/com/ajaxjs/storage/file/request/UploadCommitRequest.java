package com.ajaxjs.storage.file.request;

import java.util.Map;

import com.ajaxjs.storage.file.model.AccessControl;

/**
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public class UploadCommitRequest {
	/**
	 * 上传所指定的存储引擎, 如果没有指定则为空
	 */
	private String storage;

	/**
	 * 由签名步骤产生的上传 ID
	 */
	private String uploadId;

	private String filename;

	private Long filesize;

	/**
	 * 文件内容 MD5，主要用于检查文件完整性
	 */
	private String contentMd5;

	private String contentType;

	/**
	 * 文件权限
	 */
	private AccessControl accessControl;

	/**
	 * 定制请求头信息
	 */
	private Map<String, String> requestHeaders;

	/**
	 * 定制访问文件时产生的 HTTP 的响应头
	 */
	private Map<String, String> responseHeaders;

	/**
	 * 文件元数据，一经定义不能修改
	 */
	private Map<String, String> metadata;

	/**
	 * 提交超时.由于 S3 在上传后，并不能立即获取，所以增加超时，该时间内不断间隔重试
	 */
	private Long retryTimeoutMillis;

	public String getStorage() {
		return storage;
	}

	public void setStorage(String storage) {
		this.storage = storage;
	}

	public String getUploadId() {
		return uploadId;
	}

	public void setUploadId(String uploadId) {
		this.uploadId = uploadId;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public Long getFilesize() {
		return filesize;
	}

	public void setFilesize(Long filesize) {
		this.filesize = filesize;
	}

	public String getContentMd5() {
		return contentMd5;
	}

	public void setContentMd5(String contentMd5) {
		this.contentMd5 = contentMd5;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public AccessControl getAccessControl() {
		return accessControl;
	}

	public void setAccessControl(AccessControl accessControl) {
		this.accessControl = accessControl;
	}

	public Map<String, String> getRequestHeaders() {
		return requestHeaders;
	}

	public void setRequestHeaders(Map<String, String> requestHeaders) {
		this.requestHeaders = requestHeaders;
	}

	public Map<String, String> getResponseHeaders() {
		return responseHeaders;
	}

	public void setResponseHeaders(Map<String, String> responseHeaders) {
		this.responseHeaders = responseHeaders;
	}

	public Map<String, String> getMetadata() {
		return metadata;
	}

	public void setMetadata(Map<String, String> metadata) {
		this.metadata = metadata;
	}

	public Long getRetryTimeoutMillis() {
		return retryTimeoutMillis;
	}

	public void setRetryTimeoutMillis(Long retryTimeoutMillis) {
		this.retryTimeoutMillis = retryTimeoutMillis;
	}

}
