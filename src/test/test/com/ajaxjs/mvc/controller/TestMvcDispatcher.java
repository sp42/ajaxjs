package test.com.ajaxjs.mvc.controller;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Test;

import com.ajaxjs.mvc.controller.IController;
import com.ajaxjs.mvc.controller.MvcDispatcher;
import com.ajaxjs.util.io.resource.Scanner;

public class TestMvcDispatcher {
	@Test
	public void testScan() {
		Scanner scaner = new Scanner(new MvcDispatcher.IControllerScanner());// 定义一个扫描器，专门扫描 IController
		@SuppressWarnings("unchecked")
		Set<Class<IController>> controllers = (Set<Class<IController>>) scaner.scan("test.com.ajaxjs.mvc");
		assertTrue(controllers.size() > 0);
		System.out.println(controllers.size());
	}
}
