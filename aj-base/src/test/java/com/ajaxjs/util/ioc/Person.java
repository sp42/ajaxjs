package com.ajaxjs.util.ioc;

@Component
public class Person extends Base {
	private String name = "Rose";

	public String getName() {
		return name;
	}

}
