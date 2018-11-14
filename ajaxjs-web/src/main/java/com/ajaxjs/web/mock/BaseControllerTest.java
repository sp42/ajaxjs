package com.ajaxjs.web.mock;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.StringWriter;
import java.util.Enumeration;
import java.util.Vector;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ajaxjs.mvc.controller.MvcDispatcher;

/**
 * 方便测试的基础类
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public abstract class BaseControllerTest {
	public static FilterConfig filterConfig;
	public static MvcDispatcher dispatcher;
	public static FilterChain chain;

	// 单测技巧，每个 url 对应一个 request、一个 response
	public HttpServletRequest request;
	public HttpServletResponse response;
	public StringWriter writer;
	public ServletOutputStream os;

	/**
	 * 控制器的包名
	 * 
	 * @param packageName 包名
	 * @throws ServletException
	 */
	public static void init(String packageName) throws ServletException {
		filterConfig = mock(FilterConfig.class);

		// 模拟 web.xml
		Vector<String> v = new Vector<>();
		v.addElement("controller");
		when(filterConfig.getInitParameter("controller")).thenReturn(packageName);

		Enumeration<String> e = v.elements();
		when(filterConfig.getInitParameterNames()).thenReturn(e);

		dispatcher = new MvcDispatcher();
		dispatcher.init(filterConfig);

		chain = mock(FilterChain.class);
	}
}
