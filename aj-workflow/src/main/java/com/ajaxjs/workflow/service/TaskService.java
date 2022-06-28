package com.ajaxjs.workflow.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.ajaxjs.util.map.JsonHelper;
import com.ajaxjs.workflow.WorkflowException;
import com.ajaxjs.workflow.WorkflowUtils;
import com.ajaxjs.workflow.model.Execution;
import com.ajaxjs.workflow.model.TaskModel;
import com.ajaxjs.workflow.model.entity.Task;
import com.ajaxjs.workflow.model.entity.TaskActor;

/**
 * Actor
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
@Component
public class TaskService extends TaskBaseService {
	/**
	 * 向指定的任务id添加参与者
	 * 
	 * @param taskId 任务id
	 * @param actors 参与者
	 */
	public void addTaskActor(Long taskId, Long... actors) {
		addTaskActor(taskId, null, actors);
	}

	/**
	 * 向指定任务添加参与者 该方法根据performType类型判断是否需要创建新的活动任务
	 * 
	 * @param taskId      任务id
	 * @param performType 参与类型
	 * @param actors      参与者
	 */
	public void addTaskActor(Long taskId, Integer performType, Long... actors) {
		Task task = findById(taskId);
		Objects.requireNonNull(task, "指定的任务[id=" + taskId + "]不存在");

		if (!task.isMajor())
			return;
		if (performType == null)
			performType = task.getPerformType();
		if (performType == null)
			performType = 0;

		switch (performType) {
		case 0:
			assignTask(task.getId(), actors);
			Map<String, Object> data = JsonHelper.parseMap(task.getVariable());
			if (data == null)
				data = Collections.emptyMap();

			String oldActor = (String) data.get(Task.KEY_ACTOR);
			data.put(Task.KEY_ACTOR, oldActor + "," + WorkflowUtils.join(actors));
			task.setVariable(JsonHelper.toJson(data));
			update(task);
			break;
		case 1:
			try {
				for (Long actor : actors) {
					Task newTask = (Task) task.clone();
					newTask.setOperator(actor);

					Map<String, Object> taskData = JsonHelper.parseMap(task.getVariable());
					if (taskData == null)
						taskData = Collections.emptyMap();

					taskData.put(Task.KEY_ACTOR, actor);
					task.setVariable(JsonHelper.toJson(taskData));
					create(newTask);
					assignTask(newTask.getId(), actor);
				}
			} catch (CloneNotSupportedException ex) {
				throw new WorkflowException("任务对象不支持复制", ex.getCause());
			}

			break;
		default:
			break;
		}
	}

	/**
	 * 对指定的任务id删除参与者
	 * 
	 * @param taskId 任务id
	 * @param actors 参与者
	 */
	public void removeTaskActor(Long taskId, Long... actors) {
		Task task = findById(taskId);
		Objects.requireNonNull(task, "指定的任务[id=" + taskId + "]不存在");

		if (actors == null || actors.length == 0)
			return;

		if (task.isMajor()) {
			removeTaskActor(task.getId(), actors);
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
				update(task);
			}
		}
	}

	@Override
	public Long[] getTaskActors(TaskModel model, Execution execution) {
		Object assigneeObject = null;
		BiFunction<TaskModel, Execution, Object> handler = model.getAssignment();

		if (StringUtils.hasText(model.getAssignee()))
			assigneeObject = execution.getArgs().get(model.getAssignee());
		else if (handler != null)
			assigneeObject = handler.apply(model, execution);

		System.out.println("SSSSSSSSS:" + (assigneeObject == null));

		return getTaskActors(assigneeObject == null ? model.getAssignee() : assigneeObject);
	}

	/**
	 * 根据 taskmodel 指定的 assignee 属性，从 args 中取值将取到的值处理为 String[] 类型。
	 * 
	 * @param actors 参与者对象
	 * @return 参与者数组
	 */
	Long[] getTaskActors(Object actors) {
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
			throw new WorkflowException("任务参与者对象[" + actors + "]类型不支持." + "合法参数示例:Long,Integer,new String[]{},'10000,20000',List<String>");
		}

		return results;
	}

	/**
	 * 
	 * 基于用户或组（角色、部门等）的访问策略类。 该策略类适合组作为参与者的情况。 如果操作人id所属的组只要有一项存在于参与者集合中，则表示可访问。
	 * 根据操作人id、参与者集合判断是否允许访问所属任务。
	 * 
	 * @param operator 操作人id
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
		Function<String, String> fn = by("orderId", id);

		if (childOrderId != null && childOrderId != 0)
			fn.andThen(setWhere("id NOT IN(" + childOrderId + ")"));

		if (!ObjectUtils.isEmpty(activeNodes)) {
			int i = 0;

			for (String str : activeNodes)
				activeNodes[i++] = "'" + str + "'";

			fn.andThen(setWhere("name IN(" + String.join(",", activeNodes) + ")"));
		}

		return findList(fn);
	}

	@Override
	public BiFunction<Long, List<TaskActor>, Boolean> getTaskAccessStrategy() {
		return taskAccessStrategy;
	}

	public void setTaskAccessStrategy(BiFunction<Long, List<TaskActor>, Boolean> taskAccessStrategy) {
		this.taskAccessStrategy = taskAccessStrategy;
	}
}
