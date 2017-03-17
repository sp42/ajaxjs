package com.ajaxjs.util.aop;

/**
 * 如果返回该实例则表示在 before 时候中止 aop
 * @author xinzhang
 *
 */
public class ReturnBefore {
	private Object returnValue;

	public ReturnBefore(Object returnValue) {
		this.returnValue = returnValue;
	}

	public Object getReturnValue() {
		return returnValue;
	}
}
