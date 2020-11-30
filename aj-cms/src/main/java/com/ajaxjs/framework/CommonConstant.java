package com.ajaxjs.framework;

/**
 * 通用实体常量
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public interface CommonConstant {
	/**
	 * 实体已上架、已上线
	 */
	public static final int ON_LINE = 1;

	/**
	 * 实体已下架、已下线
	 */
	public static final int OFF_LINE = 0;

	/**
	 * 实体已被删除的标记
	 */
	public static final int DELTETED = 2;

	public static class 实体状态 {
		/**
		 * 实体已上架、已上线
		 */
		public static final int 上线 = 1;

		/**
		 * 实体已下架、已下线
		 */
		public static final int 下线 = 0;

		/**
		 * 实体已被删除的标记
		 */
		public static final int 已删除 = 2;
	}

	/**
	 * 前端用的界面模板
	 */
	public static final boolean UI_FRONTEND = false;

	/**
	 * 后端用的界面模板
	 */
	public static final boolean UI_ADMIN = true;
}