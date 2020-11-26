package com.ajaxjs.workflow.process.service;

import com.ajaxjs.framework.BaseService;
import com.ajaxjs.sql.annotation.Select;
import com.ajaxjs.sql.annotation.TableName;
import com.ajaxjs.sql.orm.IBaseDao;
import com.ajaxjs.sql.orm.Repository;
import com.ajaxjs.util.ioc.Component;
import com.ajaxjs.workflow.process.ProcessHistory;

@Component
public class ProcessHistoryService extends BaseService<ProcessHistory> {
	@TableName(value = "wf_process_history", beanClass = ProcessHistory.class)
	public static interface ProcessHistoryDao extends IBaseDao<ProcessHistory> {

		@Select("SELECT * FROM ${tableName} WHERE orderId = ?")
		public ProcessHistory findByActiveId(Long activeId);
	}

	public static ProcessHistoryDao HISTORY_DAO = new Repository().bind(ProcessHistoryDao.class);

	{
		setUiName("历史流程");
		setShortName("process_history");
		setDao(HISTORY_DAO);
	}
}