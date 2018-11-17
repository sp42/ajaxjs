package com.ajaxjs.cms.service;

import java.util.List;
import java.util.Map;

import com.ajaxjs.cms.dao.ArticleDao;
import com.ajaxjs.cms.service.aop.CommonService;
import com.ajaxjs.config.ConfigService;
import com.ajaxjs.framework.service.ServiceException;
import com.ajaxjs.framework.service.aop.CacheService;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.orm.dao.DaoHandler;
import com.ajaxjs.orm.dao.PageResult;
import com.ajaxjs.orm.dao.QueryParams;

@Bean(value = "ArticleService", aop = { CommonService.class, CacheService.class })
public class ArticleServiceImpl implements ArticleService {
	ArticleDao dao = new DaoHandler<ArticleDao>().bind(ArticleDao.class);

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
	public PageResult<Map<String, Object>> findPagedList(QueryParams params, int start, int limit) {
		return dao.findPagedList(params, start, limit);
	}

	@Override
	public PageResult<Map<String, Object>> findPagedList(int start, int limit) {
		return findPagedList(start, limit);
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
	public PageResult<Map<String, Object>> findPagedListCatalog(QueryParams param, int start, int limit) {
		return dao.findPagedListCatalog(param, start, limit);
	}

	@Override
	public PageResult<Map<String, Object>> findPagedListByCatalogId(int catalogId, QueryParams param, int start, int limit) {
		int articleCatalog_Id = ConfigService.getValueAsInt("data.articleCatalog_Id");
		return dao.findPagedListByCatalogId(catalogId == 0 ? articleCatalog_Id : catalogId, param, start, limit);
	}

	@Override
	public PageResult<Map<String, Object>> findPagedListByCatalogId_Api(int catalogId, QueryParams param, int start, int limit) {
		param.status = QueryParams.FRONT_END;
		return findPagedListByCatalogId(catalogId, param, start, limit);

	}
}
