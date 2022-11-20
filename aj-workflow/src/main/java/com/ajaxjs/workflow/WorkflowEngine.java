package com.ajaxjs.workflow;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.util.map.JsonHelper;
import com.ajaxjs.workflow.common.WfConstant;
import com.ajaxjs.workflow.common.WfDao;
import com.ajaxjs.workflow.model.Execution;
import com.ajaxjs.workflow.model.ProcessModel;
import com.ajaxjs.workflow.model.TransitionModel;
import com.ajaxjs.workflow.model.node.NodeModel;
import com.ajaxjs.workflow.model.node.StartModel;
import com.ajaxjs.workflow.model.po.OrderPO;
import com.ajaxjs.workflow.model.po.ProcessPO;
import com.ajaxjs.workflow.model.po.TaskPO;
import com.ajaxjs.workflow.model.work.TaskModel;
import com.ajaxjs.workflow.service.OrderService;
import com.ajaxjs.workflow.service.ProcessService;
import com.ajaxjs.workflow.service.SurrogateService;
import com.ajaxjs.workflow.service.TaskService;

/**
 * 基本的流程引擎实现类
 * 
 * 
 * @since 1.0
 */

@Component
public class WorkflowEngine {
	public static final LogHelper LOGGER = LogHelper.getLog(WorkflowEngine.class);

	/**
	 * 根据流程定义 ID，操作人 ID，参数列表启动流程实例
	 * 
	 * @param id       流程定义 ID
	 * @param operator 操作人 ID
	 * @param args     参数列表
	 * @return 流程实例
	 */
	public OrderPO startInstanceById(Long id, Long operator, Map<String, Object> args) {
		if (args == null)
			args = new HashMap<>();

		return startInstance(processService.findById(id), operator, args);
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
	public OrderPO startInstanceByName(String name, Integer version, Long operator, Map<String, Object> args) {
		if (args == null)
			args = new HashMap<>();

		return startInstance(processService.findByVersion(name, version), operator, args);
	}

	/**
	 * 根据父执行对象启动子流程实例（用于启动子流程）
	 * 
	 * @param execution 父执行对象
	 * @return 流程实例
	 */
	public OrderPO startInstanceByExecution(Execution execution) {
		ProcessPO process = execution.getProcess();
		Execution current = createExecute(process, execution.getOperator(), execution.getArgs(), execution.getParentOrder().getId(),
				execution.getParentNodeName());
		runStart(process, current);

		return current.getOrder();
	}

	/**
	 * 启动流程实例
	 * 
	 * @param process  流程对象
	 * @param operator 操作人 ID
	 * @param args     参数列表
	 * @return 流程实例
	 */
	private OrderPO startInstance(ProcessPO process, Long operator, Map<String, Object> args) {
		// 检查流程状态
		String idOrName = process.getName();
		Objects.requireNonNull(process, "指定的流程定义[id/name=" + idOrName + "]不存在");

		if (process.getStat() != null && process.getStat() == WfConstant.STATE_FINISH)
			throw new IllegalArgumentException("指定的流程定义[id/name=" + idOrName + ",version=" + process.getVersion() + "]为非活动状态");

		Execution execution = createExecute(process, operator, args, null, null);

		if (process.getModel() != null)
			runStart(process, execution);

		return execution.getOrder();
	}

	/**
	 * 运行 start 节点
	 * 
	 * @param process
	 * @param execution
	 */
	private static void runStart(ProcessPO process, Execution execution) {
		StartModel start = process.getModel().getStart();
		Objects.requireNonNull(start, "流程定义[id=" + process.getId() + "]没有开始节点");

		LOGGER.info("运行 start 节点");
		start.execute(execution);
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
	private Execution createExecute(ProcessPO process, Long operator, Map<String, Object> args, Long parentId, String parentNodeName) {
		OrderPO order = orderService.create(process, operator, args, parentId, parentNodeName);

		Execution current = new Execution(this, process, order, args);
		current.setOperator(operator);

		LOGGER.info("创建 Execution 对象完毕");
		return current;
	}

	/**
	 * 根据任务主键 ID，操作人ID，参数列表完成任务，并且构造执行对象
	 * 
	 * @param taskId   任务id
	 * @param operator 操作人
	 * @param args     参数列表
	 * @return Execution
	 */
	private Execution executeTaskCore(Long taskId, Long operator, Map<String, Object> args) {
		if (args == null)
			args = new HashMap<String, Object>();

		TaskPO task = task().complete(taskId, operator, args);

		OrderPO order = WfDao.OrderDAO.findById(task.getOrderId());
		Objects.requireNonNull(order, "指定的流程实例[id=" + task.getOrderId() + "]已完成或不存在");

		OrderPO _update = new OrderPO();
		_update.setId(order.getId());
		_update.setUpdator(operator);
		order().update(_update);
		LOGGER.info("更新流程实例 {0} 完成", _update.getId());

		// 协办任务完成不产生执行对象
		if (!task.isMajor())
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
		ProcessPO process = processService.findById(order.getProcessId());
		Execution execution = new Execution(this, process, order, args);
		execution.setOperator(operator);
		execution.setTask(task);
		LOGGER.info("驱动任务 {0} 继续执行", task.getId());

		return execution;
	}

	/**
	 * 根据任务主键ID，操作人ID，参数列表执行任务
	 * 
	 * @param taskId
	 * @param operator
	 * @param args
	 * @return
	 */
	public List<TaskPO> executeTask(Long taskId, Long operator, Map<String, Object> args) {
		LOGGER.info("开始执行对象，先初始化相关的参数");

		Execution execution = executeTaskCore(taskId, operator, args); // 完成任务，并且构造执行对象
		if (execution == null)
			return Collections.emptyList();

//		ProcessModel model = execution.getProcess().getModel();
//
//		if (model != null) {
//			NodeModel nodeModel = model.getNode(execution.getTask().getName());
//			nodeModel.execute(execution);// 将执行对象交给该任务对应的节点模型执行
//		}

		execution.execute();
		return execution.getTasks();
	}

	/**
	 * 根据流程实例ID，操作人ID，参数列表按照节点模型model创建新的自由任务
	 */
	public List<TaskPO> createFreeTask(Long orderId, Long operator, Map<String, Object> args, TaskModel model) {
		OrderPO order = WfDao.OrderDAO.findById(orderId);
		Objects.requireNonNull(order, "指定的流程实例[id=" + orderId + "]已完成或不存在");
		order.setUpdator(operator);
//		order.setLastUpdateTime(DateHelper.getTime());

		ProcessPO process = process().findById(order.getProcessId());
		Execution execution = new Execution(this, process, order, args);
		execution.setOperator(operator);

		return task().createTask(model, execution);
	}

	/**
	 * 根据任务主键ID，操作人ID，参数列表执行任务，并且根据nodeName跳转到任意节点 1、nodeName为null时，则驳回至上一步处理
	 * 2、nodeName不为null时，则任意跳转，即动态创建转移
	 */
	public List<TaskPO> executeAndJumpTask(Long taskId, Long operator, Map<String, Object> args, String nodeName) {
		Execution execution = executeTaskCore(taskId, operator, args);

		if (execution == null)
			return Collections.emptyList();

		ProcessModel model = execution.getProcess().getModel();
		Objects.requireNonNull(model, "当前任务未找到流程定义模型");

		if (!StringUtils.hasText(nodeName)) {
			TaskPO newTask = task().rejectTask(model, execution.getTask());
			execution.addTask(newTask);
		} else {
			NodeModel nodeModel = model.getNode(nodeName);
			Objects.requireNonNull(nodeModel, "根据节点名称[" + nodeName + "]无法找到节点模型");
			// 动态创建转移对象，由转移对象执行execution实例
			TransitionModel tm = new TransitionModel();
			tm.setTarget(nodeModel);
			tm.setEnabled(true);
			tm.execute(execution);
		}

		return execution.getTasks();
	}

	@Autowired
	private ProcessService processService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private SurrogateService surrogateService;

	/**
	 * 获取 process 服务
	 * 
	 * @return ProcessService 流程定义服务
	 */
	public ProcessService process() {
		return processService;
	}

	/**
	 * 获取实例服务
	 * 
	 * @return OrderService 流程实例服务
	 */
	public OrderService order() {
		return orderService;
	}

	/**
	 * 获取任务服务
	 * 
	 * @return TaskService 任务服务
	 */
	public TaskService task() {
		return taskService;
	}

	/**
	 * 获取管理服务
	 * 
	 * @return SurrogateService 管理服务
	 */
	public SurrogateService manager() {
		return surrogateService;
	}
}
