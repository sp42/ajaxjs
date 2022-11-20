package com.ajaxjs.workflow.service.scheduling;

import java.util.List;

import com.ajaxjs.workflow.model.po.Task;

/**
 * 任务 job 执行后的回调类
 * 
 */
public interface JobCallback {
	/**
	 * 回调函数
	 * 
	 * @param taskId   当前任务 id
	 * @param newTasks 新产生的任务集合
	 */
	void callback(Long taskId, List<Task> newTasks);
}
