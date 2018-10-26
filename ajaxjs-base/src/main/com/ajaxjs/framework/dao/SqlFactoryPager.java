package com.ajaxjs.framework.dao;

import java.sql.Connection;

import com.ajaxjs.framework.dao.annotation.Select;
import com.ajaxjs.jdbc.Helper;
import com.ajaxjs.util.StringUtil;

/**
 * 分页控制器
 * 
 * @author Frank Cheung
 *
 */
public class SqlFactoryPager implements SqlFactory {
	@Override
	public SqlAndArgs toSql(SqlAndArgs s) {
		s.sql += " LIMIT ?, ?";

		return s;
	}

	/**
	 * 获取统计行数
	 * 
	 * @param select 业务逻辑 SQL 所在的注解
	 * @param sql 业务逻辑 SQL
	 * @param args DAO 方法参数
	 * @param conn 连接对象，判断是否 MySQL or SQLite
	 * @return 统计行数
	 */
	public int countTotal(Select select, String sql, Object[] args, Connection conn) {
		String countSql = getCountTotalSql(conn, select, sql);
		Object[] _args = removePageParam(args);
		Long total = Helper.queryOne(conn, countSql, Long.class, _args);
		// mysql 返回 long，转换一下
		
		return total == null ? 0 : total.intValue();
	}

	/**
	 * 获取统计行数的 SQL
	 * 
	 * @param conn 连接对象，判断是否 MySQL or SQLite
	 * @param select 业务逻辑 SQL 所在的注解
	 * @param sql 业务逻辑 SQL
	 * @return 统计行数的 SQL
	 */
	private static String getCountTotalSql(Connection conn, Select select, String sql) {
		String countSql;

		if (StringUtil.isEmptyString(select.countSql())) {
			// 另外一种统计方式，但较慢 子查询
			// 这是默认的实现，你可以通过增加 sqlCount 注解给出特定的 统计行数 之 SQL
			countSql = "SELECT COUNT(*) AS count FROM (" + sql + ") AS t;";
//			countSql = sql.replaceAll("SELECT.*FROM", "SELECT COUNT(\\*) AS count FROM");
		} else {
			countSql = DaoHandler.isSqlite(select.sqliteCountSql(), conn) ? select.sqliteCountSql() : select.countSql();
		}

		return countSql;
	}

	/**
	 * 分页参数总是在最后两个，统计的时候不需要，从数组里面去掉。
	 * 
	 * @param args
	 * @return
	 */
	private static Object[] removePageParam(Object[] args) {
		if(args == null)
			return null;
		
		int len = args.length - 2;

		if (len <= 0)
			return new Object[] {};
		else {
			Object[] pa = new Object[len];

			for (int i = 0; i < len; i++) {
				pa[i] = args[i];
			}

			return pa;
		}
	}
}
