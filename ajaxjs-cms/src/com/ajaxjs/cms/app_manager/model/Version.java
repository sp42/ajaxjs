package com.ajaxjs.cms.app_manager.model;

import com.ajaxjs.framework.model.BaseModel;

/**
 * 安卓使用的版本更新对象
 * @author frank
 *
 */
public class Version extends BaseModel {

	private static final long serialVersionUID = -8231812029255475684L;

	/**
	 * 下载地址，此为相对地址保存到数据库
	 */
	private String downloadUrl;
	
	/**
	 * 版本号
	 */
	private int version = 2016001;
	
	/**
	 * 门户 id
	 */
	private int portalId;
	
	private String portalName;
	
	/**
	 * 渠道号
	 */
	
	private String channelId;
	
	private String channelName;
	
	
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

 

	public int getPortalId() {
		return portalId;
	}

	public void setPortalId(int portalId) {
		this.portalId = portalId;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getPortalName() {
		return portalName;
	}

	public void setPortalName(String portalName) {
		this.portalName = portalName;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

}
