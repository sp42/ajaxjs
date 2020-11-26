package com.ajaxjs.workflow.process;

import java.util.Date;

import com.ajaxjs.framework.BaseModel;

/**
 * 抄送实例实体
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class ProcessCC extends BaseModel {
	private static final long serialVersionUID = -7596174225209412843L;

	private Long activeId;

	private Long actorId;

	private Long creator;

	private Date finishDate;

	public Long getActiveId() {
		return activeId;
	}

	public void setActiveId(Long activeId) {
		this.activeId = activeId;
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
