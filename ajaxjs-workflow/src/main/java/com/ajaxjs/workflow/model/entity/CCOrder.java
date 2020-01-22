/*
 * Copyright 2013-2015 www.snakerflow.com. Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.ajaxjs.workflow.model.entity;

import java.util.Date;

import com.ajaxjs.framework.BaseModel;

/**
 * 抄送实例实体
 * 
 */
public class CCOrder extends BaseModel {
	private static final long serialVersionUID = -7596174225209412843L;

	private Long orderId;

	private Long actorId;

	private Long creator;

	private Date finishDate;

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getCreator() {
		return creator;
	}

	public void setCreator(Long creator) {
		this.creator = creator;
	}

	public Long getActorId() {
		return actorId;
	}

	public void setActorId(Long actorId) {
		this.actorId = actorId;
	}

	public Date getFinishTime() {
		return finishDate;
	}

	public void setFinishTime(Date finishTime) {
		this.finishDate = finishTime;
	}
}
