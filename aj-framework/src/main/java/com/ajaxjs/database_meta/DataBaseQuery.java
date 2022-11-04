package com.ajaxjs.database_meta;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ajaxjs.sql.JdbcHelper;

/**
 * 数据库信息查询
 * 
 * @author frank
 *
 */
public class DataBaseQuery extends BaseMetaQuery {
	public DataBaseQuery(Connection conn) {
		super(conn);
	}

	/**
	 * 获取所有库名
	 * 
	 * @param conn
	 * @return
	 */
	public List<String> getDatabase() {
		List<String> list = new ArrayList<>();

		JdbcHelper.query(conn, "SHOW DATABASES", rs -> {
			try {
				while (rs.next())
					list.add(rs.getString("Database"));
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});

		return list;
	}
}
