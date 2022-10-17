package com.ajaxjs.data_service.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.ajaxjs.data_service.model.DataServiceConstant.DBType;
import com.ajaxjs.data_service.model.MyDataSource;
import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.framework.QueryTools;
import com.ajaxjs.spring.DiContextUtil;
import com.ajaxjs.sql.JdbcConnection;
import com.ajaxjs.sql.util.DataBaseMetaHelper;
import com.ajaxjs.util.WebHelper;
import com.ajaxjs.util.logger.LogHelper;

public class DataSourceService implements IDataSourceService {
	private static final LogHelper LOGGER = LogHelper.getLog(DataSourceService.class);

	@Override
	public List<MyDataSource> list(String appId) {
		Function<String, String> handler = BaseService::searchQuery_NameOnly;
		handler = handler.andThen(QueryTools.byAny()).andThen(BaseService::betweenCreateDate);

		if (appId != null)
			handler = handler.andThen(QueryTools.by("appId", appId));

		return DataSourceDAO.findList(handler);
	}

	@Override
	public Long create(MyDataSource entity) {
		String is = isRepeatUrlDir(entity);

		if (is != null)
			throw new NullPointerException(is);

		return DataSourceDAO.create(entity);
	}

	@Override
	public Boolean update(long id) {
		MyDataSource entity = WebHelper.getParameterBean(DiContextUtil.getRequest(), MyDataSource.class);

		LOGGER.info("更新数据源" + entity.getName());
		String is = isRepeatUrlDir(entity);

		if (is != null)
			throw new NullPointerException(is);

		entity.setId(id);

		return DataSourceDAO.update(entity) >= 1;
	}

	/**
	 * 是否重复数据源编码
	 *
	 * @param entity 数据源
	 * @return 是否重复
	 */
	private static String isRepeatUrlDir(MyDataSource entity) {
		Long id = entity.getId() == null ? 0L : entity.getId();
		boolean isRepeatUrlDir = !DataSourceDAO.isRepeatUrlDir(entity.getUrlDir(), id);

		return isRepeatUrlDir ? "已存在 URL 目录[" + entity.getUrlDir() + "]" : null;
	}

	@Override
	public List<TableInfo> getSelectTables(Long dataSourceId, String dbName) throws ClassNotFoundException, SQLException {
		String[] selectedTables = DataSourceDAO.findSelectedTables(dataSourceId); // ???

		if (ObjectUtils.isEmpty(selectedTables))
			throw new NullPointerException("没有数据");

		List<TableInfo> list = new ArrayList<>();
		Map<String, Boolean> sMap = new HashMap<>();

		for (String sT : selectedTables)
			sMap.put(sT, true);

		try (Connection conn = getConnection(dataSourceId)) {
			Map<String, String> tableComment = DataBaseMetaHelper.getTableComment(conn, DataBaseMetaHelper.getAllTableName(conn, dbName), dbName);

			for (String tableName : tableComment.keySet()) {
				TableInfo info = new TableInfo();
				info.tableName = tableName;
				info.comment = tableComment.get(tableName);
				info._checked = sMap.containsKey(tableName);

				list.add(info);
			}
		}

		return list;
	}

	@Override
	public PageResult<Map<String, Object>> getAllTables(Integer start, Integer limit, String tablename, String dbName) throws SQLException {
		LOGGER.info("查询表名和表注释");

		Connection conn = JdbcConnection.getConnection();
		return getTableAndComment(conn, start, limit, tablename, dbName);
	}

	@Override
	public PageResult<Map<String, Object>> getTableAndComment(Long dataSourceId, Integer start, Integer limit, String tablename, String dbName)
			throws ClassNotFoundException, SQLException {
		LOGGER.info("查询表名和表注释");

		if (start == null)
			start = 0;
		if (limit == null)
			limit = 99;

		return getTableAndComment(getConnection(dataSourceId), start, limit, tablename, dbName);
	}

	@Override
	public List<Map<String, String>> getFields(Long datasourceId, String tableName, String dbName) throws ClassNotFoundException, SQLException {
		LOGGER.info("获取所有字段:" + tableName + " 数据库：" + dbName);

		return getField(datasourceId, tableName, dbName);
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
			if (allTableName.contains("bdp_data_service"))
				allTableName.remove("bdp_data_service");

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
	 * 获取一张表的各个字段的注释
	 *
	 * @param datasourceId
	 * @param tableName
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public static List<Map<String, String>> getField(Long datasourceId, String tableName, String dbName) throws SQLException, ClassNotFoundException {
		MyDataSource ds = DataSourceDAO.findById(datasourceId);

		try (Connection conn = getConnection(ds)) {
			List<Map<String, String>> columnComment = DataBaseMetaHelper.getColumnComment(conn, tableName, dbName);

			return columnComment;
		}
	}

	/**
	 * 根据数据源记录之 id，返回连接字符串、用户密码等信息，进行数据库连接
	 *
	 * @param datasourceId 数据源记录之 id
	 * @return 数据库连对象
	 */
	public static Connection getConnection(long datasourceId) throws SQLException, ClassNotFoundException {
		return getConnection(DataSourceDAO.findById(datasourceId));
	}

	/**
	 * 返回适合数据库的驱动名称
	 *
	 * @param ds
	 * @return 数据库的驱动名称
	 */
	public static String getDbDriver(MyDataSource ds) {
		switch (ds.getType()) {
		case DBType.MYSQL:
			return "com.mysql.cj.jdbc.Driver";
		case DBType.ORACLE:
			return "com.mysql.cj.jdbc.Driver";
		case DBType.SQLSERVER:
			return "com.mysql.cj.jdbc.Driver";
		case DBType.SPARK:
			return "com.mysql.cj.jdbc.Driver";
		case DBType.SQLITE:
			return "com.mysql.cj.jdbc.Driver";
		}

		return null;
	}

	/**
	 * @param ds
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public static Connection getConnection(MyDataSource ds) throws SQLException, ClassNotFoundException {
		Class.forName(getDbDriver(ds));

		return DriverManager.getConnection(ds.getUrl(), ds.getUsername(), ds.getPassword());
	}

	/**
	 * 返回指定数据源中数据库里面所有的表名
	 *
	 * @param datasourceId 数据源记录之 id
	 * @return 表名集合，为列表
	 */
	public static List<String> getAllTableName(long datasourceId, String dbName) throws SQLException, ClassNotFoundException {
		Connection conn = getConnection(datasourceId);
		List<String> tables = DataBaseMetaHelper.getAllTableName(conn, dbName);

		try {
			conn.close();
		} catch (SQLException e) {
			LOGGER.warning(e);
		}

		return tables;
	}
}
