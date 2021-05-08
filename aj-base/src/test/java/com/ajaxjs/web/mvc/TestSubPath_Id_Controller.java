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
import com.ajaxjs.web.mock.MockRequest;
import com.ajaxjs.web.mock.MockResponse;

public class TestSubPath_Id_Controller extends BaseControllerTest {
	@Before
	public void load() {
		init("com.ajaxjs.mvc.controller.testcase");

		request = MockRequest.mockRequest("/ajaxjs-web", "/MyTopPath_And_Sub_And_ID_Path/123");
		when(request.getParameter("name")).thenReturn("Jack");

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
		when(request.getRequestURI()).thenReturn("/ajaxjs-web/MyTopPath_And_Sub_And_ID_Path");// 配置请求路径
		when(request.getMethod()).thenReturn("GET");

		// 响应对象
		response = mock(HttpServletResponse.class);
		writer = MockResponse.writerFactory(response);

		MvcDispatcherBase.DISPATCHER.apply(request, response);
		chain.doFilter(request, response);

		assertEquals("<html><meta charset=\"utf-8\" /><body>Hello World!</body></html>", writer.toString());
	}

	@Test
	public void testTopPath() throws ServletException, IOException {
		HttpServletRequest request = MockRequest.mockRequest("/ajaxjs-web", "/TopPath");
		when(request.getMethod()).thenReturn("GET");
		when(request.getParameter("name")).thenReturn("Jack");

		HttpServletResponse response = mock(HttpServletResponse.class);
		StringWriter writer = MockResponse.writerFactory(response);

		MvcDispatcherBase.DISPATCHER.apply(request, response);
		chain.doFilter(request, response);

		assertEquals("<html><meta charset=\"utf-8\" /><body>MyTopPath</body></html>", writer.toString());
	}

	@Test
	public void testSubPathGet() throws ServletException, IOException {
		HttpServletRequest request = MockRequest.mockRequest("/ajaxjs-web", "/MyTopPath_And_Sub_And_ID_Path/subPath");
		when(request.getMethod()).thenReturn("GET");
		when(request.getParameter("name")).thenReturn("Jack");

		HttpServletResponse response = mock(HttpServletResponse.class);
		StringWriter writer = MockResponse.writerFactory(response);

		MvcDispatcherBase.DISPATCHER.apply(request, response);
		chain.doFilter(request, response);

		assertEquals("<html><meta charset=\"utf-8\" /><body>Hello Jack</body></html>", writer.toString());
	}

	@Test
	public void testIdGet() throws ServletException, IOException {
		when(request.getMethod()).thenReturn("GET");

		MvcDispatcherBase.DISPATCHER.apply(request, response);
		chain.doFilter(request, response);

		assertEquals("<html><meta charset=\"utf-8\" /><body>showID: 123,Jack</body></html>", writer.toString());
	}

	@Test
	public void testPost() throws ServletException, IOException {
		when(request.getMethod()).thenReturn("POST");

		MvcDispatcherBase.DISPATCHER.apply(request, response);
		chain.doFilter(request, response);

		assertEquals("<html><meta charset=\"utf-8\" /><body>ID:123</body></html>", writer.toString());
	}

	@Test
	public void testPut() throws ServletException, IOException {
		when(request.getMethod()).thenReturn("PUT");

		MvcDispatcherBase.DISPATCHER.apply(request, response);
		chain.doFilter(request, response);

		assertEquals("ID:123", writer.toString());
	}

	@Test
	public void testDelete() throws ServletException, IOException {
		when(request.getMethod()).thenReturn("DELETE");

		MvcDispatcherBase.DISPATCHER.apply(request, response);
		chain.doFilter(request, response);

		assertEquals("{\"showJSON\":\"Jack\"}", writer.toString());
	}

	@Test
	public void testSubPathIdGet() throws ServletException, IOException {
		HttpServletRequest request = MockRequest.mockRequest("/ajaxjs-web", "/MyTopPath_And_Sub_And_ID_Path/subPath/123");
		when(request.getMethod()).thenReturn("GET");
		when(request.getParameter("name")).thenReturn("Jack");

		HttpServletResponse response = mock(HttpServletResponse.class);
		StringWriter writer = MockResponse.writerFactory(response);

		MvcDispatcherBase.DISPATCHER.apply(request, response);
		chain.doFilter(request, response);

		assertEquals("<html><meta charset=\"utf-8\" /><body>show_shuPath_ID: 123,Jack</body></html>", writer.toString());
	}
}
