package com.ajaxjs.workflow.flow;

/**
 * 自定义类
 */
public class CustomClass {
	public String execute(String msg) {
		System.out.println("execute:" + msg);
		return "return " + msg;
	}
}
