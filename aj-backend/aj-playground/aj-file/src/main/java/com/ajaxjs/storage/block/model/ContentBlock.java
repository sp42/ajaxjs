package com.ajaxjs.storage.block.model;

import java.io.Serializable;

/**
 * @TableName("ufs_storage_block")
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public class ContentBlock implements Serializable {
	private static final long serialVersionUID = -6294842490731773392L;

	private Long id;

	private String appId;

	private String storage;

	private String md5;

	private long size;

	private int refCount;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getStorage() {
		return storage;
	}

	public void setStorage(String storage) {
		this.storage = storage;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public int getRefCount() {
		return refCount;
	}

	public void setRefCount(int refCount) {
		this.refCount = refCount;
	}

}
