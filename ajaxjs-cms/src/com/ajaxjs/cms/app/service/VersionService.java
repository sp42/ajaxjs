package com.ajaxjs.cms.app.service;

import com.ajaxjs.cms.app.dao.VersionDao;
import com.ajaxjs.cms.app.model.Version;
import com.ajaxjs.framework.dao.QueryParams;
import com.ajaxjs.framework.model.PageResult;
import com.ajaxjs.framework.service.BaseDaoService;
import com.ajaxjs.framework.service.IService;

public class VersionService extends BaseDaoService<Version, Long, VersionDao> implements IService<Version, Long> {
	public VersionService() {
		initDao(VersionDao.class);
		setName("version");
		setUiName("版本");
	}

	@Override
	public Version findById(Long id) {
		return getDao().findById(id);
	}

	public Version getTop(int portalId, String channelId) {
		return getDao().selectTopVersion(portalId, channelId);
	}

	@Override
	public Long create(Version bean) {
		commonCreate(bean);
		commonUpdate(bean);
		return getDao().create(bean);
	}

	@Override
	public int update(Version bean) {
		commonUpdate(bean);
		return getDao().update(bean);
	}

	@Override
	public boolean delete(Version bean) {
		return getDao().delete(bean);
	}

	@Override
	public PageResult<Version> findPagedList(QueryParams parame) {
		return getDao().findPagedList(parame);
	}
}