package com.ajaxjs.cms;

import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.EntityMap;
import com.ajaxjs.framework.Repository;
import com.ajaxjs.ioc.Bean;

@Bean(value = "ArticleService")
public class ArticleService extends BaseService<EntityMap> {
	ArticleDao dao = new Repository().bind(ArticleDao.class);

	{
		setUiName("文章");
		setShortName("article");
		setDao(dao);
	}

//	@Override
//	public PageResult<Map<String, Object>> findPagedListByCatalogId(int catalogId, int start, int limit) {
//		int articleCatalog_Id = ConfigService.getValueAsInt("data.articleCatalog_Id");
//		return dao.findPagedListByCatalogId(catalogId == 0 ? articleCatalog_Id : catalogId, start, limit);
//	}

}
