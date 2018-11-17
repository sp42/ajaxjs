package com.ajaxjs.cms.service;

import com.ajaxjs.cms.model.AppUpdate;
import com.ajaxjs.framework.service.IService;

public interface AppUpdateService extends IService<AppUpdate, Long> {
	/**
	 * 获取安卓的最新版本
	 * 
	 * @return 安卓的最新版本
	 */
	public AppUpdate getLastApkVersion(int appId);

	/**
	 * 获取 iOS 的最新版本
	 * 
	 * @return iOS 的最新版本
	 */
	public AppUpdate getLastiOSVersion(int appId);

}