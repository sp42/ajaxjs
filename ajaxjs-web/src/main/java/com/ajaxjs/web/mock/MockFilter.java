/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by
 * applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS
 * OF ANY KIND, either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 */
package com.ajaxjs.web.mock;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Vector;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 为方便单元测试，模拟过滤器对象。
 * 
 * @author sp42 frank@ajaxjs.com
 */
public class MockFilter {
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
	 * @param request 请求对象
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
