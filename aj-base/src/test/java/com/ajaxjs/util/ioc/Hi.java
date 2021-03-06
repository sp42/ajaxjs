package com.ajaxjs.util.ioc;

@Component("Hi")
public class Hi  {
	@Resource
	private Person person;

	public String sayHello() {
		return "Hello " + person.getName();
	}
//
//	public void setPerson(Person person) {
//		this.person = person;
//	}
}