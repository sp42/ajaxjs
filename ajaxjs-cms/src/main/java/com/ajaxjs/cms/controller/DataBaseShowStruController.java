package com.ajaxjs.cms.controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.ajaxjs.framework.BaseController;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.controller.IController;
import com.ajaxjs.mvc.filter.DataBaseFilter;
import com.ajaxjs.mvc.filter.MvcFilter;
import com.ajaxjs.orm.JdbcConnection;
import com.ajaxjs.orm.JdbcHelper;

@Path("/admin/DataBaseShowStru")
public class DataBaseShowStruController implements IController {
	@GET
	public String show(ModelAndView mv) {
		return BaseController.cms("database-show-stru");
	}

	@GET
	@Path("showTables")
	@Produces(MediaType.APPLICATION_JSON)
	@MvcFilter(filters = DataBaseFilter.class)
	public String showTables(ModelAndView mv) {
		Connection conn = JdbcConnection.getConnection();

		List<String> tables = DataBaseShowStruController.getAllTableName(conn);

		return BaseController.toJson(tables);
	}
	
	@GET
	@Path("showTableAllByTableName")
	@Produces(MediaType.APPLICATION_JSON)
	@MvcFilter(filters = DataBaseFilter.class)
	public String showTableAllByTableName(ModelAndView mv, @QueryParam("tableName") @NotNull String tableName) {
		Connection conn = JdbcConnection.getConnection();
		
		List<Map<String, String>> table = DataBaseShowStruController.getColumnCommentByTableName(conn, tableName);
		return BaseController.toJson(table);
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

		JdbcHelper.stmt(conn, stmt -> {
			for (String tableName : tableNames) {
				JdbcHelper.rsHandle(stmt, "SHOW CREATE TABLE " + tableName, rs -> {
					String createDDL = null;
					try {
						if (rs.next())
							createDDL = rs.getString(2);
					} catch (SQLException e) {
						e.printStackTrace();
					}

					String comment = parse(createDDL);
					map.put(tableName, comment);
				});
			}
		});

		return map;
	}

	/**
	 * 返回注释信息
	 * 
	 * @param all
	 * @return
	 */
	private static String parse(String all) {
		if (all == null)
			return null;
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

		JdbcHelper.query(conn, "SHOW TABLES", rs -> {
			try {
				while (rs.next())
					tables.add(rs.getString(1));
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});

		return tables;
	}

	/**
	 * 
	 * @param conn			数据库连接对象
	 * @param tableNames	多张表的表名
	 * @return 包含给个字段注释的 Map，key 是表名，value 是各个列。列中的Map
	 */
	public static Map<String, List<Map<String, String>>> getColumnCommentByTableName(Connection conn, List<String> tableNames) {
		Map<String, List<Map<String, String>>> map = new HashMap<>();

		JdbcHelper.stmt(conn, stmt -> {
			for (String tableName : tableNames) {
				JdbcHelper.rsHandle(stmt, "SHOW FULL COLUMNS FROM " + tableName, rs -> {
					List<Map<String, String>> list = new ArrayList<>();
					rs2list(rs, list);
					map.put(tableName, list);
				});
			}
		});

		return map;
	}

	public static List<Map<String, String>> getColumnCommentByTableName(Connection conn, String tableName) {
		List<Map<String, String>> list = new ArrayList<>();

		JdbcHelper.query(conn, "SHOW FULL COLUMNS FROM " + tableName, rs -> {
			rs2list(rs, list);
		});

		return list;
	}

	private static void rs2list(ResultSet rs, List<Map<String, String>> list) {
		try {
			while (rs.next()) {
				Map<String, String> fieldInfos = new HashMap<>();
				fieldInfos.put("name", rs.getString("Field"));
				fieldInfos.put("type", rs.getString("Type"));
				fieldInfos.put("null", rs.getString("Null"));
				fieldInfos.put("comment", rs.getString("Comment"));
				list.add(fieldInfos);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
