package com.ajaxjs.workflow.dao;

import com.ajaxjs.sql.annotation.TableName;
import com.ajaxjs.sql.orm.IBaseDao;
import com.ajaxjs.workflow.model.entity.TaskHistory;

@TableName(value = "wf_task_history", beanClass = TaskHistory.class)
public interface TaskHistoryDao extends IBaseDao<TaskHistory> {
}
