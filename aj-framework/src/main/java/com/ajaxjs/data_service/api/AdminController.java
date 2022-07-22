package com.ajaxjs.data_service.api;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ajaxjs.data_service.DataServiceDAO;
import com.ajaxjs.data_service.model.DataServiceConfig;
import com.ajaxjs.data_service.model.DataServiceTable;
import com.ajaxjs.data_service.model.MyDataSource;
import com.ajaxjs.framework.BaseController;
import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.framework.QueryTools;
import com.ajaxjs.sql.JdbcConnection;
import com.ajaxjs.sql.util.DataBaseMetaHelper;
import com.ajaxjs.util.StrUtil;
import com.ajaxjs.util.WebHelper;
import com.ajaxjs.util.filter.DataBaseFilter;
import com.ajaxjs.util.logger.LogHelper;

/**
 * 数据服务 后台控制器
 * 
 * 数据服务本身的控制器
 * 
 * 
        "camelCase2DbStyle": true,
        "dbStyle2CamelCase": true
 */
@RestController
@RequestMapping("/admin/data_service")
public class AdminController extends BaseController implements DataServiceDAO {
	private static final LogHelper LOGGER = LogHelper.getLog(AdminController.class);

	@Autowired
	private DataServiceConfig cfg;

	@Autowired
	private ApiController apiController;

	@GetMapping(produces = JSON)
	@DataBaseFilter
	public PageResult<DataServiceTable> list(HttpServletRequest req, @RequestParam(defaultValue = "0") int start, @RequestParam(defaultValue = "9") int limit,
			Long datasourceId) {
		LOGGER.info("获取表配置列表");

		Function<String, String> handler = BaseService::searchQuery_NameOnly;
		handler = handler.andThen(QueryTools.byAny(req)).andThen(BaseService::betweenCreateDate);
		if (datasourceId != null && datasourceId != 0L)
			handler = handler.andThen(QueryTools.by("datasourceId", datasourceId));

		return DataServiceAdminDAO.findPagedList(start, limit, handler);
	}

	@DataBaseFilter
	@PostMapping(produces = JSON)
	public DataServiceTable create(DataServiceTable entity) {
		LOGGER.info("创建 DataService");

		entity.setUrlDir(entity.getTableName());
		String url = entity.getUrlDir().replaceAll("\\.", "_"); // 不能加 . 否则 URL 解析错误
		entity.setUrlDir(url);
//        LOGGER.info("" + entity.getDatasourceId());
//        LOGGER.info(DataServiceAdminService.DAO.toString());

		Long dsId = entity.getDatasourceId();
		DataServiceTable repeatUrlDir = dsId == null ? DataServiceAdminDAO.findRepeatUrlDir(url) : DataServiceAdminDAO.findRepeatUrlDirAndDsId(url, dsId);

		if (repeatUrlDir != null) {
			// 已经有重复的
			String maxId = dsId == null ? DataServiceAdminDAO.findRepeatUrlDirMaxId(url) : DataServiceAdminDAO.findRepeatUrlDirAndDsIdMaxId(url, dsId);
			String dig = "";

			if (maxId != null) {
				dig = StrUtil.regMatch("\\d+$", maxId);
				int i = Integer.parseInt(dig);
				dig = (++i) + "";
			} else
				dig = "1";

			entity.setUrlDir(entity.getUrlDir() + "_" + dig);
		}

		Long newlyId = DataServiceAdminDAO.create(entity);
		apiController.init(); // 重新加载配置

		return afterCreate(newlyId, entity);
	}

	@DataBaseFilter
	@PutMapping(value = ID_INFO)
	public Boolean update(@PathVariable long id, HttpServletRequest req) {
		DataServiceTable entity = WebHelper.getParameterBean(req, DataServiceTable.class);
		entity.setId(id);

		if (DataServiceAdminDAO.update(entity) >= 1) {
			apiController.init();// 重新加载配置

			return true;
		} else
			return false;
	}

	@DataBaseFilter
	@DeleteMapping(value = ID_INFO)
	public Boolean delete(@PathVariable long id) {
		LOGGER.info("删除配置 {0}", id);
		DataServiceTable dataServiceTable = new DataServiceTable();
		dataServiceTable.setId(id);

		if (DataServiceAdminDAO.delete(dataServiceTable)) {
			apiController.init();// 重新加载配置

			return true;
		} else
			return false;
	}

	@GetMapping(value = "getDatabases", produces = JSON)
	@DataBaseFilter
	public List<String> getDatabases(Long datasourceId) throws SQLException, ClassNotFoundException {
		LOGGER.info("查询数据库 {0}", datasourceId);
		MyDataSource dataSource = DataSourceDAO.findById(datasourceId);

		if (dataSource.getCrossDB() == null || !dataSource.getCrossDB())
			throw new NullPointerException("不是跨库的数据库连接");

		List<String> databases;

		try (Connection conn = MyDataSourceController.getConnection(dataSource)) {
			databases = DataBaseMetaHelper.getDatabase(conn);
		}

		LOGGER.info("查询数据库 {0}", databases.size());

		return databases;
	}

	@DataBaseFilter
	@RequestMapping(value = ID_INFO, produces = JSON)
	public DataServiceTable getInfo(@PathVariable(ID) long id, String dbName) throws ClassNotFoundException, SQLException {
		LOGGER.info("加载表详情");
		DataServiceTable info = DataServiceAdminDAO.findById(id);

		// 获取所有字段
		Connection conn;

		if (cfg.isEmbed())
			conn = JdbcConnection.getConnection();
		else {
			MyDataSource datasource = DataSourceDAO.findById(info.getDatasourceId());
			conn = MyDataSourceController.getConnection(datasource);
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

		return info;
	}

	@GetMapping(value = "reload", produces = JSON)
	@DataBaseFilter
	public Boolean reload() {
		apiController.init();// 重新加载配置
		// jsonOk("重新加载成功")
		return true;
	}

	/**
	 * TODO 接口重复
	 */
	@DataBaseFilter
	@RequestMapping(value = "getAllFieldsByDataSourceAndTablename", produces = JSON)
	public List<Map<String, String>> getAllFieldsByDataSourceAndTablename(Long datasourceId, String tableName, String dbName)
			throws ClassNotFoundException, SQLException {
		return MyDataSourceController.getField(datasourceId, tableName, dbName);
	}
}
