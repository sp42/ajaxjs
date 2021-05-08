package com.ajaxjs.web.mvc;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.mockito.Mock;

import com.ajaxjs.util.ioc.ComponentMgr;
import com.ajaxjs.web.mock.MockResponse;

public class TestController {
	@Mock
	private RequestDispatcher dispatcher;

	@Test
	public void testNews() throws ServletException, IOException {
		ComponentMgr.clzs.add(NewsController.class);
		MvcDispatcherBase.init(null);

		FilterChain chain = mock(FilterChain.class);

		// 请求对象
		HttpServletRequest req = mock(HttpServletRequest.class);
		when(req.getRequestDispatcher(anyString())).thenReturn(dispatcher);
		when(req.getContextPath()).thenReturn("/ajaxjs-web");
		when(req.getRequestURI()).thenReturn("/ajaxjs-web/news/");// 配置请求路径
		when(req.getParameter("start")).thenReturn("0");
		when(req.getParameter("limit")).thenReturn("10");

		// 响应对象
		HttpServletResponse resp = mock(HttpServletResponse.class);
		StringWriter writer = MockResponse.writerFactory(resp);

		// GET List
		when(req.getMethod()).thenReturn("GET");

		MvcDispatcherBase.DISPATCHER.apply(req, resp);
		chain.doFilter(req, resp);
		assertEquals("<html><meta charset=\"utf-8\" /><body>hihi</body></html>", writer.toString());

		// GET Info
		when(req.getRequestURI()).thenReturn("/ajaxjs-web/news/12");// 配置请求路径
		MvcDispatcherBase.DISPATCHER.apply(req, resp);
		chain.doFilter(req, resp);

		assertEquals("home.jsp", MockResponse.getRequestDispatcheResult(req));
	}
}
