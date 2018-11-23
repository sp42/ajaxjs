package com.ajaxjs.cms.service;

import java.util.List;
import java.util.Map;

import com.ajaxjs.cms.dao.ArticleDao;
import com.ajaxjs.cms.service.aop.CommonService;
import com.ajaxjs.config.ConfigService;
import com.ajaxjs.framework.service.aop.CacheService;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.orm.dao.DaoHandler;
import com.ajaxjs.orm.dao.PageResult;

@Bean(value = "ArticleService", aop = { CommonService.class, CacheService.class })
public class ArticleServiceImpl implements ArticleService {
	ArticleDao dao = new DaoHandler().bind(ArticleDao.class);

	@Override
	public Map<String, Object> findById(Long id) {
		return dao.findById(id);
	}

	@Override
	public Long create(Map<String, Object> bean) {
		return dao.create(bean);
	}

	@Override
	public int update(Map<String, Object> bean) {
		return dao.update(bean);
	}

	@Override
	public boolean delete(Map<String, Object> bean) {
		return dao.delete(bean);
	}

	@Override
	public PageResult<Map<String, Object>> findPagedList(int start, int limit) {
		return dao.findPagedList(start, limit);
	}

	@Override
	public PageResult<Map<String, Object>> findList() {
		return null;
	}

	@Override
	public String getName() {
		return "文章";
	}

	@Override
	public String getTableName() {
		return "article";
	}

	@Override
	public List<Map<String, Object>> getTop(int limit) {
		return dao.getTop(limit);
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
