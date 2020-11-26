package com.ajaxjs.workflow.model;

import java.util.Collections;
import java.util.List;

import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.workflow.Execution;
import com.ajaxjs.workflow.WorkflowEngine;
import com.ajaxjs.workflow.WorkflowException;
import com.ajaxjs.workflow.process.ProcessActive;
import com.ajaxjs.workflow.process.ProcessDefinition;
import com.ajaxjs.workflow.task.Task;

/**
 * 结束节点 End 元素
 * 
 */
public class EndModel extends NodeModel {
	private static final LogHelper LOGGER = LogHelper.getLog(NodeModel.class);

	private static final long serialVersionUID = -7793175180140842894L;

	@Override
	public void exec(Execution execution) {
		LOGGER.info("准备要完成了，运行  End 节点");

		WorkflowEngine engine = execution.getEngine();
		ProcessActive active = execution.getActive();
		List<Task> tasks = engine.task().findByActiveId(active.getId());// 查找当前活动的任务

		if (!CommonUtil.isNull(tasks))
			for (Task task : tasks) {
				if (task.isMajor())
					throw new WorkflowException("存在未完成的主办任务，请确认！？");

//				engine.task().complete(task.getId(), SnakerEngine.AUTO);
				engine.task().complete(task.getId(), null, null);
			}

		engine.active().complete(active.getId());// 结束当前流程实例

		// 如果存在父流程，则重新构造 Execution 执行对象，交给父流程的 SubProcessModel 模型 execute
		if (active != null && (active.getParentId() != null /* || pa.getParentId() != 0 */)) {
			ProcessActive parentActive = engine.active().findById(active.getParentId());

			if (parentActive == null)
				return;

			ProcessDefinition def = engine.process().findById(parentActive.getProcessId());
			ProcessModel pm = def.getModel();

			if (pm == null)
				return;

			SubProcessModel spm = (SubProcessModel) pm.getNode(active.getParentNodeName());
			Execution newExecution = new Execution(engine, def, parentActive, execution.getArgs());
			newExecution.setChildActiveId(active.getId());
			newExecution.setTask(execution.getTask());
			spm.execute(newExecution);

			// SubProcessModel 执行结果的 tasks 合并到当前执行对象 execution 的 tasks 列表中
			execution.addTasks(newExecution.getTasks());
		}
	}

	/**
	 * 结束节点无输出变迁
	 */
	@Override
	public List<TransitionModel> getOutputs() {
		return Collections.emptyList();
	}
}
