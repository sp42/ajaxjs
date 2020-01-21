package com.ajaxjs.workflow.service;

import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.Repository;
import com.ajaxjs.workflow.dao.TaskDao;
import com.ajaxjs.workflow.model.entity.Task;

public class TaskService extends BaseService<Task> {
	{
		setUiName("任务");
		setShortName("task");
		setDao(dao);
	}

	public static TaskDao dao = new Repository().bind(TaskDao.class);
}
