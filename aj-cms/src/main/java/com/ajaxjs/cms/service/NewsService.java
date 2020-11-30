package com.ajaxjs.cms.service;

import com.ajaxjs.framework.config.ConfigService;
import com.ajaxjs.util.ioc.Component;

@Component
public class NewsService extends ArticleService {
	{
		setUiName("新闻");
	}

	@Override
	public int getDomainCatalogId() {
		return ConfigService.getValueAsInt("data.newsCatalog_Id");
	}
}