package com.ajaxjs.workflow;

/**
 * 常量表
 * 
 * @author Administrator
 *
 */
public interface WorkflowConstant {
	/**
	 * 状态；活动状态
	 */
	public static final Integer STATE_ACTIVE = 1;

	/**
	 * 状态：结束状态
	 */
	public static final Integer STATE_FINISH = 0;

	/**
	 * 状态：终止状态
	 */
	public static final Integer STATE_TERMINATION = 2;
}
