package com.ajaxjs.simpleApp.service;

import java.util.List;
import java.util.Map;

import com.ajaxjs.framework.service.IService;

public interface NewsService extends IService<Map<String, Object>, Long> {

	List<Map<String, Object>> getTop5();

}
