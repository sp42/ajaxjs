package com.ajaxjs.sql.orm;

import java.sql.Connection;

/**
 * 数据库厂商类型
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public enum DataBaseType {
	MYSQL, SQLITE, SQLSERVER, DB2;

	/**
	 * 是否 mysql 数据库
	 * 
	 * @param conn 数据库连接对象
	 * @return true 表示为 Mysql 数据库
	 */
	public static boolean isMySql(Connection conn) {
		String connStr = conn.toString();
		return connStr.indexOf("MySQL") != -1 || connStr.indexOf("mysql") != -1;
	}

	/**
	 * 判断是否 SQLite 数据库
	 * 
	 * @param conn 数据库连接对象
	 * @return true = 是 SQLite 数据库
	 */
	public static boolean isSqlite(Connection conn) {
		return conn.toString().contains("sqlite");
	}
}
