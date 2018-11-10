package com.ajaxjs.orm.testcase;

import java.sql.Connection;

import javax.sql.DataSource;

import com.ajaxjs.orm.JdbcConnection;
import com.ajaxjs.util.io.resource.ScanClass;

public class DataSourceTestCase {
	/**
	 * 测试用数据库（SQLite）
	 */
	public static final String testUsed_sqlite = ScanClass.getResourceFilePath(News.class, "foo.sqlite");

	/**
	 * Return test-uesd data source
	 * 
	 * @return data source object
	 */
	public static DataSource getDataSource() {
		return JdbcConnection.getSqliteDataSource(testUsed_sqlite);
	}

	public static Connection getTestSqliteConnection() {
		return JdbcConnection.getTestSqliteConnection("jdbc:sqlite:" + testUsed_sqlite);
	}

}
