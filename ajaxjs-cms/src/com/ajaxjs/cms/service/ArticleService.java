package com.ajaxjs.cms.service;

import java.util.Map;

import com.ajaxjs.cms.dao.ArticleDao;
import com.ajaxjs.cms.model.Article;
import com.ajaxjs.framework.dao.QueryParams;
import com.ajaxjs.framework.model.PageResult;
import com.ajaxjs.framework.service.BaseDaoService;
import com.ajaxjs.framework.service.IService;

public class ArticleService extends BaseDaoService<Article, Long, ArticleDao> implements IService<Article, Long> {
	public ArticleService() {
		initDao(ArticleDao.class);
	}

	@Override
	public Article findById(Long id) {
		return getDao().findById(id);
	}

	@Override
	public Long create(Article bean) {
		return null;
	}

	@Override
	public int update(Article bean) {
		return 0;
	}

	@Override
	public boolean delete(Article bean) {
		return false;
	}

	@Override
	public PageResult<Article> findPagedList(QueryParams parame) {
		return getDao().findPagedList(parame);
	}

	public PageResult<Map<String, Object>> findPagedList2(QueryParams parame) {
		return getDao().findPagedList2(parame);
	}
}