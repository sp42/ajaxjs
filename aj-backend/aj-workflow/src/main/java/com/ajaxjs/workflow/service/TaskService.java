package com.ajaxjs.workflow.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.util.map.JsonHelper;
import com.ajaxjs.workflow.common.WfConstant;
import com.ajaxjs.workflow.common.WfException;
import com.ajaxjs.workflow.common.WfUtils;
import com.ajaxjs.workflow.model.Args;
import com.ajaxjs.workflow.model.Execution;
import com.ajaxjs.workflow.model.ProcessModel;
import com.ajaxjs.workflow.model.TransitionModel;
import com.ajaxjs.workflow.model.node.ForkModel;
import com.ajaxjs.workflow.model.node.JoinModel;
import com.ajaxjs.workflow.model.node.NodeModel;
import com.ajaxjs.workflow.model.node.StartModel;
import com.ajaxjs.workflow.model.node.work.SubProcessModel;
import com.ajaxjs.workflow.model.node.work.TaskModel;
import com.ajaxjs.workflow.model.po.Order;
import com.ajaxjs.workflow.model.po.ProcessPO;
import com.ajaxjs.workflow.model.po.Task;
import com.ajaxjs.workflow.model.po.TaskActor;
import com.ajaxjs.workflow.model.po.TaskHistory;

/**
 * Task 怎么生成？ Task 就是根据 XML 里面的定义转换到 Java 对象，再持久化
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
@Component
public class TaskService extends BaseWfService {
	public static final LogHelper LOGGER = LogHelper.getLog(TaskService.class);

	@Autowired
	private ProcessService processService;

	/**
	 * 根据 id 获取任务
	 * 
	 * @param taskId 任务 id
	 * @return 任务
	 */
	public static Task getTaskById(Long taskId) {
		Task task = TaskDAO.findById(taskId);
		Objects.requireNonNull(task, "指定的任务[id=" + taskId + "]不存在");

		return task;
	}

	/**
	 * 根据流程 id 查找所有的任务
	 * 
	 * @param orderId 流程 id
	 * @return 所有的任务
	 */
	public List<Task> findByOrderId(Long orderId) {
		return TaskDAO.setWhereQuery("order_id", orderId).findList();
	}

	/**
	 * 根据流程 id 查找所有的历史任务
	 * 
	 * @param orderId 流程 id
	 * @return 所有的历史任务
	 */
	public List<TaskHistory> findHistoryTasksByOrderId(Long orderId) {
		return TaskHistoryDAO.setWhereQuery("order_id", orderId).findList();
	}

	/**
	 * 根据流程 id 和任务名称查找所有的历史任务
	 * 
	 * @param orderId  流程 id
	 * @param taskName 任务名称
	 * @return 所有的历史任务
	 */
	public List<TaskHistory> findHistoryTasksByOrderIdAndTaskName(Long orderId, String taskName) {
		return TaskHistoryDAO.setWhereQuery("order_id = " + orderId + " AND name ='" + taskName + "'").findList();
	}

	/**
	 * 获取任务模型。先查找任务对象，然后获取其流程实例 id，根据流程实例 id 获取流程定义 id，然后根据流程定义 id
	 * 获取流量定义模型，从定义模型根据任务名称查找节点模型
	 * 
	 * 不用改成一次性获取
	 * 
	 * @param taskId 任务 id
	 * @return TaskModel
	 */
	public TaskModel getTaskModel(Long taskId) {
		Task task = getTaskById(taskId);
		Order order = OrderDAO.findById(task.getOrderId());
		ProcessPO process = processService.findById(order.getProcessId());

		NodeModel nodeModel = process.getModel().getNode(task.getName());
		Objects.requireNonNull(nodeModel, "任务 id 无法找到节点模型");

		if (nodeModel instanceof TaskModel)
			return (TaskModel) nodeModel;
		else
			throw new IllegalArgumentException("任务 id 找到的节点模型不匹配");
	}

	/**
	 * 完成指定任务。 该方法仅仅结束活动任务，并不能驱动流程继续执行
	 * 
	 * @param taskId   任务 id
	 * @param operator 操作人 id
	 * @param args
	 * @return
	 */
	public Task complete(Long taskId, Long operator, Map<String, Object> args) {
		Task task = getTaskById(taskId);
		task.setVariable(JsonHelper.toJson(args));

//		if (!isAllowed(task, operator))
//			throw new WorkflowException("当前参与者[" + operator + "]不允许执行任务[taskId=" + taskId + "]");

		LOGGER.info("完成任务：创建历史任务，然后删除 Task");
		TaskHistory history = new TaskHistory(task);
		history.setFinishDate(new Date());
		history.setStat(WfConstant.STATE_FINISH);
		history.setOperator(operator);

		if (history.getActorIds() == null) {
			LOGGER.info("查询 任务参与者，保存到 TaskHistory");
			List<TaskActor> actors = TaskDAO.findTaskActorsByTaskId(task.getId());
			Long[] actorIds = new Long[actors.size()];

			for (int i = 0; i < actors.size(); i++)
				actorIds[i] = actors.get(i).getActorId();

			history.setActorIds(actorIds);
		}

		TaskHistoryDAO.create(history);
		TaskDAO.delete(task);

//		orderService.getCompletion().accept(history, null);

		return task;
	}

	/**
	 * 根据任务主键 id 和操作人 id 提取任务。
	 * 
	 * @param taskId   任务 id
	 * @param operator 操作人 id
	 * @return Task 任务对象
	 */
	public Task take(Long taskId, Long operator) {
		LOGGER.info("提取任务 [{0}]", taskId);
		Task task = getTaskById(taskId);

		if (!isAllowed(task, operator))
			throw new WfException("当前参与者[" + operator + "]不允许提取任务[taskId=" + taskId + "]");

		Task u = new Task();
		u.setId(taskId);
		u.setOperator(operator);
		u.setFinishDate(new Date());
		TaskDAO.update(u);

		task.setOperator(operator);
		task.setFinishDate(u.getFinishDate());

		return task;
	}

	/**
	 * 根据任务主键 id、操作人撤回任务
	 * 
	 * @param taskId   任务 id
	 * @param operator 操作人
	 * @return Task 任务对象
	 */
	public Task withdrawTask(Long taskId, Long operator) {
		TaskHistory hist = TaskHistoryDAO.findById(taskId);
		Objects.requireNonNull(hist, "指定的历史任务[id=" + taskId + "]不存在");

		// getNextActiveTasks
		List<Task> tasks = hist.isPerformAny() ? TaskDAO.setWhereQuery("parent_task_id", hist.getId()).findList()
				: TaskDAO.getNextActiveTasks(hist.getOrderId(), hist.getName(), hist.getParentId());

		if (ObjectUtils.isEmpty(tasks))
			throw new WfException("后续活动任务已完成或不存在，无法撤回.");

		for (Task task : tasks)
			TaskDAO.delete(task);

		Task task = hist.undoTask();
		saveTask(task);
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
			throw new WfException("上一步任务ID为空，无法驳回至上一步处理");

		NodeModel current = model.getNode(currentTask.getName());
		TaskHistory history = TaskHistoryDAO.findById(parentTaskId);
		NodeModel parent = model.getNode(history.getName());

		if (!canRejected(current, parent))
			throw new WfException("无法驳回至上一步处理，请确认上一步骤并非fork、join、suprocess以及会签任务");

		Task task = history.undoTask();
		task.setOperator(history.getOperator());
		saveTask(task);
		assignTask(task.getId(), task.getOperator());

		return task;
	}

	/**
	 * 根据父节点模型、当前节点模型判断是否可退回。可退回条件：
	 * 
	 * 1、满足中间无 fork、join、subprocess 模型 2、满足父节点模型如果为任务模型时，参与类型为 any
	 * 
	 * @param current 当前节点模型
	 * @param parent  父节点模型
	 * @return 是否可以退回
	 */
	private static boolean canRejected(NodeModel current, NodeModel parent) {
		if (parent instanceof TaskModel && !((TaskModel) parent).isPerformAny())
			return false;

		boolean result = false;

		for (TransitionModel tm : current.getInputs()) {
			NodeModel source = tm.getSource();

			if (source == parent)
				return true;

			if (source instanceof ForkModel || source instanceof JoinModel || source instanceof SubProcessModel || source instanceof StartModel)
				continue;

			result = result || canRejected(source, parent);
		}

		return result;
	}

	/**
	 * 根据历史任务主键 id，操作人唤醒历史任务 该方法会导致流程状态不可控，请慎用
	 * 
	 * @param taskId   历史任务 id
	 * @param operator 操作人 id
	 * @return Task 唤醒后的任务对象
	 */
	public Task resume(Long taskId, Long operator) {
		TaskHistory histTask = TaskHistoryDAO.findById(taskId);
		Objects.requireNonNull(histTask, "指定的历史任务[id=" + taskId + "]不存在");
		boolean isAllowed = true;

		if (histTask.getOperator() != null && histTask.getOperator() != 0)
			isAllowed = histTask.getOperator() == operator;

		if (isAllowed) {
			Task task = histTask.undoTask();
			saveTask(task);
			assignTask(task.getId(), task.getOperator());

			return task;
		} else
			throw new WfException("当前参与者[" + operator + "]不允许唤醒历史任务[taskId=" + taskId + "]");
	}

	/**
	 * 根据 taskId、operator，判断当前操作人 operator 是否允许执行 taskId 指定的任务
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

		List<TaskActor> actors = TaskDAO.findTaskActorsByTaskId(task.getId());

		if (ObjectUtils.isEmpty(actors))
			return true;

		return operator != null && operator != 0 && getTaskAccessStrategy().apply(operator, actors);
	}

	/**
	 * 
	 * 基于用户或组（角色、部门等）的访问策略类。 该策略类适合组作为参与者的情况。 如果操作人 id 所属的组只要有一项存在于参与者集合中，则表示可访问。
	 * 根据操作人 id、参与者集合判断是否允许访问所属任务。
	 * 
	 * @param operator 操作人 id
	 * @param actors   参与者列表 传递至该接口的实现类中的参与者都是为非空。确定的组集合[如操作人属于多个部门、拥有多个角色]
	 * @return boolean 是否允许访问
	 */
	private BiFunction<Long, List<TaskActor>, Boolean> taskAccessStrategy = (Long operator, List<TaskActor> actors) -> {
//		List<String> assignees = ensureGroup(operator);
		List<Long> assignees = null;
		if (assignees == null)
			assignees = new ArrayList<>();

		assignees.add(operator);
		boolean isAllowed = false;

		for (TaskActor actor : actors) {
			for (Long assignee : assignees) {
				if (actor.getActorId() == assignee) {
					isAllowed = true;
					break;
				}
			}
		}

		return isAllowed;
	};

	/**
	 * 
	 * @param id
	 * @param childOrderId
	 * @param activeNodes
	 * @return
	 */
	public List<Task> findByOrderIdAndExcludedIds(Long id, Long childOrderId, String[] activeNodes) {
//		Function<String, String> fn = by("orderId", id);
//
//		if (childOrderId != null && childOrderId != 0)
//			fn.andThen(setWhere("id NOT IN(" + childOrderId + ")"));
//
//		if (!ObjectUtils.isEmpty(activeNodes)) {
//			int i = 0;
//
//			for (String str : activeNodes)
//				activeNodes[i++] = "'" + str + "'";
//
//			fn.andThen(setWhere("name IN(" + String.join(",", activeNodes) + ")"));
//		}

		return TaskDAO.findList();
	}

	public BiFunction<Long, List<TaskActor>, Boolean> getTaskAccessStrategy() {
		return taskAccessStrategy;
	}

	public void setTaskAccessStrategy(BiFunction<Long, List<TaskActor>, Boolean> strategy) {
		this.taskAccessStrategy = strategy;
	}

	/**
	 * 创建 task 对象
	 * 
	 * @param task
	 * @param actors
	 * @return
	 */
	private static Task saveTask(Task task, Long... actors) {
		task.setPerformType(PerformType.ANY);
		Long newlyId = (Long) TaskDAO.create(task);
		assignTask(newlyId, actors);
		task.setActorIds(actors);

		return task;
	}

	/**
	 * 创建 task，并根据 model 类型决定是否分配参与者
	 * 
	 * @param taskModel 模型
	 * @param exec      执行对象
	 * @return 任务列表
	 */
	public static List<Task> createTaskByModel(TaskModel taskModel, Execution exec) {
		// Date remindDate = DateHelper.processTime(args, taskModel.getReminderTime());
		Long[] actors = getActors(taskModel, exec);

		// 根据模型、执行对象、任务类型构建基本的 task 对象
		Task task = new Task();
		task.setOrderId(exec.getOrder().getId());// 关联流程实例 order
		task.setName(taskModel.getName());
		task.setDisplayName(taskModel.getDisplayName());
		task.setTaskType(taskModel.isMajor() ? TaskType.MAJOR : TaskType.AIDAN);
		task.setModel(taskModel);
		task.setExpireDate(taskModel.getExpireTime());

		if (exec.getTask() != null)
			task.setParentId(exec.getTask().getId());

		Map<String, Object> args = getArgs(exec, actors);
		task.setVariable(JsonHelper.toJson(args));

		// 设置 actionUrl
		String form = taskModel.getForm();
		String formArgs = (String) args.get(form);

		if (StringUtils.hasText(formArgs))
			form = formArgs;

		task.setActionUrl(form);

		Date remindDate = taskModel.getReminderTime();
		List<Task> tasks = new ArrayList<>();

		if (taskModel.isPerformAny()) {
			// 任务执行方式为参与者中任何一个执行即可驱动流程继续流转，该方法只产生一个 task
			task = saveTask(task, actors);
			task.setRemindDate(remindDate);
			tasks.add(task);
		} else if (taskModel.isPerformAll()) {
			// 任务执行方式为参与者中每个都要执行完才可驱动流程继续流转，该方法根据参与者个数产生对应的 task 数量
			for (Long actor : actors) {
				Task singleTask;

				try {
					singleTask = (Task) task.clone();
				} catch (CloneNotSupportedException e) {
					singleTask = task;
				}

				singleTask = saveTask(singleTask, actor);
				singleTask.setRemindDate(remindDate);
				tasks.add(singleTask);
			}
		}

		return tasks;
	}

	private static Map<String, Object> getArgs(Execution exec, Long[] actors) {
		Args args = exec.getArgs();
		args = Args.getEmpty(args);
		args.put(Task.KEY_ACTOR, WfUtils.join(actors));

		return args;
	}

	/**
	 * 对指定的任务分配参与者。参与者可以为用户、部门、角色
	 * 
	 * @param taskId   任务 id
	 * @param actorIds 参与者 id 集合
	 */
	public static void assignTask(Long taskId, Long... actorIds) {
		if (ObjectUtils.isEmpty(actorIds))
			return;

		for (Long actorId : actorIds) {
			if (actorId == null)
				continue;
			// TODO:needs?
//			TaskActor taskActor = new TaskActor();
//			taskActor.setTaskId(taskId);
//			taskActor.setActorId(actorId);

			TaskDAO.createTaskActor(taskId, actorId);
		}
	}

	/**
	 * 根据 Task 模型的 assignee、assignmentHandler 属性以及运行时数据， 确定参与者 actor = assignee
	 * 
	 * @param model 模型
	 * @param exec  执行对象
	 * @return 参与者数组
	 */
	public static Long[] getActors(TaskModel model, Execution exec) {
		String assignee = model.getAssignee();
		Map<String, Object> args = exec.getArgs();
		Object actors = assignee;

		if (StringUtils.hasText(assignee) && args != null && args.containsKey(assignee)) {
			actors = args.get(assignee);
		} else if (model.getAssignment() != null) {
			BiFunction<TaskModel, Execution, Object> handler = model.getAssignment();
			actors = handler.apply(model, exec);
		}

		return getActors(actors);
	}

	/**
	 * 根据 taskmodel 指定的 assignee 属性，从 args 中取值将取到的值处理为 String[] 类型。
	 * 
	 * @param actors 参与者对象
	 * @return 参与者数组
	 */
	private static Long[] getActors(Object actors) {
		if (actors == null)
			return null;

		Long[] results;

		if (actors instanceof String[]) {
			String[] arr = (String[]) actors;
			results = new Long[arr.length];

			for (int i = 0; i < arr.length; i++)
				results[i] = Long.parseLong(arr[i]);

		} else if (actors instanceof String) {// 如果值为字符串类型，则使用逗号,分隔
			if (actors.toString().indexOf(",") != -1) {
				String[] arr = actors.toString().split(",");
				results = new Long[arr.length];

				for (int i = 0; i < arr.length; i++)
					results[i] = Long.parseLong(arr[i]);
			} else {
				results = new Long[1];
				results[0] = Long.parseLong(actors.toString());
			}
		} else if (actors instanceof List) {
			// jackson会把stirng[]转成arraylist，此处增加arraylist的逻辑判断,by 红豆冰沙2014.11.21
			List<?> list = (List<?>) actors;
			results = new Long[list.size()];

			for (int i = 0; i < list.size(); i++)
				results[i] = Long.parseLong(list.get(i).toString());
		} else if (actors instanceof Long) {// 如果为Long类型，则返回1个元素的String[]
			results = new Long[1];
			results[0] = (Long) actors;
		} else if (actors instanceof Integer) {// 如果为Integer类型，则返回1个元素的String[]
			results = new Long[1];
			results[0] = Long.parseLong(actors + "");
		} else if (actors instanceof Long[])
			results = (Long[]) actors;// 如果为String[]类型，则直接返回
		else
			// 其它类型，抛出不支持的类型异常
			throw new WfException("任务参与者对象[" + actors + "]类型不支持." + "合法参数示例:Long,Integer,new String[]{},'10000,20000',List<String>");

		return results;
	}

	/**
	 * 对指定的任务 id 删除参与者
	 * 
	 * @param taskId 任务 id
	 * @param actors 参与者
	 */
	public static void removeTaskActor(Long taskId, Long... actors) {
		Task task = getTaskById(taskId);

		if (actors == null || actors.length == 0)
			return;

		if (task.getTaskType() == TaskType.MAJOR) {
			// removeTaskActor(task.getId(), actors);
			Map<String, Object> taskData = JsonHelper.parseMap(task.getVariable());
			String actorStr = (String) taskData.get(Task.KEY_ACTOR);

			if (StringUtils.hasText(actorStr)) {
				String[] actorArray = actorStr.split(",");
				StringBuilder newActor = new StringBuilder(actorStr.length());
				boolean isMatch;

				for (String actor : actorArray) {
					isMatch = false;
					if (!StringUtils.hasText(actor))
						continue;

					for (Long removeActor : actors) {
						if (actor.equals(removeActor)) {
							isMatch = true;
							break;
						}
					}

					if (isMatch)
						continue;

					newActor.append(actor).append(",");
				}

				newActor.deleteCharAt(newActor.length() - 1);
				taskData.put(Task.KEY_ACTOR, newActor.toString());
				task.setVariable(JsonHelper.toJson(taskData));
				TaskDAO.update(task);
			}
		}
	}

	/**
	 * 向指定的任务 id 添加参与者
	 * 
	 * @param taskId 任务 id
	 * @param actors 参与者
	 */
	public static void addTaskActor(Long taskId, Long... actors) {
		addTaskActor(taskId, null, actors);
	}

	/**
	 * 向指定任务添加参与者 该方法根据 performType 类型判断是否需要创建新的活动任务
	 * 
	 * @param taskId      任务 id
	 * @param performType 参与类型
	 * @param actors      参与者
	 */
	public static void addTaskActor(Long taskId, PerformType performType, Long... actors) {
		Task task = getTaskById(taskId);

		if (task.getTaskType() != TaskType.MAJOR)
			return;

		if (performType == null)
			performType = task.getPerformType();

		if (performType == null || performType == PerformType.ANY) {
			assignTask(task.getId(), actors);
			Map<String, Object> data = JsonHelper.parseMap(task.getVariable());
			if (data == null)
				data = Collections.emptyMap();

			String oldActor = (String) data.get(Task.KEY_ACTOR);
			data.put(Task.KEY_ACTOR, oldActor + "," + WfUtils.join(actors));
			task.setVariable(JsonHelper.toJson(data));

			TaskDAO.update(task);
		} else if (performType == PerformType.ALL) {
			try {
				for (Long actor : actors) {
					Task newTask = (Task) task.clone();
					newTask.setOperator(actor);

					Map<String, Object> taskData = JsonHelper.parseMap(task.getVariable());
					if (taskData == null)
						taskData = Collections.emptyMap();

					taskData.put(Task.KEY_ACTOR, actor);
					task.setVariable(JsonHelper.toJson(taskData));

					TaskDAO.create(newTask);
					assignTask(newTask.getId(), actor);
				}
			} catch (CloneNotSupportedException ex) {
				throw new WfException("任务对象不支持复制", ex.getCause());
			}
		}
	}

	/**
	 * 转派
	 * 
	 * @param taskId   任务 id
	 * @param operator
	 * @param actors   参与者
	 * @return
	 */
	public List<Task> transferMajor(Long taskId, Long operator, Long... actors) {
		List<Task> tasks = copyTask(taskId, TaskType.MAJOR, actors);
		complete(taskId, operator, null);

		return tasks;
	}

	/**
	 * 转派
	 * 
	 * @param taskId   任务 id
	 * @param operator
	 * @param actors   参与者
	 * @return
	 */
	public List<Task> transferAidant(Long taskId, Long operator, Long... actors) {
		List<Task> tasks = copyTask(taskId, TaskType.AIDAN, actors);
		complete(taskId, operator, null);

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
	private static List<Task> copyTask(Long taskId, TaskType taskType, Long... actors) {
		Task task = getTaskById(taskId);
		List<Task> tasks = new ArrayList<>();

		try {
			Task newTask = (Task) task.clone();
			newTask.setParentId(taskId);
			newTask.setTaskType(taskType);

			tasks.add(saveTask(newTask, actors));
		} catch (CloneNotSupportedException e) {
			throw new WfException("任务对象不支持复制", e.getCause());
		}

		return tasks;
	}

	/**
	 * engine.process().deploy(WorkflowUtils.getStreamFromClasspath("flows/leave.snaker"));
	 * engine.process().deploy(WorkflowUtils.getStreamFromClasspath("flows/borrow.snaker"));
	 * 
	 * @param orderId  流程 id
	 * @param taskName 任务名称
	 * @return
	 */
	public Args flowData(Long orderId, String taskName) {
		Args args = new Args();

		if (orderId != null && orderId != 0 && StringUtils.hasText(taskName)) {
			List<TaskHistory> histTasks = findHistoryTasksByOrderIdAndTaskName(orderId, taskName);
			List<Args> vars = new ArrayList<>();

			for (TaskHistory hist : histTasks) {
				Map<String, Object> map = JsonHelper.parseMap(hist.getVariable());
				if (map == null)
					map = Collections.emptyMap();

				Args _args = new Args();
				_args.putAll(map);
				vars.add(_args);
			}

			args.put("vars", vars);
			args.put("histTasks", histTasks);
		}

		return args;
	}
}
