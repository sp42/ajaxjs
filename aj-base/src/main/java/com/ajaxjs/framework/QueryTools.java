package com.ajaxjs.framework;

import java.io.Serializable;
import java.util.Map;
import java.util.function.Function;

import javax.servlet.http.HttpServletRequest;

import com.ajaxjs.sql.JdbcConnection;
import com.ajaxjs.sql.JdbcHelper;
import com.ajaxjs.sql.orm.IBaseDao;
import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.util.MappingValue;
import com.ajaxjs.web.ServletHelper;
import com.ajaxjs.web.mvc.MvcRequest;

/**
 * 为方便查询的工具类
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class QueryTools {
	/**
	 * 按照 id 字段进行降序。 提示：如果只有一个 SQL 参数，那么符合 Function<String, String>，直接使用
	 * ::orderById_DESC 即可，但往往需要另外的参数，于是有了高阶函数
	 * 
	 * @param sql 输入的SQL
	 * @return 修改后的 SQL
	 */
	public static String orderById_DESC(String sql) {
		return sql + IBaseDao.DESCENDING_ID;
	}

	/**
	 * 设置排序
	 * 
	 * @param o 排序规则
	 * @return SQL 处理器
	 */
	public static Function<String, String> orderBy(String o) {
		return sql -> sql + " ORDER BY " + o;
	}

	/**
	 * 取头 X 笔记录的高阶函数
	 * 
	 * @param top 头 X 笔
	 * @return SQL 处理器
	 */
	public static Function<String, String> top(int top) {
		return sql -> sql + " LIMIT 0, " + top;
	}

	/**
	 * 替换 1=1 为查询语句。这是基础的方法
	 * 
	 * @param sql   包含 1=1 的 SQL
	 * @param where 条件语句
	 * @return 新的 SQL
	 */
	public static String where(String sql, String where) {
		return sql.replace(IBaseDao.WHERE_REMARK, "(" + where + ")" + IBaseDao.WHERE_REMARK_AND);
	}

	/**
	 * 拼接 SQL 等于的语句，如果是字符串加上单引号
	 * 
	 * @param field 字段名
	 * @param value 值，如果是字符串会自动加上单引号
	 * @return 包含等号的 SQL 语句
	 */
	public static String equals(String field, Object value) {
		if (value instanceof String)
			value = "'" + value + "'";
//		System.out.println(">>>>>>>>>>>>>>:::"+field + " = " + value);
		return field + " = " + value;
	}

	/**
	 * 设置关系为 = 的条件查询语句
	 * 
	 * @param sql   包含 1=1 的 SQL
	 * @param field 字段名
	 * @param value 值，如果是字符串会自动加上单引号
	 * @return 新的 SQL
	 */
	public static String where(String sql, String field, Object value) {
		return where(sql, equals(field, value));
	}

	/**
	 * 生成查询表达式的高阶函数
	 * 
	 * @param where WHERE 子语句
	 * @return SQL 处理器
	 */
	public static Function<String, String> setWhere(String where) {
		return where == null ? sql -> sql : sql -> where(sql, where);
	}

	/**
	 * 输入 WHERE 条件，必须是“等于 =”的关系，即 WHERE id = xxx。
	 * 
	 * @param field 字段名
	 * @param value 值，如果是字符串会自动加上单引号
	 * @return SQL 处理器
	 */
	public static Function<String, String> by(String field, Object value) {
		return setWhere(equals(field, value));
	}

	/**
	 * 控制器和业务方法可以不用提供 value 的参数，由 HttpServletRequest 获取
	 * 
	 * @param r
	 * @param query
	 * @param type
	 * @return
	 */
	public static Object getValue(HttpServletRequest r, String query, Class<?> type) {
		if (r != null && r.getParameter(query) != null) {
			String _v = r.getParameter(query);
			Object v = null;

			if (type == String.class) {
				if (!ServletHelper.preventSQLInject(_v)) // 防止 SQL 注入
					return setWhere(null);

				v = _v;
			} else if (type == Long.class || type == long.class)
				v = Long.parseLong(_v);
			else if (type == Integer.class || type == int.class)
				v = Integer.parseInt(_v);

			return v;
		}

		return null;
	}

	/**
	 * 
	 * @param query
	 * @param type
	 * @return
	 */
	public static Object getValue(String query, Class<?> type) {
		return getValue(MvcRequest.getHttpServletRequest(), query, type);
	}

	/**
	 * 控制器和业务方法可以不用提供 value 的参数，由 HttpServletRequest 获取
	 * 
	 * @param r
	 * @param query
	 * @param field
	 * @param type
	 * @return
	 */
	public static Function<String, String> by(String query, Class<?> type, String field) {
		return sql -> {
			Object v = getValue(query, type);
			return v == null ? sql : where(sql, field, v);
		};
	}

	/**
	 * 按实体唯一 id查找的高阶函数
	 * 
	 * @param uid 实体唯一 id
	 * @return SQL 处理器
	 */
	public static Function<String, String> byUid(long uid) {
		return by("uid", uid);
	}

	/**
	 * 按实体 userId 查找的高阶函数
	 * 
	 * @param userId 用户 id
	 * @return SQL 处理器
	 */
	public static Function<String, String> byUserId(long userId) {
		return by("userId", userId);
	}

	/**
	 * 多个值批评的查找的高阶函数
	 * 
	 * @param field  被查询的字段
	 * @param values 多个值
	 * @return SQL 处理器
	 */
	public static Function<String, String> in(String field, String[] values) {
		return setWhere(field + " IN (" + String.join(",", values) + ")");
	}

	/**
	 * 实体状态约束
	 * 
	 * @param status 状态常量
	 * @return SQL 处理器
	 */
	public static Function<String, String> setStatus(int status) {
		switch (status) {
		case CommonConstant.DELTETED: // 连删除的都可以查看，即查看全部
			return setWhere(null);
		case CommonConstant.ON_LINE: // 前台查看
			return setWhere("stat = 1 OR stat IS NULL");
		case CommonConstant.OFF_LINE: // 连下线的都可以查看你，常用于后台查看数据
		default:
			return setWhere("stat = 0 OR stat = 1 OR stat is NULL");
		}
	}

	/**
	 * 根据关键字搜索的高阶函数
	 * 
	 * @param field   被查询的字段
	 * @param keyword 搜索的关键字
	 * @param isExact true 表示为精确查询，否则为模糊查询
	 * @return SQL 处理器
	 */
	public static Function<String, String> likeSqlHandler(String field, String keyword, boolean isExact) {
		if (!isExact)
			keyword = "%" + keyword + "%";

		return setWhere(field + "LIKE ");
	}

	/**
	 * 谨慎使用！这查询权力很大，可指定任意的字段
	 * 
	 * @return SQL 处理器
	 */
	public static Function<String, String> byAny(HttpServletRequest r) {
		String value = r.getParameter("filterValue");

		if (value == null || "null".equals(value))
			return setWhere(null);

//		return by(r.getParameter("filterField"), CommonUtil.regTest("\\d+", value) ? MappingValue.toJavaValue(value) : value);
		return by(r.getParameter("filterField"), value.matches("\\d+") ? MappingValue.toJavaValue(value) : value);
	}

	/**
	 * 对多个字段搜索
	 * 
	 * @param fields 可以被搜索的那些字段
	 * @param r      请求对象
	 * @return SQL 处理器
	 */
	public static Function<String, String> searchQuery(String[] fields, HttpServletRequest r) {
		if (r == null || CommonUtil.isEmptyString(r.getParameter("keyword")))
			return setWhere(null);

		String keyword = r.getParameter("keyword").trim(), isExact = r.getParameter("isExact");

		if (!ServletHelper.preventSQLInject(keyword)) // 防止 SQL 注入
			return setWhere(null);

		keyword = ServletHelper.MysqlRealScapeString(keyword);

		String like = MappingValue.toBoolean(isExact) ? keyword : ("'%" + keyword + "%'");

		for (int i = 0; i < fields.length; i++)
			fields[i] = fields[i] + " LIKE " + like;

		return setWhere(String.join(" OR ", fields));
	}

	/**
	 * 根据日期范围搜索的高阶函数
	 * 
	 * @param fieldName 被查询的日期字段
	 * @param r         请求对象
	 * @return SQL 处理器
	 */
	public static Function<String, String> betweenCreateDate(String fieldName, HttpServletRequest r) {
		if (r == null)
			return setWhere(null);

		String startDate = r.getParameter("startDate"), endDate = r.getParameter("endDate");

		if (r == null || CommonUtil.isEmptyString(startDate) || CommonUtil.isEmptyString(endDate))
			return setWhere(null);

		if (!ServletHelper.preventSQLInject(startDate) || !ServletHelper.preventSQLInject(endDate)) // 防止 SQL 注入
			return setWhere(null);

		return setWhere(fieldName + " BETWEEN '" + startDate + "' AND DATE_ADD('" + endDate + "', INTERVAL 1 DAY)");
	}

	/**
	 * 相邻记录
	 * 
	 * @TODO 没权限的不要列出
	 * @param mv
	 * @param tableName
	 * @param id
	 */
	public static void getNeighbor(Map<String, Object> map, String tableName, Serializable id) {
		Map<String, Object> perv, next;
		perv = JdbcHelper.queryAsMap(JdbcConnection.getConnection(), "SELECT id, name FROM " + tableName + " WHERE id < ? ORDER BY id DESC LIMIT 1", id);
		next = JdbcHelper.queryAsMap(JdbcConnection.getConnection(), "SELECT id, name FROM " + tableName + " WHERE id > ? LIMIT 1", id);

		map.put("neighbor_pervInfo", perv);
		map.put("neighbor_nextInfo", next);
	}
}
