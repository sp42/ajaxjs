package com.ajaxjs.workflow;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.util.ioc.Component;
import com.ajaxjs.util.ioc.Resource;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.util.map.JsonHelper;
import com.ajaxjs.workflow.model.NodeModel;
import com.ajaxjs.workflow.model.ProcessModel;
import com.ajaxjs.workflow.model.StartModel;
import com.ajaxjs.workflow.model.TaskModel;
import com.ajaxjs.workflow.model.TransitionModel;
import com.ajaxjs.workflow.process.ProcessActive;
import com.ajaxjs.workflow.process.ProcessDefinition;
import com.ajaxjs.workflow.process.service.ProcessActiveService;
import com.ajaxjs.workflow.process.service.ProcessDefinitionService;
import com.ajaxjs.workflow.task.Task;
import com.ajaxjs.workflow.task.service.TaskService;
import com.ajaxjs.workflow.util.surrogate.SurrogateService;

/**
 * 基本的流程引擎实现类，负责流程启动、执行任务和关联各个服务，通常为单例。
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
@Component
public class WorkflowEngine {
	private static final LogHelper LOGGER = LogHelper.getLog(WorkflowEngine.class);

	@Resource
	private ProcessDefinitionService defService;

	@Resource
	private ProcessActiveService activeService;

	@Resource
	private TaskService taskService;

	@Resource
	private SurrogateService surrogateService;

	// --------------------------------------------------启动流程---------------------------------------------------------------------

	/**
	 * 根据流程定义，操作人，参数集合来启动流程实例
	 * 
	 * @param id       流程定义 id
	 * @param operator 操作人 id
	 * @param args     参数集合
	 * @return 流程实例
	 */
	public ProcessActive startInstanceById(Long id, Long operator, Map<String, Object> args) {
		return createExecute(defService.findById(id), operator, args, null, null);
	}

	/**
	 * 根据流程名称、版本号、操作人、参数集合来启动流程实例
	 * 
	 * @param name     流程定义名称
	 * @param version  流程定义版本号
	 * @param operator 操作人 id
	 * @param args     参数集合
	 * @return 流程实例
	 */
	public ProcessActive startInstanceByName(String name, Integer version, Long operator, Map<String, Object> args) {
		return createExecute(defService.findByVersion(name, version), operator, args, null, null);
	}

	/**
	 * 根据父执行对象启动子流程实例（用于启动子流程）
	 * 
	 * @param exec 父执行对象
	 * @return 流程实例
	 */
	public ProcessActive startInstanceByExecution(Execution exec) {
		return createExecute(exec.getProcess(), exec.getOperator(), exec.getArgs(), exec.getParentActive().getId(), exec.getParentNodeName());
	}

	/**
	 * 创建流程实例，并返回执行对象
	 * 
	 * @param def        流程定义
	 * @param operator   操作人
	 * @param args       参数集合
	 * @param parentId   父流程实例 id，可以为 null
	 * @param parentNode 启动子流程的父流程节点名称 ，可以为 null
	 * @return Execution 执行对象
	 */
	private ProcessActive createExecute(ProcessDefinition def, Long operator, Map<String, Object> args, Long parentId, String parentNode) {
		Objects.requireNonNull(def, "指定的流程定义不存在");

		if (def.getStat() != null && def.getStat() == ProcessModel.STATE_FINISH)
			throw new IllegalArgumentException("指定的流程定义[name=" + def.getName() + "" + ",version=" + def.getVersion() + "]为非活动状态");

		if (args == null)
			args = new HashMap<>();

		// 创建流程实例
		ProcessActive active = activeService.create(def, operator, args, parentId, parentNode);

		// 创建执行对象
		Execution current = new Execution(this, def, active, args);
		current.setOperator(operator);
		LOGGER.info("创建 Execution 对象完毕");

		// 运行 start 节点
		StartModel start = def.getModel().getStart();
		if (start == null)
			LOGGER.info("流程定义[id={0}]没有开始节点", def.getId());
		else {
			LOGGER.info("运行 start 节点");
			start.execute(current);
		}

		return current.getActive();
	}

	// --------------------------------------------------TASK-----------------------------------------------------------------------

	/**
	 * 核心的执行任务方法
	 * 
	 * @param id       任务 id
	 * @param operator 操作人
	 * @param args     参数集合
	 * @return Execution 下一步的执行对象
	 */
	private Execution executeTaskCore(Long id, Long operator, Map<String, Object> args) {
		if (args == null)
			args = new HashMap<String, Object>();

		Task task = taskService.complete(id, operator, args); // 完成任务

		ProcessActive active = activeService.findActive(task.getOrderId()), update;
		update = new ProcessActive();
		update.setId(active.getId());
		update.setUpdator(operator);
		activeService.update(update);
		LOGGER.info("更新流程实例 {0} 完成", update.getId());

		if (!task.isMajor())// 协办任务的话，完成后不会产生执行对象
			return null;

		// 添加来自 active 的新参数
		Map<String, Object> map = JsonHelper.parseMap(active.getVariable());
		if (map == null)
			map = Collections.emptyMap();

		for (String key : map.keySet()) {
			if (args.containsKey(key))
				continue;
			args.put(key, map.get(key));
		}

		// 构造执行对象
		ProcessDefinition def = defService.findById(active.getProcessId());
		Execution exec = new Execution(this, def, active, args);
		exec.setOperator(operator);
		exec.setTask(task);
		LOGGER.info("驱动任务 {0} 继续执行", task.getId());

		return exec;
	}

	/**
	 * 执行任务
	 * 
	 * @param id       任务 id
	 * @param operator 操作人，可以为 null
	 * @param args     参数集合，可以为 null
	 * @return
	 */
	public List<Task> executeTask(Long id, Long operator, Map<String, Object> args) {
		LOGGER.info("开始执行对象，先初始化相关的参数");

		Execution exec = executeTaskCore(id, operator, args); // 完成任务，并且构造执行对象
		if (exec == null)
			return Collections.emptyList();

//		ProcessModel model = execution.getProcess().getModel();
//
//		if (model != null) {
//			NodeModel nodeModel = model.getNode(execution.getTask().getName());
//			nodeModel.execute(execution);// 将执行对象交给该任务对应的节点模型执行
//		}
//		execution.execute();

		// 将执行对象交给该任务对应的节点模型执行
		String taskName = exec.getTask().getName();
		LOGGER.info("正在执行任务 {0}", taskName);
		exec.getProcess().getModel().getNode(taskName).execute(exec);

		return exec.getTasks();
	}

	/**
	 * 按照任务模型创建新的自由任务
	 * 
	 * @param activeId 流程实例 id
	 * @param operator 操作人
	 * @param args     参数集合
	 * @param model    任务模型
	 * @return 自由任务列表
	 */
	public List<Task> createFreeTask(Long activeId, Long operator, Map<String, Object> args, TaskModel model) {
		ProcessActive active = activeService.findActive(activeId);
		active.setUpdator(operator);
//		p.setLastUpdateTime(DateHelper.getTime());

		Execution exec = new Execution(this, defService.findById(active.getProcessId()), active, args);
		exec.setOperator(operator);

		return taskService.createTask(model, exec);
	}

	/**
	 * 
	 * 执行任务，并且根据 nodeName 跳转到任意节点 1、nodeName 为 null 时，则驳回至上一步处理 2、nodeName 不为 null
	 * 时，则任意跳转，即动态创建转移
	 * 
	 * @param id       任务 id
	 * @param operator 操作人
	 * @param args     参数集合
	 * @param nodeName 节点名称
	 * @return
	 */
	public List<Task> executeAndJumpTask(Long id, Long operator, Map<String, Object> args, String nodeName) {
		Execution exec = executeTaskCore(id, operator, args);

		if (exec == null)
			return Collections.emptyList();

		ProcessModel model = exec.getProcess().getModel();
		Objects.requireNonNull(model, "当前任务未找到流程定义模型");

		if (CommonUtil.isEmptyString(nodeName)) {
			Task newTask = taskService.rejectTask(model, exec.getTask());
			exec.addTask(newTask);
		} else {
			NodeModel nodeModel = model.getNode(nodeName);
			Objects.requireNonNull(nodeModel, "根据节点名称[" + nodeName + "]无法找到节点模型");

			TransitionModel tm = new TransitionModel(); // 动态创建转移对象，由转移对象执行 execution 实例
			tm.setTarget(nodeModel);
			tm.setEnabled(true);
			tm.execute(exec);
		}

		return exec.getTasks();
	}

	// --------------------------------------------------------------------------------------------------------------------------------

	/**
	 * 获取 process 服务
	 * 
	 * @return ProcessDefinitionService 流程定义服务
	 */
	public ProcessDefinitionService process() {
		return defService;
	}

	/**
	 * 获取实例服务
	 * 
	 * @return ProcessActiveService 流程实例服务
	 */
	public ProcessActiveService active() {
		return activeService;
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

	/**
	 * 超级管理员 id
	 */
	public static final Long ADMIN = 1000L;
}
