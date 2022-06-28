package com.ajaxjs.workflow.dao;

import com.ajaxjs.sql.annotation.TableName;
import com.ajaxjs.sql.orm.IBaseDao;
import com.ajaxjs.workflow.model.entity.Order;

@TableName(value = "wf_order", beanClass = Order.class)
public interface OrderDao extends IBaseDao<Order> {
}
