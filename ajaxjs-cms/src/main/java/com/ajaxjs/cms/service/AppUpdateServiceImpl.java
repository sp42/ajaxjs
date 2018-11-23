package com.ajaxjs.cms.service;

import java.util.List;

import com.ajaxjs.cms.dao.AppUpdateDao;
import com.ajaxjs.cms.model.AppUpdate;
import com.ajaxjs.cms.service.aop.CommonService;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.orm.dao.DaoHandler;
import com.ajaxjs.orm.dao.PageResult;

@Bean(value = "AppUpdateService", aop = { CommonService.class })
public class AppUpdateServiceImpl implements AppUpdateService {
	AppUpdateDao dao = new DaoHandler().bind(AppUpdateDao.class);

	@Override
	public AppUpdate findById(Long id) {
		return dao.findById(id);
	}

	@Override
	public Long create(AppUpdate bean) {
		return dao.create(bean);
	}

	@Override
	public int update(AppUpdate bean) {
		return dao.update(bean);
	}

	@Override
	public boolean delete(AppUpdate bean) {
		return dao.delete(bean);
	}

	@Override
	public String getName() {
		return "客户端更新接口";
	}

	@Override
	public String getTableName() {
		return "appUpdate";
	}

	@Override
	public PageResult<AppUpdate> findPagedList(int start, int limit) {
		return dao.findPagedList(start, limit);
	}

	@Override
	public AppUpdate getLastApkVersion(int appId) {
		return dao.getLastAndroid(appId);
	}

	@Override
	public AppUpdate getLastiOSVersion(int appId) {
		return dao.getLastiOS(appId);
	}

	@Override
	public List<AppUpdate> findList() { 
		return null;
	}

}