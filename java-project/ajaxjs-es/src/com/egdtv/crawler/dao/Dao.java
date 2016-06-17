package com.egdtv.crawler.dao;

import java.util.Map;

import org.apache.ibatis.jdbc.SQL;

public class Dao {
	public String selectAudioById(final Map<String, Object> parames) {
		final String tableName = parames.get("tablename").toString();
		return new SQL() {
			{
				SELECT(tableName + ".*, catalog.name AS catalogName");
				FROM(tableName);
				WHERE(tableName + ".id = #{id}");
				INNER_JOIN("catalog ON " + tableName + ".catalog = catalog.id");
			}
		}.toString();
	}
}
