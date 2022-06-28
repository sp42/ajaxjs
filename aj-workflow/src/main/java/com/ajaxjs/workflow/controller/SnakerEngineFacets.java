/* Copyright 2013-2015 www.snakerflow.com
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
package com.ajaxjs.workflow.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.ajaxjs.util.map.JsonHelper;
import com.ajaxjs.workflow.WorkflowEngine;
import com.ajaxjs.workflow.model.TaskModel.TaskType;
import com.ajaxjs.workflow.model.entity.Order;
import com.ajaxjs.workflow.model.entity.Process;
import com.ajaxjs.workflow.model.entity.Surrogate;
import com.ajaxjs.workflow.model.entity.Task;
import com.ajaxjs.workflow.model.entity.TaskHistory;

/**
 * @author yuqs
 * @since 0.1
 */

public class SnakerEngineFacets {
	private WorkflowEngine engine = new WorkflowEngine();

	public void initFlows() {
//		engine.process().deploy(WorkflowUtils.getStreamFromClasspath("flows/leave.snaker"));
//		engine.process().deploy(WorkflowUtils.getStreamFromClasspath("flows/borrow.snaker"));
	}

	public WorkflowEngine getEngine() {
		return engine;
	}

	public List<String> getAllProcessNames() {
		List<Process> list = engine.process().findList();
		List<String> names = new ArrayList<>();

		for (Process entity : list) {
			if (names.contains(entity.getName()))
				continue;
			else
				names.add(entity.getName());
		}

		return names;
	}

	public Order startInstanceById(Long processId, Long operator, Map<String, Object> args) {
		return engine.startInstanceById(processId, operator, args);
	}

	public Order startInstanceByName(String name, Integer version, Long operator, Map<String, Object> args) {
		return engine.startInstanceByName(name, version, operator, args);
	}

	public Order startAndExecute(String name, Integer version, Long operator, Map<String, Object> args) {
		Order order = engine.startInstanceByName(name, version, operator, args);
		List<Task> tasks = engine.task().findByOrderId(order.getId());
		List<Task> newTasks = new ArrayList<>();

		if (tasks != null && tasks.size() > 0) {
			Task task = tasks.get(0);
			newTasks.addAll(engine.executeTask(task.getId(), operator, args));
		}

		return order;
	}

	public Order startAndExecute(Long processId, Long operator, Map<String, Object> args) {
		Order order = engine.startInstanceById(processId, operator, args);
		List<Task> tasks = engine.task().findByOrderId(order.getId());
		List<Task> newTasks = new ArrayList<>();

		if (tasks != null && tasks.size() > 0) {
			Task task = tasks.get(0);
			newTasks.addAll(engine.executeTask(task.getId(), operator, args));
		}

		return order;
	}

	public List<Task> execute(Long taskId, Long operator, Map<String, Object> args) {
		return engine.executeTask(taskId, operator, args);
	}

	public List<Task> executeAndJump(Long taskId, Long operator, Map<String, Object> args, String nodeName) {
		return engine.executeAndJumpTask(taskId, operator, args, nodeName);
	}

	public List<Task> transferMajor(Long taskId, Long operator, Long... actors) {
		List<Task> tasks = engine.task().createNewTask(taskId, TaskType.Major.ordinal(), actors);
		engine.task().complete(taskId, operator, null);
		return tasks;
	}

	public List<Task> transferAidant(Long taskId, Long operator, Long... actors) {
		List<Task> tasks = engine.task().createNewTask(taskId, TaskType.Aidant.ordinal(), actors);
		engine.task().complete(taskId, operator, null);
		return tasks;
	}

	public Map<String, Object> flowData(Long orderId, String taskName) {
		Map<String, Object> data = new HashMap<>();

		if (orderId != null && orderId != 0 && StringUtils.hasText(taskName)) {
			List<TaskHistory> histTasks = engine.task().findHistoryTasksByOrderIdAndTaskName(orderId, taskName);
			List<Map<String, Object>> vars = new ArrayList<>();

			for (TaskHistory hist : histTasks) {
				Map<String, Object> map = JsonHelper.parseMap(hist.getVariable());
				if (map == null)
					map = Collections.emptyMap();

				vars.add(map);
			}

			data.put("vars", vars);
			data.put("histTasks", histTasks);
		}

		return data;
	}

	public void addSurrogate(Surrogate entity) {
		if (entity.getStat() == null)
			entity.setStat(1);

		engine.manager().create(entity);
	}

	public void deleteSurrogate(Long id) {
		Surrogate entity = new Surrogate();
		entity.setId(id);
		engine.manager().delete(entity);
	}

}
