package com.ajaxjs.workflow.dao;

import com.ajaxjs.framework.IBaseDao;
import com.ajaxjs.orm.annotation.TableName;
import com.ajaxjs.workflow.model.entity.CCOrder;

@TableName(value = "wf_cc_order", beanClass = CCOrder.class)
public interface CCOrderDao extends IBaseDao<CCOrder> {
}
