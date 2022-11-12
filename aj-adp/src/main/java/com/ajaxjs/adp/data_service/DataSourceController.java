package com.ajaxjs.adp.data_service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ajaxjs.data_service.model.DataSourceInfo;
import com.ajaxjs.spring.DiContextUtil;
import com.ajaxjs.sql.JdbcHelper;
import com.ajaxjs.util.logger.LogHelper;

@RestController
@RequestMapping("/data_service/datasource")
public class DataSourceController {
	private static final LogHelper LOGGER = LogHelper.getLog(DataSourceController.class);

	@GetMapping
	public List<DataSourceInfo> list() throws SQLException {
		List<DataSourceInfo> list = null;

		try (Connection conn = initDb()) {
			list = JdbcHelper.queryAsBeanList(DataSourceInfo.class, conn, "SELECT * FROM sys_datasource");
		}

		return list;
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
