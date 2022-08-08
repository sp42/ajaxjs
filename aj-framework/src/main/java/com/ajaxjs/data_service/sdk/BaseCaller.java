package com.ajaxjs.data_service.sdk;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ajaxjs.data_service.api.ApiController;
import com.ajaxjs.data_service.api.Commander;
import com.ajaxjs.data_service.mybatis.MybatisInterceptor;
import com.ajaxjs.spring.DiContextUtil;
import com.ajaxjs.util.logger.LogHelper;

/**
 * Java 动态代理
 *
 * @author Frank Cheung
 */
public abstract class BaseCaller extends Commander implements InvocationHandler {
	private static final LogHelper LOGGER = LogHelper.getLog(BaseCaller.class);

	/**
	 * 
	 * @param domain 也相当于表名
	 */
	public BaseCaller(String domain) {
		setDomain(domain);
	}

	/**
	 * 
	 * @param dsNamespace 数据源的标识，url 的前缀
	 * @param domain      数据服务的 id，也相当于表名
	 */
	public BaseCaller(String dsNamespace, String domain) {
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
		initDataService();
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

	/**
	 * 初始化数据服务
	 */
	private void initDataService() {
		ApiController api = DiContextUtil.getBean(ApiController.class);
		api.initCache();
	}

	/**
	 * DAO 实际类引用，必须为接口
	 */
	private Class<? extends IDataServiceBase> clz;

	/**
	 * Bean 类引用
	 */
	private Class<?> beanClz;

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

	/**
	 * 
	 * @param method
	 * @param methodName 方法名称
	 * @param args       参数列表
	 * @return SQL 执行结果
	 */
	abstract Object get(Method method, String methodName, Object[] args);

	/**
	 * 
	 * @param methodName 方法名称
	 * @param args       参数列表
	 * @return SQL 执行结果
	 */
	abstract Serializable create(String methodName, Object[] args);

	/**
	 * 
	 * @param methodName 方法名称
	 * @param args       参数列表
	 * @return SQL 执行结果
	 */
	abstract boolean update(String methodName, Object[] args);

	/**
	 * 
	 * @param methodName 方法名称
	 * @param args       参数列表
	 * @return SQL 执行结果
	 */
	abstract boolean delete(String methodName, Object[] args);
}
