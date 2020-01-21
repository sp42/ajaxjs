package com.ajaxjs.workflow.service;

import java.util.Map;

import org.snaker.engine.Completion;
import org.snaker.engine.SnakerEngine;
import org.snaker.engine.entity.HistoryOrder;
import org.snaker.engine.helper.DateHelper;

import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.Repository;
import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.util.map.JsonHelper;
import com.ajaxjs.workflow.WorkflowConstant;
import com.ajaxjs.workflow.WorkflowUtils;
import com.ajaxjs.workflow.dao.OrderDao;
import com.ajaxjs.workflow.model.ProcessModel;
import com.ajaxjs.workflow.model.entity.Order;
import com.ajaxjs.workflow.model.entity.OrderHistory;
import com.ajaxjs.workflow.model.entity.Process;

public class OrderService extends BaseService<Order> {
	{
		setUiName("流程实例");
		setShortName("order");
		setDao(dao);
	}

	public static OrderDao dao = new Repository().bind(OrderDao.class);

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
	public Order create(Process process, String operator, Map<String, Object> args, Long parentId, String parentNodeName) {
		Order order = new Order();
		order.setParentId(parentId);
		order.setParentNodeName(parentNodeName);
		order.setCreator(operator);
		order.setLastUpdator(order.getCreator());
		order.setProcessId(process.getId());
		ProcessModel model = process.getModel();

		if (model != null && args != null) {
			if (!CommonUtil.isEmptyString(model.getExpireTime())) {
				String expireTime = DateHelper.parseTime(args.get(model.getExpireTime()));
				order.setExpireTime(expireTime);
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
	public Order create(Process process, String operator, Map<String, Object> args) {
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

		OrderHistory history = new OrderHistory(order);
		history.setOrderState(WorkflowConstant.STATE_ACTIVE);
		dao.createHistory(history);

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
		Map<String, Object> data = order.getVariableMap();
		data.putAll(args);

		Order _order = new Order();
		_order.setVariable(JsonHelper.toJson(data));
		_order.setId(orderId);

		update(_order);
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
		
		OrderHistory history = new OrderHistory();
		history.setId(orderId);
		history.setOrderState(WorkflowConstant.STATE_FINISH);
		history.setEndTime(DateHelper.getTime());		
		dao.updateHistory(history);
		
		Completion completion = getCompletion();

		if (completion != null)
			completion.complete(history);
	}
//
//
//	/**
//	 * 流程实例强制终止
//	 * 
//	 * @param orderId 流程实例id
//	 */
//	void terminate(String orderId);
//
//	/**
//	 * 流程实例强制终止
//	 * 
//	 * @param orderId  流程实例id
//	 * @param operator 处理人员
//	 */
//	void terminate(String orderId, String operator);
//
//	/**
//	 * 唤醒历史流程实例
//	 * 
//	 * @param orderId 流程实例id
//	 * @return 活动实例对象
//	 */
//	Order resume(String orderId);
	

//	/**
//	 * 谨慎使用.数据恢复非常痛苦，你懂得~~ 级联删除指定流程实例的所有数据： 1.wf_order,wf_hist_order
//	 * 2.wf_task,wf_hist_task 3.wf_task_actor,wf_hist_task_actor 4.wf_cc_order
//	 * 
//	 * @param id
//	 */
//	void cascadeRemove(String id);
}
