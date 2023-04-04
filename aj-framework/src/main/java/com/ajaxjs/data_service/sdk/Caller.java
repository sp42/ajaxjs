
package com.ajaxjs.data_service.sdk;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;

import com.ajaxjs.data_service.model.DataServiceDml;
import com.ajaxjs.data_service.model.ServiceContext;
import com.ajaxjs.data_service.mybatis.MybatisInterceptor;
import com.ajaxjs.data_service.plugin.IPlugin;
import com.ajaxjs.data_service.service.DataService;
import com.ajaxjs.framework.IBaseModel;
import com.ajaxjs.framework.Identity;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.framework.spring.DiContextUtil;
import com.ajaxjs.sql.util.geo.LocationPoint;
import com.ajaxjs.util.MappingValue;
import com.ajaxjs.util.ReflectUtil;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.util.map.JsonHelper;
import com.ajaxjs.util.map.MapTool;

/**
 * Java 内调用数据服务。通过 Java 动态代理实现
 *
 * @author Frank Cheung
 */
public class Caller extends DataService implements InvocationHandler {
	private static final LogHelper LOGGER = LogHelper.getLog(Caller.class);

	/**
	 * 
	 * @param domain 也相当于表名
	 */
	public Caller(String domain) {
		setDomain(domain);
	}

	/**
	 * 
	 * @param dsNamespace 数据源的标识，url 的前缀
	 * @param domain      数据服务的 id，也相当于表名
	 */
	public Caller(String dsNamespace, String domain) {
		setDatasourceNamespace(dsNamespace);
		setDomain(domain);
	}

	/**
	 * 数据源的标识，url 的前缀
	 */
	private String datasourceNamespace;

	/**
	 * 数据服务的 id，也相当于表名
	 */
	private String domain;

	/**
	 * 查询参数
	 */
	private Map<String, Object> queryParams;

	/**
	 * DAO 实际类引用，必须为接口
	 */
	private Class<? extends IDataServiceBase> clz;

	/**
	 * Bean 类引用
	 */
	private Class<?> beanClz;

	/**
	 * 绑定接口（使用 Java Bean）
	 * 
	 * @param <T>     DAO 类型
	 * @param daoClz  DAO 接口其引用
	 * @param beanClz Java Bean 类引用
	 * @return DAO 实现
	 */
	@SuppressWarnings("unchecked")
	public <T extends IDataServiceBase, K> T bind(Class<T> daoClz, Class<K> beanClz) {
		if (beanClz != null)
			this.setBeanClz(beanClz);

		setClz(daoClz);
		Object obj = Proxy.newProxyInstance(daoClz.getClassLoader(), new Class[] { daoClz }, this);

		return (T) obj;
	}

	/**
	 * 绑定接口（使用 Map 类型）
	 * 
	 * @param <T>    DAO 类型
	 * @param daoClz DAO 接口其引用
	 * @return DAO 实现
	 */
	public <T extends IDataServiceBase> T bind(Class<T> daoClz) {
		return bind(daoClz, null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		String methodName = method.getName();

		if (methodName.equals("setQuery")) {
			setQueryParams((Map<String, Object>) args[0]);
			return proxy;
		} else if (methodName.equals("setWhereQuery")) {
			if (queryParams == null)
				queryParams = new HashMap<>();

			if (args.length == 1) {
				if (args[0] instanceof String) // IDataServiceBase setWhereQuery(String where)
					queryParams.put("where", args[0]);
				else if (args[0] instanceof Map) {// 对应方法 IDataServiceBase setWhereQuery(Map<String, Object> queryWhereParams)
					Map<String, Object> map = (Map<String, Object>) args[0];
					if (map.size() > 0)
						queryParams.put("where", map2sql(map));
				}
			} else if (args.length == 2) { // 两个参数
				String value;
				Object arg = args[1];

				if (arg instanceof String)
					value = "'" + arg + "'";
				else
					value = arg.toString();

				queryParams.put("where", args[0] + " = " + value);
			}

			return proxy;
		} else if (methodName.startsWith("find") || methodName.startsWith("get")) {
//			LOGGER.info("相当于 HTTP GET");
			args = makeMapArgs(method, args);
			return get(method, methodName, args);
		} else if (methodName.equals("create") || methodName.startsWith("create")) {
//			LOGGER.info("相当于 HTTP POST");
			args = makeMapArgs(method, args);
			return create(methodName, args);
		} else if (methodName.startsWith("set") || methodName.equals("update")) {
//			LOGGER.info("相当于 HTTP PUT");

			args = makeMapArgs(method, args);
			return update(methodName, args);
		} else if (methodName.equals("delete") || methodName.startsWith("delete")) {
//			LOGGER.info("相当于 HTTP DELETE");
			args = makeMapArgs(method, args);
			return delete(methodName, args);
		} else {
//			LOGGER.info("其他自定义命令");
			throw new Error("暂时不支持其他自定义命令，请按照命名风格自定义命令");
		}
	}

	private Object[] makeMapArgs(Method method, Object[] args) {
		KeyOfMapParams annotation = method.getAnnotation(KeyOfMapParams.class);

		if (annotation != null) {
			String[] value = annotation.value();

			if (value.length != args.length)
				LOGGER.warning("注解声明数量与方法传递的参数数量不一致，请检查方法定义");

			// to map
			Map<String, Object> map = new HashMap<>();
			for (int i = 0; i < args.length; i++)
				map.put(value[i], args[i]);

			Object[] _args = new Object[1];
			_args[0] = map;

			return _args;
		} else
			return args;
	}

	/**
	 * 把 map 转换为 SQL 的 field = value 形式，根据不同的类型转换为符合 SQL 的
	 * 
	 * @param map
	 * @return
	 */
	private static String map2sql(Map<String, Object> map) {
		List<String> list = new ArrayList<>();

		for (String key : map.keySet())
			list.add(key + " = " + MybatisInterceptor.getParameterValue(map.get(key)));

		return String.join(" AND ", list);
	}

	@SuppressWarnings("unchecked")
	Object get(Method method, String methodName, Object[] args) {
		DataServiceDml node;
		ServiceContext ctx;
		String uri = getUri();

		Map<String, Object> params = getQueryParams() == null ? new HashMap<>() : getQueryParams();
		setQueryParams(null); // 清空，避免下次再用到

		boolean isFindOne = methodName.equals("findOne");

		// 相当于 HTTP GET
		if (methodName.equals("findById") || methodName.equals("findByIdAsMap")) {
			if (args == null)
				throw new IllegalArgumentException("缺少 id 参数");

			params.put("id", args[0]);
			node = exec(uri, GET);

			ctx = ServiceContext.factory(uri, DiContextUtil.getRequest(), node, params);
			Map<String, Object> info = info(ctx, null);

			if (info == null)
				return null; // 找不到结果

			if (methodName.equals("findByIdAsMap"))
				return info;
			else if (methodName.equals("findById")) {
				Class<?> clz = getBeanClz();

				return MapTool.map2Bean(info, clz, true, true);
			}
		} else if (isFindOne || methodName.equals("findList") || methodName.equals("findListAsListMap")) {
			uri += "/list";
			node = exec(uri, GET);

			ctx = ServiceContext.factory(uri, DiContextUtil.getRequest(), node, params);
			List<Map<String, Object>> list = list(ctx, null);

			if (list == null || list.size() == 0)
				return null; // 找不到结果

			if ((isFindOne || methodName.equals("findList")) && !CollectionUtils.isEmpty(list)) {
				Class<?> clz = getBeanClz();

				if (clz instanceof Class) { // java bean
					List<Object> _list = new ArrayList<>();

					list.forEach(item -> {
						Object bean = MapTool.map2Bean(item, clz, true, true);
						_list.add(bean);
					});

					return isFindOne ? _list.get(0) : _list;
				}
			}

			return list;
		} else if (methodName.equals("findPagedList") || methodName.equals("findPagedListAsMap")) {
			uri += "/list";
			node = exec(uri, GET);

			params.put("start", args[0]);
			params.put("limit", args[1]);

			ctx = ServiceContext.factory(uri, DiContextUtil.getRequest(), node, params);
			PageResult<Map<String, Object>> list = page(ctx, null);

			if (methodName.equals("findPagedList")) {
				Class<?> clz = getBeanClz();

				return pageMap2pageBean(clz, list);
			}

			return list;
		} else {

			// 其他类型
			uri += "/" + methodName;
			node = exec(uri, GET);
//			LOGGER.info(params);

			if (args != null && args.length >= 1) {
				// 方法参数转换为 SQL 参数
				if (args[0] instanceof Map)
					params = (Map<String, Object>) args[0];
				else {
					// TODO 反射参数获取 key/value
				}
			}

			if ("getOne".equals(node.getType())) {
				ctx = ServiceContext.factory(uri, DiContextUtil.getRequest(), node, params);
				Map<String, Object> info = info(ctx, null);

				if (info == null)
					return null; // 找不到结果
				else {
					Class<?> clz = getBeanClz();
					Class<?> returnType = method.getReturnType();

					if (returnType == String.class) {
						// 只返回一个
						for (String key : info.keySet())
							return info.get(key).toString();
					} else if (returnType == Integer.class || returnType == int.class) {
						// 只返回一个
						for (String key : info.keySet())
							return MappingValue.object2int(info.get(key));
					} else if (returnType == Long.class || returnType == long.class) {
						// 只返回一个
						for (String key : info.keySet())
							return MappingValue.object2long(info.get(key).toString());
					} else if (returnType == Map.class)
						return info;
					else if (IBaseModel.class.isAssignableFrom(returnType))
						clz = returnType;

					if (clz != null)
						return MapTool.map2Bean(info, clz, true, true);
					else
						return info;
				}
			} else if ("getRows".equals(node.getType())) {
				ctx = ServiceContext.factory(uri, DiContextUtil.getRequest(), node, params);
				List<Map<String, Object>> list = list(ctx, null);

				Type[] types = ReflectUtil.getGenericReturnType(method);

				if (types == null)
					throw new IllegalArgumentException("分页容器类没指定泛型参数？");

				Class<?> clz = ReflectUtil.type2class(types[0]);

				if (clz == Map.class)
					return list;
				else if (clz == String.class) {
					List<String> _list = new ArrayList<>();

					list.forEach(map -> {
						for (String key : map.keySet()) // 只有一个记录
							_list.add(map.get(key).toString());
					});

					return _list;
				} else if (clz == null) // map
					return list;
				else {// bean
					List<Object> _list = new ArrayList<>();

					list.forEach(item -> {
						Object bean = MapTool.map2Bean(item, clz, true, true);
						_list.add(bean);
					});

					return _list;
				}
			} else if ("getRowsPage".equals(node.getType())) {
				params.put("start", args[0]);
				params.put("limit", args[1]);

				ctx = ServiceContext.factory(uri, DiContextUtil.getRequest(), node, params);
				PageResult<Map<String, Object>> list = page(ctx, null);

				Type[] types = ReflectUtil.getGenericReturnType(method);

				if (types == null)
					throw new IllegalArgumentException("分页容器类没指定泛型参数？");

				Class<?> clz = ReflectUtil.type2class(types[0]);

				if (clz == Map.class)
					return list;
				else // bean
					return pageMap2pageBean(clz, list);
			}
		}

		return null;
	}

	/**
	 * 分页结果从 Map 的转换为 bean
	 * 
	 * @param clz
	 * @param list
	 * @return
	 */
	private static PageResult<Object> pageMap2pageBean(Class<?> clz, PageResult<Map<String, Object>> list) {
		PageResult<Object> _list = new PageResult<>();

		list.forEach(item -> {
			Object bean = MapTool.map2Bean(item, clz, true, true);
			_list.add(bean);
		});

		// copy
		_list.setTotalPage(list.getTotalPage());
		_list.setTotalCount(list.getTotalCount());
		_list.setCurrentPage(list.getCurrentPage());
		_list.setPageSize(list.getPageSize());
		_list.setStart(list.getStart());

		return _list;
	}

	public List<IPlugin> getPlugins() {
		DataService ds = DiContextUtil.getBean(DataService.class);

		return ds == null ? null : ds.getPlugins();
	}

	Serializable create(String methodName, Object[] args) {
//		LOGGER.info("Caller 创建实体");
		if ("create".equals(methodName))
			methodName = null;

		setPlugins(getPlugins());
		return create(getServiceContext(POST, methodName, args));
	}

	boolean update(String methodName, Object[] args) {
		if ("update".equals(methodName))
			methodName = null;

		setPlugins(getPlugins());
		return update(getServiceContext(PUT, methodName, args));
	}

	/**
	 * 
	 * @return
	 */
	private String getUri() {
		String uri = "", datasourceNamespace = getDatasourceNamespace();

		if (datasourceNamespace != null)
			uri = datasourceNamespace + "/";

		uri += getDomain();

		return uri;
	}

	boolean delete(String methodName, Object[] args) {
		if ("delete".equals(methodName))
			methodName = null;

		if (args[0] instanceof Identity<?>) {
			@SuppressWarnings("unchecked")
			Long id = ((Identity<Long>) args[0]).getId();
			Map<String, Object> params = new HashMap<>();
			params.put("id", id); // 写死 id
			args[0] = params;
		} else if (args[0] instanceof Number || args[0] instanceof String) {
			Map<String, Object> params = new HashMap<>();
			params.put("id", args[0]); // 写死 id
			args[0] = params;
		}

		LOGGER.info("删除实体" + args[0]);
		setPlugins(getPlugins());

		return delete(getServiceContext(DELETE, methodName, args));
	}

	/**
	 * 初始化上下文对象
	 * 
	 * @param map
	 * @param args
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private ServiceContext getServiceContext(Map<String, DataServiceDml> map, String methodName, Object[] args) {
		String uri = methodName == null ? getUri() : getUri() + "/" + methodName;
		DataServiceDml node = exec(uri, map);

		Map<String, Object> params;

		if (args[0] instanceof Map)
			params = (Map<String, Object>) args[0];
		else
			params = MapTool.bean2Map(args[0]);// bean

		// java 方式过来的 bean，可能有字段类型是 map 的，存到数据库要转换为 string
		for (String key : params.keySet()) {
			Object value = params.get(key);

			if (value instanceof LocationPoint) {
				LocationPoint p = (LocationPoint) value;
				// 对应 mysql point 类型. MyBatis 的 SQL 语句还要加上 ST_GeomFromText(）函数
				params.put(key, "POINT(" + p.getLatitude() + " " + p.getLongitude() + ")");
			} else if (value instanceof Map || value instanceof IBaseModel || value instanceof List)
				params.put(key, JsonHelper.toJson(value));
		}

		ServiceContext ctx = ServiceContext.factory(uri, DiContextUtil.getRequest(), node, params);

		return ctx;
	}

	public Class<? extends IDataServiceBase> getClz() {
		return clz;
	}

	public void setClz(Class<? extends IDataServiceBase> clz) {
		this.clz = clz;
	}

	public void setDatasourceId(Long id) {
	}

	public void setDataServiceId(Long id) {
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getDatasourceNamespace() {
		return datasourceNamespace;
	}

	public void setDatasourceNamespace(String datasourceNamespace) {
		this.datasourceNamespace = datasourceNamespace;
	}

	public Map<String, Object> getQueryParams() {
		return queryParams;
	}

	public void setQueryParams(Map<String, Object> queryParams) {
		this.queryParams = queryParams;
	}

	public Class<?> getBeanClz() {
		return beanClz;
	}

	public void setBeanClz(Class<?> beanClz) {
		this.beanClz = beanClz;
	}
}
