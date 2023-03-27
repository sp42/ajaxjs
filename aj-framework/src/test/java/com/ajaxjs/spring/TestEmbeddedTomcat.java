package com.ajaxjs.spring;

import org.junit.Test;

public class TestEmbeddedTomcat {
	@Test
	public void test() {
		new EmbeddedTomcatStarter().init(8888);
	}
}
