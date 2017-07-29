package com.ajaxjs.cms.app.service;

import java.util.Map;

import com.ajaxjs.cms.app.dao.ChannelDao;
import com.ajaxjs.framework.dao.QueryParams;
import com.ajaxjs.framework.model.PageResult;
import com.ajaxjs.framework.service.BaseDaoService;
import com.ajaxjs.framework.service.IService;
import com.ajaxjs.framework.service.ServiceException;

public class ChannelService extends BaseDaoService<Map<String, Object>, Long, ChannelDao> implements IService<Map<String, Object>, Long> {
	public ChannelService() {
		setName(ChannelDao.tableName);
		setUiName("渠道");
		initDao(ChannelDao.class);
	}

	@Override
	public Map<String, Object> findById(Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long create(Map<String, Object> bean) throws ServiceException {
		return getDao().create(bean);
	}

	@Override
	public int update(Map<String, Object> bean) throws ServiceException {
		return getDao().update(bean);
	}

	@Override
	public boolean delete(Map<String, Object> bean) throws ServiceException {
		return getDao().delete(bean);
	}

	@Override
	public PageResult<Map<String, Object>> findPagedList(QueryParams parame) throws ServiceException {
		return getDao().findPagedList(parame);
	}
}
