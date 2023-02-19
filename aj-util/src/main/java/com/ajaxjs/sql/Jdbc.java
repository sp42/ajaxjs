package com.ajaxjs.sql;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import javax.sql.DataSource;

import com.ajaxjs.util.ListUtils;
import com.ajaxjs.util.logger.LogHelper;

public class Jdbc extends JdbcHelper {
	private static final LogHelper LOGGER = LogHelper.getLog(JdbcHelper.class);

	private DataSource ds;

	private Connection conn;

	public Jdbc(DataSource ds) {
		this.ds = ds;
	}

	public Jdbc(Connection conn) {
		this.conn = conn;
	}

	public <T> List<T> queryAsBeanList(String sql, Class<T> beanClz, Object... params) {
		try (Connection conn = (this.conn == null ? ds.getConnection() : this.conn)) {
			List<T> beanList = queryAsBeanList(beanClz, conn, sql, params);

			return ListUtils.getList(beanList);
		} catch (SQLException e) {
			LOGGER.warning(e);
			return Collections.emptyList();
		}
	}

	public Serializable createBean(Object bean, String tableName) {
		try (Connection conn = (this.conn == null ? ds.getConnection() : this.conn)) {
			return createBean(conn, bean, tableName);
		} catch (SQLException e) {
			LOGGER.warning(e);
			return false;
		}
	}

	public int updateBean(Object bean, String tableName) {
		try (Connection conn = (this.conn == null ? ds.getConnection() : this.conn)) {
			return updateBean(conn, bean, tableName);
		} catch (SQLException e) {
			LOGGER.warning(e);
			return 0;
		}
	}

	public boolean delete(Serializable id, String tableName) {
		try (Connection conn = (this.conn == null ? ds.getConnection() : this.conn)) {
			return deleteById(conn, tableName, id);
		} catch (SQLException e) {
			LOGGER.warning(e);
			return false;
		}
	}

	public boolean deleteInId(String tableName, String ids) {
		try (Connection conn = (this.conn == null ? ds.getConnection() : this.conn)) {
			return update(conn, "DELETE FROM " + tableName + " WHERE id IN ?", ids) == 1;
		} catch (SQLException e) {
			LOGGER.warning(e);
			return false;
		}
	}

}
