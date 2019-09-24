/**
 * 版权所有 2017 Sp42 frank@ajaxjs.com
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
package com.ajaxjs.orm;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 自定义函数接口
 * 
 * @author Frank Cheung
 *
 */
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
		 * @throws SQLException SQL 异常
		 */
		public T process(ResultSet resultSet) throws SQLException;
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
