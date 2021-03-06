package com.ajaxjs.web.mock;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.ajaxjs.js.JsonHelper;
import com.ajaxjs.keyvalue.MapHelper;
import com.ajaxjs.mvc.controller.MvcDispatcher;

/**
 * 为方便单元测试，模拟请求对象
 * 
 * @author Frank Cheung
 *
 */
public class MockRequest extends HttpServletRequestWrapper {

	public MockRequest(HttpServletRequest request) {
		super(request);
	}

	public BaseControllerTest testInstance;

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
		testInstance.writer = MockResponse.writerFactory(testInstance.response);

		this.testInstance = testInstance;
	}

	public MockRequest setSession(Map<String, Object> sessionMap) {
		MockRequest.mockSession(this, sessionMap);
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
		return MockResponse.getRequestDispatcheResult(old);
	}
	
	/**
	 * 返回控制器输出的响应文本
	 * @return
	 */
	public String getStringResult() {
		return testInstance.writer.toString();
	}
	
	/**
	 * 返回控制器输出的响应 JSON Map
	 * @return
	 */
	public Map<String, Object> getStringResultJson() {
		return JsonHelper.parseMap(getStringResult());
	}

	public MockRequest setRequestAttribute(String... keys) {
		MockRequest.mockRequestAttribute(old, keys);
		return this;
	}

	/**
	 * 模拟请求对象的 attribute 属性
	 * 
	 * @param request 请求假对象
	 * @param keys
	 */
	public static void mockRequestAttribute(HttpServletRequest request, String... keys) {
		final Map<String, Object> hash = new HashMap<>();
	
		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				String key = invocation.getArgumentAt(0, String.class);
				Object value = invocation.getArgumentAt(1, Object.class);
				hash.put(key, value);
	
				return null;
			}
		}).when(request).setAttribute(anyString(), anyObject());
	
		Answer<Object> aswser = new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock invocation) {
				Object[] args = invocation.getArguments();
				Object obj = hash.get(args[0].toString());
	
				return obj;
			}
		};
	
		for (String key : keys) {
			doAnswer(aswser).when(request).getAttribute(key);
		}
	
		// doThrow(new Exception()).when(request).setAttribute(anyString(),
		// anyString());
	}

	/**
	 * 模拟 HttpSession
	 * 
	 * @param request
	 * @param map
	 */
	public static void mockSession(HttpServletRequest request, final Map<String, Object> map) {
		HttpSession session = mock(HttpSession.class);
		when(request.getSession()).thenReturn(session);
	
		when(session.getAttribute(anyString())).thenAnswer(new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock aInvocation) throws Throwable {
				String key = (String) aInvocation.getArguments()[0];
				return map.get(key);
			}
		});
	
		doAnswer(new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock aInvocation) throws Throwable {
				String key = (String) aInvocation.getArguments()[0];
				Object value = aInvocation.getArguments()[1];
				map.put(key, value);
				return null;
			}
		}).when(session).setAttribute(anyString(), anyObject());
	}

	/**
	 * 模拟一个请求对象
	 * 
	 * @param contextPath 项目目录
	 * @param path 要模拟的后面的目录
	 * @return 请求对象
	 */
	public static HttpServletRequest mockRequest(String contextPath, String path) {
		HttpServletRequest request = mock(HttpServletRequest.class);
	
		if (!path.startsWith("/"))
			path = "/" + path;
	
		when(request.getPathInfo()).thenReturn(contextPath + path);
		when(request.getRequestURI()).thenReturn(contextPath + path);
		when(request.getContextPath()).thenReturn(contextPath);
	
		return request;
	}
	
	/**
	 * 模拟表单请求
	 * 
	 * @param request 请求对象
	 * @param formBody 表单数据
	 * @param isByGetParams 是否通过 request.getParameter 返回值，而不是走表单流的方式
	 * @return 表单请求
	 * @throws IOException
	 */
	public static HttpServletRequest mockFormRequest(HttpServletRequest request, Map<String, String> formBody, boolean isByGetParams) throws IOException {
		if (isByGetParams) {
			for (String key : formBody.keySet())
				when(request.getParameter(key)).thenReturn(formBody.get(key));
		} else {
			String form = MapHelper.join(formBody, "&");
			final InputStream is = new ByteArrayInputStream(form.getBytes());

			when(request.getInputStream()).thenReturn(new ServletInputStream() {
				@Override
				public int read() throws IOException {
					return is.read();
				}

				
				public boolean isFinished() {
					return false;
				}

				public boolean isReady() {
					return false;
				}
				public void setReadListener(ReadListener arg0) {
				}
			});
		}

		return request;
	}
}
