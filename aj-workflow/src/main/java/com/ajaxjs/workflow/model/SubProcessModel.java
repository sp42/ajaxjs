package com.ajaxjs.workflow.model;

import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.ajaxjs.workflow.Execution;
import com.ajaxjs.workflow.WorkflowEngine;
import com.ajaxjs.workflow.WorkflowException;
import com.ajaxjs.workflow.process.ProcessActive;
import com.ajaxjs.workflow.process.ProcessDefinition;

/**
 * 子流程定义subprocess元素
 * 
 */
public class SubProcessModel extends WorkModel {
	private static final long serialVersionUID = -3923955459202018147L;

	/**
	 * 子流程名称
	 */
	private String processName;

	/**
	 * 子流程版本号
	 */
	private Integer version;

	/**
	 * 子流程定义引用
	 */
	private ProcessModel subProcess;

	@Override
	protected void exec(Execution execution) {
		runOutTransition(execution);
	}

	/**
	 * 是否以 future 方式执行启动子流程任务
	 */
	private boolean isFutureRunning = false;

	/**
	 * 
	 * @param execution
	 */
	public void handle(Execution execution) {
		// 根据子流程模型名称获取子流程定义对象
		WorkflowEngine engine = execution.getEngine();
		ProcessDefinition process = engine.process().findByVersion(getProcessName(), getVersion());

		Execution child = createSubExecution(execution, process, getName());
		ProcessActive active = null;

		if (isFutureRunning) {
			// 创建单个线程执行器来执行启动子流程的任务
			ExecutorService es = Executors.newSingleThreadExecutor();
			// 提交执行任务，并返回 future
			Future<ProcessActive> future = es.submit(new ExecuteTask(execution, process, getName()));

			try {
				es.shutdown();
				active = future.get();
			} catch (InterruptedException e) {
				throw new WorkflowException("创建子流程线程被强制终止执行", e.getCause());
			} catch (ExecutionException e) {
				throw new WorkflowException("创建子流程线程执行异常.", e.getCause());
			}
		} else
			active = engine.startInstanceByExecution(child);

		Objects.requireNonNull(active, "子流程创建失败");
		execution.addTasks(engine.task().findByActiveId(active.getId()));
	}

	/**
	 * Future 模式的任务执行。通过 call 返回任务结果集
	 * 
	 * @author sp42 frank@ajaxjs.com
	 *
	 */
	private static class ExecuteTask implements Callable<ProcessActive> {
		/**
		 * 工作流引擎
		 */
		private WorkflowEngine engine;

		/**
		 * 
		 */
		private Execution child;

		/**
		 * 创建 ExecuteTask
		 * 
		 * @param execution      执行对象
		 * @param process        流程定义
		 * @param parentNodeName 父节点名称
		 */
		public ExecuteTask(Execution execution, ProcessDefinition process, String parentNodeName) {
			engine = execution.getEngine();
			child = SubProcessModel.createSubExecution(execution, process, parentNodeName);
		}

		@Override
		public ProcessActive call() throws Exception {
			return engine.startInstanceByExecution(child);
		}
	}

	/**
	 * 根据当前执行对象 execution、子流程定 义process、当前节点名称产生子流程的执行对象 TODO
	 * 
	 * @param execution      执行对象
	 * @param process        接收流程定义
	 * @param parentNodeName 父节点名称
	 * @return 子流程的执行对象
	 */
	public static Execution createSubExecution(Execution execution, ProcessDefinition process, String parentNodeName) {
		Execution _execution = new Execution(execution.getEngine(), process, null, execution.getArgs());
		_execution.setParentNodeName(parentNodeName);
		_execution.setParentActive(execution.getActive());
		_execution.setOperator(execution.getOperator());

		return _execution;
//		return new Execution(execution, process, parentNodeName);
	}

	public ProcessModel getSubProcess() {
		return subProcess;
	}

	public void setSubProcess(ProcessModel subProcess) {
		this.subProcess = subProcess;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
}
