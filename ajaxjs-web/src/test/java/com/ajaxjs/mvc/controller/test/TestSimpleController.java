package com.ajaxjs.mvc.controller.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;

import com.ajaxjs.web.mock.BaseControllerTest;
import com.ajaxjs.web.mock.MockRequest;
import com.ajaxjs.web.mock.MockResponse;
import com.ajaxjs.web.mvc.controller.MvcDispatcher;

public class TestSimpleController extends BaseControllerTest {
	// 单测技巧，每个 url 对应一个 request、一个 response
	@Before
	public void load() throws ServletException {
		init("com.ajaxjs.mvc.controller.testcase");
		request = MockRequest.mockRequest("/ajaxjs-web", "/simple");
		response = mock(HttpServletResponse.class);
		writer = MockResponse.writerFactory(response);
	}

	@Test
	public void testGet() throws ServletException, IOException {
		when(request.getMethod()).thenReturn("GET");

		MvcDispatcher.dispatcher.apply(request, response);
		chain.doFilter(request, response);

		assertEquals("<html><meta charset=\"utf-8\" /><body>Hello World!</body></html>", writer.toString());
	}

	@Test
	public void testPost() throws ServletException, IOException {
		when(request.getMethod()).thenReturn("POST");

		MvcDispatcher.dispatcher.apply(request, response);
		chain.doFilter(request, response);

		assertEquals("/index.jsp", MockResponse.getRequestDispatcheResult(request));
	}

	@Test
	public void testPut() throws ServletException, IOException {
		// POST TODO 302 重定向不能用 writer 获取结果
		when(request.getMethod()).thenReturn("PUT");
		os = MockResponse.streamFactory(response);

		MvcDispatcher.dispatcher.apply(request, response);
		chain.doFilter(request, response);

		assertNotNull("servletOutputStream.getContent：" + os.toString());
		assertNotNull(writer.toString());
	}

	@Test
	public void testDelete() throws ServletException, IOException {
		when(request.getMethod()).thenReturn("DELETE");

		MvcDispatcher.dispatcher.apply(request, response);
		chain.doFilter(request, response);

		assertEquals("{\"name\":\"Jack\"}", writer.toString());
	}
}
