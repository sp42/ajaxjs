/**
 * 版权所有 2017 Sp42 frank@ajaxjs.com
 * 
 * 根据 2.0 版本 Apache 许可证("许可证")授权；
 * 根据本许可证，用户可以不使用此文件。
 * 用户可从下列网址获得许可证副本：
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 *    
 * 除非因适用法律需要或书面同意，根据许可证分发的软件是基于"按原样"基础提供，
 * 无任何明示的或暗示的保证或条件。详见根据许可证许可下，特定语言的管辖权限和限制。
 */
package com.ajaxjs.orm.dao;

/**
 * DAO 异常
 * 
 * @author Sp42 frank@ajaxjs.com
 *
 */
public class DaoException extends Exception {
	private static final long serialVersionUID = 2863865657051533037L;

	/**
	 * 创建一个 DAO 异常对象。
	 * 
	 * @param message
	 *            错误信息提示
	 */
	public DaoException(String message) {
		super(message);
	}
	
	private boolean zero = true; // 是否空数据
	private boolean overLimit = false; // 是否已经找到数据，但分页超出了
	
	public boolean isZero() {
		return zero;
	}

	public void setZero(boolean zero) {
		this.zero = zero;
	}

	public boolean isOverLimit() {
		return overLimit;
	}

	public void setOverLimit(boolean overLimit) {
		this.overLimit = overLimit;
	}
}
