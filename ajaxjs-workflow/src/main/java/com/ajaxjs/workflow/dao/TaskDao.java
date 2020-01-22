package com.ajaxjs.workflow.dao;

import java.util.List;

import com.ajaxjs.framework.IBaseDao;
import com.ajaxjs.orm.annotation.Select;
import com.ajaxjs.orm.annotation.TableName;
import com.ajaxjs.workflow.model.entity.Task;
import com.ajaxjs.workflow.model.entity.TaskActor;

@TableName(value = "wf_task", beanClass = Task.class)
public interface TaskDao extends IBaseDao<Task> {
	@Select("SELECT * FROM ${tableName} WHERE parentTaskId IN "
			+ "( SELECT ht.id FROM wf_hist_task ht WHERE ht.order_id = ? AND ht.task_name = ? AND ht.parent_task_id = ? )")
	public List<Task> getNextActiveTasks(Long id, String taskName, Long parentTaskId);

	public int createTaskActor(TaskActor taskActor);
}
