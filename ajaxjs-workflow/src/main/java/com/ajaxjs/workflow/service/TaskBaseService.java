package com.ajaxjs.workflow.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;

import org.snaker.engine.SnakerException;
import org.snaker.engine.core.SnakerEngineImpl;
import org.snaker.engine.helper.DateHelper;

import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.Repository;
import com.ajaxjs.ioc.Resource;
import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.util.map.JsonHelper;
import com.ajaxjs.workflow.WorkflowConstant;
import com.ajaxjs.workflow.WorkflowException;
import com.ajaxjs.workflow.dao.TaskDao;
import com.ajaxjs.workflow.dao.TaskHistoryDao;
import com.ajaxjs.workflow.model.CustomModel;
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
	{
		setUiName("任务");
		setShortName("task");
		setDao(dao);
	}

	public static TaskDao dao = new Repository().bind(TaskDao.class);

	public static TaskHistoryDao historyDao = new Repository().bind(TaskHistoryDao.class);

	public List<Task> findByOrderId(Long orderId) {
		return findList(by("orderId", orderId));
	}

	@Resource("OrderService")
	private OrderService orderService;

	@Resource("ProcessService")
	private ProcessService processService;

	/**
	 * 获取任务模型
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
		ProcessModel model = process.getModel();
		NodeModel nodeModel = model.getNode(task.getName());
		Objects.requireNonNull(nodeModel, "任务id无法找到节点模型.");

		if (nodeModel instanceof TaskModel)
			return (TaskModel) nodeModel;
		else
			throw new IllegalArgumentException("任务id找到的节点模型不匹配");
	}

	/**
	 * 根据任务主键ID，操作人ID提取任务 提取任务相当于预受理操作，仅仅标识此任务只能由此操作人处理。 提取指定任务，设置完成时间及操作人，状态不改变
	 * 
	 * @param taskId   任务id
	 * @param operator 操作人id
	 * @return Task 任务对象
	 */
	public Task take(Long taskId, Long operator) {
		Task task = findById(taskId);
		Objects.requireNonNull(task, "指定的任务[id=" + taskId + "]不存在");

		if (!isAllowed(task, operator))
			throw new WorkflowException("当前参与者[" + operator + "]不允许提取任务[taskId=" + taskId + "]");

		task.setOperator(operator);
		task.setFinishDate(new Date());
		update(task);

		return task;
	}

	/**
	 * 任务历史记录方法
	 * 
	 * @param execution 执行对象
	 * @param model     自定义节点模型
	 * @return 历史任务对象
	 */
	public TaskHistory history(Execution execution, CustomModel model) {
		TaskHistory historyTask = new TaskHistory();
		historyTask.setOrderId(execution.getOrder().getId());
		historyTask.setName(model.getName());
		historyTask.setFinishDate(new Date());
		historyTask.setDisplayName(model.getDisplayName());
		historyTask.setStat(WorkflowConstant.STATE_FINISH);
		historyTask.setTaskType(TaskType.Record.ordinal());
		historyTask.setParentTaskId(execution.getTask() == null ? 0 : execution.getTask().getId());
		historyTask.setVariable(JsonHelper.toJson(execution.getArgs()));
		historyDao.create(historyTask);

		return historyTask;
	}

	/**
	 * 根据模型、执行对象、任务类型构建基本的task对象
	 * 
	 * @param model     模型
	 * @param execution 执行对象
	 * @return Task任务对象
	 */
	private Task createTaskBase(TaskModel model, Execution execution) {
		Task task = new Task();
		task.setOrderId(execution.getOrder().getId());
		task.setName(model.getName());
		task.setDisplayName(model.getDisplayName());
		task.setCreateDate(new Date());
		task.setTaskType(model.isMajor() ? TaskType.Major.ordinal() : TaskType.Aidant.ordinal());
//		task.setParentTaskId(execution.getTask() == null ? "start" : execution.getTask().getId());
		task.setParentTaskId(execution.getTask() == null ? 0 : execution.getTask().getId());
		task.setModel(model);

		return task;
	}

	/**
	 * 根据Task模型的assignee、assignmentHandler属性以及运行时数据，确定参与者
	 * 
	 * @param model     模型
	 * @param execution 执行对象
	 * @return 参与者数组
	 */
	abstract Long[] getTaskActors(TaskModel model, Execution execution);

	/**
	 * 由DBAccess实现类创建task，并根据model类型决定是否分配参与者
	 * 
	 * @param taskModel 模型
	 * @param execution 执行对象
	 * @return List<Task> 任务列表
	 */
	public List<Task> createTask(TaskModel taskModel, Execution execution) {
		Map<String, Object> args = execution.getArgs();
		if (args == null)
			args = new HashMap<String, Object>();

		Date expireDate = DateHelper.processTime(args, taskModel.getExpireTime());
		Date remindDate = DateHelper.processTime(args, taskModel.getReminderTime());
		String form = (String) args.get(taskModel.getForm());
		String actionUrl = CommonUtil.isEmptyString(form) ? taskModel.getForm() : form;

		Long[] actors = getTaskActors(taskModel, execution);
		args.put(Task.KEY_ACTOR, String.join(",", actors));
		Task task = createTaskBase(taskModel, execution);
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
	 * 由DBAccess实现类持久化task对象
	 */
	private Task create(Task task, Long... actors) {
		task.setPerformType(PerformType.ANY.ordinal());
		create(task);
		assignTask(task.getId(), actors);
		task.setActorIds(actors);

		return task;
	}

	/**
	 * 根据已有任务、任务类型、参与者创建新的任务 适用于转派，动态协办处理
	 */
	public List<Task> createNewTask(Long taskId, int taskType, Long... actors) {
		Task task = findById(taskId);
		Objects.requireNonNull(task, "指定的任务[id=" + taskId + "]不存在");
		List<Task> tasks = new ArrayList<>();

		try {
			Task newTask = (Task) task.clone();
			newTask.setTaskType(taskType);
			newTask.setParentTaskId(taskId);
			tasks.add(create(newTask, actors));
		} catch (CloneNotSupportedException e) {
			throw new WorkflowException("任务对象不支持复制", e.getCause());
		}

		return tasks;
	}

	/**
	 * 对指定的任务分配参与者。参与者可以为用户、部门、角色
	 * 
	 * @param taskId   任务id
	 * @param actorIds 参与者id集合
	 */
	public void assignTask(Long taskId, Long... actorIds) {
		if (CommonUtil.isNull(actorIds))
			return;

		for (Long actorId : actorIds) {
			// 修复当actorId为null的bug
			if (actorId == null)
				continue;

			TaskActor taskActor = new TaskActor();
			taskActor.setTaskId(taskId);
			taskActor.setActorId(actorId);
			dao.createTaskActor(taskActor);
		}
	}

	public List<TaskHistory> findHistoryTasksByOrderId(Long orderId) {
		return historyDao.findList(by("orderId", orderId));
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
		} else {
			throw new WorkflowException("当前参与者[" + operator + "]不允许唤醒历史任务[taskId=" + taskId + "]");
		}
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

		List<Task> tasks;
		if (hist.isPerformAny())
			tasks = getNextActiveTasks(hist.getId());
		else
			tasks = getNextActiveTasks(hist.getOrderId(), hist.getName(), hist.getParentTaskId());

		if (CommonUtil.isNull(tasks))
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
		Long parentTaskId = currentTask.getParentTaskId();

		if (parentTaskId == null || parentTaskId == 0)
			throw new SnakerException("上一步任务ID为空，无法驳回至上一步处理");

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

	private List<Task> getNextActiveTasks(Long id) {
		return findList(by("parentTaskId", id));
	}

	private List<Task> getNextActiveTasks(Long id, String taskName, Long parentTaskId) {
		return dao.getNextActiveTasks(id, taskName, parentTaskId);
	}

	/**
	 * 根据taskId、operator，判断当前操作人operator是否允许执行taskId指定的任务
	 * 
	 * @param task     任务对象
	 * @param operator 操作人
	 * @return boolean 是否允许操作
	 */
	public boolean isAllowed(Task task, Long operator) {
		if (operator != null && operator != 0) {
//			if (SnakerEngine.ADMIN.equalsIgnoreCase(operator) || SnakerEngine.AUTO.equalsIgnoreCase(operator))
//				return true;

			if (task.getOperator() != null && task.getOperator() != 0)
				return operator == task.getOperator();
		}

		List<TaskActor> actors = findTaskActorsByTaskId(task.getId());

		if (CommonUtil.isNull(actors))
			return true;

		return operator != null && operator != 0 && getTaskAccessStrategy().apply(operator, actors);
	}
	
	public abstract BiFunction<Long, List<TaskActor>, Boolean> getTaskAccessStrategy();

	private List<TaskActor> findTaskActorsByTaskId(Long id) {
		return null;
	}

	/**
	 * 完成指定任务 该方法仅仅结束活动任务，并不能驱动流程继续执行
	 * 
	 * @see SnakerEngineImpl#executeTask(String, String, java.util.Map)
	 */
	public Task complete(Long taskId, Long operator, Map<String, Object> args) {
		Task task = findById(taskId);
		Objects.requireNonNull(task, "指定的任务[id=" + taskId + "]不存在");
		task.setVariable(JsonHelper.toJson(args));

		if (!isAllowed(task, operator))
			throw new WorkflowException("当前参与者[" + operator + "]不允许执行任务[taskId=" + taskId + "]");

		TaskHistory history = new TaskHistory(task);
		history.setFinishDate(new Date());
		history.setStat(WorkflowConstant.STATE_FINISH);
		history.setOperator(operator);

		if (history.getActorIds() == null) {
			List<TaskActor> actors = access().getTaskActorsByTaskId(task.getId());
			String[] actorIds = new String[actors.size()];

			for (int i = 0; i < actors.size(); i++)
				actorIds[i] = actors.get(i).getActorId();

			history.setActorIds(actorIds);
		}

		access().saveHistory(history);
		delete(task);
		
		orderService.getCompletion().accept(history, null);

		return task;
	}

	/**
	 * 完成指定任务
	 */
	public Task complete(Long taskId) {
		return complete(taskId, null, null);
	}

	/**
	 * 完成指定任务
	 */
	public Task complete(Long taskId, Long operator) {
		return complete(taskId, operator, null);
	}
}
