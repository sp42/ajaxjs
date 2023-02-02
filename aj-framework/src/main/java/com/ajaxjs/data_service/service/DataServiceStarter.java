package com.ajaxjs.data_service.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.ajaxjs.data_service.DataSerivceUtils;
import com.ajaxjs.data_service.model.DataServiceConfig;
import com.ajaxjs.data_service.model.DataServiceDml;
import com.ajaxjs.data_service.model.DataServiceEntity;
import com.ajaxjs.data_service.model.DataServiceFieldsMapping;
import com.ajaxjs.data_service.model.DataSourceInfo;
import com.ajaxjs.spring.DiContextUtil;
import com.ajaxjs.sql.JdbcConnection;
import com.ajaxjs.sql.JdbcHelper;
import com.ajaxjs.util.StrUtil;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.util.map.JsonHelper;
import com.ajaxjs.util.map.MapTool;

/**
 * 数据服务初始化
 * 
 * @author Frank Cheung sp42@qq.com
 *
 */
public class DataServiceStarter {
	private static final LogHelper LOGGER = LogHelper.getLog(DataServiceStarter.class);

	public DataServiceStarter(DataServiceConfig cfg) {
		this.cfg = cfg;
	}

	private DataServiceConfig cfg;

	@SuppressWarnings("unchecked")
	public void init(Map<Long, DataSourceInfo> mulitDataSource) {
		if (cfg == null) {
			DataService ds = DiContextUtil.getBean(DataService.class);
			cfg = ds.getCfg();
		}

		DataSource ds = cfg.getDataSource(); // 总配置的数据源
		List<DataServiceEntity> list = null;

		try (Connection conn = JdbcConnection.getConnection(ds)) {
			if (cfg.isMultiDataSource())
				initMulitDataSource(conn, mulitDataSource);

			list = JdbcHelper.queryAsBeanList(DataServiceEntity.class, conn, "SELECT * FROM aj_base.adp_data_service");
		} catch (SQLException e) {
			LOGGER.warning(e);
		}

		if (CollectionUtils.isEmpty(list)) {
			LOGGER.warning("还没有任何配置");
			return;
		} else
			LOGGER.info("读取数据服务的前期配置成功");

		clearData();

		list.forEach((DataServiceEntity item) -> {
			String jsonStr = item.getJson();

			if (StringUtils.hasText(jsonStr)) {
				Map<String, Object> json = JsonHelper.parseMap(jsonStr);

				if (json.containsKey("fieldsMapping")) {// 字段映射
					DataServiceFieldsMapping fieldsMapping = MapTool.map2Bean((Map<String, ?>) json.get("fieldsMapping"),
							DataServiceFieldsMapping.class);
					item.setFieldsMapping(fieldsMapping);
				}

				for (String type : json.keySet()) {
					String urlDir;
					DataSource _ds;

					if (cfg.isMultiDataSource()) {
						if (item.getDatasourceId() == 0) {
							LOGGER.info("非嵌入模式下，命令必须有数据源 id");
							continue;
						}

						// LOGGER.info(item.getDatasourceId() + "");
						DataSourceInfo myds = mulitDataSource.get(item.getDatasourceId());
						if (myds == null) {
//							LOGGER.warning("不存在 id 为 {0} 的数据源", item.getDatasourceId());
							continue;
						}

						_ds = myds.getInstance();
						urlDir = StrUtil.concatUrl(myds.getUrlDir(), item.getUrlDir());
					} else {
						_ds = ds;
						urlDir = item.getUrlDir();
					}

					if ("others".equals(type)) {// 其他类型的命令
						List<Map<String, Object>> customActions = (List<Map<String, Object>>) json.get(type);

						for (Map<String, Object> map : customActions) {
//							LOGGER.info(map.toString());
							saveNode(item, map.get("type").toString(), urlDir, map, _ds);
						}
					} else
						saveNode(item, type, urlDir, (Map<String, Object>) json.get(type), _ds);
				}
			} else
				LOGGER.warning(item.getTableName() + " 未进行配置");
		});
	}

	private static void clearData() {
		DataService.GET.clear();
		DataService.POST.clear();
		DataService.PUT.clear();
		DataService.DELETE.clear();
		DataService.HEAD.clear();
		DataService.OPTION.clear();
		DataService.PATCH.clear(); // 删除配置后仍驻留内存，所以要清空
	}

	/**
	 * 初始化多个数据源
	 * 
	 * @param conn
	 * @param mulitDataSource
	 */
	private void initMulitDataSource(Connection conn, Map<Long, DataSourceInfo> mulitDataSource) {
		mulitDataSource.clear();
		List<DataSourceInfo> findList = JdbcHelper.queryAsBeanList(DataSourceInfo.class, conn, "SELECT * FROM aj_base.adp_datasource WHERE stat != 0");

		findList.forEach(myds -> {
			myds.setInstance(DataSerivceUtils.getDataSourceByDataSourceInfo(myds));
			mulitDataSource.put(myds.getId(), myds);
		});
	}

	/**
	 * 对应保存命令配置
	 *
	 * @param item   表格信息，相当于父级
	 * @param type   命令类型
	 * @param urlDir
	 * @param map
	 * @param ds     数据源
	 */
	private static void saveNode(DataServiceEntity item, String type, String urlDir, Map<String, Object> map, DataSource ds) {
		DataServiceDml dml = new DataServiceDml(map);
		dml.setTableInfo(item);
		dml.setDataSource(ds);
		dml.setType(type);

		// 组合 URL
		if (StringUtils.hasText(dml.getDir()))
			urlDir += "/" + dml.getDir();

		switch (type) {
		case "info":
		case "list":
		case "getOne":
		case "getRows":
		case "getRowsPage":
			DataService.GET.put(urlDir, dml);
			break;
		case "insert":
		case "create":
			DataService.POST.put(urlDir, dml);
			break;
		case "update":
			DataService.PUT.put(urlDir, dml);
			break;
		case "delete":
			DataService.DELETE.put(urlDir, dml);
			break;
		}
	}
}
