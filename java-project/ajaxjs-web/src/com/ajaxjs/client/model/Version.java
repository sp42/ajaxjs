package com.ajaxjs.client.model;

import com.ajaxjs.framework.model.BaseModel;

/**
 * 安卓使用的版本更新对象
 * @author frank
 *
 */
public class Version extends BaseModel {
	/**
	 * 下载地址，此为相对地址保存到数据库
	 */
	private String downloadUrl;
	
	/**
	 * 版本号
	 */
	private int version = 2016001;
	
	/**
	 * 强制更新
	 */
	private boolean forceUpdate;

	/**
	 * @return 是否强制更新
	 */
	public boolean isForceUpdate() {
		return forceUpdate;
	}

	/**
	 * @param forceUpdate 是否强制更新
	 */
	public void setForceUpdate(boolean forceUpdate) {
		this.forceUpdate = forceUpdate;
	}

	/**
	 * @return the version
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(int version) {
		this.version = version;
	}

	/**
	 * @return the downloadUrl
	 */
	public String getDownloadUrl() {
		return downloadUrl;
	}

	/**
	 * @param downloadUrl the downloadUrl to set
	 */
	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

}
