package com.ajaxjs.util.aop;

public class ReturnBefore {
	private Object returnValue;

	public ReturnBefore(Object returnValue) {
		this.returnValue = returnValue;
	}

	public Object getReturnValue() {
		return returnValue;
	}
}
