package com.ajaxjs.data_service.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ajaxjs.data_service.model.DataSourceInfo;
import com.ajaxjs.data_service.service.BaseDataSourceService;
import com.ajaxjs.spring.DiContextUtil;
import com.ajaxjs.sql.JdbcHelper;
import com.ajaxjs.util.logger.LogHelper;

/**
 * 数据源配置的 CRUD
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public abstract class BaseDataSourceController {
	private static final LogHelper LOGGER = LogHelper.getLog(BaseDataSourceController.class);

	/**
	 * 返回数据源配置所在的表名
	 * 
	 * @return
	 */
	protected abstract String getTableName();

	@GetMapping
	public List<DataSourceInfo> list() throws SQLException {
		List<DataSourceInfo> list = null;

		try (Connection conn = initDb()) {
			list = JdbcHelper.queryAsBeanList(DataSourceInfo.class, conn, "SELECT * FROM " + getTableName());
		}

		return list;
	}

	@GetMapping("/test/{id}")
	Boolean test(@PathVariable Long id) throws SQLException {
		try (Connection conn = initDb()) {
			String sql = "SELECT * FROM " + getTableName() + " WHERE id = ?";
			DataSourceInfo info = JdbcHelper.queryAsBean(DataSourceInfo.class, conn, sql, id);

			try (Connection connection = BaseDataSourceService.getConnectionByDataSourceInfo(info);) {
				LOGGER.info(connection.getMetaData().getURL());
				return true;
			}
		}
	}

	@PostMapping
	Long create(@RequestBody DataSourceInfo entity) throws SQLException {
		try (Connection conn = initDb()) {
			checkIfIsRepeat(conn, entity, null);

			Long newlyId = (Long) JdbcHelper.createBean(conn, entity, getTableName());

			return newlyId;
		}
	}

	/**
	 * 是否重复数据源编码
	 * 
	 * @param conn
	 * @param entity
	 * @param dsId   数据源 id，非 null 时候表示更新排除自己
	 */
	private void checkIfIsRepeat(Connection conn, DataSourceInfo entity, Long dsId) {
		String sql;

		if (dsId != null)
			sql = "SELECT id FROM " + getTableName() + " WHERE url_dir = ? AND id != " + dsId + " LIMIT 1";
		else
			sql = "SELECT id FROM " + getTableName() + " WHERE url_dir = ? LIMIT 1";

		Long id = JdbcHelper.queryOne(conn, sql, Long.class, entity.getUrlDir());

		if (id != null)
			throw new IllegalArgumentException("已存在相同编码的数据源 " + entity.getUrlDir());
	}

	@PutMapping
	Boolean update(@RequestBody DataSourceInfo entity) throws SQLException {
		if (entity.getId() == null)
			throw new IllegalArgumentException("缺少 id 参数");

		try (Connection conn = initDb()) {
			checkIfIsRepeat(conn, entity, entity.getId());
			return JdbcHelper.updateBean(conn, entity, getTableName()) > 0;
		}
	}

	@DeleteMapping("/{id}")
	Boolean delete(@PathVariable Long id) throws SQLException {
		try (Connection conn = initDb()) {
			return JdbcHelper.deleteById(conn, getTableName(), id);
		}
	}

	public static Connection initDb() {
		DataSource ds = DiContextUtil.getBean(DataSource.class);

		try {
			return ds.getConnection();
		} catch (SQLException e) {
			LOGGER.warning(e);
			return null;
		}
	}
}
