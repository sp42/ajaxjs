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

import com.ajaxjs.web.mock.MockRequest;
import com.ajaxjs.web.mock.MockResponse;

public class TestSimpleController extends BaseTest {
	// 单测技巧，每个 url 对应一个 request、一个 response
	@Before
	public void load() throws ServletException {
		request = MockRequest.mockRequest("/ajaxjs-web", "/simple");
		response = mock(HttpServletResponse.class);
		writer = MockResponse.writerFactory(response);
	}

	@Test
	public void testGet() throws ServletException, IOException {
		when(request.getMethod()).thenReturn("GET");

		dispatcher.doFilter(request, response, chain);
		
		assertEquals("<html><meta charset=\"utf-8\" /><body>Hello World!</body></html>", writer.toString());
	}

	@Test
	public void testPost() throws ServletException, IOException {
		when(request.getMethod()).thenReturn("POST");

		dispatcher.doFilter(request, response, chain);
		
		assertEquals("/index.jsp", MockResponse.getRequestDispatcheResult(request));
	}

	@Test
	public void testPut() throws ServletException, IOException {
		// POST TODO 302 重定向不能用 writer 获取结果
		when(request.getMethod()).thenReturn("PUT");
		os = MockResponse.streamFactory(response);

		dispatcher.doFilter(request, response, chain);
		assertNotNull("servletOutputStream.getContent：" + os.toString());
		assertNotNull(writer.toString());
	}

	@Test
	public void testDelete() throws ServletException, IOException {
		when(request.getMethod()).thenReturn("DELETE");

		dispatcher.doFilter(request, response, chain);
		assertEquals("{\"name\":\"Jack\"}", writer.toString());
	}
}
