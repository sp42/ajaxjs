package test.com.ajaxjs.mvc.controller;

import static org.junit.Assert.*;

import javax.mvc.annotation.Controller;
import javax.ws.rs.Path;

import org.junit.Test;

import com.ajaxjs.mvc.controller.ControllerScanner;
import com.ajaxjs.mvc.controller.IController;

public class TestControllerScanner {
	ControllerScanner scanner = new ControllerScanner();

	@Controller
	@Path("/foo")
	public static class c1 implements IController {
	}
	@Controller
	@Path("/foo/bar/zxzx")
	public static class c2 implements IController {
	}
	@Controller
	@Path("/foo/bar2")
	public static class c3 implements IController {
	}

	@Test
	public void testTestClass() {
		ControllerScanner.testClass(c1.class);
	}
	@Test
	public void testAdd() {
		ControllerScanner.add(c1.class);
		assertNotNull(ControllerScanner.urlMappingTree.get("foo"));
		
		ControllerScanner.add(c2.class);
		assertNotNull(ControllerScanner.urlMappingTree.get("foo").children.get(0));
		assertNotNull(ControllerScanner.urlMappingTree.get("foo").children.get(0).get("bar"));
		assertNotNull(ControllerScanner.urlMappingTree.get("foo").children.get(0).get("bar").children.get(0));
		
		assertEquals("foo.bar", ControllerScanner.urlMappingTree.get("foo").children.get(0).get("bar").path);
		assertEquals("foo.bar.zxzx", ControllerScanner.urlMappingTree.get("foo").children.get(0).get("bar").children.get(0).get("zxzx").path);
		
		ControllerScanner.add(c3.class);
		assertEquals("foo.bar", ControllerScanner.urlMappingTree.get("foo").children.get(0).get("bar").path);
		
	}
}
