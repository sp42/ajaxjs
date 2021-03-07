package com.demo.aop;

import com.ajaxjs.util.ioc.Component;

@Component
public class Person extends Base implements Subject {
	private String name = "Rose";

	public String getName() {
		return name;
	}

	@Override
	public void doIt() {
		System.out.println("Person do it!");
	}
}
