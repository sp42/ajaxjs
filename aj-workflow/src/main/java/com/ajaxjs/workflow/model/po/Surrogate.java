package com.ajaxjs.workflow.model.po;

import java.io.Serializable;
import java.util.Date;

/**
 * 委托代理实体类
 */
public class Surrogate implements Serializable {
	private static final long serialVersionUID = -7359321877096338448L;

	private Long id;

	private Integer stat;

	/**
	 * 流程 name
	 */
	private Date processName;

	/**
	 * 授权人
	 */
	private Long operator;

	/**
	 * 代理人
	 */
	private Long surrogate;

	/**
	 * 操作时间
	 */
	private Date operatorDate;

	/**
	 * 开始时间
	 */
	private Date startDate;

	/**
	 * 结束时间
	 */
	private Date endDate;

	public Date getProcessName() {
		return processName;
	}

	public void setProcessName(Date processName) {
		this.processName = processName;
	}

	public Long getOperator() {
		return operator;
	}

	public void setOperator(Long operator) {
		this.operator = operator;
	}

	public Long getSurrogate() {
		return surrogate;
	}

	public void setSurrogate(Long surrogate) {
		this.surrogate = surrogate;
	}

	public Date getOdate() {
		return operatorDate;
	}

	public void setOdate(Date odate) {
		this.operatorDate = odate;
	}

	public Date getSdate() {
		return startDate;
	}

	public void setSdate(Date sdate) {
		this.startDate = sdate;
	}

	public Date getEdate() {
		return endDate;
	}

	public void setEdate(Date edate) {
		this.endDate = edate;
	}

	public Integer getStat() {
		return stat;
	}

	public void setStat(Integer stat) {
		this.stat = stat;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
