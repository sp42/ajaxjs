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
package com.ajaxjs.workflow.handler;

import java.util.List;

import org.snaker.engine.IQueryService;
import org.snaker.engine.access.QueryFilter;

import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.workflow.Execution;
import com.ajaxjs.workflow.model.ProcessModel;
import com.ajaxjs.workflow.model.SubProcessModel;
import com.ajaxjs.workflow.model.TaskModel;
import com.ajaxjs.workflow.model.entity.Order;
import com.ajaxjs.workflow.model.entity.Task;

/**
 * 合并处理的抽象处理器 需要子类提供查询无法合并的task集合的参数map
 * 
 */
public abstract class AbstractMergeHandler implements IHandler {
	/**
	 * 查询当前流程实例的无法参与合并的node列表 若所有中间node都完成，则设置为已合并状态，告诉model可继续执行join的输出变迁
	 */
	@Override
	public void handle(Execution execution) {
		IQueryService queryService = execution.getEngine().query();
		Order order = execution.getOrder();
		ProcessModel model = execution.getModel();
		String[] activeNodes = findActiveNodes();
		boolean isSubProcessMerged = false;
		boolean isTaskMerged = false;

		if (model.containsNodeNames(SubProcessModel.class, activeNodes)) {
			QueryFilter filter = new QueryFilter().setParentId(order.getId()).setExcludedIds(new String[] { execution.getChildOrderId() });
			List<Order> orders = queryService.getActiveOrders(filter);

			if (CommonUtil.isNull(orders)) // 如果所有 task 都已完成，则表示可合并
				isSubProcessMerged = true;
		} else
			isSubProcessMerged = true;

		if (isSubProcessMerged && model.containsNodeNames(TaskModel.class, activeNodes)) {
			QueryFilter filter = new QueryFilter().setOrderId(order.getId())
					.setExcludedIds(new String[] { execution.getTask().getId() }).setNames(activeNodes);
			List<Task> tasks = queryService.getActiveTasks(filter);

			if (CommonUtil.isNull(tasks)) // 如果所有 task 都已完成，则表示可合并
				isTaskMerged = true;
		}

		execution.setMerged(isSubProcessMerged && isTaskMerged);
	}

	/**
	 * 子类需要提供如何查询未合并任务的参数map
	 * 
	 * @return
	 */
	protected abstract String[] findActiveNodes();
}
