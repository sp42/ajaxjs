/*
 * Copyright 2013-2015 www.snakerflow.com. Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.ajaxjs.workflow.handler;

import java.util.List;

import com.ajaxjs.workflow.WorkflowException;
import com.ajaxjs.workflow.WorlflowEngine;
import com.ajaxjs.workflow.model.Execution;
import com.ajaxjs.workflow.model.ProcessModel;
import com.ajaxjs.workflow.model.SubProcessModel;
import com.ajaxjs.workflow.model.entity.Order;
import com.ajaxjs.workflow.model.entity.Process;
import com.ajaxjs.workflow.model.entity.Task;

/**
 * 结束流程实例的处理器
 * 
 */
public class EndProcessHandler implements IHandler {
	/**
	 * 结束当前流程实例，如果存在父流程，则触发父流程继续执行
	 */
	@Override
	public void handle(Execution execution) {
		WorlflowEngine engine = execution.getEngine();
		Order order = execution.getOrder();
		List<Task> tasks = engine.task().findByOrderId(order.getId());

		for (Task task : tasks) {
			if (task.isMajor())
				throw new WorkflowException("存在未完成的主办任务,请确认.");

//			engine.task().complete(task.getId(), SnakerEngine.AUTO);
			engine.task().complete(task.getId());
		}

		engine.order().complete(order.getId());// 结束当前流程实例

		// 如果存在父流程，则重新构造Execution执行对象，交给父流程的SubProcessModel模型execute
		if (order.getParentId() != null || order.getParentId() != 0) {
			Order parentOrder = engine.order().findById(order.getParentId());
			if (parentOrder == null)
				return;

			Process process = engine.process().findById(parentOrder.getProcessId());
			ProcessModel pm = process.getModel();
			if (pm == null)
				return;

			SubProcessModel spm = (SubProcessModel) pm.getNode(order.getParentNodeName());
			Execution newExecution = new Execution(engine, process, parentOrder, execution.getArgs());
			newExecution.setChildOrderId(order.getId());
			newExecution.setTask(execution.getTask());
			spm.execute(newExecution);

			// SubProcessModel执行结果的tasks合并到当前执行对象execution的tasks列表中
			execution.addTasks(newExecution.getTasks());
		}
	}
}
