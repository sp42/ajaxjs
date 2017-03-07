/**
 * Copyright 2015 Frank Cheung
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ajaxjs.framework.model;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.ajaxjs.util.LogHelper;
import com.ajaxjs.util.StringUtil;

/**
 * 查询时特地需求的容器，可包含特定的对象进行查询，通过 getter/setter 注入。 特定的对象一般为 Map 结果。 最后转化为 SQL 字符串。
 * 
 * @author frank
 *
 */
public class Query {
	private static final LogHelper LOGGER = LogHelper.getLog(Query.class);

	private Filter filter;
	private Search Search;
	private Match Match;
	private Order Order;
	private Map<String, String> db_field_mapping;

	/**
	 * 内部基类
	 * 
	 * @author frank
	 *
	 */
	private static class Base extends HashMap<String, String> {
		private static final long serialVersionUID = 1L;

		@Override
		public String put(String key, String value) {
			sqlCheck(key);
			sqlCheck(value);

			value = StringUtil.urlChinese(value);

			return super.put(key, value);
		}
	}

	/**
	 * 用于指定查询条件的过滤器
	 * 
	 * @author frank
	 */
	public static class Filter extends Base {
		private static final long serialVersionUID = 1L;

		private boolean isCustomOpeartor;

		/**
		 * 返回是否允许自定义操作符，而不是默认的 =
		 * 
		 * @return
		 */
		public boolean isCustomOpeartor() {
			return isCustomOpeartor;
		}

		/**
		 * 是否允许自定义操作符，而不是默认的 =
		 * 
		 * @param isCustomOpeartor
		 */
		public void setCustomOpeartor(boolean isCustomOpeartor) {
			this.isCustomOpeartor = isCustomOpeartor;
		}
	}

	/**
	 * 用于指定搜索的内部类
	 * 
	 * @author frank
	 */
	public static class Search extends Base {
		private static final long serialVersionUID = 1L;
	}

	/**
	 * 用于指定搜索的内部类（精确匹配）
	 * 
	 * @author frank
	 */
	public static class Match extends Base {
		private static final long serialVersionUID = 1L;
	}

	/**
	 * 用于指定排序的内部类
	 * 
	 * @author frank
	 */
	public static class Order extends Base {
		private static final long serialVersionUID = 1L;
	}

	/**
	 * 检查是否有 SQL 注入，如果有话抛出一个异常。
	 * 
	 * @param str
	 *            可以是请求参数
	 */
	private static void sqlCheck(String str) {
		boolean isInjected = false;

		String[] inj_stra = inj_str.split("\\|");
		for (int i = 0; i < inj_stra.length; i++) {
			if (str.toLowerCase().indexOf(" " + inj_stra[i] + " ") >= 0) {
				isInjected = true;
				break;
			}
		}

		if (isInjected) {
			IllegalArgumentException e = new IllegalArgumentException(" SQL 注入！" + str);
			LOGGER.warning(e);
			throw e;
		}
	}

	private final static String inj_str = "'|and|exec|insert|select|delete|update|count|*|%|chr|mid|master|truncate|char|declare|; |or|-|+|,";

	/**
	 * WHERE 条件查询，当前仅限于 WHERE = AND 的和 LIKE 的
	 * 
	 * @param request
	 *            请求对象
	 * @return 查询对象
	 */
	public static Query getQueryFactory(HttpServletRequest request) {
		Map<String, String[]> map = request.getParameterMap();
		Query query = new Query();

		if (request.getParameter("filterField") != null) {
			// WHERE 查询
			Query.Filter filter = new Query.Filter();
			String[] filterField = map.get("filterField"), filterValue = map.get("filterValue");

			for (int i = 0; i < filterField.length; i++) 
				filter.put(filterField[i], filterValue[i]);

			query.setFilter(filter);
		}

		if (request.getParameter("searchField") != null) {
			// search 查询（模糊）
			Query.Search search = new Query.Search();
			String[] searchField = map.get("searchField"), searchValue = map.get("searchValue");

			for (int i = 0; i < searchField.length; i++) 
				search.put(searchField[i], searchValue[i]);

			query.setSearch(search);
		}

		if (request.getParameter("matchField") != null) {
			// match 查询（精确）
			Query.Match match = new Query.Match();
			String[] matchField = map.get("matchField"), matchValue = map.get("matchValue");

			for (int i = 0; i < matchField.length; i++) 
				match.put(matchField[i], matchValue[i]);

			query.setMatch(match);
		}

		if (request.getParameter("orderField") != null) {
			// order 查询
			Query.Order order = new Query.Order();
			String[] orderField = map.get("orderField"), orderValue = map.get("orderValue");

			for (int i = 0; i < orderField.length; i++) 
				order.put(orderField[i], orderValue[i]);
			
			query.setOrder(order);
		}

		return query;
	}

	/**
	 * 请求参数中是否需要 Query 的
	 * 
	 * @param request
	 *            请求对象
	 * @return true 表示为需要 Query
	 */
	public static boolean isAnyMatch(HttpServletRequest request) {
		return request.getParameter("filterField") != null || request.getParameter("searchField") != null
			|| request.getParameter("matchField") != null  || request.getParameter("orderField") != null;
	}
	
	public Filter getFilter() {
		return filter;
	}

	public void setFilter(Filter filter) {
		this.filter = filter;
	}

	public Search getSearch() {
		return Search;
	}

	public void setSearch(Search search) {
		Search = search;
	}

	public Order getOrder() {
		return Order;
	}

	public void setOrder(Order order) {
		Order = order;
	}

	public Map<String, String> getDb_field_mapping() {
		return db_field_mapping;
	}

	public void setDb_field_mapping(Map<String, String> db_field_mapping) {
		this.db_field_mapping = db_field_mapping;
	}

	public Match getMatch() {
		return Match;
	}

	public void setMatch(Match match) {
		Match = match;
	}

}
