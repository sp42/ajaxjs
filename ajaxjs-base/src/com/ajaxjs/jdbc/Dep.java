package com.ajaxjs.jdbc;

import java.util.HashMap;
import java.util.Map;

import com.ajaxjs.framework.model.Query;
import com.ajaxjs.jdbc.sqlbuilder.SqlBuilder;
import com.ajaxjs.util.StringUtil;

public class Dep extends SqlBuilder {
//	 if (jdbcConnStr.indexOf("MySQL") != -1 || jdbcConnStr.indexOf("mysql") != -1) {
//    result = rs.next() ? rs.getInt(1) : null;
//} else {// sqlite
//     result = rs.isBeforeFirst() ? rs.getInt(1) : null;
//}
	
	// 表映射
	private Map<String, String> hidden_db_field_mapping = new HashMap<>();

	public Map<String, String> getHidden_db_field_mapping() {
		return hidden_db_field_mapping;
	}

	public void setHidden_db_field_mapping(Map<String, String> hidden_db_field_mapping) {
		this.hidden_db_field_mapping = hidden_db_field_mapping;
	}

	public String getFileds(Map<String, Object> parames) {
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

	// 通过子查询获得图片列表 图片表是根据实体 uid 获得其所有的图片，形成列表返回 这里返回的 SqlBuilder Concat
	// 结果后的字符串，用 , 分隔开 UI 层需要 split 字符串
	// "(SELECT group_concat(fileName) FROM img WHERE img.parentId = %s.uid) AS
	// imgs";

	// UNION 时，SQLite 居然不能直接使用括号，所以必须得 SELECT * FROM
	// 可以用 Union 合并 两次查询为一次
	// “SELECT * FROM (SELECT id, name FROM ${from} WHERE id > ${id} LIMIT 1) "
	// + "UNION ALL " + "SELECT * FROM (SELECT id, name FROM ${from} WHERE id <
	// ${id} ORDER BY id DESC LIMIT 1)")

	public static String perRecordSql = "SELECT %s, name FROM %s WHERE createDate < %s ORDER BY createDate DESC LIMIT 1";
	public static String nextRecordSql = "SELECT %s, name FROM %s WHERE createDate > %s ORDER BY createDate ASC LIMIT 1";

	public static String perRecordSql2 = "SELECT id, name FROM %s WHERE createDate < datetime('%s') ORDER BY createDate DESC LIMIT 1";
	public static String nextRecordSql2 = "SELECT id, name FROM %s WHERE createDate > datetime('%s') ORDER BY createDate ASC LIMIT 1";

	// public static Map<String, Map<String, Object>> getNeighbor(Connection
	// conn, String tablename, String datetime){
	// Map<String, Map<String, Object>> map = new HashMap<>();
	//
	// String _perRecordSql = String.format(nextRecordSql, tablename, datetime);
	// map.put("perRecord", queryMap(conn, _perRecordSql));
	//
	// String _nextRecordSql = String.format(perRecordSql, tablename, datetime);
	// map.put("nextRecord", queryMap(conn, _nextRecordSql));
	//
	// return map;
	// }

}
