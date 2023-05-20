package com.ajaxjs.workflow.model;

import java.util.ArrayList;
import java.util.List;

import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.workflow.WorkflowEngine;
import com.ajaxjs.workflow.common.WfException;
import com.ajaxjs.workflow.model.po.Order;
import com.ajaxjs.workflow.model.po.ProcessPO;
import com.ajaxjs.workflow.model.po.Task;
import lombok.Data;

/**
 * 流程执行过程中所传递的执行对象，其中包含流程定义、流程模型、流程实例对象、执行参数、返回的任务列表
 */
@Data
public class Execution {
    public static final LogHelper LOGGER = LogHelper.getLog(Execution.class);

    /**
     * 构造函数，接收流程定义、流程实例对象、执行参数
     *
     * @param engine  引擎
     * @param process 接收流程定义
     * @param order   流程实例对象
     * @param args    执行参数
     */
    public Execution(WorkflowEngine engine, ProcessPO process, Order order, Args args) {
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
     * @param exec       执行对象
     * @param process    接收流程定义
     * @param parentNode 父节点名称
     */
    public Execution(Execution exec, ProcessPO process, String parentNode) {
        if (exec == null || process == null || parentNode == null)
            throw new WfException("构造 Execution 对象失败，请检查 execution、process、parentNodeName 是否为空");

        this.engine = exec.getEngine();
        this.process = process;
        this.args = exec.getArgs();
        this.parentOrder = exec.getOrder();
        this.parentNodeName = parentNode;
        this.operator = exec.getOperator();
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
     * WorkflowEngine holder
     */
    private WorkflowEngine engine;

    /**
     * 流程定义对象
     */
    private ProcessPO process;

    /**
     * 流程实例对象
     */
    private Order order;

    /**
     * 父流程实例
     */
    private Order parentOrder;

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
    private Args args;

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
     * 是否已合并 针对join节点的处理
     */
    private boolean isMerged = false;

    /**
     * 获取流程模型对象
     *
     * @return 流程模型对象
     */
    public ProcessModel getModel() {
        return process.getModel();
    }

    /**
     * 添加任务集合
     *
     * @param tasks 任务集合
     */
    public void addTasks(List<Task> tasks) {
        this.tasks.addAll(tasks);
    }

    /**
     * 添加任务
     *
     * @param task 任务
     */
    public void addTask(Task task) {
        tasks.add(task);
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
}
