package com.ajaxjs.jdbc;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class ConnectionMgr {
	private static ThreadLocal<Connection> connection = new ThreadLocal<>();
	private static ThreadLocal<List<String>> sqls = new ThreadLocal<>();

	public static Connection getConnection() {
		return connection.get();
	}

	public static List<String> getSqls() {
		return sqls.get();
	}
	
	public static void setConnection(Connection conn) {
		connection.set(conn);
	}
	
	public static void setSqls(List<String> _sqls) {
		sqls.set(_sqls);
	}
	
	/**
	 * 保存加入一个 sql 语句
	 * @param sql
	 */
	public static void addSql(String sql) {
		if(getSqls() == null)
			setSqls(new ArrayList<String>());
		getSqls().add(sql);
	}
	
	public static void clean () {
		connection.set(null);
		sqls.set(null);
	}
}
