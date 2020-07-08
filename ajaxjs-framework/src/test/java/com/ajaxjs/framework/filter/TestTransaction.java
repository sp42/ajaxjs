package com.ajaxjs.framework.filter;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ajaxjs.framework.config.TestHelper;
import com.ajaxjs.mvc.controller.MvcDispatcher;
import com.ajaxjs.web.mock.BaseControllerTest;
import com.ajaxjs.web.mock.MockRequest;
import com.ajaxjs.web.mock.MockResponse;

public class TestTransaction extends BaseControllerTest {
	@BeforeClass
	public void init() throws ServletException {
//		System.out.println(AbstractScanner.getResourcesFromClasspath("\\test.db"));
		TestHelper.loadSQLiteTest("D:\\project\\ajaxjs-framework\\src\\test\\test.db");
		init("com.ajaxjs.cms.filter");
	}

	@Before
	public void load() throws ServletException {
		response = mock(HttpServletResponse.class);
		writer = MockResponse.writerFactory(response);
	}

//	@Test
	public void testFoo1() throws ServletException, IOException {
		request = MockRequest.mockRequest("/ajaxjs-web", "/foo");

		when(request.getMethod()).thenReturn("GET");
		
		MvcDispatcher.dispatcher.apply(request, response);
		chain.doFilter(request, response);
		
		assertEquals("<html><meta charset=\"utf-8\" /><body>Foo</body></html>", writer.toString());
	}

	@Test
	public void testFoo2() throws ServletException, IOException {
		request = MockRequest.mockRequest("/ajaxjs-web", "/foo/bar");

		when(request.getMethod()).thenReturn("GET");
		
		MvcDispatcher.dispatcher.apply(request, response);
		chain.doFilter(request, response);
		
		assertEquals("", writer.toString());
	}
}
