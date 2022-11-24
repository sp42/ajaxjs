package com.ajaxjs.adp.data_service;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ajaxjs.data_service.model.DataSourceInfo;
import com.ajaxjs.data_service.service.BaseDataSourceService;
import com.ajaxjs.spring.DiContextUtil;
import com.ajaxjs.sql.JdbcHelper;
import com.ajaxjs.util.logger.LogHelper;

@RestController
@RequestMapping("/data_service/datasource")
public class DataSourceController {
	private static final LogHelper LOGGER = LogHelper.getLog(DataSourceController.class);

	final static String tableName = "sys_datasource";

	@GetMapping
	public List<DataSourceInfo> list() throws SQLException {
		List<DataSourceInfo> list = null;

		try (Connection conn = initDb()) {
			list = JdbcHelper.queryAsBeanList(DataSourceInfo.class, conn, "SELECT * FROM " + tableName);
		}

		return list;
	}

	@GetMapping("/test/{id}")
	Boolean test(@PathVariable Long id) throws SQLException {
		try (Connection conn = initDb()) {
			String sql = "SELECT * FROM " + tableName + " WHERE id = ?";
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
			checkIfIsRepeat(conn, entity);

			Long newlyId = (Long) JdbcHelper.createBean(conn, entity, tableName);

			return newlyId;
		}
	}

	/**
	 * 是否重复数据源编码
	 * 
	 * @param conn
	 * @param entity
	 */
	private void checkIfIsRepeat(Connection conn, DataSourceInfo entity) {
		Long id = JdbcHelper.queryOne(conn, "SELECT id FROM " + tableName + " WHERE name = ? LIMIT 1", Long.class, entity.getName());

		if (id != null)
			throw new IllegalArgumentException("已存在相同名称的数据源 " + entity.getName());
	}

	@PutMapping
	Boolean update(@RequestBody DataSourceInfo entity) throws SQLException {
		if (entity.getId() == null)
			throw new IllegalArgumentException("缺少 id 参数");

		try (Connection conn = initDb()) {
			checkIfIsRepeat(conn, entity);
			return JdbcHelper.updateBean(conn, entity, tableName) > 0;
		}
	}

	@DeleteMapping("/{id}")
	Boolean delete(@PathVariable Long id) throws SQLException {
		try (Connection conn = initDb()) {
			return JdbcHelper.deleteById(conn, tableName, id);
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