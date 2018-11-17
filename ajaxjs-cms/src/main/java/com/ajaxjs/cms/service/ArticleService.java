package com.ajaxjs.cms.service;

import java.util.List;
import java.util.Map;

import com.ajaxjs.framework.service.IService;
import com.ajaxjs.orm.dao.PageResult;
import com.ajaxjs.orm.dao.QueryParams;

public interface ArticleService extends IService<Map<String, Object>, Long> {
	/**
	 * 获取最新发布的前 x 条新闻
	 * 
	 * @param limit 前 x 条数量
	 * @return
	 */
	List<Map<String, Object>> getTop(int limit);

	/**
	 * 
	 * @param param
	 * @return
	 */
	public PageResult<Map<String, Object>> findPagedListCatalog(QueryParams param, int start, int limit);

	/**
	 * 根據分类 id 查詢新聞
	 * 
	 * @param catalogId 类别 id
	 * @param param 查询参数
	 * @return 新闻分页列表
	 */
	public PageResult<Map<String, Object>> findPagedListByCatalogId(int catalogId, QueryParams param, int start, int limit);

	/**
	 * 
	 * @param catalogId 类别 id
	 * @param param
	 * @return
	 */
	public PageResult<Map<String, Object>> findPagedListByCatalogId_Api(int catalogId, QueryParams param, int start, int limit);
}
