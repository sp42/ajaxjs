package com.ajaxjs.cms.app;

import com.ajaxjs.config.ConfigService;
import com.ajaxjs.ioc.Bean;

@Bean
public class NewsService extends ArticleService {
	{
		setUiName("新闻");
	}

	@Override
	public int getDomainCatalogId() {
		return ConfigService.getValueAsInt("data.newsCatalog_Id");
	}
}