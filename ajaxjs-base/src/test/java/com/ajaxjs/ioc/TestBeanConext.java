package com.ajaxjs.ioc;

import java.util.function.Function;

import org.junit.Test;

public class TestBeanConext {
	Function<Class<?>, String> giveName = clz -> {
		String[] arr = clz.getName().split("\\.");
		return arr[arr.length - 1];
	};

	@Test
	public void test() {
		BeanContext.simplePut("com.ajaxjs.ioc", giveName);
	}
}
