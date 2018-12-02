package com.ajaxjs.ioc;

import com.ajaxjs.ioc.Bean;
import com.ajaxjs.ioc.Resource;

@Bean("hi")
public class Hi {
	@Resource("person")
	private Person person;

	public String sayHello() {
		return "Hello " + person.getName();
	}
//
//	public void setPerson(Person person) {
//		this.person = person;
//	}
}