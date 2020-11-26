package com.ajaxjs.workflow.process.service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

import com.ajaxjs.framework.BaseService;
import com.ajaxjs.sql.orm.Repository;
import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.util.ioc.Component;
import com.ajaxjs.util.ioc.Resource;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.util.map.JsonHelper;
import com.ajaxjs.workflow.WorkflowUtils;
import com.ajaxjs.workflow.model.ProcessModel;
import com.ajaxjs.workflow.process.ProcessActive;
import com.ajaxjs.workflow.process.ProcessCC;
import com.ajaxjs.workflow.process.ProcessDefinition;
import com.ajaxjs.workflow.process.ProcessHistory;
import com.ajaxjs.workflow.task.Task;
import com.ajaxjs.workflow.task.TaskHistory;
import com.ajaxjs.workflow.task.service.TaskService;

@Component
public class ProcessActiveService extends BaseService<ProcessActive> {
	private static final LogHelper LOGGER = LogHelper.getLog(ProcessActiveService.class);

	{
		setUiName("流程实例");
		setShortName("active_process");
		setDao(DAO);
	}

	public static ProcessActiveDao DAO = new Repository().bind(ProcessActiveDao.class);

	@Resource
	private TaskService taskService;

	@Resource
	private ProcessCC_Service ccOrderService;

	/**
	 * 查询流程实例，有空指针检查
	 * 
	 * @param id 流程实例 id
	 * @return 流程实例
	 */
	public ProcessActive findActive(Long id) {
		ProcessActive proc = findById(id);
		Objects.requireNonNull(proc, "指定的流程实例[id=" + id + "]已完成或不存在");

		return proc;
	}

	/**
	 * 根据流程、操作人员、父流程实例 id 创建流程实例
	 * 
	 * @param def            流程定义对象
	 * @param operator       操作人员 id
	 * @param args           参数列表
	 * @param parentId       父流程实例 id
	 * @param parentNodeName 父流程节点模型
	 * @return 活动流程实例对象
	 */
	public ProcessActive create(ProcessDefinition def, Long operator, Map<String, Object> args, Long parentId, String parentNodeName) {
		LOGGER.info("创建流程实例 " + def.getName());

		ProcessActive active = new ProcessActive();
		active.setParentId(parentId);
		active.setParentNodeName(parentNodeName);
		active.setCreator(operator);
		active.setUpdator(operator);
		active.setProcessId(def.getId());
		active.setVersion(0);
		active.setVariable(JsonHelper.toJson(args));

		ProcessModel model = def.getModel();

		if (model != null && args != null) {
			LOGGER.info("设置过期时间或生成编号");

			if (model.getExpireDate() != null) // 过期时间
				active.setExpireDate(model.getExpireDate());
//				String expireTime = DateHelper.parseTime(args.get(model.getExpireTime()));
//				active.setExpireTime(expireTime);

			if (args.get("ajFlow.orderNo") != null) // 生成编号
				active.setOrderNo((String) args.get("ajFlow.orderNo"));
			else
				active.setOrderNo(WorkflowUtils.generate(model));
		}

		create(active);

		return active;
	}

	/**
	 * 保存流程实例。流程实例数据会保存至活动实例表、历史实例表
	 * 
	 * @param active 流程实例对象
	 * @return 新建 id
	 */
	@Override
	public Long create(ProcessActive active) {
		Long id = super.create(active);

		LOGGER.info("保存历史流程实例[{0}]", active.getName());

		ProcessHistory history = new ProcessHistory(active);// 复制一份
		history.setStat(ProcessModel.STATE_ACTIVE);
		new ProcessHistoryService().create(history);

		return id;
	}

	/**
	 * 更新流程实例。更新活动实例的last_Updator、last_Update_Time、expire_Time、version、variable
	 * 
	 * @return
	 */
	@Override
	public int update(ProcessActive active) {
		return super.update(active);
	}

	/**
	 * 向指定实例添加全局变量数据
	 * 
	 * @param id   流程实例 id
	 * @param args 变量数据
	 */
	public void addVariable(Long id, Map<String, Object> args) {
		ProcessActive active = findById(id);

		Map<String, Object> data = JsonHelper.parseMap(active.getVariable());
		if (data == null)
			data = Collections.emptyMap();

		data.putAll(args);

		ProcessActive _active = new ProcessActive();
		_active.setId(id);
		_active.setVariable(JsonHelper.toJson(data));

		update(_active);
	}

	/**
	 * 更新历史流程
	 * 
	 * @param id    流程实例 id
	 * @param state 历史流程状态
	 * @return 历史流程
	 */
	private ProcessHistory updateHistoryOrder(Long id, int state) {
		ProcessActive active = new ProcessActive();
		active.setId(id);
		delete(active);

		ProcessHistory history = new ProcessHistory(); // 标记历史流程
		history.setId(id);
		history.setStat(state);
		history.setEndDate(new Date());
		ProcessHistoryService.HISTORY_DAO.update(history);

		getCompletion().accept(null, history);

		return history;
	}

	/**
	 * 流程实例正常完成
	 * 
	 * @param id 流程实例 id
	 */
	public void complete(Long id) {
		LOGGER.info("结束 {0} 流程", id);
		updateHistoryOrder(id, ProcessModel.STATE_FINISH);
	}

	/**
	 * 强制中止活动实例，并强制完成活动任务
	 * 
	 * @param id       流程实例 id
	 * @param operator 处理人员 id
	 */
	public void terminate(Long id, Long operator) {
		List<Task> tasks = taskService.findByActiveId(id);

		for (Task task : tasks)
			taskService.complete(task.getId(), operator, null);

		updateHistoryOrder(id, ProcessModel.STATE_TERMINATION);
	}

	/**
	 * 激活已完成的历史流程实例
	 * 
	 * @param id 历史流程实例 id
	 * @return 流程实例对象
	 */
	public ProcessActive resume(Long historyId) {
		ProcessHistory history = ProcessHistoryService.HISTORY_DAO.findByActiveId(historyId);
		ProcessActive active = history.undo();
		create(active);

		ProcessHistory _history = new ProcessHistory(); // 不用 update 那么多字段
		_history.setId(history.getId());
		_history.setStat(ProcessModel.STATE_ACTIVE);
		ProcessHistoryService.HISTORY_DAO.update(_history);

		List<TaskHistory> histTasks = taskService.findHistoryTasksByActiveId(historyId);

		if (!CommonUtil.isNull(histTasks)) {
			TaskHistory histTask = histTasks.get(0);
			taskService.resume(histTask.getId(), histTask.getOperator());
		}

		return active;
	}

	/**
	 * 谨慎使用.数据恢复非常痛苦，你懂得~~ 级联删除指定流程实例的所有数据： 1.wf_order,wf_hist_order
	 * 2.wf_task,wf_hist_task 3.wf_task_actor,wf_hist_task_actor 4.wf_cc_order
	 * 级联删除指定流程实例的所有数据： 1.wf_order,wf_hist_order 2.wf_task,wf_hist_task
	 * 3.wf_task_actor,wf_hist_task_actor 4.wf_cc_order
	 * 
	 * @param id 流程实例id
	 */
	public void cascadeRemove(Long id) {
		List<Task> activeTasks = taskService.findByActiveId(id);
		List<TaskHistory> historyTasks = taskService.findHistoryTasksByActiveId(id);

		for (Task task : activeTasks)
			taskService.delete(task);

		for (TaskHistory historyTask : historyTasks)
			TaskService.HISTORY_DAO.delete(historyTask);

		List<ProcessCC> ccOrders = ccOrderService.findByActiveId(id);

		for (ProcessCC ccOrder : ccOrders)
			ccOrderService.delete(ccOrder);

		ProcessActive acitve = findById(id);
		ProcessHistoryService.HISTORY_DAO.delete(ProcessHistoryService.HISTORY_DAO.findByActiveId(id));
		delete(acitve);
	}

	/**
	 * 
	 * @param parentId
	 * @param childActiveId
	 * @return
	 */
	public List<ProcessActive> findByIdAndExcludedIds(Long parentId, Long childActiveId) {
		Function<String, String> fn = by("parentId", parentId);

		if (childActiveId != null && childActiveId != 0)
			fn.andThen(setWhere("id NOT IN(" + childActiveId + ")"));

		return findList(fn);
	}

	/**
	 * 默认的任务、实例完成时触发的动作
	 */
	private BiConsumer<TaskHistory, ProcessHistory> completion = (TaskHistory task, ProcessHistory proc) -> {
		if (task != null)
			LOGGER.info("任务[{0}] 已经由用户 [{1}] 执行完成。", task.getId(), task.getOperator());

		if (proc != null)
			LOGGER.info("流程[{0}] 已经完成。", proc.getId());
	};

	public BiConsumer<TaskHistory, ProcessHistory> getCompletion() {
		return completion;
	}

	public void setCompletion(BiConsumer<TaskHistory, ProcessHistory> completion) {
		this.completion = completion;
	}
}
