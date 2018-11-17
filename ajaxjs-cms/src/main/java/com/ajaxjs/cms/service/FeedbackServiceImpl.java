package com.ajaxjs.cms.service;

import com.ajaxjs.cms.dao.FeedbackDao;
import com.ajaxjs.cms.model.Feedback;
import com.ajaxjs.cms.service.aop.CommonService;
import com.ajaxjs.cms.service.aop.GlobalLogAop;
import com.ajaxjs.framework.service.ServiceException;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.orm.dao.DaoHandler;
import com.ajaxjs.orm.dao.PageResult;
import com.ajaxjs.orm.dao.QueryParams;

@Bean(value = "FeedbackService", aop = { CommonService.class, GlobalLogAop.class })
public class FeedbackServiceImpl implements FeedbackService {
	FeedbackDao dao = new DaoHandler<FeedbackDao>().bind(FeedbackDao.class);

	@Override
	public Feedback findById(Long id) throws ServiceException {
		return dao.findById(id);
	}

	@Override
	public Long create(Feedback bean) throws ServiceException {
		return dao.create(bean);
	}

	@Override
	public int update(Feedback bean) throws ServiceException {
		return dao.update(bean);
	}

	@Override
	public boolean delete(Feedback bean) throws ServiceException {
		return dao.delete(bean);
	}

	@Override
	public PageResult<Feedback> findPagedList(QueryParams params, int start, int limit) throws ServiceException {
		return dao.findPagedList(params, start, limit);
	}

	@Override
	public PageResult<Feedback> findPagedList(int start, int limit) throws ServiceException {
		return dao.findPagedList(start, limit);
	}

	@Override
	public String getName() {
		return "留言反馈";
	}

	@Override
	public String getTableName() {
		return "feedback";
	}
}
