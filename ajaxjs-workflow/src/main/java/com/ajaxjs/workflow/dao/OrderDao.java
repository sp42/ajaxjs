package com.ajaxjs.workflow.dao;

import com.ajaxjs.framework.IBaseDao;
import com.ajaxjs.orm.annotation.TableName;
import com.ajaxjs.workflow.model.entity.Order;

@TableName(value = "wf_order", beanClass = Order.class)
public interface OrderDao extends IBaseDao<Order> {
}
