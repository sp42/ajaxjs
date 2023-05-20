package com.ajaxjs.workflow.common;

/**
 * 常量表
 */
public interface WfConstant {
	/**
	 * 状态；活动状态
	 */
	static final Integer STATE_ACTIVE = 1;

	/**
	 * 状态：结束状态
	 */
	static final Integer STATE_FINISH = 0;

	/**
	 * 状态：终止状态
	 */
	public static final Integer STATE_TERMINATION = 2;

	/**
	 * 任务类型
	 */
	public static enum TaskType {
		/**
		 * 主办任务
		 */
		MAJOR,

		/**
		 * 协办任务
		 */
		AIDAN,

		/**
		 * 仅作为记录的
		 */
		RECORD
	}

	/**
	 * 任务参与类型
	 */
	public static enum PerformType {
		/**
		 * 普通任务，任何一个参与者处理完即执行下一步
		 */
		ANY,

		/**
		 * 会签任务，所有参与者都完成，才可执行下一步
		 */
		ALL
	}
}
