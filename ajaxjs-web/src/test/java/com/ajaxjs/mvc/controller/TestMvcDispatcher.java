package com.ajaxjs.mvc.controller;

import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;

public class TestMvcDispatcher {
	@Test
	public void testScan() {
		ControllerScanner scanner = new ControllerScanner();
		Set<Class<IController>> controllers = scanner.scan("com.ajaxjs.mvc");
		assertTrue(controllers.size() > 0);
	}
}
