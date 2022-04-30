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

	@ApiModelProperty(value = "定制请求头信息")
	private Map<String, String> requestHeaders;

	@ApiModelProperty("定制访问文件时产生的 HTTP 的响应头")
	private Map<String, String> responseHeaders;

	@ApiModelProperty("文件元数据，一经定义不能修改")
	private Map<String, String> metadata;

	@ApiModelProperty(value = "提交超时", notes = "由于 S3 在上传后，并不能立即获取，所以增加超时，该时间内不断间隔重试")
	private Long retryTimeoutMillis;
}
