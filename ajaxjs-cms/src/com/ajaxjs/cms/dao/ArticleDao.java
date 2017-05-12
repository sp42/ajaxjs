package com.ajaxjs.cms.dao;

import java.util.Map;

import com.ajaxjs.cms.model.Article;
import com.ajaxjs.framework.dao.IDao;
import com.ajaxjs.framework.dao.QueryParams;
import com.ajaxjs.framework.dao.annotation.Select;
import com.ajaxjs.framework.model.PageResult;

public interface ArticleDao extends IDao<Article, Long> {
	final static String tableName = "news";

	@Select("SELECT * FROM " + tableName + " WHERE id = ?")
	@Override
	public Article findById(Long id);

	@Select("SELECT COUNT(*) AS Total FROM " + tableName)
	@Override
	public int count();

	@Select(value = "SELECT id, name, createDate, intro FROM " + tableName + " WHERE status = 1")
	public PageResult<Article> findPagedList(QueryParams parame);

	@Select(value = "SELECT news.id, news.name, createDate, intro, catalog.name AS catalogName FROM " + tableName
			+ " INNER JOIN catalog ON news.catalog = catalog.id WHERE status = 1 ORDER BY news.ID DESC")
	public PageResult<Map<String, Object>> findPagedList2(QueryParams parame);
}
