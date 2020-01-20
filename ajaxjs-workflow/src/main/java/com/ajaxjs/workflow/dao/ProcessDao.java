package com.ajaxjs.workflow.dao;

import java.util.Map;

import com.ajaxjs.framework.IBaseDao;
import com.ajaxjs.orm.annotation.TableName;

@TableName(value = "wf_process", beanClass = Map.class)
public interface ProcessDao extends IBaseDao<Map<String, Object>> {

}
