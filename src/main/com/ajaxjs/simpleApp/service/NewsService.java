package com.ajaxjs.simpleApp.service;

import java.util.List;
import java.util.Map;

import com.ajaxjs.framework.service.IService;
import com.ajaxjs.simpleApp.model.Catalog;

public interface NewsService extends IService<Map<String, Object>, Long> {

	List<Map<String, Object>> getTop5();
	public List<Catalog> getNewsCatalog();
}
