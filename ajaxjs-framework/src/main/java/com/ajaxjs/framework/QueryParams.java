/**
 * 版权所有 2017 Sp42 frank@ajaxjs.com
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
package com.ajaxjs.framework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import javax.servlet.http.HttpServletRequest;

import com.ajaxjs.mvc.controller.MvcRequest;
import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.util.Encode;
import com.ajaxjs.util.MappingValue;
import com.ajaxjs.util.map.MapTool;

/**
 * DAO 用的查询参数，可以是 分页 的查询参数，也可以是排序、过滤、搜索等的参数 * 查询时特地需求的容器，可包含特定的对象进行查询，通过
 * getter/setter 注入。 特定的对象一般为 Map 结果。最后转化为 SQL 字符串。 通常是耦合
 * HttpServletRequest.getParameterMap() 返回请求数据
 * 
 * @author Sp42 frank@ajaxjs.com
 *
 */
public class QueryParams {
	/**
	 * 请求的数据
	 */
	public Map<String, Object> paramsMap;

	/**
	 * 创建一个查询参数对象
	 * 
	 * @param requestData 请求参数
	 */
	public QueryParams(Map<String, String[]> requestData) {
		paramsMap = MapTool.as(requestData, arr -> MappingValue.toJavaValue(arr[0]));
		set(requestData, "filterField", "filterValue");// where 查询（精确）
		set(requestData, "searchField", "searchValue");// search 查询（模糊）
		set(requestData, "matchField", "matchValue");// match 查询（精确）
		set(requestData, "orderField", "orderField");// order 查询
	}

	/**
	 * 
	 * @param requestData
	 * @param key
	 * @param value
	 */
	private void set(Map<String, String[]> requestData, String key, String value) {
		if (requestData.containsKey(key) && requestData.containsKey(value)) {
			setData(key, MapTool.toMap(requestData.get(key), requestData.get(value), MappingValue::toJavaValue));
		}
	}

	/**
	 * 
	 * @param type
	 * @param data
	 */
	private void setData(String type, Map<String, Object> data) {
		switch (type) {
		case "filterField":
			filter = data;
			break;
		case "searchField":
			search = data;
			break;
		case "matchField":
			match = data;
			break;
		case "orderField":
			order = data;
		}
	}

	public Map<String, Object> filter = null, search = null, match = null, order = null;

	/**
	 * 所有 where 条件
	 */
	public List<String> wheres = new ArrayList<>();

	/**
	 * 添加 WHERE 子语句
	 * 
	 * @param sql 输入的原 SQL 语句
	 * @return 添加 WHERE 子语句的 SQL 语句
	 */
	public String addWhereToSql(String sql) {
		if (filter != null) {
			for (String key : filter.keySet()) {
				if(filter.get(key) == null)
					continue;
				String str = filter.get(key).toString();
				wheres.add(key + " = " + Encode.urlChinese(str));
			}
		}

		if (search != null) {
			for (String key : search.keySet()) {
				if(search.get(key) == null)
					continue;
				String str = search.get(key).toString();
//				wheres.add(key + " LIKE '%" + Encode.urlChinese(search.get(key).toString()) + "%'");
				wheres.add(key + " LIKE '%" + str + "%'");
			}
		}

		if (match != null) {
			for (String key : match.keySet()) {
				if(match.get(key) == null)
					continue;
				String str = match.get(key).toString();
				wheres.add(key + " LIKE '" + Encode.urlChinese(str) + "'");
			}
		}

		// 增加到原 sql 身上
		if (wheres.size() > 0) {
			String c = String.join(" AND ", wheres);
			String regexp = "(?i)1\\s?(=|AND)\\s?1"; // 支持 1=1、1 AND 1

			if (CommonUtil.regMatch(regexp, sql) != null) {
				sql = sql.replaceAll(regexp, c);
			} else if (sql.contains("WHERE")) {
				sql = sql.replaceAll("WHERE", "WHERE " + c + " AND ");// 写死 AND 并关系，但如果要 OR 呢？
			} else {
				sql += " WHERE " + c;
			}

			wheres.clear(); // 重复执行导致没有退栈
		}

		return sql;
	}

	/**
	 * 转化 ORDER BY
	 * 
	 * @param sql 输入的原 SQL 语句
	 * @return 添加了 ORDER BY 子语句的 SQL 语句
	 */
	public String orderToSql(String sql) {
		if (order != null) {
			List<String> orders = new ArrayList<>();

			for (String key : order.keySet()) {
				orders.add(key + " " + order.get(key));
			}

			String orderBy = String.join(",", orders);

			if (sql.toUpperCase().contains("ORDER BY ")) {
				sql = sql.replaceAll("(?i)ORDER BY ", "ORDER BY " + orderBy + ", ");
			} else {
				sql += " ORDER BY " + orderBy;
			}
		}

		return sql;
	}

	public static String addWhere(String sql) {
		Map<String, String[]> inputMap = new HashMap<>();
		{
			inputMap.put("filterField", new String[] { "status", "catelog" });
			inputMap.put("filterValue", new String[] { "2", "17" });
		}
		return new QueryParams(inputMap).addWhereToSql(sql);
	}

	/**
	 * 
	 * @return 返回 null 而不是空 sqlHandler
	 */
	public static QueryParams init() {
		HttpServletRequest r = MvcRequest.getHttpServletRequest();
		if (r == null)
			return null;

		Map<String, String[]> requestData = MvcRequest.getHttpServletRequest().getParameterMap();
		System.out.println(requestData);
		return requestData.size() > 0 ? new QueryParams(requestData) : null;
	}

	/**
	 * 
	 * @param req
	 * @return
	 */
	public static Function<String, String> initSqlHandler(HttpServletRequest req) {
		Map<String, String[]> requestData = req.getParameterMap();
		return initSqlHandler(new QueryParams(requestData));
	}

	public static Function<String, String> initSqlHandler(QueryParams qs) {
		return sql -> {
			return qs == null ? sql : qs.addWhereToSql(sql);
		};
	}
	public static Function<String, String> makeQuery(String querySql) {
		return sql -> sql.replaceAll("(?i)1\\s?(=|AND)\\s?1", querySql);
	}

	public static <T> BiFunction<Integer, Integer, PageResult<T>> doPageFilterQuery(BaseController.PagedDao<T> fn) {
		return (s, l) -> fn.apply(s, l, QueryParams.initSqlHandler(QueryParams.init()));
	}
}
