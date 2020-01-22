package com.ajaxjs.workflow.dao;

import java.util.Map;

import com.ajaxjs.framework.IBaseDao;
import com.ajaxjs.orm.annotation.TableName;

@TableName(value = "sth", beanClass = Map.class)
public interface MiscDao extends IBaseDao<Map<String, Object>> {
}
