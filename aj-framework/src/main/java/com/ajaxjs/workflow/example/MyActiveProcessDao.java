package com.ajaxjs.workflow.example;

import java.util.function.Function;

import com.ajaxjs.sql.annotation.Select;
import com.ajaxjs.sql.annotation.TableName;
import com.ajaxjs.sql.orm.PageResult;
import com.ajaxjs.workflow.process.ProcessActive;
import com.ajaxjs.workflow.process.service.ProcessActiveDao;

@TableName(value = "wf_process_active", beanClass = ProcessActive.class)
public interface MyActiveProcessDao extends ProcessActiveDao {
	@Select("SELECT e.*, p.name, u.name AS creatorUserName FROM ${tableName} e "
			+ "INNER JOIN wf_process_definition p ON e.processId = p.id "
			+ "LEFT JOIN user u ON e.creator = u.id " + WHERE_REMARK_ORDER)
	public PageResult<ProcessActive> findAdminList(int start, int limit, Function<String, String> sqlHandler);
}
