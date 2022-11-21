package com.ajaxjs.workflow;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.workflow.common.WfConstant;
import com.ajaxjs.workflow.model.Args;
import com.ajaxjs.workflow.model.Execution;
import com.ajaxjs.workflow.model.node.StartModel;
import com.ajaxjs.workflow.model.po.Order;
import com.ajaxjs.workflow.model.po.ProcessPO;
import com.ajaxjs.workflow.model.po.Task;
import com.ajaxjs.workflow.service.OrderService;
import com.ajaxjs.workflow.service.ProcessService;
import com.ajaxjs.workflow.service.SurrogateService;
import com.ajaxjs.workflow.service.TaskService;
import com.ajaxjs.workflow.service.task.ExecuteTask;

/**
 * 基本的流程引擎实现类
 * 
 */
@Component
public class WorkflowEngine {
	public static final LogHelper LOGGER = LogHelper.getLog(WorkflowEngine.class);

	/**
	 * 根据流程定义 id，操作人 id，参数列表启动流程实例
	 * 
	 * @param id       流程定义 id
	 * @param operator 操作人 id
	 * @param args     参数列表
	 * @return 流程实例
	 */
	public Order startInstanceById(Long id, Long operator, Args args) {
		args = Args.getEmpty(args);

		return startInstance(processService.findById(id), operator, args);
	}

	/**
	 * 根据流程名称、版本号、操作人、参数列表启动流程实例
	 * 
	 * @param name     流程名称
	 * @param ver      版本号
	 * @param operator 操作人 id
	 * @param args     参数列表
	 * @return 流程实例
	 */
	public Order startInstanceByName(String name, Integer ver, Long operator, Args args) {
		args = Args.getEmpty(args);

		return startInstance(processService.findByVersion(name, ver), operator, args);
	}

	/**
	 * 根据父执行对象启动子流程实例（用于启动子流程）
	 * 
	 * @param exec 父执行对象
	 * @return 流程实例
	 */
	public Order startInstanceByExecution(Execution exec) {
		ProcessPO process = exec.getProcess();
		Execution current = createExecute(process, exec.getOperator(), exec.getArgs(), exec.getParentOrder().getId(), exec.getParentNodeName());
		runStart(process, current);

		return current.getOrder();
	}

	/**
	 * 启动流程实例
	 * 
	 * @param process  流程对象
	 * @param operator 操作人 id
	 * @param args     参数列表
	 * @return 流程实例
	 */
	private Order startInstance(ProcessPO process, Long operator, Args args) {
		// 检查流程状态
		String idOrName = process.getName();
		Objects.requireNonNull(process, "指定的流程定义[id/name=" + idOrName + "]不存在");

		if (process.getStat() != null && process.getStat() == WfConstant.STATE_FINISH)
			throw new IllegalArgumentException("指定的流程定义[id/name=" + idOrName + ",version=" + process.getVersion() + "]为非活动状态");

		Execution exec = createExecute(process, operator, args, null, null);

		if (process.getModel() != null)
			runStart(process, exec);

		return exec.getOrder();
	}

	/**
	 * 运行 start 节点
	 * 
	 * @param process
	 * @param exec
	 */
	private static void runStart(ProcessPO process, Execution exec) {
		StartModel start = process.getModel().getStart();
		Objects.requireNonNull(start, "流程定义[id=" + process.getId() + "]没有开始节点");

		LOGGER.info("运行 start 节点");
		start.execute(exec);
	}

	/**
	 * 创建流程实例，并返回执行对象
	 * 
	 * @param process        流程定义
	 * @param operator       操作人
	 * @param args           参数列表
	 * @param parentId       父流程实例 id
	 * @param parentNodeName 启动子流程的父流程节点名称
	 * @return Execution 执行对象
	 */
	private Execution createExecute(ProcessPO process, Long operator, Args args, Long parentId, String parentNodeName) {
		Order order = orderService.create(process, operator, args, parentId, parentNodeName);

		Execution current = new Execution(this, process, order, args);
		current.setOperator(operator);

		LOGGER.info("创建 Execution 对象完毕");
		return current;
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
		return new ExecuteTask(this).executeTask(taskId, operator, args);
	}

	/**
	 * 
	 * @param taskId
	 * @param operator
	 * @param args
	 * @param nodeName
	 * @return
	 */
	public List<Task> executeAndJumpTask(Long taskId, Long operator, Args args, String nodeName) {
		return new ExecuteTask(this).executeAndJumpTask(taskId, operator, args, nodeName);
	}

	@Autowired
	public ProcessService processService;

	@Autowired
	public OrderService orderService;

	@Autowired
	public TaskService taskService;

	@Autowired
	public SurrogateService surrogateService;
}
