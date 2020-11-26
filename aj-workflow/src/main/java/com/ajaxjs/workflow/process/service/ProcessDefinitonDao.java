package com.ajaxjs.workflow.process.service;

import com.ajaxjs.sql.annotation.Select;
import com.ajaxjs.sql.annotation.TableName;
import com.ajaxjs.sql.orm.IBaseDao;
import com.ajaxjs.workflow.process.ProcessDefinition;

@TableName(value = "wf_process_definition", beanClass = ProcessDefinition.class)
public interface ProcessDefinitonDao extends IBaseDao<ProcessDefinition> {
	@Select("SELECT max(version) FROM ${tableName} WHERE name = ?")
	public Integer getLatestProcessVersion(String name);
}
