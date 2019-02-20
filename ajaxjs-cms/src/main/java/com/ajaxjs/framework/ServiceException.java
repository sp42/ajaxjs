package com.ajaxjs.framework;

/**
 * 业务层异常
 * 
 * @author Frank Cheung
 *
 */
public class ServiceException extends Exception {
	private static final long serialVersionUID = 5896479272205540554L;

	/**
	 * 创建一个业务异常
	 * 
	 * @param msg 异常说明
	 */
	public ServiceException(String msg) {
		super(msg);
	}
}
