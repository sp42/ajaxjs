package com.ajaxjs.workflow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ajaxjs.workflow.model.ProcessModel;
import com.ajaxjs.workflow.process.ProcessActive;
import com.ajaxjs.workflow.process.ProcessDefinition;
import com.ajaxjs.workflow.task.Task;

/**
 * 流程执行过程中所传递的执行对象，其中包含流程定义、流程模型、流程实例对象、执行参数、返回的任务列表
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class Execution implements Serializable {
	private static final long serialVersionUID = 3730741790729624400L;

	/**
	 * 创建一个 Execution 实例
	 * 
	 * @param engine 工作流引擎实例
	 * @param def    接收流程定义
	 * @param active 流程实例对象
	 * @param args   执行参数
	 */
	public Execution(WorkflowEngine engine, ProcessDefinition def, ProcessActive active, Map<String, Object> args) {
		if (def == null)
			throw new WorkflowException("构造 Execution 对象失败，请检查 process、activeProcess 是否为空");

		this.engine = engine;
		this.process = def;
		this.active = active;
		this.args = args;
	}

	/**
	 * 用于产生子流程执行对象使用
	 * 
	 * @deprecated
	 * @param execution      执行对象
	 * @param def            接收流程定义
	 * @param parentNodeName 父节点名称
	 */
	public Execution(Execution execution, ProcessDefinition def, String parentNodeName) {
		if (execution == null || def == null || parentNodeName == null)
			throw new WorkflowException("构造 Execution 对象失败，请检查 execution、process、parentNodeName 是否为空");

		this.engine = execution.getEngine();
		this.process = def;
		this.args = execution.getArgs();
		this.parentActive = execution.getActive();
		this.parentNodeName = parentNodeName;
		this.operator = execution.getOperator();
	}

//	/**
//	 * 将执行对象交给该任务对应的节点模型执行
//	 */
//	public void execute() {
//		String taskName = task.getName();
//		LOGGER.info("正在执行任务 {0}", taskName);
//		process.getModel().getNode(taskName).execute(this);
//	}

	/**
	 * WorlflowEngine holder
	 */
	private WorkflowEngine engine;

	/**
	 * 流程定义对象
	 */
	private ProcessDefinition process;

	/**
	 * 流程实例对象
	 */
	private ProcessActive active;

	/**
	 * 父流程实例
	 */
	private ProcessActive parentActive;

	/**
	 * 父流程实例节点名称
	 */
	private String parentNodeName;

	/**
	 * 子流程实例节点名称
	 */
	private Long childActiveId;

	/**
	 * 执行参数
	 */
	private Map<String, Object> args;

	/**
	 * 操作人
	 */
	private Long operator;

	/**
	 * 任务
	 */
	private Task task;

	/**
	 * 返回的任务列表
	 */
	private List<Task> tasks = new ArrayList<>();

	/**
	 * 是否已合并 针对 join 节点的处理
	 */
	private boolean isMerged = false;

	/**
	 * 获取流程定义对象
	 * 
	 * @return
	 */
	public ProcessDefinition getProcess() {
		return process;
	}

	/**
	 * 获取流程模型对象
	 * 
	 * @return
	 */
	public ProcessModel getModel() {
		return process.getModel();
	}

	/**
	 * 获取流程实例对象
	 * 
	 * @return
	 */
	public ProcessActive getActive() {
		return active;
	}

	/**
	 * 获取执行参数
	 * 
	 * @return 执行参数
	 */
	public Map<String, Object> getArgs() {
		return args;
	}

	/**
	 * 返回任务结果集
	 * 
	 * @return 任务结果集
	 */
	public List<Task> getTasks() {
		return tasks;
	}

	/**
	 * 添加任务集合
	 * 
	 * @param tasks 任务集合
	 */
	public void addTasks(List<Task> tasks) {
		tasks.addAll(tasks);
	}

	/**
	 * 添加任务
	 * 
	 * @param task
	 */
	public void addTask(Task task) {
		tasks.add(task);
	}

	/**
	 * 返回当前操作人ID
	 * 
	 * @return 当前操作人ID
	 */
	public Long getOperator() {
		return operator;
	}

	/**
	 * 设置当前操作人ID
	 * 
	 * @param operator 当前操作人ID
	 */
	public void setOperator(Long operator) {
		this.operator = operator;
	}

	/**
	 * 返回任务
	 * 
	 * @return 任务
	 */
	public Task getTask() {
		return task;
	}

	/**
	 * 设置任务
	 * 
	 * @param task 任务
	 */
	public void setTask(Task task) {
		this.task = task;
	}

	/**
	 * 判断是否已经成功合并
	 * 
	 * @return 是否已经成功合并
	 */
	public boolean isMerged() {
		return isMerged;
	}

	/**
	 * 设置是否为已合并
	 * 
	 * @param isMerged 是否为已合并
	 */
	public void setMerged(boolean isMerged) {
		this.isMerged = isMerged;
	}

	/**
	 * 获取引擎
	 * 
	 * @return 引擎
	 */
	public WorkflowEngine getEngine() {
		return engine;
	}

	public ProcessActive getParentActive() {
		return parentActive;
	}

	public void setParentActive(ProcessActive parentActive) {
		this.parentActive = parentActive;
	}

	public String getParentNodeName() {
		return parentNodeName;
	}

	public void setParentNodeName(String parentNodeName) {
		this.parentNodeName = parentNodeName;
	}

	public Long getChildActiveId() {
		return childActiveId;
	}

	public void setChildActiveId(Long childActiveId) {
		this.childActiveId = childActiveId;
	}
}
