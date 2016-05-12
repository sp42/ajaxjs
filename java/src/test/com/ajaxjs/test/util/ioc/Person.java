package com.ajaxjs.test.util.ioc;

import com.ajaxjs.util.ioc.Bean;

@Bean("person")
public class Person {
	private String name = "Rose";

	public String getName() {
		return name;
	}
}
