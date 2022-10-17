
package com.ajaxjs.data_service.sdk;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;

import com.ajaxjs.data_service.api.RuntimeData;
import com.ajaxjs.data_service.model.DataServiceDml;
import com.ajaxjs.data_service.model.ServiceContext;
import com.ajaxjs.data_service.plugin.IPlugin;
import com.ajaxjs.framework.IBaseModel;
import com.ajaxjs.framework.Identity;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.spring.DiContextUtil;
import com.ajaxjs.sql.util.geo.LocationPoint;
import com.ajaxjs.util.MappingValue;
import com.ajaxjs.util.ReflectUtil;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.util.map.JsonHelper;
import com.ajaxjs.util.map.MapTool;

public class Caller extends BaseCaller {
	private static final LogHelper LOGGER = LogHelper.getLog(Caller.class);

	public Caller(String domain) {
		super(domain);
	}

	public Caller(String dsNamespace, String domain) {
		super(dsNamespace, domain);
	}

	@SuppressWarnings("unchecked")
	@Override
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
			node = RuntimeData.exec(uri, RuntimeData.GET);

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
			node = RuntimeData.exec(uri, RuntimeData.GET);

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
			node = RuntimeData.exec(uri, RuntimeData.GET);

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
			node = RuntimeData.exec(uri, RuntimeData.GET);
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

	@SuppressWarnings("unchecked")
	public List<IPlugin> getPlugins() {
		Object bean = DiContextUtil.getBean("DataServicePlugins");

		return bean == null ? null : (List<IPlugin>) bean;
	}

	@Override
	Serializable create(String methodName, Object[] args) {
//		LOGGER.info("Caller 创建实体");
		if ("create".equals(methodName))
			methodName = null;

		return create(getServiceContext(RuntimeData.POST, methodName, args), getPlugins());
	}

	@Override
	boolean update(String methodName, Object[] args) {
		if ("update".equals(methodName))
			methodName = null;

		return update(getServiceContext(RuntimeData.PUT, methodName, args), getPlugins());
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

	@Override
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
		return delete(getServiceContext(RuntimeData.DELETE, methodName, args), getPlugins());
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
		DataServiceDml node = RuntimeData.exec(uri, map);

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
			} else if (value instanceof Map || value instanceof IBaseModel)
				params.put(key, JsonHelper.toJson(value));
		}

		ServiceContext ctx = ServiceContext.factory(uri, DiContextUtil.getRequest(), node, params);

		return ctx;
	}
}
