package com.ajaxjs.cms.service;

import com.ajaxjs.cms.dao.FeedbackDao;
import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.EntityMap;
import com.ajaxjs.framework.Repository;
import com.ajaxjs.ioc.Bean;

@Bean("FeedbackService")
public class FeedbackService extends BaseService<EntityMap> {
	FeedbackDao dao = new Repository().bind(FeedbackDao.class);

	{
		setUiName("留言反馈");
		setShortName("feedback");
		setDao(dao);
	}
}
