package com.ajaxjs.workflow.model.po;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 委托代理实体类
 */
@Data
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

}
