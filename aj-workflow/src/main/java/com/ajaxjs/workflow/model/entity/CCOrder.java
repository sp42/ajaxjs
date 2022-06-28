package com.ajaxjs.workflow.model.entity;

import java.util.Date;

import com.ajaxjs.framework.BaseModel;

/**
 * 抄送实例实体
 * 
 */
public class CCOrder extends BaseModel {

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
