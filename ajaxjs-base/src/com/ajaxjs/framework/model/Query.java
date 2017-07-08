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
package com.ajaxjs.framework.model;

import java.util.HashMap;
import java.util.Map;

 import com.ajaxjs.util.LogHelper;
import com.ajaxjs.util.StringUtil;

/**
 * 查询时特地需求的容器，可包含特定的对象进行查询，通过 getter/setter 注入。 特定的对象一般为 Map 结果。最后转化为 SQL 字符串。
 * 通常是耦合 HttpServletRequest.getParameterMap() 返回请求数据
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
	 */
	public static class Filter extends Base {
		private static final long serialVersionUID = 1L;

		private boolean isCustomOpeartor;

		/**
		 * 返回是否允许自定义操作符，而不是默认的 =
		 * 
		 * @return 是否允许自定义操作符
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
	 */
	public static class Match extends Base {
		private static final long serialVersionUID = 1L;
	}

	/**
	 * 用于指定排序的内部类
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
	 * @param map
	 *            请求参数
	 * @return 查询对象
	 */
	public static Query getQueryFactory(Map<String, String[]> map) {
		Query query = new Query();

		if (map.get("filterField") != null) {
			// WHERE 查询
			Query.Filter filter = new Query.Filter();
			String[] filterField = map.get("filterField"), filterValue = map.get("filterValue");

			for (int i = 0; i < filterField.length; i++) 
				filter.put(filterField[i], filterValue[i]);

			LOGGER.info("found filter:" + filterField.length);
			query.setFilter(filter);
		}

		if (map.get("searchField") != null) {
			// search 查询（模糊）
			Query.Search search = new Query.Search();
			String[] searchField = map.get("searchField"), searchValue = map.get("searchValue");

			for (int i = 0; i < searchField.length; i++) 
				search.put(searchField[i], searchValue[i]);

			query.setSearch(search);
		}               

		if (map.get("matchField") != null) {
			// match 查询（精确）
			Query.Match match = new Query.Match();
			String[] matchField = map.get("matchField"), matchValue = map.get("matchValue");

			for (int i = 0; i < matchField.length; i++) 
				match.put(matchField[i], matchValue[i]);

			query.setMatch(match);
		}

		if (map.get("orderField") != null) {
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
	 * @param map
	 *            请求参数
	 * @return true 表示为需要 Query
	 */
	public static boolean isAnyMatch(Map<String, String[]> map) {
		return map.get("filterField") != null || map.get("searchField") != null
			|| map.get("matchField") != null  || map.get("orderField") != null;
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
