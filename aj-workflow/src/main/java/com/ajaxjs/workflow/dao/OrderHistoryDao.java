package com.ajaxjs.workflow.dao;

import com.ajaxjs.sql.annotation.Select;
import com.ajaxjs.sql.annotation.TableName;
import com.ajaxjs.sql.orm.IBaseDao;
import com.ajaxjs.workflow.model.entity.OrderHistory;

@TableName(value = "wf_order_history", beanClass = OrderHistory.class)
public interface OrderHistoryDao extends IBaseDao<OrderHistory> {

	@Select("SELECT * FROM ${tableName} WHERE orderId = ?")
	public OrderHistory findByOrderId(Long orderId);
}
