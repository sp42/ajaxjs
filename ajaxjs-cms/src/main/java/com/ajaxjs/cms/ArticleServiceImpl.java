package com.ajaxjs.cms;

import java.util.Map;

import com.ajaxjs.config.ConfigService;
import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.EntityMap;
import com.ajaxjs.framework.Repository;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.orm.dao.PageResult;

@Bean(value = "ArticleService")
public class ArticleServiceImpl extends BaseService<EntityMap> {
	ArticleDao dao = new Repository().bind(ArticleDao.class);

	@Override
	public String getName() {
		return "文章";
	}

	@Override
	public String getTableName() {
		return "article";
	}


	@Override
	public PageResult<Map<String, Object>> findPagedListCatalog(int start, int limit) {
		return dao.findPagedListCatalog(start, limit);
	}

	@Override
	public PageResult<Map<String, Object>> findPagedListByCatalogId(int catalogId, int start, int limit) {
		int articleCatalog_Id = ConfigService.getValueAsInt("data.articleCatalog_Id");
		return dao.findPagedListByCatalogId(catalogId == 0 ? articleCatalog_Id : catalogId, start, limit);
	}

	@Override
	public PageResult<Map<String, Object>> findPagedListByCatalogId_Api(int catalogId, int start, int limit) {
		return findPagedListByCatalogId(catalogId, start, limit);

	}
}
