package com.ajaxjs.storage.block.model;

/**
 * Part 上传结果
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public class UploadPartResult {
	/**
	 * 
	 */
	private int partNumber;

	/**
	 * 
	 */
	private String etag;

	public int getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(int partNumber) {
		this.partNumber = partNumber;
	}

	public String getEtag() {
		return etag;
	}

	public void setEtag(String etag) {
		this.etag = etag;
	}
}
