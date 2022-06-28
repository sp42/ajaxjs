/* Copyright 2013-2015 www.snakerflow.com.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ajaxjs.workflow.model;

import java.util.List;

import org.springframework.util.CollectionUtils;

import com.ajaxjs.workflow.model.entity.Order;
import com.ajaxjs.workflow.model.entity.Task;
import com.ajaxjs.workflow.service.TaskService;

/**
 * 合并处理的抽象处理器 需要子类提供查询无法合并的task集合的参数map
 * 
 */
public abstract class AbstractMergeHandler implements IHandler {
	/**
	 * 查询当前流程实例的无法参与合并的 node 列表。 若所有中间 node 都完成，则设置为已合并状态，告诉 model 可继续执行 join 的输出变迁
	 */
	@Override
	public void handle(Execution execution) {
		Long orderId = execution.getOrder().getId();
		ProcessModel model = execution.getModel();
		String[] activeNodes = findActiveNodes();
		boolean isSubProcessMerged = false, isTaskMerged = false;

		if (model.containsNodeNames(SubProcessModel.class, activeNodes)) {
			List<Order> orders = execution.getEngine().order().findByIdAndExcludedIds(orderId, execution.getChildOrderId());

			if (CollectionUtils.isEmpty(orders)) // 如果所有 task 都已完成，则表示可合并
				isSubProcessMerged = true;
		} else
			isSubProcessMerged = true;

		TaskService taskService = execution.getEngine().task();

		if (isSubProcessMerged && model.containsNodeNames(TaskModel.class, activeNodes)) {
			List<Task> tasks = taskService.findByOrderIdAndExcludedIds(orderId, execution.getTask().getId(), activeNodes);

			if (CollectionUtils.isEmpty(tasks)) // 如果所有 task 都已完成，则表示可合并
				isTaskMerged = true;
		}

		execution.setMerged(isSubProcessMerged && isTaskMerged);
	}

	/**
	 * 子类需要提供如何查询未合并任务的参数map
	 * 
	 * @return 当前流程实例的无法参与合并的 node 列表
	 */
	protected abstract String[] findActiveNodes();
}
