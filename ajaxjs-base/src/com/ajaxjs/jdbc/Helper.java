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
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ajaxjs.util.StringUtil;

/**
 * 数据库工具类，跟具体业务无关
 * @author frank
 *
 */
public class Helper {
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
	 * 记录集合转换为 Map
	 * @param conn
	 * @param sql
	 * @return Map 结果
	 */
	public static Map<String, Object> queryMap(Connection conn, String sql) {
		try (Statement statement = conn.createStatement(); ResultSet rs = statement.executeQuery(sql);) {
			if (rs.isBeforeFirst()) {
				return getResultMap(rs);
			} else {
				System.err.println("查询 SQL：" + sql + " 没有符合的记录！");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
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
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static String perRecordSql  = "SELECT id, name FROM %s WHERE createDate < datetime('%s') ORDER BY createDate DESC LIMIT 1";
	public static String nextRecordSql = "SELECT id, name FROM %s WHERE createDate > datetime('%s') ORDER BY createDate ASC LIMIT 1";
	
	public static Map<String, Map<String, Object>> getNeighbor(Connection conn, String tablename, String datetime){
		Map<String, Map<String, Object>> map = new HashMap<>();

		String _perRecordSql = String.format(nextRecordSql, tablename, datetime);
		map.put("perRecord", queryMap(conn, _perRecordSql));
		
		String _nextRecordSql = String.format(perRecordSql, tablename, datetime);
		map.put("nextRecord", queryMap(conn, _nextRecordSql));
		
		return map;
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
