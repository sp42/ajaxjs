package com.ajaxjs.workflow.service.handler;

import java.util.List;

import org.springframework.util.CollectionUtils;

import com.ajaxjs.workflow.model.Execution;
import com.ajaxjs.workflow.model.ProcessModel;
import com.ajaxjs.workflow.model.node.work.SubProcessModel;
import com.ajaxjs.workflow.model.node.work.TaskModel;
import com.ajaxjs.workflow.model.po.Order;
import com.ajaxjs.workflow.model.po.Task;
import com.ajaxjs.workflow.service.TaskService;

/**
 * 合并处理的抽象处理器 需要子类提供查询无法合并的 task 集合的参数 map
 */
public abstract class AbstractMergeHandler implements IHandler {
	/**
	 * 查询当前流程实例的无法参与合并的 node 列表。 若所有中间 node 都完成，则设置为已合并状态，告诉 model 可继续执行 join 的输出变迁
	 */
	@Override
	public void handle(Execution exec) {
		Long orderId = exec.getOrder().getId();
		ProcessModel model = exec.getModel();
		String[] activeNodes = findActiveNodes();
		boolean isSubProcessMerged = false, isTaskMerged = false;

		if (model.containsNodeNames(SubProcessModel.class, activeNodes)) {
			List<Order> orders = exec.getEngine().orderService.findByIdAndExcludedIds(orderId, exec.getChildOrderId());

			if (CollectionUtils.isEmpty(orders)) // 如果所有 task 都已完成，则表示可合并
				isSubProcessMerged = true;
		} else
			isSubProcessMerged = true;

		TaskService taskService = exec.getEngine().taskService;

		if (isSubProcessMerged && model.containsNodeNames(TaskModel.class, activeNodes)) {
			List<Task> tasks = taskService.findByOrderIdAndExcludedIds(orderId, exec.getTask().getId(), activeNodes);

			if (CollectionUtils.isEmpty(tasks)) // 如果所有 task 都已完成，则表示可合并
				isTaskMerged = true;
		}

		exec.setMerged(isSubProcessMerged && isTaskMerged);
	}

	/**
	 * 子类需要提供如何查询未合并任务的参数map
	 * 
	 * @return 当前流程实例的无法参与合并的 node 列表
	 */
	protected abstract String[] findActiveNodes();
}
