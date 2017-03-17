package com.ajaxjs.framework.service;

import java.io.Serializable;
import java.lang.reflect.Method;

import com.ajaxjs.framework.dao.IDao;
import com.ajaxjs.framework.model.PageResult;
import com.ajaxjs.util.aop.Aop;
import com.ajaxjs.util.aop.ReturnBefore;
import com.ajaxjs.util.map.SimpleCache;


public class CacheService<T, ID extends Serializable, D extends IDao<T, ID>> extends Aop<IService<T, ID>> {
	SimpleCache<Integer, PageResult<T> > cache = new SimpleCache<>(10);
	
	@Override
	protected Object before(Method method, Object[] args) {
		int start = (int) args[0], limit = (int) args[1];

		if ("getPageRows".equals(method.getName()) && start == 2 && limit == 2 && cache.get(0) != null) {
			System.out.println("print RightHandler.before");
			return new ReturnBefore(cache.get(0));
		}
		
		return null;
	}

	@Override
	protected void after(Method method, Object[] args, Object returnObj) {
		int start = (int) args[0], limit = (int) args[1];

		if ("getPageRows".equals(method.getName()) && start == 2 && limit == 2) {
			System.out.println("print RightHandler.before");
			@SuppressWarnings("unchecked")
			PageResult<T> pageResult = (PageResult<T> )returnObj;
			cache.put(0, pageResult);
		}
	} 
}
