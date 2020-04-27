package com.ajaxjs.orm;

import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.junit.Test;
import org.sqlite.SQLiteDataSource;
import org.sqlite.SQLiteJDBCLoader;

import com.ajaxjs.util.logger.LogHelper;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

/**
 * 测试 JDNI 数据源绑定，以实现单元测试
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class TestJDNIDataSource {
	private static final LogHelper LOGGER = LogHelper.getLog(TestJdbcConnection.class);

	/**
	 * JDNI 是一个为 Java 应用程序提供命名服务的应用程序接口 模拟数据库链接的配置 需要加入 tomcat-juli.jar 这个包，tomcat7
	 * 此包位于 tomcat 根目录的 bin 下。
	 *
	 * @return InitialContext 上下文
	 */
	public static InitialContext initIc() {
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
			LOGGER.warning(e);
			return null;
		}
	}

	/**
	 * 根据 JDBC Url 创建 MySQL 数据库 JDNI 数据源
	 *
	 * @param url      像 "jdbc:mysql://localhost:3306/databaseName"
	 * @param user     用户名
	 * @param password 密码
	 */
	public static void initMySqlDBConnection(String url, String user, String password) {
		MysqlDataSource dataSource = new MysqlDataSource();
		dataSource.setURL(url);
		dataSource.setUser(user);
		dataSource.setPassword(password);
		dataSource.setUseUnicode(true);
		dataSource.setCharacterEncoding("UTF-8");

		try {
			initIc().bind("java:/comp/env/jdbc/mysql", dataSource);
		} catch (NamingException e) {
			LOGGER.warning(e);
		}
	}

	/**
	 * 根据 JDBC Url 创建 SQLite 数据库 JDNI 数据源。
	 *
	 * @param url 以 jdbc:sqlite: 开头
	 */
	public static void initSqliteDBConnection(String url) {
		String perfix = "jdbc:sqlite:";

		if (!url.startsWith(perfix))
			url = perfix + url;

		try {
			SQLiteJDBCLoader.initialize();
		} catch (Exception e) {
			LOGGER.warning(e);
		}

		SQLiteDataSource dataSource = new SQLiteDataSource();
		dataSource.setUrl(url);

		try {
			initIc().bind("java:/comp/env/jdbc/sqlite", dataSource);
		} catch (NamingException e) {
			LOGGER.warning(e);
		}
	}

	@Test
	public void testGetDataSource() throws SQLException {
//		TestHelper.initSqliteDBConnection(TestHelper.testUsed_sqlite);
//		assertNotNull(getDataSource("jdbc/sqlite"));
//		Connection conn = getDataSource("jdbc/sqlite").getConnection();
//		assertNotNull(conn);
//		conn.close();
	}
}
