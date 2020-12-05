/**
 * Copyright sp42 frank@ajaxjs.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ajaxjs.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 自定义函数接口
 * 
 * @author sp42 frank@ajaxjs.com
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
	public static interface HasZeroResult {
		/**
		 * 
		 * @param conn 数据库连接对象
		 * @param rs
		 * @param sql
		 * @return true 表示为没有匹配数据
		 * @throws SQLException SQL 异常
		 */
		public boolean test(Connection conn, ResultSet rs, String sql) throws SQLException;
	}
}
