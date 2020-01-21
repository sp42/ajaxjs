package com.ajaxjs.workflow.dao;

import com.ajaxjs.framework.IBaseDao;
import com.ajaxjs.orm.annotation.TableName;
import com.ajaxjs.workflow.model.entity.Surrogate;

@TableName(value = "wf_surrogate", beanClass = Surrogate.class)
public interface SurrogateDao extends IBaseDao<Surrogate> {
}
