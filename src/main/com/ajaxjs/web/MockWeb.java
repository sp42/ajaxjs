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
package com.ajaxjs.web;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.ajaxjs.js.JsonHelper;
import com.ajaxjs.util.collection.MapHelper;

/**
 * 为方便单元测试，模拟请求对象。 Create a mock request object.
 * 
 * @author Sp42 frank@ajaxjs.com
 */
public class MockWeb {
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
	 * @param entry url 目录
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

	public static class DummyController extends HttpServlet {
		private static final long serialVersionUID = 1L;

		protected void doGet(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
			response.getWriter().append("Served at: ").append(request.getContextPath());
		}

		protected void doPost(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
			doGet(request, response);
		}
	}

	public static class DummyFilter implements Filter {
		public void destroy() {
		}

		public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
				throws IOException, ServletException {
			chain.doFilter(request, response);
		}

		public void init(FilterConfig fConfig) throws ServletException {
		}
	}

	/**
	 * 进行请求 在请求之前，你可以设定请求的参数
	 * 
	 * @param request  请求对象
	 * @param response 响应对象
	 * @return 响应对象 writer 字符串
	 */
	public static String doRequest(HttpServletRequest request, HttpServletResponse response) {
		DummyController controller = new DummyController();
		FilterChain filterChain = mock(FilterChain.class);
		DummyFilter filter = new DummyFilter();

		try {
			controller.init(initServletConfig(DummyController.class));

			filter.init(initFilterConfig(controller.getServletContext()));
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

	public static final Map<String, Object> hash = new HashMap<>();

	public static Answer<String> aswser = new Answer<String>() {
		public String answer(InvocationOnMock invocation) {
			Object[] args = invocation.getArguments();
			Object obj = hash.get(args[0].toString());

			return obj == null ? null : obj.toString();
		}
	};

	public static class StubServletOutputStream extends ServletOutputStream {
		private OutputStream os = new ByteArrayOutputStream();

		@Override
		public void write(int i) throws IOException {
			os.write(i);
		}

		public String getContent() {
			return os.toString();
		}
	}

	@Deprecated
	class StubServletOutputStream22 extends ServletInputStream {
		public ByteArrayOutputStream os = new ByteArrayOutputStream();

		public void write(int i) throws IOException {
			os.write(i);
		}

		public String getContent() {
			return os.toString();
		}

		@Override
		public int read() throws IOException {
			return 0;
		}
	}

	/**
	 * Writer:形象的比喻：当我们调用 response.getWriter()
	 * 这个对象同时获得了网页的画笔，这时你就可以通过这个画笔在网页上画任何你想要显示的东西。Writer 就是向页面输出信息，负责让客户端显示内容
	 * 
	 * @param response 响应对象
	 * @return writer 以便获取输出信息
	 */
	public static StringWriter writerFactory(HttpServletResponse response) {
		StringWriter writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);

		try {
			when(response.getWriter()).thenReturn(printWriter);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return writer;
	}

	/**
	 * 除了字符串使用 StringWriter，Response 输出的还可以是流
	 * 
	 * @param response 响应对象
	 * @return 流对象以便获取信息
	 */
	public static StubServletOutputStream streamFactory(HttpServletResponse response) {
		StubServletOutputStream os = new StubServletOutputStream();

		try {
			when(response.getOutputStream()).thenReturn(os);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return os;
	}

	/**
	 * 获取 MVC 跳转模版的那个路径
	 * 
	 * @param request 请求对象
	 * @return 模版路径
	 */
	public static String getRequestDispatcheResult(HttpServletRequest request) {
		ArgumentCaptor<String> dispatcherArgument = ArgumentCaptor.forClass(String.class);
		verify(request).getRequestDispatcher(dispatcherArgument.capture());

		return dispatcherArgument.getValue();
	}

	/**
	 * 初始化 Servlet 配置，这里是模拟 注解
	 * 
	 * @param cls 控制器类
	 * @return Servlet 配置
	 */
	public static ServletConfig initServletConfig(Class<? extends HttpServlet> cls) {
		ServletConfig servletConfig = mock(ServletConfig.class);

		// 模拟注解
		Vector<String> v = new Vector<>();

		// 通过反射获取注解内容

		if (cls != null) {
			WebServlet WebServlet_an = cls.getAnnotation(WebServlet.class);

			for (WebInitParam p : WebServlet_an.initParams()) {
				v.addElement(p.name());
				when(servletConfig.getInitParameter(p.name())).thenReturn(p.value());
			}
		}

		when(servletConfig.getInitParameterNames()).thenReturn(v.elements());

		return servletConfig;
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	public static FilterConfig initFilterConfig(ServletContext context) {
		FilterConfig filterConfig = mock(FilterConfig.class);
		// 模拟注解
		Vector<String> v = new Vector<>();
		v.addElement("urlPatterns");
		when(filterConfig.getInitParameterNames()).thenReturn(v.elements());
		when(filterConfig.getInitParameter("urlPatterns")).thenReturn("/service/*");
		when(filterConfig.getServletContext()).thenReturn(context);

		return filterConfig;
	}
}
