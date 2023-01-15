package com.ajaxjs.workflow.service.task;

import java.util.Collections;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.ajaxjs.util.map.JsonHelper;
import com.ajaxjs.workflow.common.WfException;
import com.ajaxjs.workflow.common.WfUtils;
import com.ajaxjs.workflow.model.po.Task;
import com.ajaxjs.workflow.service.BaseWfService;
import com.ajaxjs.workflow.service.TaskService;

public class TaskActorMgr extends BaseWfService {
	/**
	 * 向指定的任务 id 添加参与者
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
	public void addTaskActor(Long taskId, PerformType performType, Long... actors) {
		Task task = TaskService.getTaskById(taskId);

		if (task.getTaskType() != TaskType.MAJOR)
			return;

		if (performType == null)
			performType = task.getPerformType();

		if (performType == null || performType == PerformType.ANY) {
			TaskService.assignTask(task.getId(), actors);
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
					TaskService.assignTask(newTask.getId(), actor);
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
		Task task = TaskService.getTaskById(taskId);

		if (actors == null || actors.length == 0)
			return;

		if (task.getTaskType() == TaskType.MAJOR) {
//			removeTaskActor(task.getId(), actors);
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
}
