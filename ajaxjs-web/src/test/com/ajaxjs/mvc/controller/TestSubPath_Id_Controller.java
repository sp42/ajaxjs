package test.com.ajaxjs.mvc.controller;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.*;

import com.ajaxjs.web.mock.MockResponse;

public class TestSubPath_Id_Controller extends BaseTest {
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
		
		dispatcher.doFilter(request, response, chain);
		
		assertEquals("<html><meta charset=\"utf-8\" /><body>Hello World!</body></html>", writer.toString());
	}
	
	@Test
	public void testSubPathGet() throws ServletException, IOException {
		HttpServletRequest request;
		HttpServletResponse response;
		StringWriter writer;
		
		// 请求对象
		request = mock(HttpServletRequest.class);
		when(request.getContextPath()).thenReturn("/ajaxjs-web");
		when(request.getRequestURI()).thenReturn("/ajaxjs-web/MyTopPath_And_Sub_And_ID_Path/subPath");// 配置请求路径
		when(request.getMethod()).thenReturn("GET");
		when(request.getParameter("name")).thenReturn("Jack");
		
		// 响应对象
		response = mock(HttpServletResponse.class);
		writer = MockResponse.writerFactory(response);
		
		dispatcher.doFilter(request, response, chain);
		
		assertEquals("<html><meta charset=\"utf-8\" /><body>Hello Jack</body></html>", writer.toString());
	}
	
	@Before
	public void load() throws ServletException {
		// 请求对象
		request = mock(HttpServletRequest.class);
		when(request.getContextPath()).thenReturn("/ajaxjs-web");
		when(request.getRequestURI()).thenReturn("/ajaxjs-web/MyTopPath_And_Sub_And_ID_Path/123");// 配置请求路径
		when(request.getMethod()).thenReturn("GET");
		when(request.getParameter("name")).thenReturn("Jack");
		
		// 响应对象
		response = mock(HttpServletResponse.class);
		writer = MockResponse.writerFactory(response);
	}
	
	@Test
	public void testIdGet() throws ServletException, IOException {

		dispatcher.doFilter(request, response, chain);
		
		assertEquals("<html><meta charset=\"utf-8\" /><body>id: 123,Jack</body></html>", writer.toString());
	}

	@Test
	public void testPost() throws ServletException, IOException {
		when(request.getMethod()).thenReturn("POST");
		
		dispatcher.doFilter(request, response, chain);
		
		assertEquals("<html><meta charset=\"utf-8\" /><body>ID:123</body></html>", writer.toString());
	}
	
	@Test
	public void testPut() throws ServletException, IOException {
		when(request.getMethod()).thenReturn("PUT");

		dispatcher.doFilter(request, response, chain);
		
		assertEquals("ID:123", writer.toString());
	}

	@Test
	public void testDelete() throws ServletException, IOException {
		when(request.getMethod()).thenReturn("DELETE");

		dispatcher.doFilter(request, response, chain);
		assertEquals("{\"name\":\"Jack\"}", writer.toString());
	}

	@Test
	public void testSubPathIdGet() throws ServletException, IOException {
		HttpServletRequest request;
		HttpServletResponse response;
		StringWriter writer;
		
		// 请求对象
		request = mock(HttpServletRequest.class);
		when(request.getContextPath()).thenReturn("/ajaxjs-web");
		when(request.getRequestURI()).thenReturn("/ajaxjs-web/MyTopPath_And_Sub_And_ID_Path/subPath/123");// 配置请求路径
		when(request.getMethod()).thenReturn("GET");
		when(request.getParameter("name")).thenReturn("Jack");
		
		// 响应对象
		response = mock(HttpServletResponse.class);
		writer = MockResponse.writerFactory(response);
		
		dispatcher.doFilter(request, response, chain);
		
		assertEquals("<html><meta charset=\"utf-8\" /><body>id: 123,Jack</body></html>", writer.toString());
	}
}
