package com.ajaxjs.data_service.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ajaxjs.data_service.DataSerivceUtils;
import com.ajaxjs.data_service.model.DataServiceEntity;
import com.ajaxjs.data_service.model.DataSourceInfo;
import com.ajaxjs.data_service.service.DataService;
import com.ajaxjs.framework.IBaseController;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.spring.easy_controller.ControllerMethod;
import com.ajaxjs.sql.JdbcConnection;
import com.ajaxjs.sql.JdbcHelper;
import com.ajaxjs.sql.util.DataBaseMetaHelper;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.util.map.JsonHelper;
import com.ajaxjs.util.regexp.RegExpUtils;

/**
 * 数据服务 后台控制器
 */
public abstract class BaseDataServiceAdminController implements IBaseController<DataServiceEntity> {
	private static final LogHelper LOGGER = LogHelper.getLog(BaseDataServiceAdminController.class);

	@Autowired
	private DataService dataService;

	/**
	 * 返回数据源配置所在的表名
	 *
	 * @return
	 */
	protected abstract String getDataSourceTableName();

	/**
	 * 返回数据服务配置所在的表名
	 *
	 * @return
	 */
	protected abstract String getDataServiceTableName();

	/**
	 * 返回数据库连接
	 *
	 * @return
	 */
	protected abstract Connection getConnection();

	@GetMapping("/reload")
	public Boolean reload() {
		dataService.init();// 重新加载配置

		return true;
	}

	@GetMapping("/{id}")
	public DataServiceEntity info(@PathVariable long id, String dbName) throws ClassNotFoundException, SQLException {
		LOGGER.info("加载表详情");

		Connection conn;
		DataServiceEntity info;

		try (Connection _conn = getConnection()) {
			String sql = "SELECT * FROM " + getDataServiceTableName() + " WHERE id = " + id;
			info = JdbcHelper.queryAsBean(DataServiceEntity.class, _conn, sql);

			if (info == null)
				throw new NullPointerException("找不到 id 為 " + id + " 的数据服务配置。");

			// 获取所有字段
			if (dataService.getCfg().isMultiDataSource())
				conn = DataSerivceUtils.getConnByDataSourceInfo(_conn, getDataSourceTableName(), info.getDatasourceId());
			else
				conn = JdbcConnection.getConnection();
		}

		List<Map<String, String>> columnComment = null;

		try {
			columnComment = DataBaseMetaHelper.getColumnComment(conn, info.getTableName(), dbName);
		} catch (Throwable e) {
			LOGGER.warning(e);
		} finally {
			conn.close();
		}

		if (columnComment != null) {
			Map<String, String> map = new HashMap<>();

			for (int i = 0; i < columnComment.size(); i++)
				map.put(columnComment.get(i).get("name"), columnComment.get(i).get("comment"));

			info.setFields(map);
		}

		str2Json(info);

		return info;
	}

	@GetMapping
	public List<DataServiceEntity> list(Long datasourceId) {
		LOGGER.info("获取表配置列表");

		try (Connection conn = getConnection()) {
			List<DataServiceEntity> list = JdbcHelper.queryAsBeanList(DataServiceEntity.class, conn, "SELECT * FROM " + getDataServiceTableName());

			for (DataServiceEntity e : list)
				str2Json(e);

			return list;
		} catch (SQLException e) {
			LOGGER.warning(e);
			throw new RuntimeException(e);
		}
	}

	private static void str2Json(DataServiceEntity e) {
		String json = e.getJson();
		Map<String, Object> map = JsonHelper.parseMap(json);
		e.setData(map);
		e.setJson(null);
	}

	@PostMapping
	@Override
	public DataServiceEntity create(@RequestBody DataServiceEntity entity) {
		LOGGER.info("创建 DataService");

		String url = entity.getUrlDir().replaceAll("\\.", "_"); // 不能加 . 否则 URL 解析错误
		entity.setUrlDir(url);
		entity.setUrlDir(entity.getTableName());
//        LOGGER.info("" + entity.getDatasourceId());
//        LOGGER.info(DataServiceAdminService.DAO.toString());

		try (Connection conn = getConnection()) {
			Long dsId = entity.getDatasourceId();
			DataServiceEntity repeatUrlDir = getRepeatUrlDir(conn, dsId, url);

			if (repeatUrlDir != null) {
				// 已经有重复的
				String maxId = getMaxId(conn, dsId, url);
				String dig = "";

				if (maxId != null) {
					dig = RegExpUtils.regMatch("\\d+$", maxId);
					int i = Integer.parseInt(dig);
					dig = (++i) + "";
				} else
					dig = "1";

				entity.setUrlDir(entity.getUrlDir() + "_" + dig);
			}

			Long newlyId = (Long) JdbcHelper.createBean(conn, entity, getDataServiceTableName());
			dataService.init(); // 重新加载配置
			entity.setId(newlyId);

			return entity;
		} catch (SQLException e) {
			LOGGER.warning(e);
			throw new RuntimeException(e);
		}
	}

	private DataServiceEntity getRepeatUrlDir(Connection conn, Long dsId, String url) {
		String sql;

		if (dsId == null) {
			sql = "SELECT id FROM ${tableName} WHERE urlDir = ? LIMIT 1";
			return JdbcHelper.queryAsBean(DataServiceEntity.class, conn, getDataServiceTableName(), sql, url);
		} else {
			sql = "SELECT id FROM ${tableName} WHERE urlDir = ? AND datasourceId = ? LIMIT 1";
			return JdbcHelper.queryAsBean(DataServiceEntity.class, conn, getDataServiceTableName(), sql, url, dsId);
		}
	}

	private String getMaxId(Connection conn, Long dsId, String url) {
		String sql;

		if (dsId == null) {
			sql = "SELECT urlDir FROM ${tableName} WHERE urlDir REGEXP CONCAT(?, '_[0-9]+$') ORDER BY urlDir DESC LIMIT 1";
			return JdbcHelper.queryOne(conn, sql, String.class, url);
		} else {
			sql = "SELECT urlDir FROM ${tableName} WHERE urlDir REGEXP CONCAT(?, '_[0-9]+$') AND datasourceId = ? ORDER BY urlDir DESC LIMIT 1";
			return JdbcHelper.queryOne(conn, sql, String.class, url, dsId);
		}
	}

	@PutMapping
	@Override
	public Boolean update(@RequestBody DataServiceEntity entity) {
		try (Connection conn = getConnection()) {
			JdbcHelper.updateBean(conn, entity, getDataServiceTableName());
			dataService.init();// 重新加载配置

			return true;
		} catch (SQLException e) {
			LOGGER.warning(e);
			throw new RuntimeException(e);
		}
	}

	@DeleteMapping("/{id}")
	@Override
	public Boolean delete(@PathVariable long id) {
		LOGGER.info("删除配置 {0}", id);
		DataServiceEntity dataServiceTable = new DataServiceEntity();
		dataServiceTable.setId(id);

		try (Connection conn = getConnection()) {
			return JdbcHelper.delete(conn, dataServiceTable, getDataServiceTableName());
		} catch (SQLException e) {
			LOGGER.warning(e);
			throw new RuntimeException(e);
		}
	}

	@GetMapping("/get_databases/{datasourceId}")
	@ControllerMethod("查询数据库所有的库名")
	public List<String> getDatabases(@PathVariable Long datasourceId) throws SQLException {
		LOGGER.info("查询数据库所有的库名 {0}", datasourceId);
		List<String> databases;

		try (Connection conn = getConnection()) {
			DataSourceInfo info = DataSerivceUtils.getDataSourceInfoById(conn, getDataSourceTableName(), datasourceId);

			if (info.getCrossDb() == null || !info.getCrossDb())
				throw new NullPointerException("不是跨库的数据库连接");

			try (Connection conn2 = DataSerivceUtils.getConnection(info)) {
				databases = DataBaseMetaHelper.getDatabase(conn2);
			}
		}

		return databases;
	}

	/**
	 * 单数据源返回数据源下的表名和表注释
	 *
	 * @param start
	 * @param limit
	 * @param tablename 搜索的关键字
	 * @deprecated
	 * @return
	 * @throws SQLException
	 */
	@GetMapping("/getAllTables")
	public PageResult<Map<String, Object>> getAllTables(Integer start, Integer limit, String tablename, String dbName) throws SQLException {
		LOGGER.info("查询表名和表注释");

		Connection conn = JdbcConnection.getConnection();
		return getTableAndComment(conn, start, limit, tablename, dbName);
	}

	/**
	 * 指定数据源返回数据源下的表名和表注释
	 *
	 * @param dataSourceId
	 * @param start
	 * @param limit
	 * @param tablename    搜索的关键字
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */

	@GetMapping("/{dataSourceId}/getAllTables")
	public PageResult<Map<String, Object>> getTableAndComment(@PathVariable Long dataSourceId, Integer start, Integer limit, String tablename, String dbName)
			throws ClassNotFoundException, SQLException {
		LOGGER.info("查询表名和表注释");

		if (start == null)
			start = 0;
		if (limit == null)
			limit = 99;

		return getTableAndComment(DataSerivceUtils.getConnByDataSourceInfo(getConnection(), getDataSourceTableName(), dataSourceId), start, limit, tablename,
				dbName);
	}

	@GetMapping("/{dataSourceId}/getFields/{tableName}")
	public List<Map<String, String>> getFields(@PathVariable Long dataSourceId, @PathVariable String tableName, String dbName)
			throws SQLException, ClassNotFoundException {
		LOGGER.info("获取所有字段:" + tableName + " 数据库：" + dbName);

		return getField(dataSourceId, tableName, dbName);
	}

	/**
	 * 返回数据源下的表名和表注释，支持分页和表名搜索
	 *
	 * @param _conn
	 * @param start
	 * @param limit
	 * @param tablename 搜索的关键字
	 * @return
	 * @throws SQLException
	 */
	private static PageResult<Map<String, Object>> getTableAndComment(Connection _conn, Integer start, Integer limit, String tablename, String dbName)
			throws SQLException {
		int total = 0;
		List<Map<String, Object>> list = null;

		try (Connection conn = _conn) {
			List<String> allTableName = DataBaseMetaHelper.getAllTableName(conn, dbName);

			// 有可能出现配置表本身，删除
			if (allTableName.contains("adp_data_service"))
				allTableName.remove("adp_data_service");

			if (StringUtils.hasLength(tablename)) // 搜索关键字
				allTableName = allTableName.stream().filter(item -> item.contains(tablename)).collect(Collectors.toList());

			total = allTableName.size();

			if (total > 0) {
//                List<String> subList = allTableName.subList(start, limit); // 有坑 会返回空 List
				List<String> subList = new ArrayList<>();

				for (int i = start; i < (start + limit); i++) {
					if (i < total)
						subList.add(allTableName.get(i));
				}

				list = DataBaseMetaHelper.getTableCommentWithAnnotateAsList(conn, subList, dbName);
			}
		}

		PageResult<Map<String, Object>> result = new PageResult<>();

		if (list != null)
			result.addAll(list);

		// 排序
		Comparator<Map<String, Object>> byName = (o1, o2) -> o1.get("tableName").toString().compareTo(o2.get("tableName").toString());
		result.sort(byName);
		result.setTotalCount(total);

		return result;
	}

	/**
	 * 获取所有字段
	 *
	 * @param datasourceId
	 * @param tableName
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	private List<Map<String, String>> getField(Long datasourceId, String tableName, String dbName) throws SQLException, ClassNotFoundException {
		try (Connection conn = getConnection(); Connection conn2 = DataSerivceUtils.getConnByDataSourceInfo(conn, getDataSourceTableName(), datasourceId);) {
			List<Map<String, String>> columnComment = DataBaseMetaHelper.getColumnComment(conn2, tableName, dbName);

			return columnComment;
		}
	}
}
