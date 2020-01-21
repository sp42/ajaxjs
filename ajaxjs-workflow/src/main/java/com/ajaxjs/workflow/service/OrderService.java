package com.ajaxjs.workflow.service;

import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.Repository;
import com.ajaxjs.workflow.dao.ProcessDao;
import com.ajaxjs.workflow.model.entity.Order;

public class OrderService extends BaseService<Order> {
	{
		setUiName("流程实例");
		setShortName("order");
		setDao(dao);
	}
	
	public static ProcessDao dao = new Repository().bind(ProcessDao.class);
}
