package com.ajaxjs.util.ioc;

import javax.inject.Named;

import com.ajaxjs.util.ioc.testcase.Subject;

@Named
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
