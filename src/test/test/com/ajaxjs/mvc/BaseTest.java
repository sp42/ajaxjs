package test.com.ajaxjs.mvc;

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
import com.ajaxjs.web.test.MockResponse.StubServletOutputStream;

public abstract class BaseTest {
	static FilterConfig filterConfig;
	static MvcDispatcher dispatcher;
	static FilterChain chain;

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
	HttpServletRequest request;
	HttpServletResponse response;
	StringWriter writer;
	StubServletOutputStream os;
}
