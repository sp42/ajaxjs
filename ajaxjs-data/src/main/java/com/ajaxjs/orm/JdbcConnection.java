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
package com.ajaxjs.orm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.ajaxjs.util.logger.LogHelper;

/**
 * 连接数据库。 保存线程内的连接对象，还可以保存调用过的 SQL 语句，以便于日志记录
 * 
 * @author pp42 frank@ajaxjs.com
 *
 */
public class JdbcConnection {
	private static final LogHelper LOGGER = LogHelper.getLog(JdbcConnection.class);

	/**
	 * 连接数据库。这种方式最简单，但是没有经过数据库连接池，要采用池化的方式，请使用 getConnectionByJNDI()。
	 * 
	 * @param jdbcUrl 连接字符串
	 * @param props   连接属性，可选（可为 null）
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
	 * 连接数据库。这种方式最简单，但是没有经过数据库连接池，要采用池化的方式，请使用 getConnectionByJNDI()。
	 * 
	 * @param jdbcUrl 连接字符串
	 * @return 数据库连接对象
	 */
	public static Connection getConnection(String jdbcUrl) {
		return getConnection(jdbcUrl, null);
	}

	/**
	 * 根据数据源对象获得数据库连接对象
	 * 
	 * @param source 数据源对象
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
	 * 根据 JNDI 获取数据源。这是配套 Tomcat 自带 Pool 的数据库连接池服务。
	 * 
	 * @param jndi JNDI 的路径，参阅 META-INF/context.xml
	 * @return 数据源对象
	 */
	public static DataSource getDataSourceByJNDI(String jndi) {
		try {
			Object obj = new InitialContext().lookup("java:/comp/env");
			Objects.requireNonNull(obj, "没有该节点 java:/comp/env");

			Context context = (Context) obj; // 环境变量
			Object result = context.lookup(jndi);

			return (DataSource) result;
		} catch (NamingException e) {
			String msg = "读取数据源的配置文件失败，请检查 Tomcat 连接池配置！ path: " + jndi;
			msg += " 提示：没发现数据库 /WebRoot/META-INF/context.xml 下的 XML 配置文件，该文件位置一般不可移动，请参阅 TomatPool 数据库连接池的相关文档。";
			LOGGER.warning(msg, e);

			return null;
		}
	}

	/**
	 * 根据 JNDI 路径获得数据库连接对象。这是配套 Tomcat 自带 Pool 的数据库连接池服务。
	 * 
	 * @param jndi JNDI 的路径，参阅 META-INF/context.xml
	 * @return 数据库连接对象
	 */
	public static Connection getConnectionByJNDI(String jndi) {
		return getConnection(getDataSourceByJNDI(jndi));
	}

	/**
	 * 初始化数据库连接并保存到 ThreadLocal 中。这是框架内最主要的调用数据库连接方法，带有池化的服务。
	 * 
	 * @param jndi JNDI 的路径，参阅 META-INF/context.xml
	 */
	public static void initDbByJNDI(String jndi) {
		Objects.requireNonNull(jndi, "缺少 jndiPath 参数！");
		LOGGER.info("启动数据库 JNDI 连接……" + jndi);

		try {
			if (getConnection() == null || getConnection().isClosed()) {
				Connection conn = getConnectionByJNDI(jndi);
				setConnection(conn);
				LOGGER.info("数据库连接详情：" + conn);
			}
		} catch (SQLException e) {
			LOGGER.warning(e);
		}
	}

	/**
	 * 数据库连接对象
	 */
	private static ThreadLocal<Connection> connection = new ThreadLocal<>();

	/**
	 * 保存刚刚调用过的 SQL 语句
	 */
	private static ThreadLocal<List<String>> sqls = new ThreadLocal<>();

	/**
	 * 获取一个数据库连接
	 * 
	 * @return 数据库连接对象
	 */
	public static Connection getConnection() {
		return connection.get();
	}

	/**
	 * 获取刚刚调用过的 SQL 语句
	 * 
	 * @return SQL 语句
	 */
	public static List<String> getSqls() {
		return sqls.get();
	}

	/**
	 * 保存一个数据库连接对象
	 * 
	 * @param conn 数据库连接对象
	 */
	public static void setConnection(Connection conn) {
		connection.set(conn);
	}

	/**
	 * 保存一个SQL 语句
	 * 
	 * @param _sqls SQL 语句
	 */
	public static void setSqls(List<String> _sqls) {
		sqls.set(_sqls);
	}

	/**
	 * 保存加入一个 sql 语句
	 * 
	 * @param sql SQL 语句
	 */
	public static void addSql(String sql) {
		if (getSqls() == null)
			setSqls(new ArrayList<String>());

		getSqls().add(sql);
	}

	/**
	 * 关闭数据库连接
	 */
	public static void closeDb() {
		Connection conn = getConnection();

		try {
			if (conn != null && !conn.isClosed()) {
				conn.close();
				LOGGER.info("关闭数据库连接成功！ Close database OK！");
			}
		} catch (SQLException e) {
			LOGGER.warning(e);
		}

		clean();
	}

	/**
	 * 清除 sql 内容
	 */
	public static void cleanSql() {
		if (getSqls() != null)
			getSqls().clear();

		sqls.set(null);
	}

	/**
	 * 清除内容
	 */
	public static void clean() {
		connection.set(null);
		cleanSql();
	}

	/**
	 * 根据 JDBC 连接字符串创建 MySql 数据库连接对象
	 * 
	 * @param jdbcUrl  JDBC 连接字符串。如果连接字符串已经包含用户名和密码，请直接使用 getConnection() 方法即可。
	 * @param username 数据库用户名
	 * @param password 数据库密码
	 * @return 数据库连接对象
	 */
	public static Connection getMySqlConnection(String jdbcUrl, String username, String password) {
		if (!jdbcUrl.startsWith("jdbc:mysql://"))
			throw new IllegalArgumentException("参数为非法的 JDBC URL： " + jdbcUrl);

		if (!jdbcUrl.contains("?"))
			jdbcUrl += "?";

		return getConnection(jdbcUrl + "&user=" + username + "&password=" + password);
	}

	/**
	 * 根据 JDBC 连接字符串创建 SQLite 数据库连接对象
	 * 
	 * @param jdbcUrl JDBC 连接字符串， 例如
	 *                "jdbc:sqlite:D:/software/sqlite/java-sqlite.db"，也可以纯粹文件路径，如
	 *                "D:\\project\\ajaxjs-data\\src\\main\\resources\\test_used_database.sqlite"，
	 *                自动添加 jdbc:sqlite: 前缀
	 * @return 数据库连接对象
	 */
	public static Connection getSqliteConnection(String jdbcUrl) {
		if (!jdbcUrl.startsWith("jdbc:sqlite:")) // 自动添加前缀
			jdbcUrl = "jdbc:sqlite:" + jdbcUrl;

		return getConnection(jdbcUrl);
	} 
}
