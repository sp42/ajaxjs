package com.ajaxjs.workflow.dao;

import com.ajaxjs.sql.annotation.TableName;
import com.ajaxjs.sql.orm.IBaseDao;
import com.ajaxjs.workflow.model.entity.Surrogate;

@TableName(value = "wf_surrogate", beanClass = Surrogate.class)
public interface SurrogateDao extends IBaseDao<Surrogate> {
}
