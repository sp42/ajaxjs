/*
 * Copyright 2013-2015 www.snakerflow.com. Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.ajaxjs.workflow;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.ajaxjs.ioc.Resource;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.workflow.model.Execution;
import com.ajaxjs.workflow.model.StartModel;
import com.ajaxjs.workflow.model.entity.Order;
import com.ajaxjs.workflow.model.entity.Process;
import com.ajaxjs.workflow.service.OrderService;
import com.ajaxjs.workflow.service.ProcessService;
import com.ajaxjs.workflow.service.SurrpgateService;
import com.ajaxjs.workflow.service.TaskService;

/**
 * 基本的流程引擎实现类
 * 
 * @author yuqs
 * @since 1.0
 */
public class WorlflowEngine {
	public static final LogHelper LOGGER = LogHelper.getLog(WorlflowEngine.class);

	@Resource("ProcessService")
	private ProcessService processService;

	@Resource("OrderService")
	private OrderService orderService;

	/**
	 * 根据流程定义 ID，操作人 ID，参数列表启动流程实例
	 * 
	 * @param id       流程定义 ID
	 * @param operator 操作人 ID
	 * @param args     参数列表
	 * @return 流程实例
	 */
	public Order startInstanceById(Long id, Long operator, Map<String, Object> args) {
		if (args == null)
			args = new HashMap<>();

		return startProcess(processService.findById(id), operator, args);
	}

	/**
	 * 根据流程名称、版本号、操作人、参数列表启动流程实例
	 * 
	 * @param name     流程名称
	 * @param version  版本号
	 * @param operator 操作人 ID
	 * @param args     参数列表
	 * @return 流程实例
	 */
	public Order startInstanceByName(String name, Integer version, Long operator, Map<String, Object> args) {
		if (args == null)
			args = new HashMap<>();

		return startProcess(processService.findByVersion(name, version), operator, args);
	}

	/**
	 * 启动流程实例
	 * 
	 * @param process  流程对象
	 * @param operator 操作人 ID
	 * @param args     参数列表
	 * @return 流程实例
	 */
	private Order startProcess(Process process, Long operator, Map<String, Object> args) {
		processService.check(process, process.getName());
		Execution execution = execute(process, operator, args, null, null);

		if (process.getModel() != null) {
			StartModel start = process.getModel().getStart();
			Objects.requireNonNull(start, "流程定义[name=" + process.getName() + ", version=" + process.getVersion() + "]没有开始节点");
			start.execute(execution);
		}

		return execution.getOrder();
	}

	/**
	 * 根据父执行对象启动子流程实例（用于启动子流程）
	 */
	public Order startInstanceByExecution(Execution execution) {
		Process process = execution.getProcess();
		StartModel start = process.getModel().getStart();
		Objects.requireNonNull(start, "流程定义[id=" + process.getId() + "]没有开始节点");

		Execution current = execute(process, execution.getOperator(), execution.getArgs(), execution.getParentOrder().getId(), execution.getParentNodeName());
		start.execute(current);

		return current.getOrder();
	}

	/**
	 * 创建流程实例，并返回执行对象
	 * 
	 * @param process        流程定义
	 * @param operator       操作人
	 * @param args           参数列表
	 * @param parentId       父流程实例id
	 * @param parentNodeName 启动子流程的父流程节点名称
	 * @return Execution 执行对象
	 */
	private Execution execute(Process process, Long operator, Map<String, Object> args, Long parentId, String parentNodeName) {
		Order order = orderService.create(process, operator, args, parentId, parentNodeName);
		LOGGER.info("创建流程实例对象:" + order);

		Execution current = new Execution(this, process, order, args);
		current.setOperator(operator);

		return current;
	}

//	/**
//	 * 根据任务主键ID，操作人ID，参数列表执行任务
//	 */
//	public List<Task> executeTask(String taskId, String operator, Map<String, Object> args) {
//		// 完成任务，并且构造执行对象
//		Execution execution = execute(taskId, operator, args);
//		if (execution == null)
//			return Collections.emptyList();
//
//		ProcessModel model = execution.getProcess().getModel();
//
//		if (model != null) {
//			NodeModel nodeModel = model.getNode(execution.getTask().getTaskName());
//			// 将执行对象交给该任务对应的节点模型执行
//			nodeModel.execute(execution);
//		}
//
//		return execution.getTasks();
//	}
//
//	/**
//	 * 根据任务主键ID，操作人ID，参数列表执行任务，并且根据nodeName跳转到任意节点 1、nodeName为null时，则驳回至上一步处理
//	 * 2、nodeName不为null时，则任意跳转，即动态创建转移
//	 */
//
//	public List<Task> executeAndJumpTask(String taskId, String operator, Map<String, Object> args, String nodeName) {
//		Execution execution = execute(taskId, operator, args);
//
//		if (execution == null)
//			return Collections.emptyList();
//
//		ProcessModel model = execution.getProcess().getModel();
//		Objects.requireNonNull(model, "当前任务未找到流程定义模型");
//
//		if (CommonUtil.isEmptyString(nodeName)) {
//			Task newTask = task().rejectTask(model, execution.getTask());
//			execution.addTask(newTask);
//		} else {
//			NodeModel nodeModel = model.getNode(nodeName);
//			Objects.requireNonNull(nodeModel, "根据节点名称[" + nodeName + "]无法找到节点模型");
//			// 动态创建转移对象，由转移对象执行execution实例
//			TransitionModel tm = new TransitionModel();
//			tm.setTarget(nodeModel);
//			tm.setEnabled(true);
//			tm.execute(execution);
//		}
//
//		return execution.getTasks();
//	}
//
//	/**
//	 * 根据流程实例ID，操作人ID，参数列表按照节点模型model创建新的自由任务
//	 */
//	public List<Task> createFreeTask(String orderId, String operator, Map<String, Object> args, TaskModel model) {
//		Order order = query().getOrder(orderId);
//		Objects.requireNonNull(order, "指定的流程实例[id=" + orderId + "]已完成或不存在");
//		order.setLastUpdator(operator);
//		order.setLastUpdateTime(DateHelper.getTime());
//		Process process = process().getProcessById(order.getProcessId());
//		Execution execution = new Execution(this, process, order, args);
//		execution.setOperator(operator);
//		
//		return task().createTask(model, execution);
//	}
//
//	/**
//	 * 根据任务主键ID，操作人ID，参数列表完成任务，并且构造执行对象
//	 * 
//	 * @param taskId   任务id
//	 * @param operator 操作人
//	 * @param args     参数列表
//	 * @return Execution
//	 */
//	private Execution execute(String taskId, String operator, Map<String, Object> args) {
//		if (args == null)
//			args = new HashMap<String, Object>();
//
//		Task task = task().complete(taskId, operator, args);
//		LOGGER.info("任务[taskId=" + taskId + "]已完成");
//		
//		Order order = query().getOrder(task.getOrderId());
//		Objects.requireNonNull(order, "指定的流程实例[id=" + task.getOrderId() + "]已完成或不存在");
//		order.setLastUpdator(operator);
//		order.setLastUpdateTime(DateHelper.getTime());
//		order().updateOrder(order);
//
//		// 协办任务完成不产生执行对象
//		if (!task.isMajor())
//			return null;
//
//		Map<String, Object> orderMaps = order.getVariableMap();
//		if (orderMaps != null) {
//			for (Map.Entry<String, Object> entry : orderMaps.entrySet()) {
//				if (args.containsKey(entry.getKey()))
//					continue;
//
//				args.put(entry.getKey(), entry.getValue());
//			}
//		}
//
//		Process process = processService.findById(order.getProcessId());
//		Execution execution = new Execution(this, process, order, args);
//		execution.setOperator(operator);
//		execution.setTask(task);
//
//		return execution;
//	}

	/**
	 * 获取 process 服务
	 * 
	 * @return ProcessService 流程定义服务
	 */
	public ProcessService process() {
		return processService;
	}

//	/**
//	 * 获取查询服务
//	 * 
//	 * @return IQueryService 常用查询服务
//	 */
//	public IQueryService query();

	/**
	 * 获取实例服务
	 * 
	 * @return IQueryService 流程实例服务
	 */
	public OrderService order() {
		return orderService;
	}

	@Resource("TaskService")
	private TaskService taskService;

	/**
	 * 获取任务服务
	 * 
	 * @return TaskService 任务服务
	 */
	public TaskService task() {
		return taskService;
	}

	@Resource("SurrpgateService")
	private SurrpgateService surrpgateService;

	/**
	 * 获取管理服务
	 * 
	 * @return SurrpgateService 管理服务
	 */
	public SurrpgateService manager() {
		return surrpgateService;
	}
}
