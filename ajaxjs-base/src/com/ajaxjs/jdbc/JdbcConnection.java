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
package com.ajaxjs.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.ajaxjs.util.LogHelper;

/**
 * 连接数据库
 * @author xinzhang
 *
 */
public class JdbcConnection {
	private static final LogHelper LOGGER = LogHelper.getLog(JdbcConnection.class);
	
	/**
	 * 获取数据源 
	 * 
	 * @param path
	 *            参阅 META-INF/context.xml
	 * @return 数据源对象
	 */
	public static DataSource getDataSource(String path) {
		try {
			Context context = (Context) new InitialContext().lookup("java:/comp/env");// 环境变量
			return (DataSource) context.lookup(path);
		} catch (NamingException e) {
			LOGGER.warning("读取数据源的配置文件失败，请检查 Tomcat 连接池配置！ path:" + path, e);
			return null;
		} 
	}
	
	/**
	 * 通过数据源对象获得数据库连接对象
	 * 
	 * @param source
	 *            数据源对象
	 * @return 数据库连接对象
	 */
	public static Connection getConnection(DataSource source) {
		try {
			return source.getConnection();
		} catch (SQLException e) {
			LOGGER.warning("通过数据源对象获得数据库连接对象失败！", e);
			return null;
		}
	}

	/**
	 * 连接数据库
	 * 
	 * @param jdbcUrl
	 *            链接字符串
	 * @return 数据库连接对象
	 */
	public static Connection getConnection(String jdbcUrl, Properties props) {
		Connection conn = null;

		try {
			if (props == null)
				conn = DriverManager.getConnection(jdbcUrl);
			else
				conn = DriverManager.getConnection(jdbcUrl, props);
			LOGGER.info("数据库连接成功： " + conn.getMetaData().getURL());
		} catch (SQLException e) {
			LOGGER.warning("数据库连接失败！", e);
		}
		return conn;
	}
	
	/**
	 * 连接数据库
	 * 
	 * @param jdbcUrl
	 *            链接字符串
	 * @return 数据库连接对象
	 */
	public static Connection getConnection(String jdbcUrl) {
		return getConnection(jdbcUrl, null);
	}
}
