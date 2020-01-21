package com.ajaxjs.workflow.dao;

import com.ajaxjs.framework.IBaseDao;
import com.ajaxjs.orm.annotation.TableName;
import com.ajaxjs.workflow.model.entity.Task;

@TableName(value = "wf_task", beanClass = Task.class)
public interface TaskDao extends IBaseDao<Task> {
}
