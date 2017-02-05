package com.ajaxjs.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import com.ajaxjs.framework.model.PageResult;

/**
 * 简单的分页类
 * @author xinzhang
 *
 */
public class SimplePager {
	private Connection conn;

	private String sql;

	private int pageSize = 5;

	private int start;

	/**
	 * 
	 * @param conn
	 * @param tableName
	 * @param start
	 */
	public SimplePager(Connection conn, String tableName, int start) {
		this.conn = conn;
		this.sql = tableName;
		this.start = start;
	}

	/**
	 * 
	 * @param conn
	 * @param tableName
	 * @param start
	 */
	public SimplePager(Connection conn, String tableName, String start) {
		this.conn = conn;
		this.sql = tableName;
		this.start = start == null ? 0 : Integer.parseInt(start);
	}

	/**
	 * 获取分页结果
	 * @return
	 */
	public PageResult<Map<String, Object>> getResult() {
		int total = getTotal();

		if (total > 0) {
			String _sql = sql + String.format(" LIMIT %d, %d", start, pageSize);
			List<Map<String, Object>> results = Helper.queryList(conn, _sql);

			PageResult<Map<String, Object>> pr = new PageResult<>();
			
			pr.setRows(results);
			pr.setStart(start);
			pr.setPageSize(pageSize);
			pr.setTotalCount(total);
			pr.page();

			return pr;
		} else {
			return null;
		}
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * 获取记录总数
	 * 
	 * @return
	 */
	private int getTotal() {
		String _sql = sql.replaceAll("SELECT", "SELECT COUNT(\\*) AS count, ");
		try (Statement statement = conn.createStatement(); ResultSet rs = statement.executeQuery(_sql);) {
			if (rs.isBeforeFirst()) {
				return rs.getInt(1);
			} else {
				System.err.println("查询 SQL：" + sql + " 没有符合的记录！");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return 0;
	}

}
