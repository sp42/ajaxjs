/*
 * Copyright 2013-2015 www.snakerflow.com. Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.ajaxjs.workflow.model.entity;

import java.util.Date;

import org.snaker.engine.SnakerEngine;
import org.snaker.engine.core.ServiceContext;

/**
 * 历史流程实例实体类
 * 
 */
public class OrderHistory extends Order {
	private static final long serialVersionUID = 5853727929104539328L;
	
	public OrderHistory() {
	}
	
	/**
	 * 
	 * @param order
	 */
	public OrderHistory(Order order) {
		setId(order.getId());
		this.processId = order.getProcessId();
		this.createDate = order.getCreateDate();
		this.expireDate = order.getExpireDate();
		this.creator = order.getCreator();
		this.parentId = order.getParentId();
		this.priority = order.getPriority();
		this.orderNo = order.getOrderNo();
		this.variable = order.getVariable();
	}

	/**
	 * 根据历史实例撤回活动实例
	 * 
	 * @return 活动实例对象
	 */
	public Order undo() {
		Order order = new Order();
		order.setId(this.id);
		order.setProcessId(this.processId);
		order.setParentId(this.parentId);
		order.setCreator(this.creator);
		order.setCreateDate(this.createDate);
		order.setLastUpdator(this.creator);
		order.setLastUpdateDate(this.endDate);
		order.setExpireDate(this.expireDate);
		order.setOrderNo(this.orderNo);
		order.setPriority(this.priority);
		order.setVariable(this.variable);
		order.setVersion(0);

		return order;
	}

	/**
	 * TODO ????
	 * 
	 * @return
	 */
	public String getProcessName() {
		SnakerEngine engine = ServiceContext.getEngine();
		Process process = engine.process().getProcessById(getProcessId());

		if (process == null)
			return getProcessId() + "";

		return process.getDisplayName();
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
