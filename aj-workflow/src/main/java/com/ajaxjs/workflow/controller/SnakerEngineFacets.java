package com.ajaxjs.workflow.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.ajaxjs.util.map.JsonHelper;
import com.ajaxjs.workflow.WorkflowEngine;
import com.ajaxjs.workflow.model.Args;
import com.ajaxjs.workflow.model.po.Order;
import com.ajaxjs.workflow.model.po.ProcessPO;
import com.ajaxjs.workflow.model.po.Surrogate;
import com.ajaxjs.workflow.model.po.Task;
import com.ajaxjs.workflow.model.po.TaskHistory;
import com.ajaxjs.workflow.service.BaseWfService;
import com.ajaxjs.workflow.service.task.TaskFactory;

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

	public Order startInstanceById(Long processId, Long operator, Args args) {
		return engine.startInstanceById(processId, operator, args);
	}

	public Order startInstanceByName(String name, Integer version, Long operator, Args args) {
		return engine.startInstanceByName(name, version, operator, args);
	}

	public Order startAndExecute(String name, Integer version, Long operator, Args args) {
		Order order = engine.startInstanceByName(name, version, operator, args);
		List<Task> tasks = engine.taskService.findByOrderId(order.getId());
		List<Task> newTasks = new ArrayList<>();

		if (tasks != null && tasks.size() > 0) {
			Task task = tasks.get(0);
			newTasks.addAll(engine.executeTask(task.getId(), operator, args));
		}

		return order;
	}

	public Order startAndExecute(Long processId, Long operator, Args args) {
		Order order = engine.startInstanceById(processId, operator, args);
		List<Task> tasks = engine.taskService.findByOrderId(order.getId());
		List<Task> newTasks = new ArrayList<>();

		if (tasks != null && tasks.size() > 0) {
			Task task = tasks.get(0);
			newTasks.addAll(engine.executeTask(task.getId(), operator, args));
		}

		return order;
	}

	public List<Task> executeAndJump(Long taskId, Long operator, Args args, String nodeName) {
		return engine.executeAndJumpTask(taskId, operator, args, nodeName);
	}

	public List<Task> transferMajor(Long taskId, Long operator, Long... actors) {
		List<Task> tasks = TaskFactory.createNewTask(taskId, TaskType.MAJOR, actors);
		engine.taskService.complete(taskId, operator, null);
		return tasks;
	}

	public List<Task> transferAidant(Long taskId, Long operator, Long... actors) {
		List<Task> tasks = TaskFactory.createNewTask(taskId, TaskType.AIDANT, actors);
		engine.taskService.complete(taskId, operator, null);
		return tasks;
	}

	public Args flowData(Long orderId, String taskName) {
		Map<String, Object> data = new HashMap<>();

		if (orderId != null && orderId != 0 && StringUtils.hasText(taskName)) {
			List<TaskHistory> histTasks = engine.taskService.findHistoryTasksByOrderIdAndTaskName(orderId, taskName);
			List<Args> vars = new ArrayList<>();

			for (TaskHistory hist : histTasks) {
				Map<String, Object> map = JsonHelper.parseMap(hist.getVariable());
				if (map == null)
					map = Collections.emptyMap();

				Args args = new Args();
				args.putAll(map);
				vars.add(args);
			}

			data.put("vars", vars);
			data.put("histTasks", histTasks);
		}

		Args args = new Args();
		args.putAll(data);

		return args;
	}

	public void addSurrogate(Surrogate entity) {
		if (entity.getStat() == null)
			entity.setStat(1);

		SurrogateDAO.create(entity);
	}

	public void deleteSurrogate(Long id) {
		Surrogate entity = new Surrogate();
		entity.setId(id);
		SurrogateDAO.delete(entity);
	}
}
