/**
 * Copyright Sp42 frank@ajaxjs.com Licensed under the Apache License, Version
 * 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
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
		return conn.toString().toLowerCase().contains("mysql");
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
