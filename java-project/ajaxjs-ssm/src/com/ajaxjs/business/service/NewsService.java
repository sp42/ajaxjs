package com.ajaxjs.business.service;

import com.ajaxjs.business.dao.NewsMapper;
import com.ajaxjs.framework.service.BaseService;

public class NewsService extends BaseService<com.ajaxjs.business.model.News, NewsMapper> {
	public NewsService(){
		setMapper(NewsMapper.class);
		setTableName("news");
		setUiName("新闻");
	}
}