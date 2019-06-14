package com.ajaxjs.cms.appupdate;

import com.ajaxjs.framework.IBaseService;

public interface AppUpdateService extends IBaseService<AppUpdate> {
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