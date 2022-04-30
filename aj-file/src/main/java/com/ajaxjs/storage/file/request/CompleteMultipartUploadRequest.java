package com.ajaxjs.storage.file.request;

import java.util.List;

import com.ajaxjs.storage.block.model.UploadPartResult;

/**
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public class CompleteMultipartUploadRequest extends UploadCommitRequest {

	/**
	 * 文件块上传后，产生的块信息集
	 */
	private List<UploadPartResult> parts;

	private String filename;

	public List<UploadPartResult> getParts() {
		return parts;
	}

	public void setParts(List<UploadPartResult> parts) {
		this.parts = parts;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

}
