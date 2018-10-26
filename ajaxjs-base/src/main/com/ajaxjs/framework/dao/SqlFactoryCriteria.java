package com.ajaxjs.framework.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.ajaxjs.util.CollectionUtil;

/**
 * 
 * @author Frank Cheung
 *
 */
public class SqlFactoryCriteria implements SqlFactory {
	private QueryParams queryParam;

	@Override
	public SqlAndArgs toSql(SqlAndArgs s) {
		queryParam = getQueryParam(s.args);
		s.args = removeItem(s.args, null); // 有时 QueryParam 是 null

		if (queryParam != null) {
			s.args = removeItem(s.args, queryParam); // 删掉数组里的那个特殊的 QueryParam
			s.sql = queryParam.orderToSql(s.sql);
			s.sql = queryParam.addWhereToSql(s.sql);
		}

		return s;
	}

	/**
	 * 看看是否有特殊的 QueryParam
	 * 
	 * @param args 参数，可能 查询参数对象
	 * @return 查询参数对象，null 表示没有查询参数对象
	 */
	public static QueryParams getQueryParam(Object[] args) {
		if (CollectionUtil.isNull(args))
			return null;

		/* 通常 QueryParam 放在最后一个的参数列表，于是我们从最后开始找，这样程序会快点 */
		for (int i = args.length - 1; i >= 0; i--) {
			if (args[i] instanceof QueryParams)
				return (QueryParams) args[i];
		}

		return null;
	}

	/**
	 * 删除数组中的某一个元素
	 * 
	 * @param objs 数组
	 * @return 新数组
	 */
	private static Object[] removeItem(Object[] objs, Object item) {
		if (objs == null || objs.length == 0)
			return objs;
		List<Object> list = new ArrayList<>(Arrays.asList(objs));
		list.remove(item);

		return list.size() > 0 ? list.toArray() : null;
	}

	/**
	 * 找到 QueryParams 并从数组里面清除掉
	 * 
	 * @param args 参数列表
	 * @return 去掉 QueryParams 的参数列表
	 */
	public static Object[] cleanQueryParam(Object[] args) {
		QueryParams qs = getQueryParam(args);
		return qs == null ? args : removeItem(args, qs);
	}

	public QueryParams getQueryParam() {
		return queryParam;
	}

	public void setQueryParam(QueryParams queryParam) {
		this.queryParam = queryParam;
	}

	public String addWhereToSql(String sql) {
		return queryParam == null ? sql : queryParam.addWhereToSql(sql);
	}
}
