package com.ajaxjs.cms.dao.sqlFactory;

import java.lang.reflect.Method;

import com.ajaxjs.framework.dao.SqlAndArgs;
import com.ajaxjs.framework.dao.SqlFactory;
import com.ajaxjs.framework.dao.SqlFactoryCriteria;
import com.ajaxjs.orm.dao.QueryParams;
import com.ajaxjs.util.CommonUtil;

public class CatelogServiceSqlFactory implements SqlFactory {
	final static String findCatelog = " IN ( SELECT id FROM general_catelog WHERE `path` LIKE (CONCAT (( SELECT `path` FROM general_catelog WHERE id = ? ) , '%')))";

	@Override
	public SqlAndArgs toSql(SqlAndArgs s) {
		QueryParams qs = SqlFactoryCriteria.getQueryParam(s.args);

		String reg = "#\\{ajSql:(\\s?\\w+,?)+(?=\\})";
		String result = CommonUtil.regMatch(reg, s.sql);

		result = result.replaceAll("#\\{ajSql:", "");

		String[] arr = result.split(",");

		for (String item : arr) {
			item = item.trim();
			if (qs != null && qs.paramsMap != null && qs.paramsMap.containsKey(item)) {
				String sub = item + findCatelog.replaceAll("\\?", qs.paramsMap.get(item).toString());
				qs.wheres.add(sub);
			}
		}

		s.sql = s.sql.replaceAll("#\\{ajSql:(\\s?\\w+,?)+\\}", "");

		return s;
	}

	public static String parser(String sql, Method method, Object[] args) {
		return null;
	}

}
