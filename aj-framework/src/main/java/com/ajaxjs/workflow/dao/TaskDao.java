package com.ajaxjs.workflow.dao;

import java.util.List;

import com.ajaxjs.sql.annotation.Insert;
import com.ajaxjs.sql.annotation.Select;
import com.ajaxjs.sql.annotation.TableName;
import com.ajaxjs.sql.orm.IBaseDao;
import com.ajaxjs.workflow.model.entity.Task;
import com.ajaxjs.workflow.model.entity.TaskActor;

@TableName(value = "wf_task", beanClass = Task.class)
public interface TaskDao extends IBaseDao<Task> {
	@Select("SELECT * FROM ${tableName} WHERE parentTaskId IN "
			+ "( SELECT ht.id FROM wf_hist_task ht WHERE ht.order_id = ? AND ht.task_name = ? AND ht.parent_task_id = ? )")
	public List<Task> getNextActiveTasks(Long id, String taskName, Long parentTaskId);
	
	@Insert("INSERT INTO wf_task_actor (taskId, actorId) VALUES (?, ?)")
	public int createTaskActor(Long taskId, Long actorId);
	
	@Select("SELECT * FROM wf_task_actor WHERE taskId = ?")
	public List<TaskActor> findTaskActorsByTaskId(Long taskId);
}
