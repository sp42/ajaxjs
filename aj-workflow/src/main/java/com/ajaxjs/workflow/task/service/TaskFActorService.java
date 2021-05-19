package com.ajaxjs.workflow.task.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.util.map.JsonHelper;
import com.ajaxjs.workflow.Execution;
import com.ajaxjs.workflow.WorkflowException;
import com.ajaxjs.workflow.WorkflowUtils;
import com.ajaxjs.workflow.model.TaskModel;
import com.ajaxjs.workflow.task.Task;
import com.ajaxjs.workflow.task.TaskActor;

/**
 * 这个类主要关注于 Actor 不知为何 这个类不能随便改名，跟我的 ioc 有关
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public abstract class TaskFActorService extends TaskBaseService {
	private static final LogHelper LOGGER = LogHelper.getLog(TaskFActorService.class);

	/**
	 * 向指定的任务添加参与者
	 * 
	 * @param taskId 任务 id
	 * @param actors 参与者
	 */
	public void addTaskActor(Long taskId, Long... actors) {
		addTaskActor(taskId, null, actors);
	}

	/**
	 * 向指定任务添加参与者 该方法根据 performType 类型判断是否需要创建新的活动任务
	 * 
	 * @param taskId      任务 id
	 * @param performType 参与类型
	 * @param actors      参与者
	 */
	public void addTaskActor(Long taskId, Integer performType, Long... actors) {
		LOGGER.info("向指定任务添加参与者");
		Task task = findTask(taskId);

		if (!task.isMajor())
			return;

		if (performType == null)
			performType = task.getPerformType();
		if (performType == null)
			performType = TaskModel.PERFORM_TYPE_ANY;

		switch (performType) {
		case TaskModel.PERFORM_TYPE_ANY:
			assignTask(task.getId(), actors);
			Map<String, Object> data = JsonHelper.parseMap(task.getVariable());
			if (data == null)
				data = Collections.emptyMap();

			String oldActor = (String) data.get(TaskModel.KEY_ACTOR);
			data.put(TaskModel.KEY_ACTOR, oldActor + "," + WorkflowUtils.join(actors));
			task.setVariable(JsonHelper.toJson(data));
			update(task);
			break;
		case TaskModel.PERFORM_TYPE_ALL:
			try {
				for (Long actor : actors) {
					Task newTask = (Task) task.clone();
					newTask.setOperator(actor);

					Map<String, Object> taskData = JsonHelper.parseMap(task.getVariable());
					if (taskData == null)
						taskData = Collections.emptyMap();

					taskData.put(TaskModel.KEY_ACTOR, actor);
					task.setVariable(JsonHelper.toJson(taskData));
					create(newTask);
					assignTask(newTask.getId(), actor);
				}
			} catch (CloneNotSupportedException ex) {
				throw new WorkflowException("任务对象不支持复制", ex.getCause());
			}

			break;
		}
	}

	/**
	 * 对指定的任务 id 删除参与者
	 * 
	 * @param taskId 任务 id
	 * @param actors 参与者
	 */
	public void removeTaskActor(Long taskId, Long... actors) {
		Task task = findTask(taskId);

		if (actors == null || actors.length == 0)
			return;

		if (task.isMajor()) {
			removeTaskActor(task.getId(), actors);
			Map<String, Object> taskData = JsonHelper.parseMap(task.getVariable());
			String actorStr = (String) taskData.get(TaskModel.KEY_ACTOR);

			if (!CommonUtil.isEmptyString(actorStr)) {
				String[] actorArray = actorStr.split(",");
				StringBuilder newActor = new StringBuilder(actorStr.length());
				boolean isMatch;

				for (String actor : actorArray) {
					isMatch = false;
					if (CommonUtil.isEmptyString(actor))
						continue;

					for (Long removeActor : actors) {
						if (actor.equals(removeActor + "")) {
							isMatch = true;
							break;
						}
					}

					if (isMatch)
						continue;
					newActor.append(actor).append(",");
				}

				newActor.deleteCharAt(newActor.length() - 1);
				taskData.put(TaskModel.KEY_ACTOR, newActor.toString());
				task.setVariable(JsonHelper.toJson(taskData));
				update(task);
			}
		}
	}

	@Override
	public Long[] getTaskActors(TaskModel model, Execution execution) {
		Object actors = null;
		// 从 TaskModel 指定的 assignee 属性
		String assignee = model.getAssignee();

		if (CommonUtil.isEmptyString(assignee)) {
			BiFunction<TaskModel, Execution, Object> handler = model.getAssignment();

			if (handler == null) {
				LOGGER.warning("无法获取 Assignee");
				return null;
			}

			actors = handler.apply(model, execution);
		} else {
			if (execution.getArgs().containsKey(assignee))
				actors = execution.getArgs().get(assignee);
			else
				actors = assignee;
		}

		// 类型转换
		if (actors instanceof String[]) // String[] 转换为 Long[]
			return toLongArray((String[]) actors);
		else if (actors instanceof String) {// 如果值为字符串类型，则使用逗号,分隔
			if (actors.toString().indexOf(",") != -1)
				return toLongArray(actors.toString().split(","));
			else
				return new Long[] { Long.parseLong(actors.toString()) }; // 只是一个数字的字符串
		} else if (actors instanceof List) {
			List<?> list = (List<?>) actors;
			Long[] results = new Long[list.size()];

			for (int i = 0; i < list.size(); i++)
				results[i] = Long.parseLong(list.get(i).toString());

			return results;
		} else if (actors instanceof Long)
			return new Long[] { (Long) actors };
		else if (actors instanceof Integer)
			return new Long[] { Long.parseLong(actors + "") };
		else if (actors instanceof Long[])
			return (Long[]) actors;// 直接返回
		else
			// 其它类型，抛出不支持的类型异常
			throw new WorkflowException("任务参与者对象[%s]类型不支持。合法参数示例：Long,Integer,new String[]{},'10000,20000',List<String>", actors.toString());
	}

	/**
	 * 转换为 Long 数组
	 * 
	 * @param arr
	 * @return Long 数组
	 */
	private static Long[] toLongArray(String[] arr) {
		Long[] results = new Long[arr.length];

		for (int i = 0; i < arr.length; i++)
			results[i] = Long.parseLong(arr[i]);

		return results;
	}

	/**
	 * 
	 * @param id
	 * @param childOrderId
	 * @param activeNodes
	 * @return
	 */
	public List<Task> findByActiveIdAndExcludedIds(Long id, Long childOrderId, String[] activeNodes) {
		Function<String, String> fn = by("orderId", id);

		if (childOrderId != null && childOrderId != 0)
			fn.andThen(setWhere("id NOT IN(" + childOrderId + ")"));

		if (!CommonUtil.isNull(activeNodes)) {
			int i = 0;

			for (String str : activeNodes)
				activeNodes[i++] = "'" + str + "'";

			fn.andThen(setWhere("name IN(" + String.join(",", activeNodes) + ")"));
		}

		return findList(fn);
	}
	
	/**
	 * 
	 * 基于用户或组（角色、部门等）的访问策略类。 该策略类适合组作为参与者的情况。 如果操作人id所属的组只要有一项存在于参与者集合中，则表示可访问。
	 * 根据操作人id、参与者集合判断是否允许访问所属任务。
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

	public BiFunction<Long, List<TaskActor>, Boolean> getTaskAccessStrategy() {
		return taskAccessStrategy;
	}

	public void setTaskAccessStrategy(BiFunction<Long, List<TaskActor>, Boolean> taskAccessStrategy) {
		this.taskAccessStrategy = taskAccessStrategy;
	}
}
