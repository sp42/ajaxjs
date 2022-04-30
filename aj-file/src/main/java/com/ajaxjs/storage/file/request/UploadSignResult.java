package com.ajaxjs.storage.file.request;

import com.ajaxjs.storage.block.model.SignedRequest;

/**
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public class UploadSignResult extends SignedRequest {
	/**
	 * 上传 ID，在 Commit 步骤需要用到
	 */
	private String uploadId;

	public String getUploadId() {
		return uploadId;
	}

	public void setUploadId(String uploadId) {
		this.uploadId = uploadId;
	}
}
