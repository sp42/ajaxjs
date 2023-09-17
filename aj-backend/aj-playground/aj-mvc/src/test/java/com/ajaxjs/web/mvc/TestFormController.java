package com.ajaxjs.web.mvc;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;

import com.ajaxjs.web.mock.BaseControllerTest;
import com.ajaxjs.web.mock.MockRequest;
import com.ajaxjs.web.mock.MockResponse;

public class TestFormController extends BaseControllerTest {
	// 单测技巧，每个 url 对应一个 request、一个 response
	@Before
	public void load() {
		init("com.ajaxjs.mvc.controller.testcase");
		request = MockRequest.mockRequest("/ajaxjs-web", "/form");
		response = mock(HttpServletResponse.class);
		writer = MockResponse.writerFactory(response);
	}

	@Test
	public void testPost() throws ServletException, IOException {
		when(request.getMethod()).thenReturn("POST");
		when(request.getParameter("username")).thenReturn("Jack");

		MvcDispatcherBase.dispatcher.apply(request, response);
		chain.doFilter(request, response);

		assertEquals("<html><meta charset=\"utf-8\" /><body>Jack</body></html>", writer.toString());
	}

	static Map<String, String[]> map = new HashMap<>();

	@Test
	public void testMap() throws ServletException, IOException {
		HttpServletRequest request = MockRequest.mockRequest("/ajaxjs-web", "/form/map");
		HttpServletResponse response = mock(HttpServletResponse.class);
		StringWriter writer = MockResponse.writerFactory(response);

		map.put("username", new String[] { "Jack" });
		map.put("age", new String[] { "28" });
		when(request.getParameterMap()).thenReturn(map);

		when(request.getMethod()).thenReturn("POST");
		when(request.getParameter("username")).thenReturn("Jack");
		when(request.getParameter("age")).thenReturn("28");

		MvcDispatcherBase.dispatcher.apply(request, response);
		chain.doFilter(request, response);

		assertEquals("<html><meta charset=\"utf-8\" /><body>Jack</body></html>", writer.toString());
	}

	@Test
	public void testBean() throws ServletException, IOException {
		HttpServletRequest request = MockRequest.mockRequest("/ajaxjs-web", "/form/bean");
		HttpServletResponse response = mock(HttpServletResponse.class);
		StringWriter writer = MockResponse.writerFactory(response);

		when(request.getMethod()).thenReturn("POST");
		map.put("name", new String[] { "Jack" });
		when(request.getParameterMap()).thenReturn(map);

		MvcDispatcherBase.dispatcher.apply(request, response);
		chain.doFilter(request, response);

		assertEquals("<html><meta charset=\"utf-8\" /><body>Jack</body></html>", writer.toString());
	}

}
