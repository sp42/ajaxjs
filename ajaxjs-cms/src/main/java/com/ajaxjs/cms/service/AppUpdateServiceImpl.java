package com.ajaxjs.cms.service;

import com.ajaxjs.cms.dao.AppUpdateDao;
import com.ajaxjs.cms.model.AppUpdate;
import com.ajaxjs.cms.service.aop.CommonService;
import com.ajaxjs.framework.service.ServiceException;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.orm.dao.DaoHandler;
import com.ajaxjs.orm.dao.PageResult;
import com.ajaxjs.orm.dao.QueryParams;

@Bean(value = "AppUpdateService", aop = { CommonService.class })
public class AppUpdateServiceImpl implements AppUpdateService {
	AppUpdateDao dao = new DaoHandler<AppUpdateDao>().bind(AppUpdateDao.class);

	@Override
	public AppUpdate findById(Long id) throws ServiceException {
		return dao.findById(id);
	}

	@Override
	public Long create(AppUpdate bean) throws ServiceException {
		return dao.create(bean);
	}

	@Override
	public int update(AppUpdate bean) throws ServiceException {
		return dao.update(bean);
	}

	@Override
	public boolean delete(AppUpdate bean) throws ServiceException {
		return dao.delete(bean);
	}

	@Override
	public PageResult<AppUpdate> findPagedList(int start, int limit) throws ServiceException {
		PageResult<AppUpdate> findAppPagedList = dao.findPagedList(start, limit);

		return findAppPagedList;
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
	public PageResult<AppUpdate> findPagedList(QueryParams params, int start, int limit) throws ServiceException {
		return dao.findPagedList(params, start, limit);
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