package com.ajaxjs.storage.file.request;

/**
 * 该对象主要用于文件的上传/下载签名，如果需要进行秒传，请设置 HTTP 头：Content-MD5
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public class UploadSignRequest extends SignRequest {
	/**
	 * 指定的存储引擎, 如果没有指定则为空
	 */
	private String storage;
	/**
	 * 上传所使用的 HTTP METHOD：PUT、POST
	 */
	private String method;

	/**
	 * 指定过期时间，单位：秒
	 */
	private Integer expires;

	/**
	 * 文件名
	 */
	private String filename;

	/**
	 * 创建人（该字段只作为留档使用，具体请查阅：ApplicationSettings#constraints#archiveCreatedBy）
	 */
	private String createdBy;

	public String getStorage() {
		return storage;
	}

	public void setStorage(String storage) {
		this.storage = storage;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Integer getExpires() {
		return expires;
	}

	public void setExpires(Integer expires) {
		this.expires = expires;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

}
