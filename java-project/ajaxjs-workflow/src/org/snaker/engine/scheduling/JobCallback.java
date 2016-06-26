package org.snaker.engine.scheduling;

import java.util.List;

import org.snaker.engine.entity.Task;

/**
 * 任务job执行后的回调类
 * 
 * @author yuqs
 * @since 1.4
 */
public interface JobCallback {
	/**
	 * 回调函数
	 * 
	 * @param taskId
	 *            当前任务id
	 * @param newTasks
	 *            新产生的任务集合
	 */
	void callback(String taskId, List<Task> newTasks);
}
