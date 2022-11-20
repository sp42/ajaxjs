package com.ajaxjs.workflow.model.po;

import java.util.Date;

/**
 * 抄送实例实体
 * 
 */
public class OrderCc extends BasePersistantObject {
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
