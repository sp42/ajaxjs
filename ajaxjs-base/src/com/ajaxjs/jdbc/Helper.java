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

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	 * 记录集合转换为 Map
	 * 
	 * @param rs
	 *            记录集合
	 * @return Map 结果
	 * @throws SQLException 
	 */
	public static Map<String, Object> getResultMap(ResultSet rs) throws SQLException {
		Map<String, Object> map = new LinkedHashMap<>(); // LinkedHashMap 是 HashMap 的一个子类，保存了记录的插入顺序
		ResultSetMetaData rsmd = rs.getMetaData();

		for (int i = 1; i <= rsmd.getColumnCount(); i++) {// 遍历结果集
			String key = rsmd.getColumnLabel(i);
			Object value = rs.getObject(i);
			
//				if(value != null)
//					System.out.println(value.getClass().getName());
//				else 
//					System.out.println(key);
			map.put(key, value);
		}
	
		return map;
	}
	
	/**
	 * 查询单个结果，保存为 Map<String, Object> 结构。如果查询不到任何数据返回 null。
	 * 
	 * @param conn
	 *            数据库连接对象
	 * @param sql
	 *            SQL 语句，可以带有 ? 的占位符
	 * @param params
	 *            插入到 SQL 中的参数，可单个可多个可不填
	 * @return Map<String, Object> 结构的结果。如果查询不到任何数据返回 null。
	 */
	public static Map<String, Object> query(Connection conn, String sql, Object... params) {
		Map<String, Object> map = null;
		printRealSql(sql, params);
		
		try (PreparedStatement ps = conn.prepareStatement(sql);) {
			for (int i = 0; i < params.length; i++) 
				ps.setObject(i + 1, params[i]);
			
			try (ResultSet rs = ps.executeQuery();) {
				if (rs.isBeforeFirst()) {
					map = getResultMap(rs);
				} else {
					LOGGER.info("查询 SQL：{0} 没有符合的记录！", sql);
				}
			}
		} catch (SQLException e) {
			LOGGER.warning(e);
		}
		
		return map;
	}
	
	/**
	 * 查询一组结果，保存为 List<Map<String, Object>> 结构。如果查询不到任何数据返回 null。
	 * 
	 * @param conn
	 *            数据库连接对象
	 * @param sql
	 *            SQL 语句，可以带有 ? 的占位符
	 * @param params
	 *            插入到 SQL 中的参数，可单个可多个可不填
	 * @return Map<String, Object> 结构的结果。如果查询不到任何数据返回 null。
	 */
	public static List<Map<String, Object>> queryList(Connection conn, String sql, Object... params) {
		List<Map<String, Object>> list = new ArrayList<>();
		printRealSql(sql, params);

		try (PreparedStatement ps = conn.prepareStatement(sql);) {
			for (int i = 0; i < params.length; i++) 
				ps.setObject(i + 1, params[i]);
			
			try(ResultSet rs = ps.executeQuery();){				
				while (rs.next()) 
					list.add(getResultMap(rs));
			}
		} catch (SQLException e) {
			LOGGER.warning(e);
		}
		
		return list.size() == 0 ? null : list;
	}
	
	/**
	 * 新建记录。为兼顾主键类型，返回的类型设为同时兼容 int/long/string 的 Serializable
	 * 
	 * @param conn
	 *            数据库连接对象
	 * @param sql
	 *            SQL 语句，可以带有 ? 的占位符
	 * @param params
	 *            插入到 SQL 中的参数，可单个可多个可不填
	 * @return 新增主键
	 */
	public static Serializable create(Connection conn, String sql, Object... params) {
		Object newlyId = null;
		printRealSql(sql, params);

		try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
			for (int i = 0; i < params.length; i++)
				ps.setObject(i + 1, params[i]);

			ps.executeUpdate();

			// 当保存之后会自动获得数据库返回的主键
			try (ResultSet rs = ps.getGeneratedKeys();) {
				if (rs.next())
					newlyId = rs.getObject(1);
			}
		} catch (SQLException e) {
			LOGGER.warning(e);
		}

		if (!(newlyId instanceof Serializable))
			throw new RuntimeException("返回 id 类型不是 Serializable");

		return (Serializable) newlyId;
	}
	
	/**
	 * 执行 SQL UPDATE 更新。
	 * 
	 * @param conn
	 *            数据库连接对象
	 * @param sql
	 *            SQL 语句，可以带有 ? 的占位符
	 * @param params
	 *            插入到 SQL 中的参数，可单个可多个可不填
	 * @return 成功修改的行数
	 */
	public static int update(Connection conn, String sql, Object... params) {
		int effectRows = 0;
		printRealSql(sql, params);
		
		try (PreparedStatement ps = conn.prepareStatement(sql);) {
			for (int i = 0; i < params.length; i++) 
				ps.setObject(i + 1, params[i]);

			effectRows = ps.executeUpdate();
		} catch (SQLException e) {
			LOGGER.warning(e);
		}

		return effectRows;
	}
	
	/**
	 * 在开发过程，SQL语句有可能写错，如果能把运行时出错的 SQL 语句直接打印出来，那对排错非常方便，因为其可以直接拷贝到数据库客户端进行调试。
	 * 
	 * @param sql
	 *            SQL 语句，可以带有 ? 的占位符
	 * @param params
	 *            插入到 SQL 中的参数，可单个可多个可不填
	 * @return 实际 sql 语句
	 */
	public static String printRealSql(String sql, Object[] params) {
		if (!match(sql, params)) {
			System.err.println("SQL 语句中的占位符与参数个数不匹配。SQL：" + sql);
			return null;
		}

		int cols = params.length;
		Object[] values = new Object[cols];
		System.arraycopy(params, 0, values, 0, cols);

		for (int i = 0; i < cols; i++) {
			Object value = values[i];
			if (value instanceof Date) {
				values[i] = "'" + value + "'";
			} else if (value instanceof String) {
				values[i] = "'" + value + "'";
			} else if (value instanceof Boolean) {
				values[i] = (Boolean) value ? 1 : 0;
			}
		}
		
		String statement = String.format(sql.replaceAll("\\?", "%s"), values);

		LOGGER.info("The SQL is: " + statement);

		return statement;
	}
	
	/**
	 * ? 和参数的实际个数是否匹配
	 * 
	 * @param sql
	 *            SQL 语句，可以带有 ? 的占位符
	 * @param params
	 *            插入到 SQL 中的参数，可单个可多个可不填
	 * @return true 表示为 ? 和参数的实际个数匹配
	 */
	private static boolean match(String sql, Object[] params) {
		Matcher m = Pattern.compile("(\\?)").matcher(sql);
		int count = 0;
		while (m.find()) {
			count++;
		}
		return count == params.length;
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
