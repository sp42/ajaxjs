/*
 * Copyright 2013-2015 www.snakerflow.com. Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.ajaxjs.workflow.model.po;

import java.util.Date;

/**
 * 历史流程实例实体类
 * 
 * history 减少了 version 和 parentNodeName 字段，增加了 endDate 字段
 */
public class OrderHistory extends OrderPO {
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
	public OrderHistory(OrderPO order) {
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
	public OrderPO undo() {
		OrderPO order = new OrderPO();
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
