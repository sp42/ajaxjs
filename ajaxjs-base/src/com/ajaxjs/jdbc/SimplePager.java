/**
 * 版权所有 2017 Frank Cheung
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
 * 
 * @author xinzhang
 *
 */
public class SimplePager {
	/**
	 * 数据库连接对象
	 */
	private Connection conn;

	/**
	 * 欲分页的 SQL 语句
	 */
	private String sql;

	/**
	 * 默认的每页记录数
	 */
	private int pageSize = 5;

	/**
	 * 分页起始数
	 */
	private int start;

	/**
	 * 
	 * @param conn
	 *            数据库连接对象
	 * @param sql
	 *            欲分页的 SQL 语句
	 * @param start
	 *            分页起始数
	 */
	public SimplePager(Connection conn, String sql, int start) {
		this.conn = conn;
		this.sql = sql;
		this.start = start;
	}

	/**
	 * 
	 * @param conn
	 *            数据库连接对象
	 * @param sql
	 *            欲分页的 SQL 语句
	 * @param start
	 *            分页起始数
	 */
	public SimplePager(Connection conn, String sql, String start) {
		this.conn = conn;
		this.sql = sql;
		this.start = start == null ? 0 : Integer.parseInt(start);
	}

	/**
	 * 获取分页结果
	 * 
	 * @return 分页结果 bean
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

	/**
	 * 设置每页大小
	 * 
	 * @param pageSize
	 *            每页大小
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * 获取记录总数
	 * 
	 * @return 记录总数
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
