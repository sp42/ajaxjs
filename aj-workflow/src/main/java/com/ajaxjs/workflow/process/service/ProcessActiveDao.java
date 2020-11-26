package com.ajaxjs.workflow.process.service;

import java.util.function.Function;

import com.ajaxjs.sql.annotation.Select;
import com.ajaxjs.sql.annotation.TableName;
import com.ajaxjs.sql.orm.IBaseDao;
import com.ajaxjs.sql.orm.PageResult;
import com.ajaxjs.workflow.process.ProcessActive;

@TableName(value = "wf_process_active", beanClass = ProcessActive.class)
public interface ProcessActiveDao extends IBaseDao<ProcessActive> {
	@Select("SELECT e.*, p.name FROM ${tableName} e INNER JOIN wf_process_definition p ON e.processId = p.id " + WHERE_REMARK_ORDER)
	public PageResult<ProcessActive> findDetailList(int start, int limit, Function<String, String> sqlHandler);
}
