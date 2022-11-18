package com.ajaxjs.data_service.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.ajaxjs.data_service.DataServiceConstant.DatabaseType;
import com.ajaxjs.data_service.model.DataSourceInfo;
import com.ajaxjs.data_service.mybatis.MSUtils;
import com.ajaxjs.util.logger.LogHelper;

public class BaseDataSourceService {
	private static final LogHelper LOGGER = LogHelper.getLog(BaseDataSourceService.class);

	/**
	 * 通过 Tomcat JDBC Pool 获取 DataSource
	 * 
	 * @param info
	 * @return
	 */
	public static DataSource getDataSourceByDataSourceInfo(DataSourceInfo info) {
		DataSource ds = MSUtils.setupJdbcPool(getDbDriver(info), info.getUrl(), info.getUsername(), info.getPassword());

		return ds;
	}

	/**
	 * 通过 Tomcat JDBC Pool 获取 Connection
	 * 
	 * @param info
	 * @return 数据库连接对象
	 */
	public static Connection getConnectionByDataSourceInfo(DataSourceInfo info) {
		DataSource ds = getDataSourceByDataSourceInfo(info);

		try {
			return ds.getConnection();
		} catch (SQLException e) {
			LOGGER.warning(e);
		}

		return null;
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
