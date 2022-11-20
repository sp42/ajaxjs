package com.ajaxjs.workflow.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.workflow.WorkflowEngine;
import com.ajaxjs.workflow.common.WfException;
import com.ajaxjs.workflow.model.po.OrderPO;
import com.ajaxjs.workflow.model.po.ProcessPO;
import com.ajaxjs.workflow.model.po.TaskPO;

/**
 * 流程执行过程中所传递的执行对象，其中包含流程定义、流程模型、流程实例对象、执行参数、返回的任务列表
 * 
 */
public class Execution implements Serializable {
	public static final LogHelper LOGGER = LogHelper.getLog(Execution.class);

	private static final long serialVersionUID = 3730741790729624400L;

	/**
	 * 构造函数，接收流程定义、流程实例对象、执行参数
	 * 
	 * @param process 接收流程定义
	 * @param order   流程实例对象
	 * @param args    执行参数
	 */
	public Execution(WorkflowEngine engine, ProcessPO process, OrderPO order, Map<String, Object> args) {
		if (process == null || order == null)
			throw new WfException("构造 Execution 对象失败，请检查 process、order 是否为空");

		this.engine = engine;
		this.process = process;
		this.order = order;
		this.args = args;
	}

	/**
	 * 用于产生子流程执行对象使用
	 * 
	 * @param execution      执行对象
	 * @param process        接收流程定义
	 * @param parentNodeName 父节点名称
	 */
	public Execution(Execution execution, ProcessPO process, String parentNodeName) {
		if (execution == null || process == null || parentNodeName == null)
			throw new WfException("构造 Execution 对象失败，请检查 execution、process、parentNodeName 是否为空");

		this.engine = execution.getEngine();
		this.process = process;
		this.args = execution.getArgs();
		this.parentOrder = execution.getOrder();
		this.parentNodeName = parentNodeName;
		this.operator = execution.getOperator();
	}

	/**
	 * 将执行对象交给该任务对应的节点模型执行
	 */
	public void execute() {
		ProcessModel model = process.getModel();

		String taskName = task.getName();
		LOGGER.info("正在执行任务 {0}", taskName);
		model.getNode(taskName).execute(this);
	}

	/**
	 * 根据当前执行对象 execution、子流程定义 process、当前节点名称产生子流程的执行对象
	 * 
	 * @param execution      执行对象
	 * @param process        接收流程定义
	 * @param parentNodeName 父节点名称
	 * @return 子流程的执行对象
	 */
	public static Execution createSubExecution(Execution execution, ProcessPO process, String parentNodeName) {
		return new Execution(execution, process, parentNodeName);
	}

	/**
	 * WorlflowEngine holder
	 */
	private WorkflowEngine engine;

	/**
	 * 流程定义对象
	 */
	private ProcessPO process;

	/**
	 * 流程实例对象
	 */
	private OrderPO order;

	/**
	 * 父流程实例
	 */
	private OrderPO parentOrder;

	/**
	 * 父流程实例节点名称
	 */
	private String parentNodeName;

	/**
	 * 子流程实例节点名称
	 */
	private Long childOrderId;

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
	private TaskPO task;

	/**
	 * 返回的任务列表
	 */
	private List<TaskPO> tasks = new ArrayList<>();

	/**
	 * 是否已合并 针对join节点的处理
	 */
	private boolean isMerged = false;

	/**
	 * 获取流程定义对象
	 * 
	 * @return
	 */
	public ProcessPO getProcess() {
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
	public OrderPO getOrder() {
		return order;
	}

	/**
	 * 获取执行参数
	 * 
	 * @return
	 */
	public Map<String, Object> getArgs() {
		return args;
	}

	/**
	 * 返回任务结果集
	 * 
	 * @return
	 */
	public List<TaskPO> getTasks() {
		return tasks;
	}

	/**
	 * 添加任务集合
	 * 
	 * @param tasks
	 */
	public void addTasks(List<TaskPO> tasks) {
		this.tasks.addAll(tasks);
	}

	/**
	 * 添加任务
	 * 
	 * @param task
	 */
	public void addTask(TaskPO task) {
		this.tasks.add(task);
	}

	/**
	 * 返回当前操作人ID
	 * 
	 * @return
	 */
	public Long getOperator() {
		return operator;
	}

	/**
	 * 设置当前操作人ID
	 * 
	 * @param operator
	 */
	public void setOperator(Long operator) {
		this.operator = operator;
	}

	/**
	 * 返回任务
	 * 
	 * @return
	 */
	public TaskPO getTask() {
		return task;
	}

	/**
	 * 设置任务
	 * 
	 * @param task
	 */
	public void setTask(TaskPO task) {
		this.task = task;
	}

	/**
	 * 判断是否已经成功合并
	 * 
	 * @return
	 */
	public boolean isMerged() {
		return isMerged;
	}

	/**
	 * 设置是否为已合并
	 * 
	 * @param isMerged
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

	public OrderPO getParentOrder() {
		return parentOrder;
	}

	public String getParentNodeName() {
		return parentNodeName;
	}

	public Long getChildOrderId() {
		return childOrderId;
	}

	public void setChildOrderId(Long childOrderId) {
		this.childOrderId = childOrderId;
	}
}
