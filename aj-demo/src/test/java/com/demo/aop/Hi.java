package com.demo.aop;

import com.ajaxjs.util.ioc.Component;
import com.ajaxjs.util.ioc.Resource;

@Component("Hi")
public class Hi implements Subject {
	@Resource
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