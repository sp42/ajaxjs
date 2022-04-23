package com.ajaxjs.workflow.service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.ajaxjs.framework.BaseService;
import com.ajaxjs.sql.orm.Repository;
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

/**
 * 流程实例
 * @author Frank Cheung
 *
 */
@Component
public class OrderService extends BaseService<Order> {
	public static final LogHelper LOGGER = LogHelper.getLog(OrderService.class);

	{

		setDao(dao);
	}

	public static OrderDao dao = new Repository().bind(OrderDao.class);

	public static OrderHistoryDao historyDao = new Repository().bind(OrderHistoryDao.class);

	@Autowired(required = false)
	private TaskService taskService;

	@Autowired(required = false)
	private CCOrderService ccOrderService;

	/**
	 * 根据流程、操作人员、父流程实例 ID 创建流程实例
	 * 
	 * @param process        流程定义对象
	 * @param operator       操作人员ID
	 * @param args           参数列表
	 * @param parentId       父流程实例ID
	 * @param parentNodeName 父流程节点模型
	 * @return 活动流程实例对象
	 */
	public Order create(Process process, Long operator, Map<String, Object> args, Long parentId, String parentNodeName) {
		LOGGER.info("创建流程实例 " + process.getName());

		Order order = new Order();
		order.setParentId(parentId);
		order.setParentNodeName(parentNodeName);
		order.setCreator(operator);
		order.setUpdator(operator);
		order.setProcessId(process.getId());
		order.setVersion(0);
		order.setVariable(JsonHelper.toJson(args));

		ProcessModel model = process.getModel();

		if (model != null && args != null) {
			LOGGER.info("设置过期时间或生成编号");

			if (model.getExpireDate() != null) // 过期时间
				order.setExpireDate(model.getExpireDate());
//				String expireTime = DateHelper.parseTime(args.get(model.getExpireTime()));
//				order.setExpireTime(expireTime);

			if (args.get("ajFlow.orderNo") != null) // 生成编号
				order.setOrderNo((String) args.get("ajFlow.orderNo"));
			else
				order.setOrderNo(WorkflowUtils.generate(model));
		}

		create(order);

		return order;
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

		LOGGER.info("保存历史流程实例 " + order.getName());

		OrderHistory history = new OrderHistory(order);// 复制一份
		history.setStat(WorkflowConstant.STATE_ACTIVE);
		new OrderHistoryService().create(history);

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
	 * 向指定实例id添加全局变量数据
	 * 
	 * @param orderId 实例id
	 * @param args    变量数据
	 */
	public void addVariable(Long orderId, Map<String, Object> args) {
		Order order = findById(orderId);

		Map<String, Object> data = JsonHelper.parseMap(order.getVariable());
		if (data == null)
			data = Collections.emptyMap();

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
	private OrderHistory updateHistoryOrder(Long id, int state) {
		Order order = new Order();
		order.setId(id);
		delete(order);

		OrderHistory history = new OrderHistory(); // 标记历史流程
		history.setId(id);
		history.setStat(state);
		history.setEndDate(new Date());
		historyDao.update(history);

		getCompletion().accept(null, history);

		return history;
	}

	/**
	 * 流程实例正常完成
	 * 
	 * @param orderId 流程实例id
	 */
	public void complete(Long orderId) {
		LOGGER.info("结束 {0} 流程", orderId);
		updateHistoryOrder(orderId, WorkflowConstant.STATE_FINISH);
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
			taskService.complete(task.getId(), operator, null);

		updateHistoryOrder(orderId, WorkflowConstant.STATE_TERMINATION);
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

		if (!ObjectUtils.isEmpty(histTasks)) {
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

	/**
	 * 
	 * @param parentId
	 * @param childOrderId
	 * @return
	 */
	public List<Order> findByIdAndExcludedIds(Long parentId, Long childOrderId) {
		Function<String, String> fn = by("parentId", parentId);

		if (childOrderId != null && childOrderId != 0)
			fn.andThen(setWhere("id NOT IN(" + childOrderId + ")"));

		return findList(fn);
	}

	/**
	 * 默认的任务、实例完成时触发的动作
	 */
	private BiConsumer<TaskHistory, OrderHistory> completion = (TaskHistory task, OrderHistory order) -> {
		if (task != null)
			LOGGER.info("任务[{0}] 已经由用户 [{1}] 执行完成。", task.getId(), task.getOperator());

		if (order != null)
			LOGGER.info("流程[{0}] 已经完成。", order.getId());
	};

	public BiConsumer<TaskHistory, OrderHistory> getCompletion() {
		return completion;
	}

	public void setCompletion(BiConsumer<TaskHistory, OrderHistory> completion) {
		this.completion = completion;
	}
}
