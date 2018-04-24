/**
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
package com.ajaxjs.web.test;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.ajaxjs.js.JsonHelper;
import com.ajaxjs.util.collection.MapHelper;

/**
 * Create a mock request object.
 * 
 * @author Sp42 frank@ajaxjs.com
 */
public class MockRequest {
	/**
	 * 模拟一个请求对象
	 * 
	 * @param contextPath
	 *            项目目录
	 * @param path
	 *            要模拟的后面的目录
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
	 * @param request
	 *            请求对象
	 * @param formBody
	 *            表单数据
	 * @param isByGetParams
	 *            是否通过 request.getParameter 返回值，而不是走表单流的方式
	 * @return 表单请求
	 * @throws IOException
	 */
	public HttpServletRequest initRequest(HttpServletRequest request, Map<String, String> formBody, boolean isByGetParams) throws IOException {
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
			});
		}

		return request;
	}

	/**
	 * 初始化请求对象
	 * 
	 * @param entry
	 *            url 目录
	 * @return 请求对象
	 */
	public HttpServletRequest initRequest(String entry) {
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getPathInfo()).thenReturn("/new_test/service/" + entry);
		when(request.getRequestURI()).thenReturn("/new_test/service/" + entry);
		when(request.getContextPath()).thenReturn("/new_test");

		when(request.getMethod()).thenReturn("GET");
		// 设置参数
		// when(request.getParameter("a")).thenReturn("aaa");

		final Map<String, Object> hash = new HashMap<>();
		Answer<String> aswser = new Answer<String>() {
			public String answer(InvocationOnMock invocation) {
				Object[] args = invocation.getArguments();
				Object obj = hash.get(args[0].toString());

				return obj == null ? null : obj.toString();
			}
		};

		when(request.getAttribute("isRawOutput")).thenReturn(true);
		when(request.getAttribute("errMsg")).thenAnswer(aswser);
		when(request.getAttribute("output")).thenAnswer(aswser);
		when(request.getAttribute("msg")).thenAnswer(aswser);
		// doThrow(new Exception()).when(request).setAttribute(anyString(),
		// anyString());

		doAnswer(new Answer<Object>() {
			public Object answer(InvocationOnMock invocation) {
				Object[] args = invocation.getArguments();
				// Object mock = invocation.getMock();
				// 测试终端的模拟器接收到数据
				hash.put(args[0].toString(), args[1]);

				return "called with arguments: " + args;
			}
		}).when(request).setAttribute(anyString(), anyString());

		return request;
	}

	/**
	 * 进行请求 在请求之前，你可以设定请求的参数
	 * 
	 * @param request
	 *            请求对象
	 * @param response
	 *            响应对象
	 * @return 响应对象 writer 字符串
	 */
	public static String doRequest(HttpServletRequest request, HttpServletResponse response) {
		DummyController controller = new DummyController();
		FilterChain filterChain = mock(FilterChain.class);
		DummyFilter filter = new DummyFilter();

		try {
			controller.init(WebBaseInit.initServletConfig(DummyController.class));

			filter.init(WebBaseInit.initFilterConfig(controller.getServletContext()));
			filter.doFilter(request, response, filterChain);

			controller.doGet(request, response);

			return response.getWriter().toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Map<String, Object> shouldbe_json_return(String js_code) {
		return JsonHelper.parseMap(js_code);
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object>[] shouldbe_jsonArray_return(String js_code) {
		List<Map<String, Object>> list = JsonHelper.parseList(js_code);
		return list.toArray(new Map[list.size()]);
	}

	public boolean shouldbe_hasRecord(Map<String, Object> json) {
		int total = (int) json.get("total");
		return total > 0;
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

		Mockito.doAnswer(new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock aInvocation) throws Throwable {
				String key = (String) aInvocation.getArguments()[0];
				Object value = aInvocation.getArguments()[1];
				map.put(key, value);
				return null;
			}
		}).when(session).setAttribute(anyString(), anyObject());
	}
}
