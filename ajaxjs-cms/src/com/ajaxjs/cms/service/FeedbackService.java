package com.ajaxjs.cms.service;

import com.ajaxjs.cms.dao.FeedbackDao;
import com.ajaxjs.cms.model.Feedback;
import com.ajaxjs.framework.dao.QueryParams;
import com.ajaxjs.framework.model.PageResult;
import com.ajaxjs.framework.service.BaseDaoService;
import com.ajaxjs.framework.service.IService;
import com.ajaxjs.framework.service.ServiceException;

public class FeedbackService extends BaseDaoService<Feedback, Long, FeedbackDao> implements IService<Feedback, Long> {
	public FeedbackService() {
		setName(FeedbackDao.tableName);
		setUiName("留言");
		initDao(FeedbackDao.class);
	}

	@Override
	public Feedback findById(Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long create(Feedback bean) throws ServiceException {
		return getDao().create(bean);
	}

	@Override
	public int update(Feedback bean) throws ServiceException {
		return getDao().update(bean);
	}

	@Override
	public boolean delete(Feedback bean) throws ServiceException {
		return getDao().delete(bean);
	}

	@Override
	public PageResult<Feedback> findPagedList(QueryParams parame) throws ServiceException {
		return getDao().findPagedList(parame);
	}
}
