package com.ajaxjs.data_service.controller;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ajaxjs.data_service.DataServiceDAO;
import com.ajaxjs.data_service.api.ApiController;
import com.ajaxjs.data_service.api.MyDataSourceController;
import com.ajaxjs.data_service.model.DataServiceConfig;
import com.ajaxjs.data_service.model.DataServiceConstant;
import com.ajaxjs.data_service.model.DataServiceDTO;
import com.ajaxjs.data_service.model.DataServiceDml;
import com.ajaxjs.data_service.model.DataServiceFieldsMapping;
import com.ajaxjs.data_service.model.DataServiceTable;
import com.ajaxjs.data_service.model.MyDataSource;
import com.ajaxjs.data_service.model.ServiceContext;
import com.ajaxjs.data_service.mybatis.MSUtils;
import com.ajaxjs.data_service.plugin.IPlugin;
import com.ajaxjs.data_service.service.ApiCommander;
import com.ajaxjs.framework.QueryTools;
import com.ajaxjs.framework.Status;
import com.ajaxjs.spring.DiContextUtil;
import com.ajaxjs.spring.easy_controller.ControllerMethod;
import com.ajaxjs.sql.JdbcConnection;
import com.ajaxjs.util.StrUtil;
import com.ajaxjs.util.WebHelper;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.util.map.JsonHelper;
import com.ajaxjs.util.map.MapTool;

/**
 * 数据服务 API 基类 数据服务向外提供的基础API
 *
 */
@RestController
@RequestMapping("${DataService.api_root:/api}/**")
public class ApiService extends ApiCommander implements DataServiceDAO, DataServiceDTO, DataServiceConstant {
	private static final LogHelper LOGGER = LogHelper.getLog(ApiService.class);

	@GetMapping
	@ControllerMethod("数据服务 GET")
	public Object get(HttpServletRequest req) {
		return get(getServiceContext(req, GET, null), plugins);
	}

	@PostMapping
	@ControllerMethod("数据服务 创建实体")
	public Serializable post(Map<String, Object> formPostMap, HttpServletRequest req) {
		Serializable newlyId = create(getServiceContext(req, POST, formPostMap), plugins);

		if (newlyId != null) {
			if (NOT_AUTOCREMENT_ID.equals(newlyId))
				return NOT_AUTOCREMENT_ID; // 创建成功，但没返回 id
			else
				return newlyId;
		} else
			throw new NullPointerException("创建失败，未能返回新创建实体之 id");
	}

	@PutMapping
	@ControllerMethod("数据服务 修改实体")
	public Boolean put(Map<String, Object> formPostMap, HttpServletRequest req) {
		return update(getServiceContext(req, PUT, formPostMap), plugins);
	}

	@DeleteMapping
	@ControllerMethod("数据服务 删除实体/批量删除")
	public Boolean delete(HttpServletRequest req) {
		return delete(getServiceContext(req, DELETE, null), plugins);
	}

	private ServiceContext getServiceContext(HttpServletRequest req, Map<String, DataServiceDml> dml, Map<String, Object> formPostMap) {
		initCache();
		String uri = getUri(req);
		DataServiceDml node = exec(uri, dml);

		if (dml == GET || dml == DELETE)
			return ServiceContext.factory(uri, req, node, WebHelper.getQueryParameters(req));
		if (dml == POST || dml == PUT)
			return ServiceContext.factory(uri, req, node, WebHelper.getParamMap(req, formPostMap));

		throw new IllegalArgumentException("不可能走到这步");
	}

	/**
	 * API 接口的总目录
	 */
	@Value("${DataService.api_root:/api}")
	public String API_URL_ROOT;

	/**
	 * 获取路径，转化为命令
	 *
	 * @param req 请求对象
	 * @return 命令
	 */
	private String getUri(HttpServletRequest req) {
		String uri = WebHelper.getRoute(req);
		uri = uri.replace(API_URL_ROOT, "");

		if (uri.startsWith("/"))
			uri = uri.substring(1, uri.length());

		return uri;
	}

	/**
	 * 请求与配置信息对应起来之后的信息。key 是各个 url dir 之组合
	 */
	public static Map<String, DataServiceDml> GET = new ConcurrentHashMap<>();

	public static Map<String, DataServiceDml> POST = new ConcurrentHashMap<>();

	public static Map<String, DataServiceDml> PUT = new ConcurrentHashMap<>();

	public static Map<String, DataServiceDml> DELETE = new ConcurrentHashMap<>();

	public static Map<String, DataServiceDml> HEAD = new ConcurrentHashMap<>();

	public static Map<String, DataServiceDml> OPTION = new ConcurrentHashMap<>();

	public static Map<String, DataServiceDml> PATCH = new ConcurrentHashMap<>();

	/**
	 * 是否已初始化的标识
	 */
	private static AtomicBoolean initialized = new AtomicBoolean(false);

	@Autowired
	private DataServiceConfig cfg;

	@Autowired(required = false)
	@Qualifier("DataServicePlugins")
	protected List<IPlugin> plugins;

	/**
	 * 初始化
	 */
	@SuppressWarnings("unchecked")
	public synchronized void init() {
//		LOGGER.info("初始化 API 接口。");
		DataSource ds = cfg.getDataSource(); // 总配置的数据源

		try {
			Connection connection = ds.getConnection();

			if (connection == null)
				LOGGER.warning("不能建立数据库连接");

			JdbcConnection.setConnection(connection);
		} catch (SQLException e) {
			LOGGER.warning(e);
		}

		if (!cfg.isEmbed())
			initMulitDataSource();

		final List<DataServiceTable> list = DataServiceAdminDAO.findList(null);
		JdbcConnection.closeDb();

		LOGGER.info("读取前期配置成功");

		GET.clear();
		POST.clear();
		PUT.clear();
		DELETE.clear();
		HEAD.clear();
		OPTION.clear();
		PATCH.clear(); // 删除配置后仍驻留内存，所以要清空

		if (CollectionUtils.isEmpty(list)) {
			LOGGER.warning("还没有任何配置");
			return;
		}

		list.forEach((DataServiceTable item) -> {
			String jsonStr = item.getJson();

			if (StringUtils.hasText(jsonStr)) {
				Map<String, Object> json = JsonHelper.parseMap(jsonStr);

				if (json.containsKey("fieldsMapping")) {// 字段映射
					DataServiceFieldsMapping fieldsMapping = MapTool.map2Bean((Map<String, ?>) json.get("fieldsMapping"), DataServiceFieldsMapping.class);
					item.setFieldsMapping(fieldsMapping);
				}

				for (String type : json.keySet()) {
					String urlDir;
					DataSource _ds;

					if (cfg.isEmbed()) {
						_ds = ds;
						urlDir = item.getUrlDir();
					} else {
						if (item.getDatasourceId() == 0) {
							LOGGER.info("非嵌入模式下，命令必须有数据源 id");
							continue;
						}

						// LOGGER.info(item.getDatasourceId() + "");
						MyDataSource myds = mulitDataSource.get(item.getDatasourceId());
						if (myds == null) {
//							LOGGER.warning("不存在 id 为 {0} 的数据源", item.getDatasourceId());
							continue;
						}

						_ds = myds.getInstance();
						urlDir = StrUtil.concatUrl(myds.getUrlDir(), item.getUrlDir());
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

	/**
	 * 初始化（带缓存控制）
	 */
	public void initCache() {
		if (initialized.compareAndSet(false, true)) // 如果为false，更新为true
			init();
	}

	/**
	 * 保存多个数据源
	 */
	private Map<Long, MyDataSource> mulitDataSource = new HashMap<>();

	/**
	 * 初始化多个数据源
	 */
	private void initMulitDataSource() {
//		LOGGER.info("初始化所有数据源");

		mulitDataSource.clear();
		List<MyDataSource> findList = DataSourceDAO.findList(QueryTools.setStatus(Status.ONLINE.getValue()));

		findList.forEach(myds -> {
			DataSource ds = MSUtils.setupJdbcPool(MyDataSourceController.getDbDriver(myds), myds.getUrl(), myds.getUsername(), myds.getPassword());
			myds.setInstance(ds);
			mulitDataSource.put(myds.getId(), myds);
		});
	}

	/**
	 * 根据数据源 id 获取数据源（可能会太慢）
	 *
	 * @param datasourceId
	 * @return
	 */
	public Connection getConnectionByDatasourceId(long datasourceId) {
		MyDataSource myds = mulitDataSource.get(datasourceId);
		DataSource ds;

		if (myds != null) {
			LOGGER.info("Get DS from Cache!");
			ds = myds.getInstance();
		} else {
			myds = DataSourceDAO.findById(datasourceId);
			LOGGER.info("Created MyDs " + myds);

			if (myds != null)
				ds = MSUtils.setupJdbcPool(MyDataSourceController.getDbDriver(myds), myds.getUrl(), myds.getUsername(), myds.getPassword());
			else
				return null;
		}

		try {
			return ds.getConnection();
		} catch (SQLException e) {
			LOGGER.warning(e);
			return null;
		}
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
	private static void saveNode(DataServiceTable item, String type, String urlDir, Map<String, Object> map, DataSource ds) {
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
			GET.put(urlDir, dml);
			break;
		case "insert":
		case "create":
			POST.put(urlDir, dml);
			break;
		case "update":
			PUT.put(urlDir, dml);
			break;
		case "delete":
			DELETE.put(urlDir, dml);
			break;
		}
	}

	/**
	 * URL 匹配命令。根据两个输入条件找到匹配的 DML 命令
	 * 
	 * @param uri    命令 URL
	 * @param states 状态 Map
	 * @return
	 */
	public static DataServiceDml exec(String uri, Map<String, DataServiceDml> states) {
		if (states.containsKey(uri)) {
			DataServiceDml node = states.get(uri);

			if (node.isEnable())
				return node;
			else
				throw new RuntimeException("该命令 [" + uri + "] 未启用");
		} else {
			if (states == null || states.size() == 0) {
				// 自定义类型好像不会触发 ds 初始化，这里强制执行 TODO
				ApiController api = DiContextUtil.getBean(ApiController.class);
				api.init();
//				api.initCache();

				throw new RuntimeException("未初始化数据服务");
			} else {
				for (String key : states.keySet())
					System.out.println(key);

				throw new RuntimeException("不存在该路径 [" + uri + "] 之配置");
			}
		}
	}
}