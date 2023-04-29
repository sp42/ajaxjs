package com.ajaxjs.web.mvc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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

public class TestComboController extends BaseControllerTest {
	@Before
	public void init() {
		init("com.ajaxjs.mvc.controller.testcase");
		request = MockRequest.mockRequest("/ajaxjs-web", "/combo");
		response = mock(HttpServletResponse.class);
		writer = MockResponse.writerFactory(response);
	}

	@Test
	public void testGet_main() throws ServletException, IOException {
		when(request.getMethod()).thenReturn("GET");

		MvcDispatcherBase.dispatcher.apply(request, response);
		chain.doFilter(request, response);

		assertEquals("hihi", writer.toString());
	}

	@Test
	public void testPost() throws ServletException, IOException {
		when(request.getMethod()).thenReturn("POST");

		MvcDispatcherBase.dispatcher.apply(request, response);
		chain.doFilter(request, response);
	}

	@Test
	public void testPut() throws ServletException, IOException {
		when(request.getMethod()).thenReturn("PUT");
		os = MockResponse.streamFactory(response);

		// dispatcher.doFilter(request, response, chain);
		assertNotNull("servletOutputStream.getContent：" + os.toString());
		assertNotNull(writer.toString());
	}

	@Test
	public void testDelete() throws ServletException, IOException {
		when(request.getMethod()).thenReturn("DELETE");

		MvcDispatcherBase.dispatcher.apply(request, response);
		chain.doFilter(request, response);

		assertEquals("<html><meta charset=\"utf-8\" /><body>Hello World!</body></html>", writer.toString());
	}

	@Test
	public void testGet_MVC_goto_jsp() throws ServletException, IOException {
		HttpServletRequest request = MockRequest.mockRequest("/ajaxjs-web", "/combo/mvc");
		when(request.getMethod()).thenReturn("GET");

		MvcDispatcherBase.dispatcher.apply(request, response);
		chain.doFilter(request, response);

		assertEquals("index.jsp", MockResponse.getRequestDispatcheResult(request));
	}

	HttpServletRequest request2;
	HttpServletResponse response2;
	StringWriter writer2;

	@Before
	public void init3() throws ServletException {
		request2 = MockRequest.mockRequest("/ajaxjs-web", "/combo/person");

		// 响应对象
		response2 = mock(HttpServletResponse.class);
		writer2 = MockResponse.writerFactory(response2);
	}

	@Test
	public void testGet_Person() throws ServletException, IOException {
		when(request2.getMethod()).thenReturn("GET");

		MvcDispatcherBase.dispatcher.apply(request, response);
		chain.doFilter(request, response);

		assertEquals("just person", writer2.toString());
	}

	// @Test
	public void testPost_Person() throws ServletException, IOException {
		when(request2.getMethod()).thenReturn("POST");
		when(request2.getParameter("name")).thenReturn("Jack");

		MvcDispatcherBase.dispatcher.apply(request, response);
		chain.doFilter(request, response);

		assertEquals("{\"name\":\"Jack\"}", writer2.toString());
	}

	@Test
	public void testGet_Person_ID() throws ServletException, IOException {
		HttpServletRequest request = MockRequest.mockRequest("/ajaxjs-web", "/combo/person/88");

		// 响应对象
		HttpServletResponse response = mock(HttpServletResponse.class);
		StringWriter writer = MockResponse.writerFactory(response);

		when(request.getMethod()).thenReturn("GET");
		when(request.getParameter("name")).thenReturn("Jack");
		when(request.getParameter("word")).thenReturn("Love");

		MvcDispatcherBase.dispatcher.apply(request, response);
		chain.doFilter(request, response);

		assertEquals("Jack Love_88", writer.toString());
	}

	@Test
	public void testQueryString() throws ServletException, IOException {
		when(request2.getMethod()).thenReturn("POST");
		when(request2.getParameter("name")).thenReturn("Jack");
		when(request2.getParameter("age")).thenReturn("28");

		MvcDispatcherBase.dispatcher.apply(request, response);
		chain.doFilter(request, response);

		assertEquals("{\"name\":\"Jack\"}", writer2.toString());
	}

	@Test
	public void testDefaultValue() throws ServletException, IOException {
		HttpServletRequest request = MockRequest.mockRequest("/ajaxjs-web", "/combo/testDefault");

		// 响应对象
		HttpServletResponse response = mock(HttpServletResponse.class);
		StringWriter writer = MockResponse.writerFactory(response);

		when(request.getMethod()).thenReturn("POST");
		when(request.getParameter("name")).thenReturn("Jack");
		when(request.getParameter("age")).thenReturn("28");

		MvcDispatcherBase.dispatcher.apply(request, response);
		chain.doFilter(request, response);

		assertEquals("{\"name\":\"Jack\"}", writer.toString());
	}
}