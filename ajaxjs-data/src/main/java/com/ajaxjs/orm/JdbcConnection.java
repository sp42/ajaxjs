/**
 * 版权所有 2017 Sp42 frank@ajaxjs.com
 * 
 * 根据 2.0 版本 Apache 许可证("许可证")授权；
 * 根据本许可证，用户可以不使用此文件。
 * 用户可从下列网址获得许可证副本：
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 *    
 * 除非因适用法律需要或书面同意，根据许可证分发的软件是基于"按原样"基础提供，
 * 无任何明示的或暗示的保证或条件。详见根据许可证许可下，特定语言的管辖权限和限制。
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

import org.sqlite.SQLiteDataSource;
import org.sqlite.SQLiteJDBCLoader;

import com.ajaxjs.util.io.resource.ScanClass;
import com.ajaxjs.util.logger.LogHelper;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

/**
 * 连接数据库。 保存线程内的连接对象，还可以保存调用过的 SQL 语句，以便于日志记录
 * 
 * @author Sp42 frank@ajaxjs.com
 *
 */
public class JdbcConnection {
	private static final LogHelper LOGGER = LogHelper.getLog(JdbcConnection.class);

	/**
	 * 通过 JNDI 获取数据源
	 * 
	 * @param path JNDI 的路径，参阅 META-INF/context.xml
	 * @return 数据源对象
	 */
	public static DataSource getDataSource(String path) {
		try {
			Object obj = new InitialContext().lookup("java:/comp/env");
			Objects.requireNonNull(obj, "没有该节点 java:/comp/env");

			Context context = (Context) obj; // 环境变量
			Object result = context.lookup(path);
			return (DataSource) result;
		} catch (NamingException e) {
			String msg = "读取数据源的配置文件失败，请检查 Tomcat 连接池配置！ path: " + path;
			msg += " 提示：没发现数据库 /WebRoot/META-INF/context.xml 下的 XML 配置文件，该文件位置一般不可移动，请参阅 tomat pool 数据库连接池的相关文档。";
			
			LOGGER.warning(msg, e);
			return null;
		}
	}

	/**
	 * 初始化数据库连接
	 * 
	 * @param jndiPath JNDI 路径
	 */
	public static void initDbByJNDI(String jndiPath) {
		Objects.requireNonNull(jndiPath, "缺少 jndiPath 参数！");
System.out.println(jndiPath);
		try {
			if (getConnection() == null || getConnection().isClosed()) {
				Connection conn = getConnection(getDataSource(jndiPath));
				setConnection(conn);
				LOGGER.info("启动数据库链接……" + conn);
			}
		} catch (SQLException e) {
			LOGGER.warning(e);
		}
	}

	/**
	 * 通过数据源对象获得数据库连接对象
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
	 * 连接数据库
	 * 
	 * @param jdbcUrl 链接字符串
	 * @param props   链接属性
	 * @return 数据库连接对象
	 */
	public static Connection getConnection(String jdbcUrl, Properties props) {
		Connection conn = null;

		if (jdbcUrl.indexOf("mysql") != -1) {
			try {
				Class.forName("com.mysql.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				LOGGER.warning(e);
			}
		}

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
	 * @param jdbcUrl 链接字符串
	 * @return 数据库连接对象
	 */
	public static Connection getConnection(String jdbcUrl) {
		return getConnection(jdbcUrl, null);
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

/////////////////
	/*
	 * 为方便测试提供的假对象 Mock data source object.
	 */
/////////////////

	/**
	 * 根据 JDBC Url 创建 SQLite 数据源对象
	 *
	 * @param url 以 jdbc:sqlite: 开头
	 * @return 数据源对象
	 */
	public static DataSource getSqliteDataSource(String url) {
		String perfix = "jdbc:sqlite:";
		if (!url.startsWith(perfix)) {
			url = perfix + url;
		}

		try {
			SQLiteJDBCLoader.initialize();
		} catch (Exception e) {
			LOGGER.warning(e);
		}

		SQLiteDataSource dataSource = new SQLiteDataSource();
		dataSource.setUrl(url);

		return dataSource;
	}

	/**
	 * 根据 JDBC Url 创建 MySQL 数据源对象
	 *
	 * @param url      像 "jdbc:mysql://localhost:3306/databaseName"
	 * @param user     用户名
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
	 * @param url 以 jdbc:sqlite: 开头，如果没有自动加上
	 * @return 数据库连接对象
	 */
	public static Connection getSqliteConnection(String url) {
		return getConnection(getSqliteDataSource(url));
	}

	/**
	 * 测试用数据库（SQLite）
	 */
	public static final String testUsed_sqlite = ScanClass.getResourcesByFileName("test_used_database.sqlite");

	/**
	 * 创建 SQLite 数据库连接对象（测试用）
	 *
	 * @return 数据库连接对象
	 */
	public static Connection getTestSqliteConnection() {
		return getSqliteConnection(testUsed_sqlite);
	}

	/**
	 * 根据 JDBC Url 创建 MySQL 数据库连接对象
	 *
	 * @param url      像 "jdbc:mysql://localhost:3306/databaseName"
	 * @param user     用户名
	 * @param password 密码
	 * @return 数据库连接对象
	 */
	public static Connection getMySqlConnection(String url, String user, String password) {
		return getConnection(getMySqlDataSource(url, user, password));
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
			LOGGER.warning(e);
			return null;
		}
	}

	/**
	 * 根据 JDBC Url 创建 SQLite 数据库 JDNI 数据源。
	 *
	 * @param url 以 jdbc:sqlite: 开头
	 */
	public static void initSqliteDBConnection(String url) {
		DataSource ds = getSqliteDataSource(url);

		try {
			initIc().bind("java:/comp/env/jdbc/sqlite", ds);
		} catch (NamingException e) {
			LOGGER.warning(e);
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
		try {
			initIc().bind("java:/comp/env/jdbc/mysql", getMySqlDataSource(url, user, password));
		} catch (NamingException e) {
			LOGGER.warning(e);
		}
	}
}
