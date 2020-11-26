package com.ajaxjs.workflow.util.surrogate;

import com.ajaxjs.sql.annotation.TableName;
import com.ajaxjs.sql.orm.IBaseDao;

@TableName(value = "wf_surrogate", beanClass = Surrogate.class)
public interface SurrogateDao extends IBaseDao<Surrogate> {
}
