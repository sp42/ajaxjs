package com.ajaxjs.workflow.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.ajaxjs.util.map.JsonHelper;
import com.ajaxjs.workflow.common.WfException;
import com.ajaxjs.workflow.common.WfUtils;
import com.ajaxjs.workflow.model.po.Task;
import com.ajaxjs.workflow.model.po.TaskActor;

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
	 * 向指定任务添加参与者 该方法根据 performType 类型判断是否需要创建新的活动任务
	 * 
	 * @param taskId      任务 id
	 * @param performType 参与类型
	 * @param actors      参与者
	 */
	public void addTaskActor(Long taskId, PerformType performType, Long... actors) {
		Task task = TaskDAO.findById(taskId);
		Objects.requireNonNull(task, "指定的任务[id=" + taskId + "]不存在");

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
	 * 对指定的任务 id 删除参与者
	 * 
	 * @param taskId 任务 id
	 * @param actors 参与者
	 */
	public void removeTaskActor(Long taskId, Long... actors) {
		Task task = TaskDAO.findById(taskId);
		Objects.requireNonNull(task, "指定的任务[id=" + taskId + "]不存在");

		if (actors == null || actors.length == 0)
			return;

		if (task.getTaskType() == TaskType.MAJOR) {
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
				TaskDAO.update(task);
			}
		}
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
//	public List<TaskPO> findByOrderIdAndExcludedIds(Long id, Long childOrderId, String[] activeNodes) {
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
//
//		return findList(fn);
//	}

	@Override
	public BiFunction<Long, List<TaskActor>, Boolean> getTaskAccessStrategy() {
		return taskAccessStrategy;
	}

	public void setTaskAccessStrategy(BiFunction<Long, List<TaskActor>, Boolean> taskAccessStrategy) {
		this.taskAccessStrategy = taskAccessStrategy;
	}
}
