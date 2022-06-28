package com.ajaxjs.workflow.dao;

import com.ajaxjs.sql.annotation.TableName;
import com.ajaxjs.sql.orm.IBaseDao;
import com.ajaxjs.workflow.model.entity.CCOrder;

@TableName(value = "wf_cc_order", beanClass = CCOrder.class)
public interface CCOrderDao extends IBaseDao<CCOrder> {
}
