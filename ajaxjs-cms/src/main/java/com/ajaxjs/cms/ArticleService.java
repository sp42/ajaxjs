package com.ajaxjs.cms;

import java.util.List;

import com.ajaxjs.cms.app.catelog.Catelogable;
import com.ajaxjs.config.ConfigService;
import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.EntityMap;
import com.ajaxjs.framework.Repository;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.orm.dao.PageResult;

@Bean(value = "ArticleService")
public class ArticleService extends BaseService<EntityMap> implements Catelogable<EntityMap> {
	ArticleDao dao = new Repository().bind(ArticleDao.class);

	{
		setUiName("文章");
		setShortName("article");
		setDao(dao);
	}

	@Override
	public PageResult<EntityMap> findPagedListByCatelogId(int catelogId, int start, int limit) {
		if (catelogId == 0)
			catelogId = getDomainCatelogId();
		return dao.findPagedListByCatelogId(catelogId, start, limit);
	}

	@Override
	public List<EntityMap> findListByCatelogId(int catelogId) {
		if (catelogId == 0)
			catelogId = getDomainCatelogId();
		return dao.findPagedListByCatelogId(catelogId, 0, 9999);
	}

	@Override
	public int getDomainCatelogId() {
		return ConfigService.getValueAsInt("data.articleCatalog_Id");
	}
}
