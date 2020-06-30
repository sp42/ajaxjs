package com.ajaxjs.util.ioc;

@Bean("hi")
public class Hi {
	@Resource("Person")
	private Person person;

	public String sayHello() {
		return "Hello " + person.getName();
	}
//
//	public void setPerson(Person person) {
//		this.person = person;
//	}
}