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
import java.util.ArrayList;
import java.util.List;

/**
 * 连接管理器，还可以保存调用过的 SQL 语句，以便于日志记录
 * 
 * @author xinzhang
 *
 */
public class ConnectionMgr {
	/**
	 * 数据库连接对象
	 */
	private static ThreadLocal<Connection> connection = new ThreadLocal<>();

	/**
	 * 保存刚刚调用过的 SQL 语句
	 */
	private static ThreadLocal<List<String>> sqls = new ThreadLocal<>();

	/**
	 * 获取一个数据库连接
	 * 
	 * @return 数据库连接对象
	 */
	public static Connection getConnection() {
		return connection.get();
	}

	/**
	 * 获取刚刚调用过的 SQL 语句
	 * 
	 * @return SQL 语句
	 */
	public static List<String> getSqls() {
		return sqls.get();
	}

	/**
	 * 保存一个数据库连接对象
	 * 
	 * @param conn
	 *            数据库连接对象
	 */
	public static void setConnection(Connection conn) {
		connection.set(conn);
	}

	/**
	 * 保存一个SQL 语句
	 * 
	 * @param _sqls
	 *            SQL 语句
	 */
	public static void setSqls(List<String> _sqls) {
		sqls.set(_sqls);
	}

	/**
	 * 保存加入一个 sql 语句
	 * 
	 * @param sql
	 *            SQL 语句
	 */
	public static void addSql(String sql) {
		if (getSqls() == null)
			setSqls(new ArrayList<String>());
		getSqls().add(sql);
	}

	/**
	 * 清除内容
	 */
	public static void clean() {
		connection.set(null);
		sqls.set(null);
	}
}
