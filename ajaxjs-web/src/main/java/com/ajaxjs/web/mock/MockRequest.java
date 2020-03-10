/**
 * Copyright sp42 frank@ajaxjs.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ajaxjs.web.mock;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.ajaxjs.mvc.controller.MvcDispatcher;
import com.ajaxjs.util.map.JsonHelper;
import com.ajaxjs.util.map.MapTool;

/**
 * 为方便单元测试，模拟请求对象
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class MockRequest extends HttpServletRequestWrapper {
	/**
	 * 
	 * @param request 请求对象
	 */
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
		parameter.forEach((k, v) -> when(getParameter(k)).thenReturn(v));

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
	 * 
	 * @return
	 */
	public String getRequestDispatcheResult() {
		return MockResponse.getRequestDispatcheResult(old);
	}

	/**
	 * 返回控制器输出的响应文本
	 * 
	 * @return
	 */
	public String getStringResult() {
		return testInstance.writer.toString();
	}

	/**
	 * 返回控制器输出的响应 JSON Map
	 * 
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
			public Object answer(InvocationOnMock in) throws Throwable {
				String key = (String) in.getArguments()[0];
				return map.get(key);
			}
		});

		doAnswer(new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock in) throws Throwable {
				String key = (String) in.getArguments()[0];
				Object value = in.getArguments()[1];
				map.put(key, value);

				return null;
			}
		}).when(session).setAttribute(anyString(), anyObject());
	}

	/**
	 * 模拟一个请求对象
	 * 
	 * @param contextPath 项目目录
	 * @param path        要模拟的后面的目录
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
	 * @param request       请求对象
	 * @param formBody      表单数据
	 * @param isByGetParams 是否通过 request.getParameter 返回值，而不是走表单流的方式
	 * @return 表单请求
	 * @throws IOException
	 */
	public static HttpServletRequest mockFormRequest(HttpServletRequest request, Map<String, String> formBody,
			boolean isByGetParams) throws IOException {
		if (isByGetParams) {
			formBody.forEach((k, v) -> when(request.getParameter(k)).thenReturn(v));
		} else {
			String form = MapTool.join(formBody, "&");
			when(request.getInputStream()).thenReturn(new MockServletInputStream(form.getBytes()));
		}

		return request;
	}
}
