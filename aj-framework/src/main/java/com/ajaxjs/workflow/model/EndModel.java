package com.ajaxjs.workflow.model;

import java.util.Collections;
import java.util.List;

import org.springframework.util.ObjectUtils;

import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.workflow.WorkflowEngine;
import com.ajaxjs.workflow.WorkflowException;
import com.ajaxjs.workflow.model.entity.Order;
import com.ajaxjs.workflow.model.entity.Process;
import com.ajaxjs.workflow.model.entity.Task;

/**
 * 结束节点end元素
 * 
 */
public class EndModel extends NodeModel {
	public static final LogHelper LOGGER = LogHelper.getLog(NodeModel.class);

	private static final long serialVersionUID = -7793175180140842894L;

	@Override
	public void exec(Execution execution) {
		fire(new IHandler() {
			@Override
			public void handle(Execution execution) {
				LOGGER.info("准备要完成了，运行  End 节点");

				WorkflowEngine engine = execution.getEngine();
				Order order = execution.getOrder();
				List<Task> tasks = engine.task().findByOrderId(order.getId());// 查找当前活动的任务

				if (!ObjectUtils.isEmpty(tasks))
					for (Task task : tasks) {
						if (task.isMajor())
							throw new WorkflowException("存在未完成的主办任务，请确认！？");

//					engine.task().complete(task.getId(), SnakerEngine.AUTO);
						engine.task().complete(task.getId(), null, null);
					}

				engine.order().complete(order.getId());// 结束当前流程实例

				// 如果存在父流程，则重新构造 Execution 执行对象，交给父流程的 SubProcessModel 模型 execute

				if (order != null && (order.getParentId() != null /* || order.getParentId() != 0 */)) {
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

					// SubProcessModel 执行结果的 tasks 合并到当前执行对象 execution 的 tasks 列表中
					execution.addTasks(newExecution.getTasks());
				}
			}
		}, execution);
	}

	/**
	 * 结束节点无输出变迁
	 */
	@Override
	public List<TransitionModel> getOutputs() {
		return Collections.emptyList();
	}
}