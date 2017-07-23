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
package com.ajaxjs.jdbc.sqlbuilder;

import java.util.Map;


import com.ajaxjs.framework.model.Query;
import com.ajaxjs.framework.model.Query.*;
import com.ajaxjs.util.StringUtil;
import com.ajaxjs.util.logger.LogHelper;

/**
 * 动态的 SqlBuilder
 * 
 * @author frank
 *
 */
public class DynamicSqlProvider extends SqlBuilder {
	private static final LogHelper LOGGER = LogHelper.getLog(DynamicSqlProvider.class);

	/**
	 * 返回查询总数的 SqlBuilder
	 * 
	 * @param parames
	 *            MyBatis 参数
	 * @return SqlBuilder 语句
	 */
	public String pageCount(final Map<String, Object> parames) {
		SELECT("COUNT(*)");
		FROM(parames.get("tablename").toString());
		if (parames.containsKey("query")) 
			addWhere(this, (Query) parames.get("query"));

		LOGGER.info("pageCount-------------->" + toString());

		return toString();
	}

	/**
	 * 返回查询列表的 SqlBuilder
	 * 
	 * @param parames
	 *            MyBatis 参数
	 * @return SqlBuilder 语句
	 */
	public String page(final Map<String, Object> parames) {
		final int start = (int) parames.get("start"), limit = (int) parames.get("limit");
		final String tablename = parames.get("tablename").toString();

		String sql = new SqlBuilder() {
			{
				SELECT(getFileds(parames));
				FROM(tablename);

				if (parames.containsKey("query")) {
					Query query = (Query) parames.get("query");
					addWhere(this, query);

					if (query.getOrder() != null) {
						Order order = query.getOrder();
						for (String key : order.keySet()) {
							if (!StringUtil.isEmptyString(order.get(key)))
								ORDER_BY(key + " " + order.get(key));
						}
					}
				}

				if (!parames.containsKey("query")
						|| (parames.containsKey("query") && ((Query) parames.get("query")).getOrder() == null)) {
					ORDER_BY("id DESC"); // 默认排序
				}
			}

		}.toString() + " LIMIT " + start + ", " + limit;

		LOGGER.info("page sql-------------->" + sql);

		return sql;
	}

	private String getFileds(Map<String, Object> parames) {
		String addStr = "*";

		if (parames.containsKey("query")) {
			Query query = (Query) parames.get("query");
			if (query.getDb_field_mapping() != null && query.getDb_field_mapping().size() > 0) {
				String[] pairs = new String[query.getDb_field_mapping().size()];

				int i = 0;
				for (String key : query.getDb_field_mapping().keySet())
					pairs[i++] = query.getDb_field_mapping().get(key) + " AS " + key;

				addStr += " ," + StringUtil.stringJoin(pairs, ",");
			}
		}

		return addStr;
	}
 

	/**
	 * 添加 WHERE 子语句
	 * 
	 * @param sql
	 *            动态 SqlBuilder 实例
	 * @param query
	 *            Query 查询对象
	 */
	private static void addWhere(SqlBuilder sql, Query query) {
		Map<String, String> map;
		if (query.getFilter() != null) {
			Filter filter = query.getFilter();

			map = (Map<String, String>) filter;

			for (String key : map.keySet()) {
				if (!StringUtil.isEmptyString(map.get(key))) {

					if (filter.isCustomOpeartor()) {
						sql.WHERE(key + map.get(key));
					} else {
						sql.WHERE(key + " = " + map.get(key));
					}
				}
			}
		}
		if (query.getSearch() != null) {
			map = query.getSearch();
			for (String key : map.keySet()) {
				if (!StringUtil.isEmptyString(map.get(key)))
					sql.WHERE(key + " LIKE '%" + map.get(key) + "%'");
			}
		}
		if (query.getMatch() != null) {
			map = query.getMatch();
			for (String key : map.keySet()) {
				if (!StringUtil.isEmptyString(map.get(key)))
					sql.WHERE(key + " LIKE '" + map.get(key) + "'");
			}
		}
	}
 
	

}
