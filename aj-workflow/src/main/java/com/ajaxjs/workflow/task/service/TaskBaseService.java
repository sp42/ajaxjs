package com.ajaxjs.workflow.task.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.ajaxjs.framework.BaseService;
import com.ajaxjs.sql.orm.Repository;
import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.util.ioc.Resource;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.util.map.JsonHelper;
import com.ajaxjs.workflow.Execution;
import com.ajaxjs.workflow.WorkflowUtils;
import com.ajaxjs.workflow.model.NodeModel;
import com.ajaxjs.workflow.model.TaskModel;
import com.ajaxjs.workflow.model.TaskModel.PerformType;
import com.ajaxjs.workflow.process.ProcessActive;
import com.ajaxjs.workflow.process.ProcessDefinition;
import com.ajaxjs.workflow.process.service.ProcessActiveService;
import com.ajaxjs.workflow.process.service.ProcessDefinitionService;
import com.ajaxjs.workflow.task.Task;
import com.ajaxjs.workflow.task.TaskActor;
import com.ajaxjs.workflow.task.TaskHistory;

/**
 * 任务 CRUD 操作
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public abstract class TaskBaseService extends BaseService<Task> {
	private static final LogHelper LOGGER = LogHelper.getLog(TaskBaseService.class);

	{
		setUiName("任务");
		setShortName("task");
		setDao(DAO);
	}

	public static TaskDao DAO = new Repository().bind(TaskDao.class);

	public static TaskHistoryDao HISTORY_DAO = new Repository().bind(TaskHistoryDao.class);

	@Resource
	private ProcessActiveService activeService;

	@Resource
	private ProcessDefinitionService defService;

	/**
	 * 查询任务实例，有空指针检查
	 * 
	 * @param id 任务 id
	 * @return 任务实例
	 */
	public Task findTask(Long id) {
		Task task = findById(id);
		Objects.requireNonNull(task, "指定的任务[id=" + id + "]不存在");

		return task;
	}

	/**
	 * 根据流程 id 查找所有的任务
	 * 
	 * @param activeProcId 流程实例 id
	 * @return 所有的任务
	 */
	public List<Task> findByActiveId(Long activeProcId) {
		return findList(by("orderId", activeProcId));
	}

	/**
	 * 根据流程 id 查找所有的历史任务
	 * 
	 * @param activeId 流程实例 id
	 * @return 所有的历史任务
	 */
	public List<TaskHistory> findHistoryTasksByActiveId(Long activeId) {
		return HISTORY_DAO.findList(by("orderId", activeId));
	}

	/**
	 * 根据流程 id 和任务名称查找所有的历史任务
	 * 
	 * @param activeId 流程实例 id
	 * @param taskName 任务名称
	 * @return 所有的历史任务
	 */
	public List<TaskHistory> findHistoryTasks(Long activeId, String taskName) {
		return HISTORY_DAO.findList(by("orderId", activeId).andThen(by("name", taskName)));
	}

	/**
	 * 查询任务、参与者关联实体
	 * 
	 * @param id 任务 id
	 * @return 任务、参与者关联实体
	 */
	public List<TaskActor> findTaskActorsByTaskId(Long id) {
		return DAO.findTaskActorsByTaskId(id);
	}

	/**
	 * 创建任务对象
	 * 
	 * @param task   任务
	 * @param actors 参与者列表
	 * @return 任务
	 */
	protected Task create(Task task, Long... actors) {
		task.setPerformType(PerformType.ANY.ordinal());
		create(task);
		assignTask(task.getId(), actors);
		task.setActorIds(actors);

		return task;
	}

	/**
	 * 对指定的任务分配参与者。参与者可以为用户、部门、角色
	 * 
	 * @param id       任务 id
	 * @param actorIds 参与者 id 集合
	 */
	static void assignTask(Long id, Long... actorIds) {
		if (CommonUtil.isNull(actorIds))
			return;

		for (Long actorId : actorIds) {
			if (actorId == null)
				continue;

			TaskActor taskActor = new TaskActor();
			taskActor.setTaskId(id);
			taskActor.setActorId(actorId);

			DAO.createTaskActor(id, actorId);
		}
	}

	/**
	 * 创建任务，并根据任务类型决定是否分配参与者
	 * 
	 * @param model     任务模型
	 * @param execution 执行对象
	 * @return List<Task> 任务列表
	 */
	public List<Task> createTask(TaskModel model, Execution execution) {
		LOGGER.info("创建 {0} 任务", model.getName());

		Map<String, Object> args = execution.getArgs();
		if (args == null)
			args = new HashMap<String, Object>();

		Long[] actors = getTaskActors(model, execution);
		args.put(TaskModel.KEY_ACTOR, WorkflowUtils.join(actors));

		Task task = model.toTask();
		task.setOrderId(execution.getActive().getId());
//		task.setParentTaskId(execution.getTask() == null ? "start" : execution.getTask().getId());
		task.setParentId(execution.getTask() == null ? 0 : execution.getTask().getId());
		task.setActionUrl(getActionUrl(model, args));
		task.setExpireDate(model.getExpireTime());
		task.setVariable(JsonHelper.toJson(args));

		List<Task> tasks = new ArrayList<>();
		/*
		 * 原版：args 里面可能有 remindDate，那么这时候 taskModel.getReminderTime() 应该为 key
		 * DateHelper.processTime(args, taskModel.getReminderTime())
		 */
		Date remindDate = model.getReminderTime();

		if (model.isPerformAny()) {// 普通任务（Any）
			task = create(task, actors);
			task.setRemindDate(remindDate);
			tasks.add(task);
		} else if (model.isPerformAll()) {// 会签任务（All）
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

	private static String getActionUrl(TaskModel model, Map<String, Object> args) {
		String form = model.getForm();
		Object obj = args.get(form);

		return obj == null ? form : obj.toString();
	}

	/**
	 * 获取任务模型
	 * 
	 * @param id 任务 id
	 * @return TaskModel
	 */
	public TaskModel getTaskModel(Long id) {
		Task task = findTask(id);
		ProcessActive pa = activeService.findActive(task.getOrderId());
		ProcessDefinition process = defService.findById(pa.getProcessId());
		NodeModel nodeModel = process.getModel().getNode(task.getName());
		Objects.requireNonNull(nodeModel, "任务 id 无法找到节点模型.");

		if (nodeModel instanceof TaskModel)
			return (TaskModel) nodeModel;
		else
			throw new IllegalArgumentException("任务 id 找到的节点模型不匹配");
	}

	/**
	 * 根据 Task 模型的 assignee、assignmentHandler 属性以及运行时数据，确定参与者
	 * 
	 * @param model     任务模型
	 * @param execution 执行对象
	 * @return 参与者数组
	 */
	public abstract Long[] getTaskActors(TaskModel model, Execution execution);
}
