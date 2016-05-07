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
import com.ajaxjs.web.PageUtil;

/**
 * 查询时特地需求的容器，可包含特定的对象进行查询，通过 getter/setter 注入。
 * 特定的对象一般为 Map 结果。
 * 最后转化为 SQL 字符串。
 * @author frank
 *
 */
public class Query {
	private static final LogHelper LOGGER = LogHelper.getLog(Query.class);
	
	private Filter<String, String> filter;
	private Search<String, String> Search;
	private Order<String, String>  Order;
	
	/**
	 * 用于指定查询条件的过滤器
	 * @author frank
	 *
	 * @param <K>
	 * @param <V>
	 */
	public static class Filter<K, V> extends HashMap<String, String> {
		private static final long serialVersionUID = 1L;

		private boolean isCustomOpeartor;
		
		@Override
		public String put(String key, String value) {
			sqlCheck(key);
			sqlCheck(value);
			return super.put(key, value);
		}

		/**
		 * 返回是否允许自定义操作符，而不是默认的 = 
		 * @return
		 */
		public boolean isCustomOpeartor() {
			return isCustomOpeartor;
		}

		/**
		 * 是否允许自定义操作符，而不是默认的 = 
		 * @param isCustomOpeartor
		 */
		public void setCustomOpeartor(boolean isCustomOpeartor) {
			this.isCustomOpeartor = isCustomOpeartor;
		}
	}

	/**
	 * 用于指定搜索的内部类
	 * @author frank
	 *
	 * @param <K>
	 * @param <V>
	 */
	public static class Search<K, V> extends HashMap<String, String> {
		private static final long serialVersionUID = 1L;

		@Override
		public String put(String key, String value) {
			sqlCheck(key);
			sqlCheck(value);
		
			value = PageUtil.urlChinese(value);
			return super.put(key, value);
		}
	}
	/**
	 * 用于指定排序的内部类
	 * @author frank
	 *
	 * @param <K>
	 * @param <V>
	 */
	public static class Order<K, V> extends HashMap<String, String> {
		private static final long serialVersionUID = 1L;
		
		@Override
		public String put(String key, String value) {
			sqlCheck(key);
			sqlCheck(value);
			return super.put(key, value);
		}
	}

	/**
	 * 检查是否有 SQL 注入，如果有话抛出一个异常。
	 * 
	 * @param str
	 *            可以是请求参数
	 */
	private static void sqlCheck(String str) {
		if (sqlInject(str)) {
			IllegalArgumentException e = new IllegalArgumentException(" SQL 注入！" + str);
			LOGGER.warning(e);
			throw e;
		}
	}
	
	private final static String inj_str = "'|and|exec|insert|select|delete|update|count|*|%|chr|mid|master|truncate|char|declare|; |or|-|+|,";

	/**
	 * 检查字符串是否 SQL 注入
	 * 
	 * @param str
	 *            可以是请求参数
	 * @return
	 */
	public static boolean sqlInject(String str) {
		String[] inj_stra = inj_str.split("\\|");
		for (int i = 0; i < inj_stra.length; i++) {
			if (str.indexOf(" " + inj_stra[i] + " ") >= 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @deprecated
	 * @param sql
	 * @return
	 */
	public static String TransactSQLInjection(String sql) {
		return sql.replaceAll(".*([';]+|(--)+).*", " ");
	}
	

	public Filter<String, String> getFilter() {
		return filter;
	}

	public void setFilter(Filter<String, String> filter) {
		this.filter = filter;
	}

	public Search<String, String> getSearch() {
		return Search;
	}

	public void setSearch(Search<String, String> search) {
		Search = search;
	}

	public Order<String, String> getOrder() {
		return Order;
	}

	public void setOrder(Order<String, String> order) {
		Order = order;
	}

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
		
		if(request.getParameter("filterField") != null) {
			// WHERE 查询
			Filter<String, String> filter = new Filter<>();
			String[] filterField = map.get("filterField"), filterValue = map.get("filterValue");
			
			for(int i = 0; i < filterField.length; i++) {
				filter.put(filterField[i], filterValue[i]);
			}
			
			query.setFilter(filter);
		}
		
		if(request.getParameter("searchField") != null) {
			// search 查询
			Search<String, String> search = new Search<>();
			String[] searchField = map.get("searchField"), searchValue = map.get("searchValue");
			
			for(int i = 0; i < searchField.length; i++) {
				search.put(searchField[i], searchValue[i]);
			}
			
			query.setSearch(search);
		}
		
		if(request.getParameter("orderField") != null) {
			// order 查询
			Order<String, String> order = new Order<>();
			String[] orderField = map.get("orderField"), orderValue = map.get("orderValue");
			
			for(int i = 0; i < orderField.length; i++) {
				order.put(orderField[i], orderValue[i]);
			}
			query.setOrder(order);
		}
		
		return query;
	}


}
