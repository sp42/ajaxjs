package test.com.ajaxjs.mvc.controller;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.*;

import com.ajaxjs.web.test.MockResponse;

public class TestSimpleController extends BaseTest {
	// 单测技巧，每个 url 对应一个 request、一个 response
	@Before
	public void load() throws ServletException {
		// 请求对象
		request = mock(HttpServletRequest.class);
		when(request.getContextPath()).thenReturn("/ajaxjs-web");
		when(request.getRequestURI()).thenReturn("/ajaxjs-web/simple");// 配置请求路径

		// 响应对象
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
		
		System.out.println("servletOutputStream.getContent：" + os.getContent());
		System.out.println(writer.toString());
	}

	@Test
	public void testDelete() throws ServletException, IOException {
		when(request.getMethod()).thenReturn("DELETE");

		dispatcher.doFilter(request, response, chain);
		assertEquals("{\"name\":\"Jack\"}", writer.toString());
	}
}
