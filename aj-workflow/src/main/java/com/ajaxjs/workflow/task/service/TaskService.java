package com.ajaxjs.workflow.task.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.util.ioc.Component;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.util.map.JsonHelper;
import com.ajaxjs.workflow.WorkflowEngine;
import com.ajaxjs.workflow.WorkflowException;
import com.ajaxjs.workflow.WorkflowUtils;
import com.ajaxjs.workflow.model.NodeModel;
import com.ajaxjs.workflow.model.ProcessModel;
import com.ajaxjs.workflow.task.Task;
import com.ajaxjs.workflow.task.TaskActor;
import com.ajaxjs.workflow.task.TaskHistory;

/**
 * 任务服务。
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
@Component
public class TaskService extends TaskFActorService {
	private static final LogHelper LOGGER = LogHelper.getLog(TaskFActorService.class);

	/**
	 * 完成指定任务
	 * 
	 * @param id       任务 id
	 * @param operator 操作人
	 * @param args     参数集合
	 * @return
	 */
	public Task complete(Long id, Long operator, Map<String, Object> args) {
		Task task = findTask(id);
		task.setVariable(JsonHelper.toJson(args));

		if (!isAllowed(task, operator))
			throw new WorkflowException("当前参与者[{0}]不允许执行任务[taskId={0}]", operator + "", id + "");

		LOGGER.info("完成任务：创建历史任务，然后删除 Task");
		TaskHistory history = new TaskHistory(task);
		history.setFinishDate(new Date());
		history.setStat(ProcessModel.STATE_FINISH);
		history.setOperator(operator);

		if (history.getActorIds() == null) {
			LOGGER.info("查询相关的任务参与者，保存到 TaskHistory");
			List<TaskActor> actors = findTaskActorsByTaskId(task.getId());
			Long[] actorIds = new Long[actors.size()];

			for (int i = 0; i < actors.size(); i++)
				actorIds[i] = actors.get(i).getActorId();

			history.setActorIds(actorIds);
		}

		initCreate(history);
		HISTORY_DAO.create(history);
		delete(task);

//		orderService.getCompletion().accept(history, null);

		return task;
	}

	/**
	 * 提取任务
	 * 
	 * @param id       任务 id
	 * @param operator 操作人 id
	 * @return Task 任务对象
	 */
	public Task take(Long id, Long operator) {
		LOGGER.info("提取任务 [{0}]", id);
		Task task = findTask(id);

		if (!isAllowed(task, operator))
			throw new WorkflowException("当前参与者[%s]不允许提取任务[taskId=%s]", operator + "", id + "");

		Task update = new Task();
		update.setId(id);
		update.setOperator(operator); // 设置操作人
		update.setFinishDate(new Date()); // 设置完成时间
		update(update);

		task.setOperator(operator);
		task.setFinishDate(update.getFinishDate());

		return task;
	}

	/**
	 * 创建新的任务。适用于转派，动态协办处理
	 * 
	 * @param id       任务 id
	 * @param taskType 任务类型
	 * @param actors   参与者列表
	 * @return 任务列表
	 */
	public List<Task> createNewTask(Long id, int taskType, Long... actors) {
		Task task = findTask(id);
		List<Task> tasks = new ArrayList<>();

		try {
			Task newTask = (Task) task.clone();
			newTask.setParentId(id);
			newTask.setTaskType(taskType);
			tasks.add(create(newTask, actors));
		} catch (CloneNotSupportedException e) {
			throw new WorkflowException("任务对象不支持复制", e.getCause());
		}

		return tasks;
	}

	/**
	 * 撤回任务
	 * 
	 * @param id       任务 id
	 * @param operator 操作人
	 * @return Task 任务对象
	 */
	public Task withdrawTask(Long id, Long operator) {
		TaskHistory histTask = findTaskHistory(id);

		// getNextActiveTasks
		// 由该历史任务派发的所有活动任务
		List<Task> tasks = histTask.isPerformAny() ? findList(by("parentTaskId", histTask.getId()))
				: DAO.getNextActiveTasks(histTask.getOrderId(), histTask.getName(), histTask.getParentId());

		if (CommonUtil.isNull(tasks)) // 如果无活动任务则不允许撤回
			throw new WorkflowException("后续活动任务已完成或不存在，无法撤回");

		for (Task task : tasks)
			delete(task);

		return resume(histTask, false);
	}

	/**
	 * 驳回至上一步处理
	 * 
	 * @param model       流程定义模型，用以获取上一步模型对象
	 * @param currentTask 当前任务对象
	 * @return Task 任务对象
	 */
	public Task rejectTask(ProcessModel model, Task currentTask) {
		Long parentTaskId = currentTask.getParentId();

		if (!WorkflowUtils.hasNumber(parentTaskId))
			throw new WorkflowException("上一步任务 id 为空，无法驳回至上一步处理");

		TaskHistory histTask = findTaskHistory(parentTaskId);
		NodeModel parent = model.getNode(histTask.getName());
		NodeModel current = model.getNode(currentTask.getName());

		if (!NodeModel.canRejected(current, parent))
			throw new WorkflowException("无法驳回至上一步处理，请确认上一步骤并非 fork、join、suprocess 以及会签任务");

		return resume(histTask, true);
	}

	/**
	 * 唤醒历史任务。该方法会导致流程状态不可控，请慎用 TODO 缺少单测
	 * 
	 * @param id       历史任务 id
	 * @param operator 操作人 id
	 * @return Task 唤醒后的任务对象
	 */
	public Task resume(Long id, Long operator) {
		TaskHistory histTask = findTaskHistory(id);
		boolean isAllowed = true;

		if (WorkflowUtils.hasNumber(histTask.getOperator()))
			isAllowed = histTask.getOperator() == operator;

		if (isAllowed)
			return resume(histTask, false);
		else
			throw new WorkflowException("当前参与者[%s]不允许唤醒历史任务[taskId=%s]", operator + "", id + "");
	}

	/**
	 * 
	 * @param histTask
	 * @param isSetOperator
	 * @return
	 */
	private Task resume(TaskHistory histTask, boolean isSetOperator) {
		Task task = histTask.undoTask();// 恢复原来的任务

		if (isSetOperator)
			task.setOperator(histTask.getOperator());

		create(task);// 持久化
		assignTask(task.getId(), task.getOperator());

		return task;
	}

	private static TaskHistory findTaskHistory(Long id) {
		TaskHistory histTask = HISTORY_DAO.findById(id);
		Objects.requireNonNull(histTask, "指定的历史任务[id=" + id + "]不存在");

		return histTask;
	}

	/**
	 * 判断当前操作人是否允许执行指定的任务
	 * 
	 * @param task     任务对象
	 * @param operator 操作人
	 * @return boolean 是否允许操作
	 */
	private boolean isAllowed(Task task, Long operator) {
		if (WorkflowUtils.hasNumber(operator)) {
			if (WorkflowEngine.ADMIN == operator)
				return true;

			if (WorkflowUtils.hasNumber(task.getOperator()))
				return operator == task.getOperator();
		}

		List<TaskActor> actors = findTaskActorsByTaskId(task.getId());

		if (CommonUtil.isNull(actors)) // 没有其他相关的参与者就表明上面的鉴权已经足够了
			return true;
		else
			return operator != null && operator != 0 && getTaskAccessStrategy().apply(operator, actors); // TODO 改为接口
	}

}
