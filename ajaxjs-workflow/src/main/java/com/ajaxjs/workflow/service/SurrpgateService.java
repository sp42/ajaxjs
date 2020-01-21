package com.ajaxjs.workflow.service;

import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.Repository;
import com.ajaxjs.workflow.dao.SurrogateDao;
import com.ajaxjs.workflow.model.entity.Surrogate;

public class SurrpgateService extends BaseService<Surrogate> {
	{
		setUiName("委托");
		setShortName("surrogate");
		setDao(dao);
	}

	public static SurrogateDao dao = new Repository().bind(SurrogateDao.class);
}
