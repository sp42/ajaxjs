package com.ajaxjs.mysql;

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

/**
 * 数据库元数据查询
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public abstract class BaseMetaQuery {
	private static final LogHelper LOGGER = LogHelper.getLog(BaseMetaQuery.class);

	/**
	 * 
	 * @param <T>
	 * @param connect 数据库连接对象
	 * @param sql     要执行的 SQL 语句
	 * @param cb
	 * @param clz
	 * @return
	 */
	public static <T> List<T> getResult(Connection connect, String sql, Function<ResultSet, T> cb, Class<T> clz) {
		List<T> list = new ArrayList<>();

		try (Statement st = connect.createStatement(); ResultSet rs = st.executeQuery(sql);) {
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
	 * @param connect 数据库连接对象
	 * @param sql     要执行的 SQL 语句
	 * @param cb
	 * @param clz
	 * @param isLoop  是否 进行 while (rs.next())
	 * @return
	 */
	public static Map<String, String> getMapResult(Connection connect, String sql, BiConsumer<ResultSet, Map<String, String>> cb, boolean isLoop) {
		Map<String, String> map = new HashMap<>();

		try (Statement st = connect.createStatement(); ResultSet rs = st.executeQuery(sql);) {
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
	 * @param connect 数据库连接对象
	 * @param sql     要执行的 SQL 语句
	 * @param cb
	 * @return
	 */
	public static Map<String, String> getMapResult(Connection connect, String sql, BiConsumer<ResultSet, Map<String, String>> cb) {
		return getMapResult(connect, sql, cb, true);
	}
}
