package com.ajaxjs.workflow.service;

import com.ajaxjs.framework.BaseService;
import com.ajaxjs.sql.orm.Repository;
import com.ajaxjs.workflow.dao.OrderHistoryDao;
import com.ajaxjs.workflow.model.entity.OrderHistory;

public class OrderHistoryService extends BaseService<OrderHistory> {
	public static OrderHistoryDao historyDao = new Repository().bind(OrderHistoryDao.class);

	{
		setDao(historyDao);
	}
}
