package com.ajaxjs.cms.service;

import com.ajaxjs.cms.dao.HrDao;
import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.EntityMap;
import com.ajaxjs.framework.Repository;
import com.ajaxjs.ioc.Bean;

@Bean("HrService")
public class HrServiceImpl extends BaseService<EntityMap> {
	HrDao dao = new Repository().bind(HrDao.class);
	
	{
		setUiName("招聘");
		setShortName("hr");
		setDao(dao);
	}
}
