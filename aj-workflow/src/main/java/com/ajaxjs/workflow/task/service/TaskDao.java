package com.ajaxjs.workflow.task.service;

import java.util.List;
import java.util.function.Function;

import com.ajaxjs.sql.annotation.Insert;
import com.ajaxjs.sql.annotation.Select;
import com.ajaxjs.sql.annotation.TableName;
import com.ajaxjs.sql.orm.IBaseDao;
import com.ajaxjs.sql.orm.PageResult;
import com.ajaxjs.workflow.task.Task;
import com.ajaxjs.workflow.task.TaskActor;

@TableName(value = "wf_task", beanClass = Task.class)
public interface TaskDao extends IBaseDao<Task> {
	@Select("SELECT * FROM ${tableName} WHERE parentTaskId IN "
			+ "( SELECT ht.id FROM wf_hist_task ht WHERE ht.order_id = ? AND ht.task_name = ? AND ht.parent_task_id = ? )")
	public List<Task> getNextActiveTasks(Long id, String taskName, Long parentTaskId);
	
	@Insert("INSERT INTO wf_task_actor (taskId, actorId) VALUES (?, ?)")
	public int createTaskActor(Long taskId, Long actorId);
	
	@Select("SELECT * FROM wf_task_actor WHERE taskId = ?")
	public List<TaskActor> findTaskActorsByTaskId(Long taskId);
	
	
	@Select("SELECT e.*, d.name AS procName, a.createDate AS procCreateDate FROM ${tableName} e INNER JOIN wf_process_active a ON a.id = e.orderId "
			+ "INNER JOIN wf_process_definition d ON d.id = a.processId " + WHERE_REMARK_ORDER)
	@Override
	public PageResult<Task> findPagedList(int start, int limit, Function<String, String> sqlHandler);
}
