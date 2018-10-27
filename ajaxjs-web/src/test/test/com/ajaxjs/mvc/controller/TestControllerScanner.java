package test.com.ajaxjs.mvc.controller;

import static org.junit.Assert.*;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.junit.Test;

import com.ajaxjs.mvc.controller.Action;
import com.ajaxjs.mvc.controller.ControllerScanner;
import com.ajaxjs.mvc.controller.IController;
import com.ajaxjs.util.ReflectUtil;

public class TestControllerScanner {
	ControllerScanner scanner = new ControllerScanner();

	@Path("/foo")
	public static class c1 implements IController {
	}

	@Path("/foo/bar/zxzx")
	public static class c2 implements IController {
	}

	@Path("/foo/bar2")
	public static class c3 implements IController {

	}

	@Path("/foo/bar2")
	public static class c4 implements IController {
		@Path("/id")
		public String getInfo() {
			return "";
		}
	}
	
	@Path("/foo/bar3")
	public static class c5 implements IController {
		@Path("/id/info")
		public String getInfo2() {
			return "";
		}
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
		assertNotNull(ControllerScanner.urlMappingTree.get("foo").children);
		assertNotNull(ControllerScanner.urlMappingTree.get("foo").children.get("bar"));

		assertEquals("foo.bar", ControllerScanner.urlMappingTree.get("foo").children.get("bar").path);
		assertEquals("foo.bar.zxzx", ControllerScanner.urlMappingTree.get("foo").children.get("bar").children.get("zxzx").path);

		ControllerScanner.add(c3.class);
		assertEquals("foo.bar2", ControllerScanner.urlMappingTree.get("foo").children.get("bar2").path);
		
		ControllerScanner.add(c4.class);
		assertEquals("foo.bar2.id", ControllerScanner.urlMappingTree.get("foo").children.get("bar2").children.get("id").path);
		
		ControllerScanner.add(c5.class);
		assertEquals("foo.bar3.id.info", ControllerScanner.urlMappingTree.get("foo").children.get("bar3").children.get("id").children.get("info").path);

		assertNotNull(ControllerScanner.urlMappingTree.get("foo").children.get("bar").children.get("zxzx"));
	}
	
	@Path("/foo")
	public static class c6 implements IController {
		@GET
		public String get() {
			return "get";
		}

		@Path("/bar/info")
		@GET
		public String getInfo2() {
			return "getInfo2";
		}
	}
	
	@Test
	public void testGetMethod() {
		ControllerScanner.add(c1.class);
		ControllerScanner.add(c2.class);
		
		
		ControllerScanner.add(c6.class);
		assertNotNull(ControllerScanner.urlMappingTree.get("foo").getMethod);
		assertNotNull(ControllerScanner.urlMappingTree.get("foo").children.get("bar").children.get("info").getMethod);
		
		assertEquals("get", ReflectUtil.executeMethod(ControllerScanner.urlMappingTree.get("foo").controller, ControllerScanner.urlMappingTree.get("foo").getMethod));
		assertEquals("getInfo2", 
			ReflectUtil.executeMethod(
					ControllerScanner.urlMappingTree.get("foo").children.get("bar").children.get("info").controller, 
					ControllerScanner.urlMappingTree.get("foo").children.get("bar").children.get("info").getMethod));
	}
	
	@Test
	public void testFind() {
		assertEquals("foo.bar3.id.info", ControllerScanner.find("foo/bar3/id/info").path);
		assertEquals("foo.bar.info", ControllerScanner.find("foo/bar/info").path);
	}
	
	@Path("/foo")
	public static class c7 implements IController {
		@GET
		public String get() {
			return "get22";
		}

		@Path("/bar/info")
		@GET
		public String getInfo2() {
			return "getInfo2";
		}
		
		@Path("/bar/info")
		@POST
		public String getInfo3() {
			return "getInfo2";
		}
	}
	
	
	@Test
	public void testTestIfEmpty() {
		ControllerScanner.add(c6.class);
		
		assertNotNull(ControllerScanner.urlMappingTree.get("foo").controller);
		assertEquals("get", ReflectUtil.executeMethod(ControllerScanner.urlMappingTree.get("foo").controller, ControllerScanner.urlMappingTree.get("foo").getMethod));

		assertEquals("foo.bar.info", ControllerScanner.find("foo/bar/info").path);
		
		ControllerScanner.add(c7.class);
		Action action = ControllerScanner.find("foo/bar/info");
		assertEquals("getInfo2", ReflectUtil.executeMethod(action.controller, action.postMethod));
		
	}
}
