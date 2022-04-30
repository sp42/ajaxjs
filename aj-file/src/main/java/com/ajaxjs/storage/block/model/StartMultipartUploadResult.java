package com.ajaxjs.storage.block.model;

import java.util.Date;

public class StartMultipartUploadResult {

	/**
	 * 上传 ID
	 */
	private String uploadId;

	/**
	 * 该分块上传作业的过期时间
	 */
	private Date abortTime;

	public String getUploadId() {
		return uploadId;
	}

	public void setUploadId(String uploadId) {
		this.uploadId = uploadId;
	}

	public Date getAbortTime() {
		return abortTime;
	}

	public void setAbortTime(Date abortTime) {
		this.abortTime = abortTime;
	}

}
