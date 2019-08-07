package com.ajaxjs.ioc.testcase;

import javax.inject.Named;

@Named
public class Person {
	private String name = "Rose";

	public String getName() {
		return name;
	}
}
