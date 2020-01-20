package com.ajaxjs.workflow.service;

import java.util.Map;

import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.framework.Repository;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.workflow.dao.ProcessDao;

@Bean
public class ProcessService extends BaseService<Map<String, Object>> {
	{
		setUiName("流程");
		setShortName("process");
		setDao(dao);
	}
	public static ProcessDao dao = new Repository().bind(ProcessDao.class);

	public PageResult<Map<String, Object>> list(int start, int limit) {
		return dao.findPagedList(start, limit, null);
	}
}
