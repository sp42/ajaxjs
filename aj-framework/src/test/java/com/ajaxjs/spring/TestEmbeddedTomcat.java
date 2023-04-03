package com.ajaxjs.spring;

import org.junit.Test;

import com.ajaxjs.framework.spring.boot.EmbeddedTomcatStarter;

public class TestEmbeddedTomcat {
	@Test
	public void test() {
		new EmbeddedTomcatStarter().init(8888);
	}
}
