package com.ajaxjs.storage.file.request;

/**
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public class AbortMultipartUploadRequest {
    private String storage;

    private String uploadId;

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
}
