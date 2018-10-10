package com.ajaxjs.web.mock;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import com.ajaxjs.js.JsonHelper;
import com.ajaxjs.mvc.controller.MvcDispatcher;
import com.ajaxjs.web.BaseControllerTest;
import com.ajaxjs.web.MockWeb;

public class MockRequest extends HttpServletRequestWrapper {

	public MockRequest(HttpServletRequest request) {
		super(request);
	}

	BaseControllerTest testInstance;

	public HttpServletRequest old;

	public MockRequest(BaseControllerTest testInstance, String contextPath, String path) {
		this(mock(HttpServletRequest.class), testInstance, contextPath, path);
	}

	public MockRequest(HttpServletRequest request, BaseControllerTest testInstance, String contextPath, String path) {
		super(request);

		old = request;

		when(getPathInfo()).thenReturn(contextPath + path);
		when(getRequestURI()).thenReturn(contextPath + path);
		when(getContextPath()).thenReturn(contextPath);

		testInstance.response = mock(HttpServletResponse.class);
		testInstance.writer = MockWeb.writerFactory(testInstance.response);

		this.testInstance = testInstance;
	}

	public MockRequest setSession(Map<String, Object> sessionMap) {
		MockWeb.mockSession(this, sessionMap);
		return this;
	}

	public MockRequest setMethod(String string) {
		when(getMethod()).thenReturn(string);
		return this;
	}

	public MockRequest setHeader(String header, String value) {
		when(getHeader(header)).thenReturn(value);
		return this;
	}

	public MockRequest setParameterMap(Map<String, String[]> parameterMap) {
		when(getParameterMap()).thenReturn(parameterMap);
		return this;
	}

	public MockRequest setParameter(Map<String, String> parameter) {
		for (String key : parameter.keySet()) {
			when(getParameter(key)).thenReturn(parameter.get(key));
		}
		return this;
	}

	public MockRequest setParameter(String key, String value) {
		when(getParameter(key)).thenReturn(value);
		return this;
	}

	public void sendTest(MvcDispatcher dispatcher, FilterChain chain) {
		try {
			dispatcher.doFilter(this, testInstance.response, chain);
		} catch (IOException | ServletException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 返回 MVC 的 JSP 跳转
	 * @return
	 */
	public String getRequestDispatcheResult() {
		return MockWeb.getRequestDispatcheResult(old);
	}
	
	/**
	 * 返回控制器输出的响应文本
	 * @return
	 */
	public String getStringResult() {
		return testInstance.writer.toString();
	}
	
	/**
	 * 返回控制器输出的响应文本
	 * @return
	 */
	public Map<String, Object> getStringResultJson() {
		return JsonHelper.parseMap(getStringResult());
	}
}
