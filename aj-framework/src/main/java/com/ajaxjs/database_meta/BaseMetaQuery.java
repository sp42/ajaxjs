package com.ajaxjs.database_meta;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

import com.ajaxjs.util.logger.LogHelper;

public abstract class BaseMetaQuery {
	private static final LogHelper LOGGER = LogHelper.getLog(BaseMetaQuery.class);

	Connection conn;

	public BaseMetaQuery(Connection conn) {
		this.conn = conn;
	}

	/**
	 * 
	 * @param <T>
	 * @param sql     要执行的 SQL 语句
	 * @param cb
	 * @param clz
	 * @return
	 */
	public <T> List<T> getResult(String sql, Function<ResultSet, T> cb, Class<T> clz) {
		List<T> list = new ArrayList<>();

		try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql);) {
			while (rs.next()) {
				T v = cb.apply(rs);
				list.add(v);
			}
		} catch (SQLException e) {
			LOGGER.warning(e);
		}

		return list;
	}

	/**
	 * 
	 * @param <T>
	 * @param sql     要执行的 SQL 语句
	 * @param cb
	 * @param clz
	 * @param isLoop  是否 进行 while (rs.next())
	 * @return
	 */
	public Map<String, String> getMapResult(String sql, BiConsumer<ResultSet, Map<String, String>> cb, boolean isLoop) {
		Map<String, String> map = new HashMap<>();

		try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql);) {
			if (isLoop) {
				while (rs.next())
					cb.accept(rs, map);
			} else
				cb.accept(rs, map);
		} catch (SQLException e) {
			LOGGER.warning(e);
		}

		return map;
	}

	/**
	 * 
	 * @param sql     要执行的 SQL 语句
	 * @param cb
	 * @return
	 */
	public Map<String, String> getMapResult(String sql, BiConsumer<ResultSet, Map<String, String>> cb) {
		return getMapResult(sql, cb, true);
	}
}
