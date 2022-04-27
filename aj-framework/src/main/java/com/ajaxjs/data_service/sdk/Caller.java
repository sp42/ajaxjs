package com.ajaxjs.data_service.sdk;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;

import com.ajaxjs.data_service.api.RuntimeData;
import com.ajaxjs.data_service.model.DataServiceDml;
import com.ajaxjs.data_service.model.ServiceContext;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.util.map.MapTool;

public class Caller extends BaseCaller {
	private static final LogHelper LOGGER = LogHelper.getLog(Caller.class);

	public Caller(String domain) {
		super(domain);
	}

	public Caller(String dsNamespace, String domain) {
		super(dsNamespace, domain);
	}

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
			node = exec(uri, RuntimeData.GET);

			ctx = ServiceContext.factory(uri, null, node, params);
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
			node = exec(uri, RuntimeData.GET);
			
			ctx = ServiceContext.factory(uri, null, node, params);
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
			node = exec(uri, RuntimeData.GET);

			params.put("start", args[0]);
			params.put("limit", args[1]);

			ctx = ServiceContext.factory(uri, null, node, params);
			PageResult<Map<String, Object>> list = page(ctx, null);

			if (methodName.equals("findPagedList")) {
				Class<?> clz = getBeanClz();

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

			return list;
		} else {
			// 其他类型
			uri += "/" + methodName;
			node = exec(uri, RuntimeData.GET);
			LOGGER.info(params);

			if ("getOne".equals(node.getType())) {
				ctx = ServiceContext.factory(uri, null, node, params);
				Map<String, Object> info = info(ctx, null);

				if (info == null)
					return null; // 找不到结果
				else {
					Class<?> clz = getBeanClz();
					Class<?> returnType = method.getReturnType();

					if (returnType == Map.class)
						return info;

					if (clz != null)
						return MapTool.map2Bean(info, clz, true, true);
					else
						return info;
				}
			}
		}

		return null;
	}

	@Override
	Serializable create(String methodName, Object[] args) {
		LOGGER.info("Caller 创建实体");

		return create(getServiceContext(RuntimeData.POST, args), null);
	}

	@Override
	boolean update(String methodName, Object[] args) {
		return update(getServiceContext(RuntimeData.PUT, args), null);
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
		return delete(getServiceContext(RuntimeData.DELETE, args), null);
	}

	/**
	 * 初始化上下文对象
	 * 
	 * @param map
	 * @param args
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private ServiceContext getServiceContext(Map<String, DataServiceDml> map, Object[] args) {
		String uri = getUri();
		DataServiceDml node = exec(uri, map);

		Map<String, Object> params;

		if (args[0] instanceof Map)
			params = (Map<String, Object>) args[0];
		else
			params = MapTool.bean2Map(args[0]);// bean

		ServiceContext ctx = ServiceContext.factory(uri, null, node, params);

		return ctx;
	}
}
