package com.ajaxjs.cms.model;

import com.ajaxjs.framework.BaseModel;

public class AppUpdate extends BaseModel {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 版本状态:0-未使用-10-正在使用
	 * 
	 */
	private Integer status;
	
	/**
	 * 设置实体状态
	 * @param status     
	 *            实体状态，可自定义常量表示各种状态
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	/**
	 * 获取实体状态
	 * @return 实体状态
	 */	
	public Integer getStatus() {
		return status;
	}
	
	private String stateDescription;
	
	/**
	 * 设置实体状态
	 * @param status     
	 *            实体状态，可自定义常量表示各种状态
	 */
	public void setStateDescription(String stateDescription) {
		this.stateDescription = stateDescription;
	}
	
	/**
	 * 获取实体状态
	 * @return 实体状态
	 */	
	public String getStateDescription() {
		return stateDescription;
	}
	/**
	 * 创建者 id
	 */
	private Integer createByUser;
	
	/**
	 * 设置创建者 id
	 * @param createByUser     
	 *            创建者 id
	 */
	public void setCreateByUser(Integer createByUser) {
		this.createByUser = createByUser;
	}
	
	/**
	 * 获取创建者 id
	 * @return 创建者 id
	 */	
	public Integer getCreateByUser() {
		return createByUser;
	}
	
	/**
	 * 是否已删除 1=已删除；0/null；未删除
	 */
	private Integer deleted;
	
	/**
	 * 设置是否已删除 1=已删除；0/null；未删除
	 * @param deleted     
	 *            是否已删除 1=已删除；0/null；未删除
	 */
	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}
	
	/**
	 * 获取是否已删除 1=已删除；0/null；未删除
	 * @return 是否已删除 1=已删除；0/null；未删除
	 */	
	public Integer getDeleted() {
		return deleted;
	}
	
	/**
	 * 安卓 apk 下载连接
	 */
	private String downUrl;
	
	/**
	 * 设置安卓 apk 下载连接
	 * @param downUrl     
	 *            安卓 apk 下载连接
	 */
	public void setDownUrl(String downUrl) {
		this.downUrl = downUrl;
	}
	
	/**
	 * 获取安卓 apk 下载连接
	 * @return 安卓 apk 下载连接
	 */	
	public String getDownUrl() {
		return downUrl;
	}
	
	/**
	 * 苹果商店连接
	 */
	private String iosAppStoreUrl;
	
	/**
	 * 设置苹果商店连接
	 * @param iosAppStoreUrl     
	 *            苹果商店连接
	 */
	public void setIosAppStoreUrl(String iosAppStoreUrl) {
		this.iosAppStoreUrl = iosAppStoreUrl;
	}
	
	/**
	 * 获取苹果商店连接
	 * @return 苹果商店连接
	 */	
	public String getIosAppStoreUrl() {
		return iosAppStoreUrl;
	}
	
	/**
	 * 安卓版本号 YYYYMMDD+两位数字
	 */
	private Integer apkVersion;
	
	/**
	 * 设置安卓版本号 YYYYMMDD+两位数字
	 * @param apkVersion     
	 *            安卓版本号 YYYYMMDD+两位数字，例如2018090901
	 */
	public void setApkVersion(Integer apkVersion) {
		this.apkVersion = apkVersion;
	}
	
	/**
	 * 获取安卓版本号 YYYYMMDD+两位数字
	 * @return 安卓版本号 YYYYMMDD+两位数字
	 */	
	public Integer getApkVersion() {
		return apkVersion;
	}
	
	/**
	 * iOS版本号 YYYYMMDD+两位数字
	 */
	private Integer iosVersion;
	
	/**
	 * 设置iOS版本号 YYYYMMDD+两位数字
	 * @param iosVersion     
	 *            iOS版本号 YYYYMMDD+两位数字，例如2018090901
	 */
	public void setIosVersion(Integer iosVersion) {
		this.iosVersion = iosVersion;
	}
	
	/**
	 * 获取iOS版本号 YYYYMMDD+两位数字
	 * @return iOS版本号 YYYYMMDD+两位数字
	 */	
	public Integer getIosVersion() {
		return iosVersion;
	}
	
	public Integer getAppId() {
		return appId;
	}

	public void setAppId(Integer appId) {
		this.appId = appId;
	}

	private Integer appId;
	
}