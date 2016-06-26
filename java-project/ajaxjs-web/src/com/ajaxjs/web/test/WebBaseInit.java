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

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.FilterConfig;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;

import org.sqlite.SQLiteDataSource;
import org.sqlite.SQLiteJDBCLoader;

// import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class WebBaseInit {
	/**
	 * 模拟数据库 链接 的配置 需要加入tomcat-juli.jar这个包，tomcat7此包位于tomcat根目录的bin下。
	 * 
	 * @throws NamingException
	 */
	public static void initDBConnection() throws NamingException {
		// Create initial context
		System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.naming.java.javaURLContextFactory");
		System.setProperty(Context.URL_PKG_PREFIXES, "org.apache.naming");
		// 需要加入tomcat-juli.jar这个包，tomcat7此包位于tomcat根目录的bin下。
		InitialContext ic = new InitialContext();
		ic.createSubcontext("java:");
		ic.createSubcontext("java:/comp");
		ic.createSubcontext("java:/comp/env");
		ic.createSubcontext("java:/comp/env/jdbc");
		// Construct DataSource
		try {
			SQLiteJDBCLoader.initialize();
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		SQLiteDataSource dataSource = new SQLiteDataSource();
		dataSource.setUrl("jdbc:sqlite:F:\\project\\bigfoot\\sql\\foo.sqlite");

		ic.bind("java:/comp/env/jdbc/sqlite", dataSource);
	}

	public static void initDBConnection_Mysql(String MYSQL_DB_URL, String MYSQL_DB_USERNAME, String MYSQL_DB_PASSWORD)
			throws NamingException {
		// Create initial context
		System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.naming.java.javaURLContextFactory");
		System.setProperty(Context.URL_PKG_PREFIXES, "org.apache.naming");
		// 需要加入tomcat-juli.jar这个包，tomcat7此包位于tomcat根目录的bin下。
		InitialContext ic = new InitialContext();
		ic.createSubcontext("java:");
		ic.createSubcontext("java:/comp");
		ic.createSubcontext("java:/comp/env");
		ic.createSubcontext("java:/comp/env/jdbc");
		// Construct DataSource
//		MysqlDataSource dataSource = null;

//		try {
//			dataSource = new MysqlDataSource();
//			dataSource.setURL(MYSQL_DB_URL);
//			dataSource.setUser(MYSQL_DB_USERNAME);
//			dataSource.setPassword(MYSQL_DB_PASSWORD);
//		} catch (Exception e1) {
//			e1.printStackTrace();
//		}
//
//		ic.bind("java:/comp/env/jdbc/mysql_deploy", dataSource);
	}

	public Class<?> getBusinessServletClass() {
		return null;
	}

	/**
	 * 初始化 Servlet 配置，这里是模拟 注解
	 * 
	 * @return
	 */
	protected ServletConfig initServletConfig() {
		ServletConfig servletConfig = mock(ServletConfig.class);
		// 模拟注解
		Vector<String> v = new Vector<String>();
		// v.addElement("news");
		// when(servletConfig.getInitParameter("news")).thenReturn("ajaxjs.data.service.News");
		// v.addElement("img");
		// when(servletConfig.getInitParameter("img")).thenReturn("ajaxjs.data.service.subObject.Img");
		// v.addElement("catalog");
		// when(servletConfig.getInitParameter("catalog")).thenReturn("zjtv.SectionService");
		// v.addElement("user");
		// when(servletConfig.getInitParameter("user")).thenReturn("ajaxjs.data.user.UserService");
		// v.addElement("compere");
		// when(servletConfig.getInitParameter("compere")).thenReturn("zjtv.CompereService");

		// 通过反射获取注解内容
		Class<?> cls = getBusinessServletClass();

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

	protected FilterConfig initFilterConfig(ServletContext context) {
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
