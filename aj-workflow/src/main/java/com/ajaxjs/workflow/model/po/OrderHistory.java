package com.ajaxjs.workflow.model.po;

import java.util.Date;

/**
 * 历史流程实例实体类
 * 
 * history 减少了 version，增加了 endDate 字段
 */
public class OrderHistory extends Order {
	/**
	 * 创建历史流程实例
	 */
	public OrderHistory() {
	}

	/**
	 * 根据 Order 创建历史流程实例
	 * 
	 * @param order 活动实例对象
	 */
	public OrderHistory(Order order) {
		setId(order.getId());
		setProcessId(order.getProcessId());
		setCreateDate(order.getCreateDate());
		setExpireDate(order.getExpireDate());
		setCreator(order.getCreator());
		setParentId(order.getParentId());
		setPriority(order.getPriority());
		setOrderNo(order.getOrderNo());
		setVariable(order.getVariable());
	}

	/**
	 * 根据历史实例撤回活动实例
	 * 
	 * @return 活动实例对象
	 */
	public Order undo() {
		Order order = new Order();
		order.setId(getId());
		order.setProcessId(getProcessId());
		order.setParentId(getParentId());
		order.setCreator(getCreator());
		order.setCreateDate(getCreateDate());
		order.setUpdator(getUpdator());
		order.setUpdateDate(getEndDate());
		order.setExpireDate(getExpireDate());
		order.setOrderNo(getOrderNo());
		order.setPriority(getPriority());
		order.setVariable(getVariable());
		order.setVersion(0);

		return order;
	}

	/**
	 * 流程实例结束时间
	 */
	private Date endDate;

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
}
