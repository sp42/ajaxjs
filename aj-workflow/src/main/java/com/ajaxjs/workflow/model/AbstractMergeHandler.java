package com.ajaxjs.workflow.model;

import java.util.List;

import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.workflow.Execution;
import com.ajaxjs.workflow.process.ProcessActive;
import com.ajaxjs.workflow.task.Task;
import com.ajaxjs.workflow.task.service.TaskService;

/**
 * 合并处理的抽象处理器 需要子类提供查询无法合并的 task 集合的参数 map
 * 
 */
public abstract class AbstractMergeHandler {
	/**
	 * 查询当前流程实例的无法参与合并的 node 列表。 若所有中间 node 都完成，则设置为已合并状态，告诉 model 可继续执行 join 的输出变迁
	 * 
	 * @param execution 执行对象
	 */
	public void handle(Execution execution) {
		Long pId = execution.getActive().getId();
		ProcessModel model = execution.getModel();
		String[] activeNodes = findActiveNodes();
		boolean isSubProcessMerged = false, isTaskMerged = false;

		if (model.containsNodeNames(SubProcessModel.class, activeNodes)) {
			List<ProcessActive> ps = execution.getEngine().active().findByIdAndExcludedIds(pId, execution.getChildActiveId());

			if (CommonUtil.isNull(ps)) // 如果所有 task 都已完成，则表示可合并
				isSubProcessMerged = true;
		} else
			isSubProcessMerged = true;

		TaskService taskService = execution.getEngine().task();
		if (isSubProcessMerged && model.containsNodeNames(TaskModel.class, activeNodes)) {
			List<Task> tasks = taskService.findByActiveIdAndExcludedIds(pId, execution.getTask().getId(), activeNodes);

			if (CommonUtil.isNull(tasks)) // 如果所有 task 都已完成，则表示可合并
				isTaskMerged = true;
		}

		execution.setMerged(isSubProcessMerged && isTaskMerged);
	}

	/**
	 * 子类需要提供如何查询未合并任务的参数 map
	 * 
	 * @return 当前流程实例的无法参与合并的 node 列表
	 */
	protected abstract String[] findActiveNodes();
}
