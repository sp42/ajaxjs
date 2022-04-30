package com.ajaxjs.storage.file.request;

/**
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public class UploadPartSignRequest extends SignRequest {
	/**
	 * 指定的存储引擎, 如果没有指定则为空
	 */
	private String storage;

	/**
	 * 上传 ID
	 */
	private String uploadId;

	/**
	 * 文件名
	 */
	private String filename;

	/**
	 * 该文件块的序号，从 1 开始
	 */
	private int partNumber;

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

	public int getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(int partNumber) {
		this.partNumber = partNumber;
	}

}
