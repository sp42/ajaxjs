package com.ajaxjs.framework.spring;

/**
 * 自定义的业务异常
 * 
 * @author Frank Cheung sp42@qq.com
 *
 */
public interface ICustomException {
	/**
	 * 返回自定义的 HTTP 状态码
	 * 
	 * @return
	 */
	public int getErrCode();
}
