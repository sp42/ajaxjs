package com.ajaxjs.workflow.dao;

import com.ajaxjs.framework.IBaseDao;
import com.ajaxjs.orm.annotation.Select;
import com.ajaxjs.orm.annotation.TableName;
import com.ajaxjs.workflow.model.entity.OrderHistory;

@TableName(value = "wf_order_history", beanClass = OrderHistory.class)
public interface OrderHistoryDao extends IBaseDao<OrderHistory> {

	@Select("SELECT * FROM ${tableName} WHERE orderId = ?")
	public OrderHistory findByOrderId(Long orderId);
}
