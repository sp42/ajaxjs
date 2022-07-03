package com.ajaxjs.rpc;

import org.apache.dubbo.config.annotation.DubboService;

@DubboService
public class AnnotatedGreetingService implements GreetingService {
	public String sayHello(String name) {
		System.out.println("::::::::::::" + name);
		return "hello, " + name;
	}
}
