package com.ajaxjs.cms.service;

import com.ajaxjs.cms.dao.PortalDao;
import com.ajaxjs.cms.model.Portal;
import com.ajaxjs.framework.dao.QueryParams;
import com.ajaxjs.framework.model.PageResult;
import com.ajaxjs.framework.service.BaseDaoService;
import com.ajaxjs.framework.service.IService;
import com.ajaxjs.framework.service.ServiceException;

public class PortalService extends BaseDaoService<Portal, Long, PortalDao> implements IService<Portal, Long> {
	public PortalService() {
		setName(PortalDao.tableName);
		setUiName("门户");
		initDao(PortalDao.class);
	}

	@Override
	public Portal findById(Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long create(Portal bean) throws ServiceException {
		return getDao().create(bean);
	}

	@Override
	public int update(Portal bean) throws ServiceException {
		return getDao().update(bean);
	}

	@Override
	public boolean delete(Portal bean) throws ServiceException {
		return getDao().delete(bean);
	}

	@Override
	public PageResult<Portal> findPagedList(QueryParams parame) throws ServiceException {
		return getDao().findPagedList(parame);
	}
}
