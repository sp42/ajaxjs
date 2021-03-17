package com.ajaxjs.sql.orm;

import java.sql.Connection;
import java.util.List;

/**
 * DAO 的上下文对象
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class DaoContext {
	/**
	 * 当前的数据库连接对象
	 */
	private Connection connection;

	/**
	 * 查询参数列表
	 */
	private Object[] params;

	/**
	 * 所有执行的 SQL 语句
	 */
	private List<String> sql;

	public Connection getConnection() {
		return connection;
	}

	private DataBaseType dbType;

	public void setConnection(Connection conn) {
		if (DataBaseType.isMySql(conn))
			dbType = DataBaseType.MYSQL;
		else if (DataBaseType.isSqlite(conn))
			dbType = DataBaseType.SQLITE;

		this.connection = conn;
	}

	public Object[] getParams() {
		return params;
	}

	public void setParams(Object[] params) {
		this.params = params;
	}

	public List<String> getSql() {
		return sql;
	}

	public void setSql(List<String> sql) {
		this.sql = sql;
	}

	public DataBaseType getDbType() {
		return dbType;
	}

	public void setDbType(DataBaseType dbType) {
		this.dbType = dbType;
	}
}
