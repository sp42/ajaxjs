package com.ajaxjs.util.aop;

public class ReturnAsArg {
	private Object[] args;

	public ReturnAsArg(Object[] args) {
		this.args = args;
	}

	public ReturnAsArg(Object args) {
		this.args = new Object[]{args};
	}

	public Object[] getArgs() {
		return args;
	}
}
