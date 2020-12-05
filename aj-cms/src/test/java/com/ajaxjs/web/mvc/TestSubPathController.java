package com.ajaxjs.web.mvc;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;

import com.ajaxjs.web.mock.BaseControllerTest;
import com.ajaxjs.web.mock.MockResponse;

public class TestSubPathController extends BaseControllerTest {
	@Before
	public void load() {
		init("com.ajaxjs.mvc.controller.testcase");

		// 请求对象
		request = mock(HttpServletRequest.class);
		when(request.getContextPath()).thenReturn("/ajaxjs-web");
		when(request.getRequestURI()).thenReturn("/ajaxjs-web/MyTopPath_And_SubPath/subPath");// 配置请求路径

		// 响应对象
		response = mock(HttpServletResponse.class);
		writer = MockResponse.writerFactory(response);
	}

	@Test
	public void testMainPathGet() throws ServletException, IOException {
		HttpServletRequest request;
		HttpServletResponse response;
		StringWriter writer;

		// 请求对象
		request = mock(HttpServletRequest.class);
		when(request.getContextPath()).thenReturn("/ajaxjs-web");
		when(request.getRequestURI()).thenReturn("/ajaxjs-web/MyTopPath_And_SubPath");// 配置请求路径
		when(request.getMethod()).thenReturn("GET");

		// 响应对象
		response = mock(HttpServletResponse.class);
		writer = MockResponse.writerFactory(response);

		MvcDispatcher.dispatcher.apply(request, response);
		chain.doFilter(request, response);

		assertEquals("<html><meta charset=\"utf-8\" /><body>Hello World!</body></html>", writer.toString());
	}

	@Test
	public void testGet() throws ServletException, IOException {
		when(request.getMethod()).thenReturn("GET");
		when(request.getParameter("name")).thenReturn("Jack");

		MvcDispatcher.dispatcher.apply(request, response);
		chain.doFilter(request, response);

		assertEquals("<html><meta charset=\"utf-8\" /><body>Hello Jack</body></html>", writer.toString());
	}

	@Test
	public void testPost() throws ServletException, IOException {
		when(request.getMethod()).thenReturn("POST");
		when(request.getParameter("name")).thenReturn("Jack");

		MvcDispatcher.dispatcher.apply(request, response);
		chain.doFilter(request, response);

		assertEquals("<html><meta charset=\"utf-8\" /><body>Hello Jack</body></html>", writer.toString());
	}

	@Test
	public void testPut() throws ServletException, IOException {
		when(request.getMethod()).thenReturn("PUT");
		when(request.getParameter("name")).thenReturn("Jack");

		MvcDispatcher.dispatcher.apply(request, response);
		chain.doFilter(request, response);

		assertEquals("hi,Jack", writer.toString());
	}

	@Test
	public void testDelete() throws ServletException, IOException {
		when(request.getMethod()).thenReturn("DELETE");

		MvcDispatcher.dispatcher.apply(request, response);
		chain.doFilter(request, response);

		assertEquals("{\"name\":\"Jack\"}", writer.toString());
	}
}
