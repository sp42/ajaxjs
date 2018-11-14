package com.ajaxjs.mvc.controller;

import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;

import com.ajaxjs.mvc.controller.IController;
import com.ajaxjs.mvc.controller.IControllerScanner;

public class TestMvcDispatcher {
	@Test
	public void testScan() {
		IControllerScanner scanner = new IControllerScanner();
		Set<Class<IController>> controllers = scanner.scan("com.ajaxjs.mvc");
		assertTrue(controllers.size() > 0);
	}
}
