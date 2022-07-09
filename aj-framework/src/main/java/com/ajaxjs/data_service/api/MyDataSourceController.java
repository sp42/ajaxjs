package com.ajaxjs.data_service.api;

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

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.MediaType;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ajaxjs.data_service.DataServiceDAO;
import com.ajaxjs.data_service.model.DataServiceConstant.DBType;
import com.ajaxjs.data_service.model.MyDataSource;
import com.ajaxjs.framework.BaseController;
import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.framework.QueryTools;
import com.ajaxjs.sql.JdbcConnection;
import com.ajaxjs.sql.util.DataBaseMetaHelper;
import com.ajaxjs.util.WebHelper;
import com.ajaxjs.util.filter.DataBaseFilter;
import com.ajaxjs.util.logger.LogHelper;

/**
 * 数据源管理控制器的基类
 */
@RestController
@RequestMapping("/admin/datasource")
public class MyDataSourceController extends BaseController implements DataServiceDAO {
	private static final LogHelper LOGGER = LogHelper.getLog(MyDataSourceController.class);

	@DataBaseFilter
	@GetMapping
	public List<MyDataSource> list(HttpServletRequest req, String appId) {
		LOGGER.info("数据源列表" + appId);

		Function<String, String> handler = BaseService::searchQuery_NameOnly;
		handler = handler.andThen(QueryTools.byAny(req)).andThen(BaseService::betweenCreateDate);

		if (appId != null)
			handler = handler.andThen(QueryTools.by("appId", appId));

		return DataSourceDAO.findList(handler);
	}

	@DataBaseFilter
	@PostMapping(produces = JSON)
	public String create(MyDataSource entity) {
		String is = isRepeatUrlDir(entity);

		if (is != null)
			return is;

		return afterCreate(DataSourceDAO.create(entity));
	}

	@DataBaseFilter
	@PutMapping(value = ID_INFO, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = JSON)
	public String update(@PathVariable long id, HttpServletRequest req) {
		MyDataSource entity = WebHelper.getParameterBean(req, MyDataSource.class);

		LOGGER.info("更新数据源" + entity.getName());
		String is = isRepeatUrlDir(entity);

		if (is != null)
			return is;

		entity.setId(id);

		if (DataSourceDAO.update(entity) >= 1)
			return jsonOk("修改成功");
		else
			return jsonOk("修改成功");
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

		return isRepeatUrlDir ? jsonNoOk("已存在 URL 目录[" + entity.getUrlDir() + "]") : null;
	}

	@DataBaseFilter
	@DeleteMapping(value = ID_INFO, produces = JSON)
	public String delete(@PathVariable long id) {
		MyDataSource myDataSource = new MyDataSource();
		myDataSource.setId(id);

		if (DataSourceDAO.delete(myDataSource))
			return jsonOk("删除成功");
		else
			return jsonOk("删除失败");
	}

	/**
	 * 获取某个数据源下面的所有表
	 *
	 * @param dataSourceId 数据源 id
	 * @return 所有表
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @deprecated
	 */
	@DataBaseFilter
	@GetMapping(value = "/{id}/getSelectTables", produces = JSON)
	public String getSelectTables(@PathVariable(ID) Long dataSourceId, String dbName) throws ClassNotFoundException, SQLException {
		List<Map<String, Object>> list = new ArrayList<>();

		String[] selectedTables = DataSourceDAO.findSelectedTables(dataSourceId);

		if (ObjectUtils.isEmpty(selectedTables))
			return jsonNoOk("没有数据");

		Map<String, Boolean> sMap = new HashMap<>();

		for (String sT : selectedTables)
			sMap.put(sT, true);

		try (Connection conn = getConnection(dataSourceId)) {
			Map<String, String> tableComment = DataBaseMetaHelper.getTableComment(conn, DataBaseMetaHelper.getAllTableName(conn, dbName), dbName);

			for (String tableName : tableComment.keySet()) {
				Map<String, Object> map = new HashMap<>();
				map.put("tableName", tableName);
				map.put("comment", tableComment.get(tableName));
				map.put("_checked", sMap.containsKey(tableName));

				list.add(map);
			}
		}

		return toJson(list);
	}

	/**
	 * 单数据源返回数据源下的表名和表注释
	 *
	 * @param start
	 * @param limit
	 * @param tablename 搜索的关键字
	 * @return
	 * @throws SQLException
	 */
	@ResponseBody
	@DataBaseFilter
	@GetMapping(value = "/getAllTables", produces = JSON)
	public String getAllTables(Integer start, Integer limit, String tablename, String dbName) throws SQLException {
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
	@ResponseBody
	@DataBaseFilter
	@GetMapping(value = "/{id}/getAllTables", produces = JSON)
	public String getTableAndComment(@PathVariable(ID) Long dataSourceId, Integer start, Integer limit, String tablename, String dbName)
			throws ClassNotFoundException, SQLException {
		LOGGER.info("查询表名和表注释");
		if (start == null)
			start = 0;
		if (limit == null)
			limit = 99;

		return getTableAndComment(getConnection(dataSourceId), start, limit, tablename, dbName);
	}

	@ResponseBody
	@DataBaseFilter
	@GetMapping(value = "/{id}/getFields/{tableName}", produces = JSON)
	public String getFields(@PathVariable(ID) Long datasourceId, @PathVariable("tableName") String tableName, String dbName)
			throws SQLException, ClassNotFoundException {
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
	private static String getTableAndComment(Connection _conn, Integer start, Integer limit, String tablename, String dbName) throws SQLException {
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

		return toJson(result);
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
	public static String getField(Long datasourceId, String tableName, String dbName) throws SQLException, ClassNotFoundException {
		MyDataSource dataSource = DataSourceDAO.findById(datasourceId);
		List<Map<String, String>> columnComment;

		try (Connection conn = getConnection(dataSource)) {
			columnComment = DataBaseMetaHelper.getColumnComment(conn, tableName, dbName);
		}

		return toJson(columnComment);
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