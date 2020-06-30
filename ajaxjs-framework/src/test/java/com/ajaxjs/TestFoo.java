package com.ajaxjs;

import org.junit.Test;

import com.ajaxjs.util.ioc.ComponentMgr;

public class TestFoo {
	@Test
	public void test() {
		ComponentMgr.scan("com.ajaxjs");
	}
}
