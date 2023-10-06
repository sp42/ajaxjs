package com.ajaxjs.model;

/**
 * 能够对 Token 进行是否超时的检查
 * 
 * @author Frank Cheung
 *
 */
public interface ExpiresCheck {
	/**
	 * 
	 * @return
	 */
	Long getExpiresIn();
}
