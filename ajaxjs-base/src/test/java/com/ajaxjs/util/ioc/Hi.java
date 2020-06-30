package com.ajaxjs.util.ioc;

import com.ajaxjs.util.ioc.testcase.Subject;

@Bean("hi")
public class Hi implements Subject {
	@Resource("Person")
	private Person person;

	public String sayHello() {
		return "Hello " + person.getName();
	}
//
//	public void setPerson(Person person) {
//		this.person = person;
//	}

	@Override
	public void doIt() {
		System.out.println("Hi do it!");
	}
}