package com.ajaxjs.adp.data_service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ajaxjs.data_service.model.DataSourceInfo;
import com.ajaxjs.data_service.service.BaseDataSourceService;
import com.ajaxjs.sql.JdbcHelper;
import com.ajaxjs.sql.util.DataBaseMetaHelper;
import com.ajaxjs.util.logger.LogHelper;

/**
 * 数据服务 后台控制器
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
@RestController
@RequestMapping("/data_service")
public class DataServiceAdminController {
	private static final LogHelper LOGGER = LogHelper.getLog(DataServiceAdminController.class);

	@GetMapping("/get_databases/{datasourceId}")
	public List<String> getDatabases(@PathVariable Long datasourceId) throws SQLException {
		LOGGER.info("查询数据库 {0}", datasourceId);
		List<String> databases;

		try (Connection conn = DataSourceController.initDb()) {
			String sql = "SELECT * FROM aj_base.adp_datasource WHERE id = ?";
			DataSourceInfo info = JdbcHelper.queryAsBean(DataSourceInfo.class, conn, sql, datasourceId);

			if (info.getCrossDb() == null || !info.getCrossDb())
				throw new NullPointerException("不是跨库的数据库连接");

			try (Connection conn2 = BaseDataSourceService.getConnection(info)) {
				databases = DataBaseMetaHelper.getDatabase(conn2);
			}
		}

		return databases;
	}

	@GetMapping("/reload")
	public Boolean reload() {
//		apiController.init();// 重新加载配置
		return true;
	}
}
