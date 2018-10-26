package com.ajaxjs.framework.service.aop;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.ajaxjs.framework.service.IService;
import com.ajaxjs.ioc.Aop;
import com.ajaxjs.jdbc.PageResult;

public class CacheService<T, ID extends Serializable, S extends IService<T, ID>> extends Aop<S> {
	Map<Integer, PageResult<T>> cache = new ConcurrentHashMap<>(10);

	@Override
	public Object before(S o, Method method,String methodName,  Object[] args) {
//		if ("findPagedList".equals(method.getName())) {
//			int start = (int) args[0], limit = (int) args[1];
//
//			if (start == 2 && limit == 2 && cache.get(0) != null) {
//				return new ReturnBefore(cache.get(0));
//			}
//		}

		return null;
	}

	@Override
	public void after(S o, Method method, String methodName, Object[] args, Object returnObj) {
//		if ("findPagedList".equals(method.getName())) {
//			int start = (int) args[0], limit = (int) args[1];
//
//			if (start == 2 && limit == 2) {

//				@SuppressWarnings("unchecked")
//				PageResult<T> pageResult = (PageResult<T>) returnObj;
//				cache.put(0, pageResult);
//			}
//		}
	}
}
