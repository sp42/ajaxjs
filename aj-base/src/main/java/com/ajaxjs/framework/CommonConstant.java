/**
 * 版权所有 2017 Sp42 frank@ajaxjs.com 根据 2.0 版本 Apache 许可证("许可证")授权；
 * 根据本许可证，用户可以不使用此文件。 用户可从下列网址获得许可证副本：
 * http://www.apache.org/licenses/LICENSE-2.0
 * 除非因适用法律需要或书面同意，根据许可证分发的软件是基于"按原样"基础提供，
 * 无任何明示的或暗示的保证或条件。详见根据许可证许可下，特定语言的管辖权限和限制。
 */
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
