package com.ajaxjs.workflow.service.task;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.util.StringUtils;

import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.util.map.JsonHelper;
import com.ajaxjs.workflow.WorkflowEngine;
import com.ajaxjs.workflow.common.WfConstant.TaskType;
import com.ajaxjs.workflow.common.WfDao;
import com.ajaxjs.workflow.model.Args;
import com.ajaxjs.workflow.model.Execution;
import com.ajaxjs.workflow.model.ProcessModel;
import com.ajaxjs.workflow.model.TransitionModel;
import com.ajaxjs.workflow.model.node.NodeModel;
import com.ajaxjs.workflow.model.po.Order;
import com.ajaxjs.workflow.model.po.ProcessPO;
import com.ajaxjs.workflow.model.po.Task;

public class ExecuteTask {
	public static final LogHelper LOGGER = LogHelper.getLog(ExecuteTask.class);

	private WorkflowEngine engine;

	public ExecuteTask(WorkflowEngine engine) {
		this.engine = engine;
	}

	/**
	 * 根据任务主键 id，操作人 id，参数列表执行任务
	 * 
	 * @param taskId
	 * @param operator
	 * @param args
	 * @return
	 */
	public List<Task> executeTask(Long taskId, Long operator, Args args) {
		LOGGER.info("开始执行对象，先初始化相关的参数");

		Execution exec = executeTaskCore(taskId, operator, args); // 完成任务，并且构造执行对象
		if (exec == null)
			return Collections.emptyList();

//		ProcessModel model = execution.getProcess().getModel();
//
//		if (model != null) {
//			NodeModel nodeModel = model.getNode(execution.getTask().getName());
//			nodeModel.execute(execution);// 将执行对象交给该任务对应的节点模型执行
//		}

		exec.execute();
		return exec.getTasks();
	}

	/**
	 * 根据任务主键 id，操作人 id，参数列表执行任务，并且根据 nodeName 跳转到任意节点 1、nodeName 为 null 时，则驳回至上一步处理
	 * 2、nodeName不为null时，则任意跳转，即动态创建转移
	 */
	public List<Task> executeAndJumpTask(Long taskId, Long operator, Args args, String nodeName) {
		Execution execution = executeTaskCore(taskId, operator, args);

		if (execution == null)
			return Collections.emptyList();

		ProcessModel model = execution.getProcess().getModel();
		Objects.requireNonNull(model, "当前任务未找到流程定义模型");

		if (!StringUtils.hasText(nodeName)) {
			Task newTask = engine.taskService.rejectTask(model, execution.getTask());
			execution.addTask(newTask);
		} else {
			NodeModel nodeModel = model.getNode(nodeName);
			Objects.requireNonNull(nodeModel, "根据节点名称[" + nodeName + "]无法找到节点模型");
			// 动态创建转移对象，由转移对象执行 execution 实例
			TransitionModel tm = new TransitionModel();
			tm.setTarget(nodeModel);
			tm.setEnabled(true);
			tm.execute(execution);
		}

		return execution.getTasks();
	}

	/**
	 * 根据任务主键 id，操作人 id，参数列表完成任务，并且构造执行对象
	 * 
	 * @param taskId   任务 id
	 * @param operator 操作人
	 * @param args     参数列表
	 * @return Execution
	 */
	private Execution executeTaskCore(Long taskId, Long operator, Args args) {
		args = Args.getEmpty(args);

		Task task = engine.taskService.complete(taskId, operator, args);

		Order order = WfDao.OrderDAO.findById(task.getOrderId());
		Objects.requireNonNull(order, "指定的流程实例[id=" + task.getOrderId() + "]已完成或不存在");

		Order _update = new Order();
		_update.setId(order.getId());
		_update.setUpdator(operator);
		engine.orderService.update(_update);
		LOGGER.info("更新流程实例 {0} 完成", _update.getId());

		// 协办任务完成不产生执行对象
		if (task.getTaskType() != TaskType.MAJOR)
			return null;

		Map<String, Object> orderMaps = JsonHelper.parseMap(order.getVariable());
		if (orderMaps == null)
			orderMaps = Collections.emptyMap();

		if (orderMaps != null) {
			for (Map.Entry<String, Object> entry : orderMaps.entrySet()) {
				if (args.containsKey(entry.getKey()))
					continue;

				args.put(entry.getKey(), entry.getValue());
			}
		}

		// 创建执行对象
		ProcessPO process = engine.processService.findById(order.getProcessId());
		Execution exec = new Execution(engine, process, order, args);
		exec.setOperator(operator);
		exec.setTask(task);
		LOGGER.info("驱动任务 {0} 继续执行", task.getId());

		return exec;
	}

}
