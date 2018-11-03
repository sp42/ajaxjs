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
package com.ajaxjs.orm.dao;

import com.ajaxjs.jdbc.JdbcConnection;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.sqlite.SQLiteDataSource;
import org.sqlite.SQLiteJDBCLoader;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;

/**
 * 为方便测试提供的假对象 Mock data source object.
 *
 * @author Sp42 frank@ajaxjs.com
 *
 */
public class MockDataSource {

	/**
	 * 根据 JDBC Url 创建 SQLite 数据源对象
	 *
	 * @param url 以 jdbc:sqlite: 开头
	 * @return 数据源对象
	 */
	public static DataSource getSqliteDataSource(String url) {
		try {
			SQLiteJDBCLoader.initialize();
		} catch (Exception e) {
			e.printStackTrace();
		}

		SQLiteDataSource dataSource = new SQLiteDataSource();
		dataSource.setUrl(url);

		return dataSource;
	}

	/**
	 * 根据 JDBC Url 创建 MySQL 数据源对象
	 *
	 * @param url 像 "jdbc:mysql://localhost:3306/databaseName"
	 * @param user 用户名
	 * @param password 密码
	 * @return 数据源对象
	 */
	public static DataSource getMySqlDataSource(String url, String user, String password) {
		MysqlDataSource dataSource = new MysqlDataSource();
		dataSource.setURL(url);
		dataSource.setUser(user);
		dataSource.setPassword(password);
		dataSource.setUseUnicode(true);
		dataSource.setCharacterEncoding("UTF-8");

		return dataSource;
	}

	/**
	 * 根据 JDBC Url 创建 SQLite 数据库连接对象
	 *
	 * @param url 以 jdbc:sqlite: 开头
	 * @return 数据库连接对象
	 */
	public static Connection getTestSqliteConnection(String url) {
		return JdbcConnection.getConnection(getSqliteDataSource(url));
	}

	/**
	 * 根据 JDBC Url 创建 MySQL 数据库连接对象
	 *
	 * @param url 像 "jdbc:mysql://localhost:3306/databaseName"
	 * @param user 用户名
	 * @param password 密码
	 * @return 数据库连接对象
	 */
	public static Connection getTestMySqlConnection(String url, String user, String password) {
		return JdbcConnection.getConnection(getMySqlDataSource(url, user, password));
	}

	/**
	 * JDNI 是一个为 Java 应用程序提供命名服务的应用程序接口
	 * https://www.cnblogs.com/zhchoutai/p/7389089.html 模拟数据库链接的配置 需要加入
	 * tomcat-juli.jar 这个包，tomcat7 此包位于 tomcat 根目录的 bin 下。
	 *
	 * @return InitialContext 上下文
	 */
	private static InitialContext initIc() {
		System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.naming.java.javaURLContextFactory");
		System.setProperty(Context.URL_PKG_PREFIXES, "org.apache.naming");

		try {
			InitialContext ic = new InitialContext();
			ic.createSubcontext("java:");
			ic.createSubcontext("java:/comp");
			ic.createSubcontext("java:/comp/env");
			ic.createSubcontext("java:/comp/env/jdbc");
			return ic;
		} catch (NamingException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 根据 JDBC Url 创建 SQLite 数据库 JDNI 数据源。
	 *
	 * @param url 以 jdbc:sqlite: 开头
	 */
	public static void initSqliteDBConnection(String url) {
		try {
			initIc().bind("java:/comp/env/jdbc/sqlite", getSqliteDataSource(url));
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据 JDBC Url 创建 MySQL 数据库 JDNI 数据源
	 *
	 * @param url 像 "jdbc:mysql://localhost:3306/databaseName"
	 * @param user 用户名
	 * @param password 密码
	 */
	public static void initMySqlDBConnection(String url, String user, String password) {
		try {
			initIc().bind("java:/comp/env/jdbc/mysql", getMySqlDataSource(url, user, password));
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
}
