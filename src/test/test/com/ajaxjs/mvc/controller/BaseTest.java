package test.com.ajaxjs.mvc.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.StringWriter;
import java.util.Enumeration;
import java.util.Vector;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.BeforeClass;

import com.ajaxjs.mvc.controller.MvcDispatcher;
import com.ajaxjs.web.mock.MockResponse;
import com.ajaxjs.web.mock.MockResponse.StubServletOutputStream;

/**
 * 方便测试的基础类
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public abstract class BaseTest {
	public static FilterConfig filterConfig;
	public static MvcDispatcher dispatcher;
	public static FilterChain chain;

	@BeforeClass
	public static void init() throws ServletException {
		filterConfig = mock(FilterConfig.class);

		// 模拟 web.xml
		Vector<String> v = new Vector<>();
		v.addElement("controller");
		when(filterConfig.getInitParameter("controller")).thenReturn("test.com.ajaxjs.mvc.controller");

		Enumeration<String> e = v.elements();
		when(filterConfig.getInitParameterNames()).thenReturn(e);

		dispatcher = new MvcDispatcher();
		dispatcher.init(filterConfig);

		chain = mock(FilterChain.class);
	}

	// 单测技巧，每个 url 对应一个 request、一个 response
	public HttpServletRequest request;
	public HttpServletResponse response;
	public StringWriter writer;
	public MockResponse.StubServletOutputStream os;
}
