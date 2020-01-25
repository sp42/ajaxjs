package com.ajaxjs.workflow.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import org.snaker.engine.SnakerEngine;

import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.Repository;
import com.ajaxjs.ioc.Resource;
import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.util.map.JsonHelper;
import com.ajaxjs.workflow.WorkflowConstant;
import com.ajaxjs.workflow.WorkflowUtils;
import com.ajaxjs.workflow.dao.OrderDao;
import com.ajaxjs.workflow.dao.OrderHistoryDao;
import com.ajaxjs.workflow.model.ProcessModel;
import com.ajaxjs.workflow.model.entity.CCOrder;
import com.ajaxjs.workflow.model.entity.Order;
import com.ajaxjs.workflow.model.entity.OrderHistory;
import com.ajaxjs.workflow.model.entity.Process;
import com.ajaxjs.workflow.model.entity.Task;
import com.ajaxjs.workflow.model.entity.TaskHistory;

public class OrderService extends BaseService<Order> {
	public static final LogHelper LOGGER = LogHelper.getLog(OrderService.class);

	{
		setUiName("流程实例");
		setShortName("order");
		setDao(dao);
	}

	public static OrderDao dao = new Repository().bind(OrderDao.class);

	public static OrderHistoryDao historyDao = new Repository().bind(OrderHistoryDao.class);

	@Resource("TaskService")
	private TaskService taskService;

	@Resource("CCOrderService")
	private CCOrderService ccOrderService;

	/**
	 * 根据流程、操作人员、父流程实例ID创建流程实例
	 * 
	 * @param process        流程定义对象
	 * @param operator       操作人员ID
	 * @param args           参数列表
	 * @param parentId       父流程实例ID
	 * @param parentNodeName 父流程节点模型
	 * @return 活动流程实例对象
	 */
	public Order create(Process process, Long operator, Map<String, Object> args, Long parentId, String parentNodeName) {
		Order order = new Order();
		order.setParentId(parentId);
		order.setParentNodeName(parentNodeName);
		order.setCreator(operator);
		order.setUpdator(order.getCreator());
		order.setProcessId(process.getId());
		ProcessModel model = process.getModel();

		if (model != null && args != null) {
			if (model.getExpireDate() != null) {
//				String expireTime = DateHelper.parseTime(args.get(model.getExpireTime()));
//				order.setExpireTime(expireTime);
				order.setExpireDate(model.getExpireDate());
			}

			String orderNo = (String) args.get(SnakerEngine.ID);

			if (!CommonUtil.isEmptyString(orderNo))
				order.setOrderNo(orderNo);
			else
				order.setOrderNo(WorkflowUtils.generate(model));
		}

		order.setVariable(JsonHelper.toJson(args));

		create(order);

		return order;
	}

	/**
	 * 根据流程、操作人员、父流程实例ID创建流程实例
	 * 
	 * @param process  流程定义对象
	 * @param operator 操作人员ID
	 * @param args     参数列表
	 * @return Order 活动流程实例对象
	 */
	public Order create(Process process, Long operator, Map<String, Object> args) {
		return create(process, operator, args, null, null);
	}

	/**
	 * 保存流程实例。流程实例数据会保存至活动实例表、历史实例表
	 * 
	 * @param order 流程实例对象
	 * @return 新建 id
	 */
	@Override
	public Long create(Order order) {
		Long id = super.create(order);

		// 复制一份
		OrderHistory history = new OrderHistory(order);
		history.setStat(WorkflowConstant.STATE_ACTIVE);
		historyDao.create(history);

		return id;
	}

	/**
	 * 更新流程实例。更新活动实例的last_Updator、last_Update_Time、expire_Time、version、variable
	 * 
	 * @return
	 */
	@Override
	public int update(Order order) {
		return super.update(order);
	}

	/**
	 * 默认的任务、实例完成时触发的动作
	 */
	private BiConsumer<TaskHistory, OrderHistory> completion = (TaskHistory task, OrderHistory order) -> {
		if (task != null)
			LOGGER.info("The task[{0}] has been user[{1}] has completed", task.getId(), task.getOperator());

		if (order != null)
			LOGGER.info("The order[{0}] has completed", order.getId());
	};

	/**
	 * 向指定实例id添加全局变量数据
	 * 
	 * @param orderId 实例id
	 * @param args    变量数据
	 */
	public void addVariable(Long orderId, Map<String, Object> args) {
		Order order = findById(orderId);
		Map<String, Object> data = order.getVariableMap();
		data.putAll(args);

		Order _order = new Order();
		_order.setId(orderId);
		_order.setVariable(JsonHelper.toJson(data));

		update(_order);
	}

	/**
	 * 更新历史流程
	 * 
	 * @param id    流程实例id
	 * @param state 历史流程状态
	 * @return 历史流程
	 */
	private static OrderHistory updateHistoryOrder(Long id, int state) {
		OrderHistory history = new OrderHistory();
		history.setId(id);
		history.setStat(state);
		history.setEndDate(new Date());
		historyDao.update(history);

		return history;
	}

	/**
	 * 流程实例正常完成
	 * 
	 * @param orderId 流程实例id
	 */
	public void complete(Long orderId) {
		Order order = new Order();
		order.setId(orderId);
		delete(order);
		OrderHistory history = updateHistoryOrder(orderId, WorkflowConstant.STATE_FINISH);

		getCompletion().accept(null, history);
	}

	/**
	 * 强制中止活动实例,并强制完成活动任务
	 * 
	 * @param orderId  流程实例id
	 * @param operator 处理人员
	 */
	public void terminate(Long orderId, Long operator) {
		List<Task> tasks = taskService.findByOrderId(orderId);

		for (Task task : tasks)
			taskService.complete(task.getId(), operator);

		Order order = new Order();
		order.setId(orderId);
		delete(order);

		OrderHistory history = updateHistoryOrder(orderId, WorkflowConstant.STATE_TERMINATION);
		getCompletion().accept(null, history);
	}

	/**
	 * 强制中止流程实例
	 * 
	 * @param orderId 流程实例id
	 */
	public void terminate(Long orderId) {
		terminate(orderId, null);
	}

	/**
	 * 激活已完成的历史流程实例
	 * 
	 * @param orderId 流程实例id
	 * @return 活动实例对象
	 */
	public Order resume(Long orderId) {
		OrderHistory historyOrder = historyDao.findByOrderId(orderId);
		Order order = historyOrder.undo();
		create(order);

		OrderHistory _historyOrder = new OrderHistory(); // 不用 update 那么多字段
		_historyOrder.setId(historyOrder.getId());
		_historyOrder.setStat(WorkflowConstant.STATE_ACTIVE);
		historyDao.update(_historyOrder);

		List<TaskHistory> histTasks = taskService.findHistoryTasksByOrderId(orderId);

		if (!CommonUtil.isNull(histTasks)) {
			TaskHistory histTask = histTasks.get(0);
			taskService.resume(histTask.getId(), histTask.getOperator());
		}

		return order;
	}

	/**
	 * 谨慎使用.数据恢复非常痛苦，你懂得~~ 级联删除指定流程实例的所有数据： 1.wf_order,wf_hist_order
	 * 2.wf_task,wf_hist_task 3.wf_task_actor,wf_hist_task_actor 4.wf_cc_order
	 * 级联删除指定流程实例的所有数据： 1.wf_order,wf_hist_order 2.wf_task,wf_hist_task
	 * 3.wf_task_actor,wf_hist_task_actor 4.wf_cc_order
	 * 
	 * @param id 流程实例id
	 */
	public void cascadeRemove(Long orderId) {
		List<Task> activeTasks = taskService.findByOrderId(orderId);
		List<TaskHistory> historyTasks = taskService.findHistoryTasksByOrderId(orderId);

		for (Task task : activeTasks)
			taskService.delete(task);

		for (TaskHistory historyTask : historyTasks)
			TaskService.historyDao.delete(historyTask);

		List<CCOrder> ccOrders = ccOrderService.findByOrderId(orderId);

		for (CCOrder ccOrder : ccOrders)
			ccOrderService.delete(ccOrder);

		Order order = findById(orderId);
		historyDao.delete(historyDao.findByOrderId(orderId));
		delete(order);
	}

	public BiConsumer<TaskHistory, OrderHistory> getCompletion() {
		return completion;
	}

	public void setCompletion(BiConsumer<TaskHistory, OrderHistory> completion) {
		this.completion = completion;
	}
}
