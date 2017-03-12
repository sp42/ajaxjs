/**
 * 版权所有 2017 Frank Cheung
 * 
 * 根据 2.0 版本 Apache 许可证("许可证")授权；
 * 根据本许可证，用户可以不使用此文件。
 * 用户可从下列网址获得许可证副本：
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 *    
 * 除非因适用法律需要或书面同意，根据许可证分发的软件是基于"按原样"基础提供，
 * 无任何明示的或暗示的保证或条件。详见根据许可证许可下，特定语言的管辖权限和限制。
 */
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
