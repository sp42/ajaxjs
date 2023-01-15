package com.ajaxjs.workflow.service.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;

import org.springframework.util.StringUtils;

import com.ajaxjs.util.map.JsonHelper;
import com.ajaxjs.workflow.WorkflowEngine;
import com.ajaxjs.workflow.common.WfDao;
import com.ajaxjs.workflow.common.WfException;
import com.ajaxjs.workflow.common.WfUtils;
import com.ajaxjs.workflow.model.Args;
import com.ajaxjs.workflow.model.Execution;
import com.ajaxjs.workflow.model.node.work.TaskModel;
import com.ajaxjs.workflow.model.po.Order;
import com.ajaxjs.workflow.model.po.ProcessPO;
import com.ajaxjs.workflow.model.po.Task;
import com.ajaxjs.workflow.service.BaseWfService;
import com.ajaxjs.workflow.service.TaskService;

/**
 * Task 怎么生成？ Task 就是根据 XML 里面的定义转换到 Java 对象，再持久化
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public class TaskFactory extends BaseWfService {
	/**
	 * 根据流程实例 id，操作人 id，参数列表按照节点模型 model 创建新的自由任务
	 * 
	 * @param engine
	 * @param orderId
	 * @param operator
	 * @param args
	 * @param model
	 * @return
	 */
	public static List<Task> createFreeTask(WorkflowEngine engine, Long orderId, Long operator, Args args, TaskModel model) {
		Order order = WfDao.OrderDAO.findById(orderId);
		Objects.requireNonNull(order, "指定的流程实例[id=" + orderId + "]已完成或不存在");
		order.setUpdater(operator);
//		order.setLastUpdateTime(DateHelper.getTime());

		ProcessPO process = engine.processService.findById(order.getProcessId());
		Execution execution = new Execution(engine, process, order, args);
		execution.setOperator(operator);

		return createTaskByModel(model, execution);
	}

	/**
	 * 根据已有任务、任务类型、参与者创建新的任务 适用于转派，动态协办处理
	 * 
	 * @param taskId   任务 id
	 * @param taskType 任务类型
	 * @param actors   参与者列表
	 * @return 任务列表
	 */
	public static List<Task> createNewTask(Long taskId, TaskType taskType, Long... actors) {
		Task task = TaskService.getTaskById(taskId);
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
	 * 创建 task 对象
	 * 
	 * @param task
	 * @param actors
	 * @return
	 */
	public static Task saveTask(Task task, Long... actors) {
		task.setPerformType(PerformType.ANY);
		Long newlyId = (Long) TaskDAO.create(task);
		TaskService.assignTask(newlyId, actors);
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
//		Date remindDate = DateHelper.processTime(args, taskModel.getReminderTime());
		Long[] actors = getActors(taskModel, exec);
		Task task = create(taskModel, exec, actors);
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

	/**
	 * 根据模型、执行对象、任务类型构建基本的 task 对象
	 * 
	 * @param model 模型
	 * @param exec  执行对象
	 * @return 任务列表
	 */
	private static Task create(TaskModel model, Execution exec, Long[] actors) {
		Task task = new Task();
		task.setOrderId(exec.getOrder().getId());// 关联流程实例 order
		task.setName(model.getName());
		task.setDisplayName(model.getDisplayName());
		task.setTaskType(model.isMajor() ? TaskType.MAJOR : TaskType.AIDANT);
		task.setModel(model);
		task.setExpireDate(model.getExpireTime());

		if (exec.getTask() != null)
			task.setParentId(exec.getTask().getId());

		Map<String, Object> args = getArgs(exec, actors);
		task.setVariable(JsonHelper.toJson(args));

		setActionUrl(task, args, model);

		return task;
	}

	private static void setActionUrl(Task task, Map<String, Object> args, TaskModel model) {
		String form = model.getForm();
		String formArgs = (String) args.get(form);

		if (StringUtils.hasText(formArgs))
			form = formArgs;

		task.setActionUrl(form);
	}

	private static Map<String, Object> getArgs(Execution exec, Long[] actors) {
		Args args = exec.getArgs();
		args = Args.getEmpty(args);
		args.put(Task.KEY_ACTOR, WfUtils.join(actors));

		return args;
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
		} else if (actors instanceof Long[]) {
			results = (Long[]) actors;// 如果为String[]类型，则直接返回
		} else {
			// 其它类型，抛出不支持的类型异常
			throw new WfException("任务参与者对象[" + actors + "]类型不支持." + "合法参数示例:Long,Integer,new String[]{},'10000,20000',List<String>");
		}

		return results;
	}
}
