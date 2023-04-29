package com.ajaxjs.data_service;

import com.ajaxjs.data_service.DataServiceConstant.DatabaseType;
import com.ajaxjs.data_service.model.DataSourceInfo;
import com.ajaxjs.framework.spring.DiContextUtil;
import com.ajaxjs.sql.JdbcConnection;
import com.ajaxjs.sql.JdbcHelper;
import com.ajaxjs.util.logger.LogHelper;
import org.apache.tomcat.jdbc.pool.PoolProperties;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

/**
 * 工具类，主要是数据源的工具方法
 * 
 * @author Frank Cheung sp42@qq.com
 *
 */
public class DataServiceUtils {
	private static final LogHelper LOGGER = LogHelper.getLog(DataServiceUtils.class);

	public static Connection initDb() {
		return JdbcConnection.getConnection(Objects.requireNonNull(DiContextUtil.getBean(DataSource.class)));
	}
	
	/**
	 * 通过 JDBCHelper 实现 DAO
	 * 
	 * @param conn
	 * @param tableName
	 * @param id
	 * @return
	 */
	public static DataSourceInfo getDataSourceInfoById(Connection conn, String tableName, Long id) {
		String sql = "SELECT * FROM %s WHERE id = %s";
		sql = String.format(sql, tableName, id);

		return JdbcHelper.queryAsBean(DataSourceInfo.class, conn, sql);
	}

//	/**
//	 * 根据数据源 id 获取数据源（可能会太慢）
//	 *
//	 * @param datasourceId
//	 * @return
//	 */
//	public static Connection getConnectionByDatasourceId(long datasourceId) {
//		DataSourceInfo myds = mulitDataSource.get(datasourceId);
//		DataSource ds;
//
//		if (myds != null) {
//			LOGGER.info("Get DS from Cache!");
//			ds = myds.getInstance();
//		} else {
//			myds = JdbcHelper.queryAsBean(DataSourceInfo.class, null, "SELECT * FROM WHERE id = ?", datasourceId);
//			LOGGER.info("Created MyDs " + myds);
//
//			if (myds != null)
//				ds = DataSerivceUtils.getDataSourceByDataSourceInfo(myds);
//			else
//				return null;
//		}
//
//		return JdbcConnection.getConnection(ds);
//	}

	/**
	 * 通过 Tomcat JDBC Pool 获取 DataSource
	 * 
	 * @param info
	 * @return
	 */
	public static DataSource getDataSourceByDataSourceInfo(DataSourceInfo info) {
		return setupJdbcPool(getDbDriver(info), info.getUrl(), info.getUsername(), info.getPassword());
	}

	/**
	 * 通过 Tomcat JDBC Pool 获取 Connection
	 * 
	 * @param info
	 * @return 数据库连接对象
	 */
	public static Connection getConnectionByDataSourceInfo(DataSourceInfo info) {
		return JdbcConnection.getConnection(getDataSourceByDataSourceInfo(info));
	}

	/**
	 * 根据数据源记录之 id，返回连接字符串、用户密码等信息，进行数据库连接
	 * 
	 * @param conn
	 * @param tableName
	 * @param id
	 * @return
	 */
	public static Connection getConnByDataSourceInfo(Connection conn, String tableName, Long id) {
		DataSourceInfo info = getDataSourceInfoById(conn, tableName, id);
		return getConnectionByDataSourceInfo(info);
	}

	/**
	 * 普通创建数据库连接（没有池化的）
	 * 
	 * @param info
	 * @return 数据库连接对象
	 */
	public static Connection getConnection(DataSourceInfo info) {
		try {
			Class.forName(getDbDriver(info));
		} catch (ClassNotFoundException e) {
			LOGGER.warning(e);
		}

		try {
			return DriverManager.getConnection(info.getUrl(), info.getUsername(), info.getPassword());
		} catch (SQLException e) {
			LOGGER.warning(e);
			return null;
		}
	}

	/**
	 * 返回适合数据库的驱动名称
	 * 
	 * @param info
	 * @return
	 */
	public static String getDbDriver(DataSourceInfo info) {
		return getDbDriver(info.getType());
	}

	/**
	 * 返回适合数据库的驱动名称
	 *
	 * @param type
	 * @return 数据库的驱动名称
	 */
	public static String getDbDriver(DatabaseType type) {
		switch (type) {
		case MY_SQL:
			return "com.mysql.cj.jdbc.Driver";
		default:
			break;
		}

		return null;
	}
}
