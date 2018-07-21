package test.com.ajaxjs.mvc.controller;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.Vector;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.*;
import org.mockito.Mock;

import com.ajaxjs.mvc.controller.MvcDispatcher;
import com.ajaxjs.web.MockWeb;

import static org.mockito.Matchers.anyString;

public class TestController {
	@Mock
    private RequestDispatcher _dispatcher;
 
	
	@Test
	public void testNews() throws ServletException, IOException {
		FilterConfig filterConfig = mock(FilterConfig.class);

		// 模拟 web.xml
		Vector<String> v = new Vector<>();
		v.addElement("controller");
		when(filterConfig.getInitParameter("controller")).thenReturn("com.ajaxjs.framework.controller.NewsController");

		Enumeration<String> e = v.elements();
		when(filterConfig.getInitParameterNames()).thenReturn(e);

		MvcDispatcher dispatcher = new MvcDispatcher();
		dispatcher.init(filterConfig);

		FilterChain chain = mock(FilterChain.class);

		// 请求对象
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getRequestDispatcher(anyString())).thenReturn(_dispatcher);
		when(request.getContextPath()).thenReturn("/ajaxjs-web");
		when(request.getRequestURI()).thenReturn("/ajaxjs-web/news/list/34");// 配置请求路径
		when(request.getParameter("start")).thenReturn("0");
		when(request.getParameter("limit")).thenReturn("10");

		// 响应对象
		HttpServletResponse response = mock(HttpServletResponse.class);
		StringWriter writer;
//		StubResponseResult os;

		// GET List
		when(request.getMethod()).thenReturn("GET");
		writer = MockWeb.writerFactory(response);
		
		dispatcher.doFilter(request, response, chain);
		assertEquals(writer.toString(), "hihi");
		
		// GET Info
		when(request.getRequestURI()).thenReturn("/ajaxjs-web/news/12");// 配置请求路径
		dispatcher.doFilter(request, response, chain);
		
		
        assertEquals("home.jsp", MockWeb.getRequestDispatcheResult(request));
	}
}
