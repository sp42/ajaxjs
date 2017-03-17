package com.ajaxjs.jdbc;


import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;
import javax.sql.rowset.JdbcRowSet;
import javax.sql.rowset.RowSetProvider;


import com.ajaxjs.framework.model.Query;
import com.ajaxjs.framework.model.Query.Filter;
import com.ajaxjs.framework.model.Query.Order;
import com.ajaxjs.jdbc.sqlbuilder.SqlBuilder;
import com.ajaxjs.util.LogHelper;
import com.ajaxjs.util.StringUtil;

public class Dep extends SqlBuilder {
	// 表映射
	private Map<String, String> hidden_db_field_mapping = new HashMap<>();

	public Map<String, String> getHidden_db_field_mapping() {
		return hidden_db_field_mapping;
	}

	public void setHidden_db_field_mapping(Map<String, String> hidden_db_field_mapping) {
		this.hidden_db_field_mapping = hidden_db_field_mapping;
	}

// 通过子查询获得图片列表 图片表是根据实体 uid 获得其所有的图片，形成列表返回 这里返回的 SqlBuilder Concat 结果后的字符串，用 , 分隔开 UI 层需要 split 字符串
// "(SELECT group_concat(fileName) FROM img WHERE img.parentId = %s.uid) AS imgs";

// UNION 时，SQLite 居然不能直接使用括号，所以必须得 SELECT * FROM
// 可以用 Union 合并 两次查询为一次
// “SELECT * FROM (SELECT id, name FROM ${from} WHERE id > ${id} LIMIT 1) " + "UNION ALL " + "SELECT * FROM (SELECT id, name FROM ${from} WHERE id < ${id} ORDER BY id DESC LIMIT 1)")

	
	
	public static String perRecordSql  = "SELECT %s, name FROM %s WHERE createDate < %s ORDER BY createDate DESC LIMIT 1";
	public static String nextRecordSql = "SELECT %s, name FROM %s WHERE createDate > %s ORDER BY createDate ASC LIMIT 1";
	
	public static String perRecordSql2  = "SELECT id, name FROM %s WHERE createDate < datetime('%s') ORDER BY createDate DESC LIMIT 1";
	public static String nextRecordSql2 = "SELECT id, name FROM %s WHERE createDate > datetime('%s') ORDER BY createDate ASC LIMIT 1";
//	public static Map<String, Map<String, Object>> getNeighbor(Connection conn, String tablename, String datetime){
//		Map<String, Map<String, Object>> map = new HashMap<>();
//
//		String _perRecordSql = String.format(nextRecordSql, tablename, datetime);
//		map.put("perRecord", queryMap(conn, _perRecordSql));
//		
//		String _nextRecordSql = String.format(perRecordSql, tablename, datetime);
//		map.put("nextRecord", queryMap(conn, _nextRecordSql));
//		
//		return map;
//	}


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
