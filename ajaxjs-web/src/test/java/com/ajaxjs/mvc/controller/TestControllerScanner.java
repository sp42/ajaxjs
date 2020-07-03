package com.ajaxjs.mvc.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.junit.Test;

import com.ajaxjs.util.ReflectUtil;

public class TestControllerScanner {

	@Path("/foo/")
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
	public void testAdd() {
		MvcDispatcher.add(c1.class);
		assertEquals("foo", IController.urlMappingTree.get("foo").path);

		MvcDispatcher.add(c2.class);

		assertEquals("foo/bar", IController.urlMappingTree.get("foo").children.get("bar").path);
		assertEquals("foo/bar/zxzx", IController.urlMappingTree.get("foo").children.get("bar").children.get("zxzx").path);

		MvcDispatcher.add(c3.class);
		assertEquals("foo/bar2", IController.urlMappingTree.get("foo").children.get("bar2").path);

		MvcDispatcher.add(c4.class);
		assertEquals("foo/bar2/id", IController.urlMappingTree.get("foo").children.get("bar2").children.get("id").path);

		MvcDispatcher.add(c5.class);
		assertEquals("foo/bar3/id/info", IController.urlMappingTree.get("foo").children.get("bar3").children.get("id").children.get("info").path);

		assertNotNull(IController.urlMappingTree.get("foo").children.get("bar").children.get("zxzx"));
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
		MvcDispatcher.add(c1.class);
		MvcDispatcher.add(c2.class);

		MvcDispatcher.add(c6.class);
		assertNotNull(IController.urlMappingTree.get("foo").getMethod);
		assertNotNull(IController.urlMappingTree.get("foo").children.get("bar").children.get("info").getMethod);

		assertEquals("get", ReflectUtil.executeMethod(IController.urlMappingTree.get("foo").controller,
				IController.urlMappingTree.get("foo").getMethod));
		assertEquals("getInfo2",
				ReflectUtil.executeMethod(
						IController.urlMappingTree.get("foo").children.get("bar").children.get("info").controller,
						IController.urlMappingTree.get("foo").children.get("bar").children.get("info").getMethod));
	}

	 @Test
	public void testFind() {
		assertEquals("foo/bar3/id/info", IController.findTreeByPath("foo/bar3/id/info").path);
//		assertEquals("foo/bar/info", MvcDispatcher.find("foo/bar/info").path);
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
		MvcDispatcher.add(c6.class);

		assertNotNull(IController.urlMappingTree.get("foo").controller);
		assertEquals("get", ReflectUtil.executeMethod(IController.urlMappingTree.get("foo").controller,
				IController.urlMappingTree.get("foo").getMethod));

		assertEquals("foo/bar/info", IController.findTreeByPath("foo/bar/info").path);

		MvcDispatcher.add(c7.class);
		Action action = IController.findTreeByPath("foo/bar/info");
		assertEquals("getInfo2", ReflectUtil.executeMethod(action.controller, action.postMethod));

	}
}
