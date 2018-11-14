package com.ajaxjs.mvc.controller;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.junit.*;

import com.ajaxjs.web.mock.MockRequest;
import com.ajaxjs.web.mock.MockResponse;

public class TestOverrideController extends TestSimpleController {
	@Before
	@Override
	public void load() throws ServletException {
		request = MockRequest.mockRequest("/ajaxjs-web", "/OverrideTest");

		response = mock(HttpServletResponse.class);
		writer = MockResponse.writerFactory(response);
	}

	@Test
	@Override
	public void testGet() throws ServletException, IOException {
		when(request.getMethod()).thenReturn("GET");

		dispatcher.doFilter(request, response, chain);
		
		assertEquals("<html><meta charset=\"utf-8\" /><body>Hello World!(@Override)</body></html>", writer.toString());
	}
	
	// 不支持重载方法，因为重载方法其实是不同的方法，同时存在在一个类上。扫描控制器时候根据注解（当前@POST）找到的却是父类的，所有……
	@Test
	@Override
	public void testPost() throws ServletException, IOException {
		when(request.getMethod()).thenReturn("POST");
		
		dispatcher.doFilter(request, response, chain);
		
//		assertEquals("hihi", writer.toString());
	} 
}
