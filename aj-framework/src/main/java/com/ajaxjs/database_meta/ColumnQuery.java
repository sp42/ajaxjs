package com.ajaxjs.database_meta;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

import com.ajaxjs.database_meta.model.Column;
import com.ajaxjs.sql.JdbcHelper;
import com.ajaxjs.util.logger.LogHelper;

/**
 * 列信息查询
 * 
 * @author frank
 *
 */
public class ColumnQuery extends BaseMetaQuery {
	private static final LogHelper LOGGER = LogHelper.getLog(ColumnQuery.class);

	public ColumnQuery(Connection conn) {
		super(conn);
	}

	/**
	 * 获取一张表的各个字段的注释
	 *
	 * @param tableName 单张表名
	 * @param dbName    数据库名，可选的
	 * @return 一张表的各个字段的注释
	 */
	public List<Column> getColumnComment(String tableName, String dbName) {
		String target = "";

		if (StringUtils.hasText(dbName))
			target += dbName + ".";

		target += tableName;

		List<Column> list = new ArrayList<>();
		JdbcHelper.query(conn, "SHOW FULL COLUMNS FROM " + target, rs -> rs2list(rs, list));

		return list;
	}

	/**
	 * 获取多张表的各个字段的注释
	 *
	 * @param tableNames 多张表的表名
	 * @return 包含给个字段注释的 Map，key 是表名，value 是各个列。列中的Map
	 */
	public Map<String, List<Column>> getColumnComment(List<String> tableNames) {
		Map<String, List<Column>> map = new HashMap<>();

		JdbcHelper.stmt(conn, stmt -> {
			for (String tableName : tableNames) {
				JdbcHelper.rsHandle(stmt, "SHOW FULL COLUMNS FROM " + tableName, rs -> {
					List<Column> list = new ArrayList<>();
					rs2list(rs, list);
					map.put(tableName, list);
				});
			}
		});

		return map;
	}

	private static Pattern getLength;

	/**
	 * @param rs
	 * @param list
	 */
	private static void rs2list(ResultSet rs, List<Column> list) {
		if (getLength == null)
			getLength = Pattern.compile("\\((\\d+)\\)");

		try {
			while (rs.next()) {
				Column col = new Column();
				col.setName(rs.getString("Field"));
				String type = rs.getString("Type");

				Matcher m = getLength.matcher(type);
				col.setLength(m.find() ? Integer.parseInt(m.group(1)) : 0);
				col.setType(m.replaceAll(""));
				col.setNullValue(rs.getString("Null"));
				col.setComment(rs.getString("Comment"));
				col.setDefaultValue(rs.getString("Default"));

				String key = rs.getString("Key");
				col.setIsKey(StringUtils.hasText(key) && "PRI".equals(key));

				list.add(col);
			}
		} catch (SQLException e) {
			LOGGER.warning(e);
		}
	}
}
