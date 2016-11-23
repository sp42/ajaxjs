/**
 * Copyright 2015 Frank Cheung
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
package com.ajaxjs.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.ajaxjs.util.LogHelper;
import com.ajaxjs.util.StringUtil;

/**
 * 数据库工具类，跟具体业务无关
 * @author frank
 *
 */
public class Helper {
	private static final LogHelper LOGGER = LogHelper.getLog(Helper.class);
	
	/**
	 * 获取数据源
	 * 数据源提供了一种简单获取数据库连接的方式，并能在内部通过一个池的机制来复用数据库连接，这样就大大减少创建数据库连接的次数，提高了系统性能。 另外
	 * 使用 datasource，就不需要手动关闭连接，如果使用 connection，就需要自己关闭连接。
	 * 对于数据源的应用，一般都选择实用开源的数据源或数据库连接池来使用，当前我们这个框架使用了 Tomcat 自带 Pool。
	 * 
	 * @param path
	 *            参阅 META-INF/context.xml
	 * @return 数据源对象
	 */
	public static DataSource getDataSource(String path) {
		Context context = null; // 环境变量
		DataSource ds = null;

		try {
			context = (Context) new InitialContext().lookup("java:/comp/env");
			ds = (DataSource) context.lookup(path);
			// 简写方式
			// javax.sql.DataSource ds = (javax.sql.DataSource)new InitialContext().lookup("java:/comp/env/jdbc/derby");
		} catch (NamingException e) {
			LOGGER.warning("读取数据源的配置文件失败，请检查 Tomcat 连接池配置！ path:" + path, e);
		}

		return ds;
	}
	
	/**
	 * 连接任意数据库（不使用连接池，而是传统方式） e.g initConn("org.sqlite.JDBC",
	 * "jdbc:sqlite:c:\\project\\foo\\work\\work.sqlite", null);
	 * 
	 * @param driver
	 *            驱动字符串
	 * @param jdbcUrl
	 *            链接字符串
	 * @param props
	 *            参数属性
	 */
	public static Connection getConnection(String driver, String jdbcUrl, Properties props) {
		Connection conn = null;

		try {
			if (props == null)
				conn = DriverManager.getConnection(jdbcUrl);
			else
				conn = DriverManager.getConnection(jdbcUrl, props);

			LOGGER.info("数据库连接成功： " + conn.getMetaData().getURL());
		} catch (SQLException e) {
			LOGGER.warning("数据库连接失败！", e);

			try { // jdbc 4的新 写法可不用Class.forName，如果不支持，退回旧写法
				Class.forName(driver);
			} catch (ClassNotFoundException e1) {
				LOGGER.warning("创建数据库连接失败，请检查是否安装对应的 Driver:{0}。", driver);
			}
		}

		return conn;
	}

	/**
	 * 连接数据库（不使用连接池，而是传统方式，不推薦使用）
	 * 
	 * @param jdbcUrl
	 *            如 jdbc:sqlite:c:\\project\\foo\\work\\work.sqlite
	 * @return 数据库连接对象
	 */
	public static Connection getConnection(String jdbcUrl) {
		Connection conn = null;

		try {
			conn = DriverManager.getConnection(jdbcUrl);
			LOGGER.info("数据库连接成功： " + conn.getMetaData().getURL());
		} catch (SQLException e) {
			LOGGER.warning("数据库连接失败！", e);
		}
		return conn;
	}
	
	/**
	 * 通过数据源对象获得数据库连接对象
	 * 
	 * @param source
	 *            数据源对象
	 * @return 数据库连接对象
	 */
	public static Connection getConnection(DataSource source) {
		try {
			return source.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	/**
	 * 关闭数据库连接。 可以对任意一个数据库连接对象实施关闭。
	 * 
	 * @param conn
	 *            任意一个连接对象
	 */
	public static void closeConnection(Connection conn) {
		if (conn != null) {
			try {
				if (!conn.isClosed())
					conn.close();
			} catch (SQLException e) {
					e.printStackTrace();
			}

			LOGGER.info("关闭数据库");
		}
	}
	
	/**
	 * 查询回调
	 */
	public static interface Callback {
		/**
		 * 执行回调
		 * 
		 * @param resultset
		 *            查询结果集合，如果为 null 表示没有找到记录
		 * @return 为任意类型，由回调决定
		 * @throws SQLException
		 */
		Object doIt(ResultSet resultset) throws SQLException;
	}
	
	/**
	 * 执行查询 SQL 语句，得到的结果交给回调处理
	 * 
	 * @param conn
	 *            数据库连接
	 * @param sql
	 *            查询的 SQL 语句
	 * @param cb
	 *            回调方法签名如： Object doIt(ResultSet resultset) throws SQLException;
	 * @return 其实是回调返回的结果，为任意类型，由回调决定
	 */
	public Object queryWithCallback(Connection conn, String sql, Callback cb) {
		LOGGER.info("将要查询的 SQL 为:" + formatSql(sql));
		Object obj = null;

		// createStatement()不給定參數時，預設是ResultSet.TYPE_FORWARD_ONLY、
		// ResultSet.CONCUR_READ_ONLY。
		try (Statement statement = conn.createStatement(); ResultSet rs = statement.executeQuery(sql);) {
			if (rs.isBeforeFirst() && cb != null) {
				obj = cb.doIt(rs);
			} else {
				LOGGER.warning("查询 SQL：{0} 没有符合的记录！", sql);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return obj;
	}

	/**
	 * 记录集合转换为 Map
	 * 
	 * @param rs
	 *            记录集合
	 * @return Map 结果
	 */
	private static Map<String, Object> getResultMap(ResultSet rs) {
		Map<String, Object> map = new HashMap<>();
		try {
			ResultSetMetaData rsmd = rs.getMetaData();
			int count = rsmd.getColumnCount();

			for (int i = 1; i <= count; i++) {
				String key = rsmd.getColumnLabel(i);
				Object value = rs.getObject(i);
				map.put(key, value);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

		return map;
	}
    
	/**
	 * 记录集合列表转换为 List
	 * 
	 * @param conn
	 *            数据库连接对象
	 * @param sql
	 *            查询的 SQL 语句
	 * @return List 结果
	 */
	public static List<Map<String, Object>> queryList(Connection conn, String sql) {
		List<Map<String, Object>> list = new ArrayList<>();
		
		try (Statement statement = conn.createStatement(); ResultSet rs = statement.executeQuery(sql);) {
			while (rs.next()) {
				list.add(getResultMap(rs));
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
		
		return list;
	}
	

	/**
	 * 查询结果作为 String 返回 查询的 SQL 语句，如果查询成功有这笔记录，返回 true，否则返回 false（检查有无记录）
	 * 
	 * @param rs
	 *            结果集
	 * @param classz
	 *            期望的类型
	 * @return 数据库里面的值作为 T 出现
	 */
	@SuppressWarnings("unchecked")
	public <T> T queryAs(ResultSet rs, Class<T> classz) {
		try {
			if (String.class == classz) {
				return rs.isBeforeFirst() ? (T) rs.getString(1) : null;
			} else if (Integer.class == classz) {

				// if (jdbcConnStr.indexOf("MySQL") != -1 || jdbcConnStr.indexOf("mysql") != -1) {
				//     result = rs.next() ? rs.getInt(1) : null;
				// } else {// sqlite
				//      result = rs.isBeforeFirst() ? rs.getInt(1) : null;
				// }
				return rs.isBeforeFirst() ? (T) (Integer) rs.getInt(1) : null;
			} else if (Boolean.class == classz) {
				return (T) (Boolean) rs.isBeforeFirst();
			} else {
				return null;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 简单格式化 SQL，当前对 SELECT 语句有效
	 * 
	 * @param sql
	 *            SELECT 语句
	 * @return 美化后的 SQL
	 */
	public static String formatSql(String sql) {
		String separator = System.getProperty("line.separator");
		sql = '\t' + sql;
		sql = sql.replaceAll("(?i)SELECT\\s+", "SELECT "); // 统一大写
		sql = sql.replaceAll("\\s+(?i)FROM", separator + "\tFROM");
		sql = sql.replaceAll("\\s+(?i)WHERE", separator + "\tWHERE");
		sql = sql.replaceAll("\\s+(?i)GROUP BY", separator + "\tGROUP BY");
		sql = sql.replaceAll("\\s+(?i)ORDER BY", separator + "\tORDER BY");
		sql = sql.replaceAll("\\s+(?i)LIMIT", separator + "\tLIMIT");
		sql = sql.replaceAll("\\s+(?i)DESC", " DESC");
		sql = sql.replaceAll("\\s+(?i)ASC",  " ASC");

		return sql;
	}
	
	/**
	 * 返回供 perpareStatement 所用的 ?。 INSERT INTO 所用
	 * 
	 * @param len
	 *            长度
	 * @return Holder
	 */
	public static String getPlaceHolder(int len) {
		String[] placeHolders = new String[len];
		for (int i = 0; i < placeHolders.length; i++)
			placeHolders[i] = "?";
	
		return StringUtil.stringJoin(placeHolders, ",");
	}

	/**
	 * 返回字段名，供 INSERT/UPDATE 用
	 * 
	 * @param pair
	 *            数据
	 * @return 结对的结构
	 */
	public static String getFields(Map<String, Object> pair, boolean isFieldName_Only) {
		String[] fields = new String[pair.size()];
		int i = 0;
		for (String field : pair.keySet())
			fields[i++] = isFieldName_Only ? field : field + " = ?";
	
		return StringUtil.stringJoin(fields, ", ");
	}
	
}
