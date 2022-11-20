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
import com.ajaxjs.workflow.common.WfConstant.TaskType;
import com.ajaxjs.workflow.model.po.OrderPO;
import com.ajaxjs.workflow.model.po.ProcessPO;
import com.ajaxjs.workflow.model.po.Surrogate;
import com.ajaxjs.workflow.model.po.TaskHistoryPO;
import com.ajaxjs.workflow.model.po.TaskPO;
import com.ajaxjs.workflow.service.BaseWfService;

/**
 * 
 * @since 0.1
 */

public class SnakerEngineFacets extends BaseWfService {
	private WorkflowEngine engine = new WorkflowEngine();

	public void initFlows() {
//		engine.process().deploy(WorkflowUtils.getStreamFromClasspath("flows/leave.snaker"));
//		engine.process().deploy(WorkflowUtils.getStreamFromClasspath("flows/borrow.snaker"));
	}

	public WorkflowEngine getEngine() {
		return engine;
	}

	public List<String> getAllProcessNames() {
		List<ProcessPO> list = ProcessDAO.findList();
		List<String> names = new ArrayList<>();

		for (ProcessPO entity : list) {
			if (names.contains(entity.getName()))
				continue;
			else
				names.add(entity.getName());
		}

		return names;
	}

	public OrderPO startInstanceById(Long processId, Long operator, Map<String, Object> args) {
		return engine.startInstanceById(processId, operator, args);
	}

	public OrderPO startInstanceByName(String name, Integer version, Long operator, Map<String, Object> args) {
		return engine.startInstanceByName(name, version, operator, args);
	}

	public OrderPO startAndExecute(String name, Integer version, Long operator, Map<String, Object> args) {
		OrderPO order = engine.startInstanceByName(name, version, operator, args);
		List<TaskPO> tasks = engine.task().findByOrderId(order.getId());
		List<TaskPO> newTasks = new ArrayList<>();

		if (tasks != null && tasks.size() > 0) {
			TaskPO task = tasks.get(0);
			newTasks.addAll(engine.executeTask(task.getId(), operator, args));
		}

		return order;
	}

	public OrderPO startAndExecute(Long processId, Long operator, Map<String, Object> args) {
		OrderPO order = engine.startInstanceById(processId, operator, args);
		List<TaskPO> tasks = engine.task().findByOrderId(order.getId());
		List<TaskPO> newTasks = new ArrayList<>();

		if (tasks != null && tasks.size() > 0) {
			TaskPO task = tasks.get(0);
			newTasks.addAll(engine.executeTask(task.getId(), operator, args));
		}

		return order;
	}

	public List<TaskPO> execute(Long taskId, Long operator, Map<String, Object> args) {
		return engine.executeTask(taskId, operator, args);
	}

	public List<TaskPO> executeAndJump(Long taskId, Long operator, Map<String, Object> args, String nodeName) {
		return engine.executeAndJumpTask(taskId, operator, args, nodeName);
	}

	public List<TaskPO> transferMajor(Long taskId, Long operator, Long... actors) {
		List<TaskPO> tasks = engine.task().createNewTask(taskId, TaskType.MAJOR, actors);
		engine.task().complete(taskId, operator, null);
		return tasks;
	}

	public List<TaskPO> transferAidant(Long taskId, Long operator, Long... actors) {
		List<TaskPO> tasks = engine.task().createNewTask(taskId, TaskType.AIDANT, actors);
		engine.task().complete(taskId, operator, null);
		return tasks;
	}

	public Map<String, Object> flowData(Long orderId, String taskName) {
		Map<String, Object> data = new HashMap<>();

		if (orderId != null && orderId != 0 && StringUtils.hasText(taskName)) {
			List<TaskHistoryPO> histTasks = engine.task().findHistoryTasksByOrderIdAndTaskName(orderId, taskName);
			List<Map<String, Object>> vars = new ArrayList<>();

			for (TaskHistoryPO hist : histTasks) {
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
