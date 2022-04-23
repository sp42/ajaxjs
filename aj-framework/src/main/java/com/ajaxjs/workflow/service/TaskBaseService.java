package com.ajaxjs.workflow.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.ajaxjs.framework.BaseService;
import com.ajaxjs.sql.orm.Repository;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.util.map.JsonHelper;
import com.ajaxjs.workflow.WorkflowConstant;
import com.ajaxjs.workflow.WorkflowEngine;
import com.ajaxjs.workflow.WorkflowException;
import com.ajaxjs.workflow.WorkflowUtils;
import com.ajaxjs.workflow.dao.TaskDao;
import com.ajaxjs.workflow.dao.TaskHistoryDao;
import com.ajaxjs.workflow.model.Execution;
import com.ajaxjs.workflow.model.NodeModel;
import com.ajaxjs.workflow.model.ProcessModel;
import com.ajaxjs.workflow.model.TaskModel;
import com.ajaxjs.workflow.model.TaskModel.PerformType;
import com.ajaxjs.workflow.model.TaskModel.TaskType;
import com.ajaxjs.workflow.model.entity.Order;
import com.ajaxjs.workflow.model.entity.Process;
import com.ajaxjs.workflow.model.entity.Task;
import com.ajaxjs.workflow.model.entity.TaskActor;
import com.ajaxjs.workflow.model.entity.TaskHistory;

public abstract class TaskBaseService extends BaseService<Task> {
	public static final LogHelper LOGGER = LogHelper.getLog(TaskBaseService.class);

	{
		setDao(dao);
	}

	public static TaskDao dao = new Repository().bind(TaskDao.class);

	public static TaskHistoryDao historyDao = new Repository().bind(TaskHistoryDao.class);

	@Autowired
	private OrderService orderService;

	@Autowired
	private ProcessService processService;

	/**
	 * 根据流程 id 查找所有的任务
	 * 
	 * @param orderId 流程 id
	 * @return 所有的任务
	 */
	public List<Task> findByOrderId(Long orderId) {
		return findList(by("orderId", orderId));
	}

	/**
	 * 根据流程 id 查找所有的历史任务
	 * 
	 * @param orderId 流程 id
	 * @return 所有的历史任务
	 */
	public List<TaskHistory> findHistoryTasksByOrderId(Long orderId) {
		return historyDao.findList(by("orderId", orderId));
	}

	/**
	 * 根据流程 id 和任务名称查找所有的历史任务
	 * 
	 * @param orderId
	 * @param taskName
	 * @return 所有的历史任务
	 */
	public List<TaskHistory> findHistoryTasksByOrderIdAndTaskName(Long orderId, String taskName) {
		return historyDao.findList(by("orderId", orderId).andThen(by("name", taskName)));
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	private List<TaskActor> findTaskActorsByTaskId(Long id) {
		return dao.findTaskActorsByTaskId(id);
	}

	/**
	 * 创建 task 对象
	 * 
	 * @param task
	 * @param actors
	 * @return
	 */
	private Task create(Task task, Long... actors) {
		task.setPerformType(PerformType.ANY.ordinal());
		create(task);
		assignTask(task.getId(), actors);
		task.setActorIds(actors);

		return task;
	}

	/**
	 * 创建task，并根据model类型决定是否分配参与者
	 * 
	 * @param taskModel 模型
	 * @param execution 执行对象
	 * @return List<Task> 任务列表
	 */
	public List<Task> createTask(TaskModel taskModel, Execution execution) {
		LOGGER.info("创建新任务 " + taskModel.getName());

		Map<String, Object> args = execution.getArgs();
		if (args == null)
			args = new HashMap<String, Object>();

//		Date expireDate = DateHelper.processTime(args, taskModel.getExpireTime());
//		Date remindDate = DateHelper.processTime(args, taskModel.getReminderTime());
		Date expireDate = taskModel.getExpireTime();
		Date remindDate = taskModel.getReminderTime();
		String form = (String) args.get(taskModel.getForm());
		String actionUrl = StringUtils.hasText(form) ? form : taskModel.getForm();

		Long[] actors = getTaskActors(taskModel, execution);
		args.put(Task.KEY_ACTOR, WorkflowUtils.join(actors));

		Task task = new Task();// 根据模型、执行对象、任务类型构建基本的 task 对象
		task.setOrderId(execution.getOrder().getId());
		task.setName(taskModel.getName());
		task.setDisplayName(taskModel.getDisplayName());
		task.setCreateDate(new Date());
		task.setTaskType(taskModel.isMajor() ? TaskType.Major.ordinal() : TaskType.Aidant.ordinal());
//		task.setParentTaskId(execution.getTask() == null ? "start" : execution.getTask().getId());
		task.setParentId(execution.getTask() == null ? 0 : execution.getTask().getId());
		task.setModel(taskModel);
		task.setActionUrl(actionUrl);
		task.setExpireDate(expireDate);
		task.setExpireDate(expireDate);
		task.setVariable(JsonHelper.toJson(args));

		List<Task> tasks = new ArrayList<>();

		if (taskModel.isPerformAny()) {
			// 任务执行方式为参与者中任何一个执行即可驱动流程继续流转，该方法只产生一个task
			task = create(task, actors);
			task.setRemindDate(remindDate);
			tasks.add(task);
		} else if (taskModel.isPerformAll()) {
			// 任务执行方式为参与者中每个都要执行完才可驱动流程继续流转，该方法根据参与者个数产生对应的task数量
			for (Long actor : actors) {
				Task singleTask;

				try {
					singleTask = (Task) task.clone();
				} catch (CloneNotSupportedException e) {
					singleTask = task;
				}

				singleTask = create(singleTask, actor);
				singleTask.setRemindDate(remindDate);
				tasks.add(singleTask);
			}
		}

		return tasks;
	}

	/**
	 * 根据已有任务、任务类型、参与者创建新的任务 适用于转派，动态协办处理
	 * 
	 * @param taskId   任务 id
	 * @param taskType 任务类型
	 * @param actors   参与者列表
	 * @return 任务列表
	 */
	public List<Task> createNewTask(Long taskId, int taskType, Long... actors) {
		Task task = findById(taskId);
		Objects.requireNonNull(task, "指定的任务[id=" + taskId + "]不存在");
		List<Task> tasks = new ArrayList<>();

		try {
			Task newTask = (Task) task.clone();
			newTask.setParentId(taskId);
			newTask.setTaskType(taskType);
			tasks.add(create(newTask, actors));
		} catch (CloneNotSupportedException e) {
			throw new WorkflowException("任务对象不支持复制", e.getCause());
		}

		return tasks;
	}

	/**
	 * 获取任务模型。先查找任务对象，然后获取其流程实例 id，根据流程实例 id 获取流程定义 id，然后根据流程定义 id
	 * 获取流量定义模型，从定义模型根据任务名称查找节点模型 TODO 改为 SQL 一次性获取
	 * 
	 * @param taskId 任务id
	 * @return TaskModel
	 */
	public TaskModel getTaskModel(Long taskId) {
		Task task = findById(taskId);
		Order order = orderService.findById(task.getOrderId());
		Objects.requireNonNull(task, "指定的任务[id=" + taskId + "]不存在");
		Objects.requireNonNull(order);

		Process process = processService.findById(order.getProcessId());
		NodeModel nodeModel = process.getModel().getNode(task.getName());
		Objects.requireNonNull(nodeModel, "任务id无法找到节点模型.");

		if (nodeModel instanceof TaskModel)
			return (TaskModel) nodeModel;
		else
			throw new IllegalArgumentException("任务 id 找到的节点模型不匹配");
	}

	/**
	 * 完成指定任务。 该方法仅仅结束活动任务，并不能驱动流程继续执行
	 * 
	 * @see WorkflowEngine#executeTask(String, String, java.util.Map)
	 * @param taskId
	 * @param operator
	 * @param args
	 * @return
	 */
	public Task complete(Long taskId, Long operator, Map<String, Object> args) {
		Task task = findById(taskId);
		Objects.requireNonNull(task, "指定的任务[id=" + taskId + "]不存在");
		task.setVariable(JsonHelper.toJson(args));

//		if (!isAllowed(task, operator))
//			throw new WorkflowException("当前参与者[" + operator + "]不允许执行任务[taskId=" + taskId + "]");

		LOGGER.info("完成任务：创建历史任务，然后删除 Task");
		TaskHistory history = new TaskHistory(task);
		history.setFinishDate(new Date());
		history.setStat(WorkflowConstant.STATE_FINISH);
		history.setOperator(operator);

		if (history.getActorIds() == null) {
			LOGGER.info("查询 任务参与者，保存到 TaskHistory");
			List<TaskActor> actors = findTaskActorsByTaskId(task.getId());
			Long[] actorIds = new Long[actors.size()];

			for (int i = 0; i < actors.size(); i++)
				actorIds[i] = actors.get(i).getActorId();

			history.setActorIds(actorIds);
		}

		initCreate(history);
		historyDao.create(history);
		delete(task);

//		orderService.getCompletion().accept(history, null);

		return task;
	}

	/**
	 * 对指定的任务分配参与者。参与者可以为用户、部门、角色
	 * 
	 * @param taskId   任务id
	 * @param actorIds 参与者id集合
	 */
	static void assignTask(Long taskId, Long... actorIds) {
		if (ObjectUtils.isEmpty(actorIds))
			return;

		for (Long actorId : actorIds) {
			if (actorId == null)
				continue;

			TaskActor taskActor = new TaskActor();
			taskActor.setTaskId(taskId);
			taskActor.setActorId(actorId);

			dao.createTaskActor(taskId, actorId);
		}
	}

	/**
	 * 根据任务主键 ID，操作人 ID 提取任务。
	 * 
	 * @param taskId   任务id
	 * @param operator 操作人id
	 * @return Task 任务对象
	 */
	public Task take(Long taskId, Long operator) {
		LOGGER.info("提取任务 [{0}]", taskId);

		Task task = findById(taskId);
		Objects.requireNonNull(task, "指定的任务[id=" + taskId + "]不存在");

		if (!isAllowed(task, operator))
			throw new WorkflowException("当前参与者[" + operator + "]不允许提取任务[taskId=" + taskId + "]");

		Task u = new Task();
		u.setId(taskId);
		u.setOperator(operator);
		u.setFinishDate(new Date());
		update(u);

		task.setOperator(operator);
		task.setFinishDate(u.getFinishDate());

		return task;
	}

	/**
	 * 根据任务主键id、操作人撤回任务
	 * 
	 * @param taskId   任务id
	 * @param operator 操作人
	 * @return Task 任务对象
	 */
	public Task withdrawTask(Long taskId, Long operator) {
		TaskHistory hist = historyDao.findById(taskId);
		Objects.requireNonNull(hist, "指定的历史任务[id=" + taskId + "]不存在");

		// getNextActiveTasks
		List<Task> tasks = hist.isPerformAny() ? findList(by("parentTaskId", hist.getId())) : dao.getNextActiveTasks(hist.getOrderId(), hist.getName(), hist.getParentId());

		if (ObjectUtils.isEmpty(tasks))
			throw new WorkflowException("后续活动任务已完成或不存在，无法撤回.");

		for (Task task : tasks)
			delete(task);

		Task task = hist.undoTask();
		create(task);
		assignTask(task.getId(), task.getOperator());

		return task;
	}

	/**
	 * 根据当前任务对象驳回至上一步处理
	 * 
	 * @param model       流程定义模型，用以获取上一步模型对象
	 * @param currentTask 当前任务对象
	 * @return Task 任务对象
	 */
	public Task rejectTask(ProcessModel model, Task currentTask) {
		Long parentTaskId = currentTask.getParentId();

		if (parentTaskId == null || parentTaskId == 0)
			throw new WorkflowException("上一步任务ID为空，无法驳回至上一步处理");

		NodeModel current = model.getNode(currentTask.getName());
		TaskHistory history = historyDao.findById(parentTaskId);
		NodeModel parent = model.getNode(history.getName());

		if (!NodeModel.canRejected(current, parent))
			throw new WorkflowException("无法驳回至上一步处理，请确认上一步骤并非fork、join、suprocess以及会签任务");

		Task task = history.undoTask();
		task.setOperator(history.getOperator());
		create(task);
		assignTask(task.getId(), task.getOperator());

		return task;
	}

	/**
	 * 根据历史任务主键id，操作人唤醒历史任务 该方法会导致流程状态不可控，请慎用
	 * 
	 * @param taskId   历史任务id
	 * @param operator 操作人id
	 * @return Task 唤醒后的任务对象
	 */
	public Task resume(Long taskId, Long operator) {
		TaskHistory histTask = historyDao.findById(taskId);
		Objects.requireNonNull(histTask, "指定的历史任务[id=" + taskId + "]不存在");
		boolean isAllowed = true;

		if (histTask.getOperator() != null && histTask.getOperator() != 0)
			isAllowed = histTask.getOperator() == operator;

		if (isAllowed) {
			Task task = histTask.undoTask();
			create(task);
			assignTask(task.getId(), task.getOperator());

			return task;
		} else
			throw new WorkflowException("当前参与者[" + operator + "]不允许唤醒历史任务[taskId=" + taskId + "]");
	}

	/**
	 * 根据taskId、operator，判断当前操作人operator是否允许执行taskId指定的任务
	 * 
	 * @param task     任务对象
	 * @param operator 操作人
	 * @return boolean 是否允许操作
	 */
	private boolean isAllowed(Task task, Long operator) {
		if (operator != null && operator != 0) {
//			if (SnakerEngine.ADMIN.equalsIgnoreCase(operator) || SnakerEngine.AUTO.equalsIgnoreCase(operator))
//				return true;

			if (task.getOperator() != null && task.getOperator() != 0)
				return operator == task.getOperator();
		}

		List<TaskActor> actors = findTaskActorsByTaskId(task.getId());

		if (ObjectUtils.isEmpty(actors))
			return true;

		return operator != null && operator != 0 && getTaskAccessStrategy().apply(operator, actors);
	}

	/**
	 * 根据Task模型的assignee、assignmentHandler属性以及运行时数据，确定参与者
	 * 
	 * @param model     模型
	 * @param execution 执行对象
	 * @return 参与者数组
	 */
	public abstract Long[] getTaskActors(TaskModel model, Execution execution);

	/**
	 * 
	 * @return
	 */
	public abstract BiFunction<Long, List<TaskActor>, Boolean> getTaskAccessStrategy();
}
