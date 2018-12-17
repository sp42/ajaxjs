package com.ajaxjs.cms.app.nativeapp;

import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.Repository;
import com.ajaxjs.ioc.Bean;

@Bean("AppUpdateService")
public class AppUpdateServiceImpl extends BaseService<AppUpdate> implements AppUpdateService {
	AppUpdateDao dao = new Repository().bind(AppUpdateDao.class);

	{
		setUiName("客户端更新接口");
		setShortName("appUpdate");
		setDao(dao);
	}

	@Override
	public AppUpdate getLastApkVersion(int appId) {
		return dao.getLastAndroid(appId);
	}

	@Override
	public AppUpdate getLastiOSVersion(int appId) {
		return dao.getLastiOS(appId);
	}
}