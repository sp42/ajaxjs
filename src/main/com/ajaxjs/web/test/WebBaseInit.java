/**
 * Copyright 2015 Frank Cheung
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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Enumeration;
import java.util.Vector;

import javax.servlet.FilterConfig;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;


public class WebBaseInit {
	/**
	 * 初始化 Servlet 配置，这里是模拟 注解
	 * 
	 * @param cls
	 *            控制器类
	 * @return  Servlet 配置
	 */
	public static ServletConfig initServletConfig(Class<? extends HttpServlet> cls) {
		ServletConfig servletConfig = mock(ServletConfig.class);
		// 模拟注解
		Vector<String> v = new Vector<String>();
		// v.addElement("news");
		// when(servletConfig.getInitParameter("news")).thenReturn("ajaxjs.data.service.News");
		// v.addElement("img");
		// when(servletConfig.getInitParameter("img")).thenReturn("ajaxjs.data.service.subObject.Img");

		// 通过反射获取注解内容

		if (cls != null) {
			WebServlet WebServlet_an = cls.getAnnotation(WebServlet.class);

			for (WebInitParam p : WebServlet_an.initParams()) {
				v.addElement(p.name());
				when(servletConfig.getInitParameter(p.name())).thenReturn(p.value());
			}
		}

		Enumeration<String> e = v.elements();
		when(servletConfig.getInitParameterNames()).thenReturn(e);

		return servletConfig;
	}

	public static FilterConfig initFilterConfig(ServletContext context) {
		FilterConfig filterConfig = mock(FilterConfig.class);
		when(filterConfig.getServletContext()).thenReturn(context);

		// 模拟注解
		Vector<String> v = new Vector<>();
		v.addElement("urlPatterns");
		when(filterConfig.getInitParameter("urlPatterns")).thenReturn("/service/*");

		Enumeration<String> e = v.elements();
		when(filterConfig.getInitParameterNames()).thenReturn(e);

		return filterConfig;
	}
}
