package com.ajaxjs.cms.controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.ajaxjs.framework.BaseController;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.controller.IController;

@Path("/admin/DataBaseShowStru")
public class DataBaseShowStruController implements IController {
	@GET
	public String show(ModelAndView mv, HttpServletRequest request) {
		return  BaseController.cms("database-show-stru");
	}

	/**
	 * 获得某表的注释
	 * 
	 * @param tableName 表名
	 * @return 表注释
	 */
	public static String getCommentByTableName(Connection conn, String tableName) {
		try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SHOW CREATE TABLE " + tableName);) {
			if (rs != null && rs.next()) {
				String createDDL = rs.getString(2);
				return parse(createDDL);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 获得多张表的注释
	 * 
	 * @param tableNames 表名集合
	 * @return 表注释集合
	 */
	public static Map<String, String> getCommentByTableName(Connection conn, List<String> tableNames) {
		Map<String, String> map = new HashMap<>();

		try (Statement stmt = conn.createStatement();) {
			for (String tableName : tableNames) {
				try (ResultSet rs = stmt.executeQuery("SHOW CREATE TABLE " + tableName);) {
					if (rs != null && rs.next()) {
						String createDDL = rs.getString(2);
						String comment = parse(createDDL);
						map.put(tableName, comment);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return map;
	}

	/**
	 * 返回注释信息
	 * 
	 * @param all
	 * @return
	 */
	private static String parse(String all) {
		String comment = null;
		int index = all.indexOf("COMMENT='");
		if (index < 0)
			return "";

		comment = all.substring(index + 9);
		comment = comment.substring(0, comment.length() - 1);
		return comment;
	}

	/**
	 * 获取当前数据库下的所有表名称
	 * 
	 * @return 所有表名称
	 */
	public static List<String> getAllTableName(Connection conn) {
		List<String> tables = new ArrayList<>();
		try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SHOW TABLES ");) {
			while (rs.next())
				tables.add(rs.getString(1));
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return tables;
	}

	public static Map<String, List<Map<String, String>>> getColumnCommentByTableName(Connection conn, List<String> tableNames) {
		Map<String, List<Map<String, String>>> map = new HashMap<>();

		try (Statement stmt = conn.createStatement();) {
			for (String tableName : tableNames) {
				try (ResultSet rs = stmt.executeQuery("show full columns from " + tableName);) {
					List<Map<String, String>> list = new ArrayList<>();

					while (rs.next()) {
						Map<String, String> fieldInfos = new HashMap<>();
						fieldInfos.put("name", rs.getString("Field"));
						fieldInfos.put("type", rs.getString("Type"));
						fieldInfos.put("null", rs.getString("Null"));
						fieldInfos.put("comment", rs.getString("Comment"));
						list.add(fieldInfos);
					}

					map.put(tableName, list);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return map;
	}

	public static List<Map<String, String>> getColumnCommentByTableName(Connection conn, String tableName) {
		List<Map<String, String>> list = new ArrayList<>();

		try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("show full columns from " + tableName);) {
			rs2list(rs, list);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;
	}

	private static void rs2list(ResultSet rs, List<Map<String, String>> list) throws SQLException {
		while (rs.next()) {
			Map<String, String> fieldInfos = new HashMap<>();
			fieldInfos.put("name", rs.getString("Field"));
			
			fieldInfos.put("type", rs.getString("Type"));
			fieldInfos.put("null", rs.getString("Null"));
			fieldInfos.put("comment", rs.getString("Comment"));
			list.add(fieldInfos);
		}
	}
}
