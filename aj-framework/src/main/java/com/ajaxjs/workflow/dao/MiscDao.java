package com.ajaxjs.workflow.dao;

import java.util.Map;

import com.ajaxjs.sql.annotation.TableName;
import com.ajaxjs.sql.orm.IBaseDao;

@TableName(value = "sth", beanClass = Map.class)
public interface MiscDao extends IBaseDao<Map<String, Object>> {
}
