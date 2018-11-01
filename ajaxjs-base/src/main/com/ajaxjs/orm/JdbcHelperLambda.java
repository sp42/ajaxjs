package com.ajaxjs.orm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcHelperLambda {
	/**
	 * 
	 * @param <T>
	 */
	@FunctionalInterface
	public interface ResultSetProcessor<T> {
		/**
		 * 
		 * @param resultSet
		 * @param currentRow
		 * @throws SQLException SQL 异常
		 */
		public T process(ResultSet resultSet) throws SQLException;
	}

	/**
	 * 
	 * @param <T>
	 */
	@FunctionalInterface
	public static interface ExecutePs<T> {
		/**
		 * 执行 SQL 语句
		 * 
		 * @param ps PreparedStatement 对象
		 * @return 执行 SQL后的结果
		 * @throws SQLException SQL 异常
		 */
		public T execute(PreparedStatement ps) throws SQLException;
	}

	/**
	 * SQL 查询是否有数据返回，没有返回 true
	 *
	 */
	@FunctionalInterface
	public static interface HasZeoResult {
		/**
		 * 
		 * @param conn 数据库连接对象
		 * @param rs
		 * @param sql
		 * @return
		 * @throws SQLException SQL 异常
		 */
		public boolean test(Connection conn, ResultSet rs, String sql) throws SQLException;
	}
}
