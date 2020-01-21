package com.ajaxjs.workflow.dao;

import com.ajaxjs.framework.IBaseDao;
import com.ajaxjs.orm.annotation.Select;
import com.ajaxjs.orm.annotation.TableName;
import com.ajaxjs.workflow.model.entity.Process;

@TableName(value = "wf_process", beanClass = Process.class)
public interface ProcessDao extends IBaseDao<Process> {
	@Select("SELECT max(version) FROM ${tableName} WHERE name = ?")
	public Integer getLatestProcessVersion(String name);
}
