package test.com.ajaxjs.mvc.controller;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.mvc.annotation.Controller;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import org.junit.Test;
import org.mockito.Mockito;

import com.ajaxjs.framework.mock.News;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.controller.ControllerScanner;
import com.ajaxjs.mvc.controller.IController;
import com.ajaxjs.mvc.controller.MvcRequest;
import com.ajaxjs.mvc.controller.RequestParam;
import com.ajaxjs.web.test.MockRequest;
import com.ajaxjs.web.test.MockResponse;


public class TestRequestParam {
	
	@Controller
	@Path("/foo")
	public static class c1 implements IController {
		@GET
		public String get(Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) {
			return "get";
		}
		
		@POST
		public String get2(ModelAndView mv, News news) {
			return "get";
		}

		@Path("/bar")
		@GET
		public String getInfo2(@QueryParam("name") String name, @QueryParam("sex") String sex) {
			return "getInfo2";
		}
		
		@Path("/bar")
		@POST
		public String getInfo3(@QueryParam("sex") String sex) {
			return "getInfo2";
		}

		@Path("/{id}")
		@PUT
		public String getInfo4(@PathParam("id") String id) {
			return "getInfo2";
		}
	}
	
	static Map<String, String[]> map = new HashMap<>();
	
	
	@Test
	public void testGetArgs() {
		ControllerScanner.add(c1.class);
		
		HttpServletRequest request = MockRequest.mockRequest("/ajaxjs-web", "/combo/person");
		
		map.put("foo", new String[] {"bar"});
		when(request.getParameterMap()).thenReturn(map);

		HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
		StringWriter writer = MockResponse.writerFactory(response);
		
		Object[] args = RequestParam.getArgs(new MvcRequest(request), response, ControllerScanner.urlMappingTree.get("foo").getMethod);
//		System.out.println(args.length);
		System.out.println(writer.toString());
		
		assertTrue(args[0] instanceof Map);
		assertTrue(args[1] instanceof HttpServletRequest);
		assertTrue(args[2] instanceof HttpServletResponse);
		
		args = RequestParam.getArgs(new MvcRequest(request), response, ControllerScanner.urlMappingTree.get("foo").postMethod);
		assertTrue(args[0] instanceof ModelAndView);
		assertTrue(args[1] instanceof News);
	}
	
	@Test
	public void testGetArgsValue() {
		HttpServletRequest request = MockRequest.mockRequest("/ajaxjs-web", "/foo/12888/");
		HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
		
		when(request.getParameter("name")).thenReturn("foo");
		when(request.getParameter("sex")).thenReturn("bar");
		
		ControllerScanner.add(c1.class);
		Object[] args = RequestParam.getArgs(new MvcRequest(request), response, ControllerScanner.urlMappingTree.get("foo").children.get("bar").getMethod);
		assertEquals("foo", args[0]);
		assertEquals("bar", args[1]);
		
		args = RequestParam.getArgs(new MvcRequest(request), response, ControllerScanner.urlMappingTree.get("foo").children.get("bar").postMethod);
		assertEquals("bar", args[0]);
		
		args = RequestParam.getArgs(new MvcRequest(request), response, ControllerScanner.urlMappingTree.get("foo").children.get("{id}").putMethod);
		assertEquals("12888", args[0]);
	}

}
