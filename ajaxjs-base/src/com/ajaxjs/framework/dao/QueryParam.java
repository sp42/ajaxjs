package com.ajaxjs.framework.dao;

import com.ajaxjs.framework.model.Query;

/**
 * DAO 用的查询参数，可以是 分页 的查询参数，也可以是排序、过滤、搜索等的参数
 * 
 * @author xinzhang
 *
 */
public class QueryParam {
	public QueryParam() {
	}

	public QueryParam(int start, int limit) {
		pageParam[0] = start;
		pageParam[1] = limit;
	}

	public QueryParam(int start, int limit, Query query) {
		pageParam[0] = start;
		pageParam[1] = limit;
		
		this.query = query;
	}

	public QueryParam(Query query) {
		this.query = query;
	}

	/**
	 * 分页之起始行数\偏量值
	 */
	public final int[] pageParam = new int[2];

	/**
	 * 排序、过滤、搜索等的参数
	 */
	public Query query;
}
