package com.ajaxjs.framework;

import java.io.Serializable;
import java.util.Map;
import java.util.function.Function;

import javax.servlet.http.HttpServletRequest;

import com.ajaxjs.cms.app.CommonConstant;
import com.ajaxjs.orm.JdbcConnection;
import com.ajaxjs.orm.JdbcHelper;
import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.util.MappingValue;
import com.ajaxjs.web.ServletHelper;

/**
 * 为方便查询的工具类
 * 
 * @author Frank Cheung
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
	 * 取头 X 笔记录的高阶函数
	 * 
	 * @param top 头 X 笔
	 * @return SQL 处理器
	 */
	public static Function<String, String> top(int top) {
		return sql -> sql + " LIMIT 0, " + top;
	}

	/**
	 * 生成查询表达式的高阶函数
	 * 
	 * @param where WHERE 子语句
	 * @return SQL 处理器
	 */
	public static Function<String, String> setWhere(String where) {
		return where == null ? sql -> sql
				: sql -> sql.replace(IBaseDao.WHERE_REMARK, "(" + where + ")" + IBaseDao.WHERE_REMARK_AND);
	}

	/**
	 * 输入 WHERE 条件，必须是“等于 =”的关系，即 WHERE id = xxx。
	 * 
	 * @param field 字段名
	 * @param value 值，如果是字符串会自动加上单引号
	 * @return SQL 处理器
	 */
	public static Function<String, String> by(String field, Object value) {
		if (value instanceof String)
			value = "'" + value + "'";

		return setWhere(field + " = " + value);
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
	 * @param uid userId
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

		if (value == null)
			return setWhere(null);

		return by(r.getParameter("filterField"),
				CommonUtil.regTest("\\d+", value) ? MappingValue.toJavaValue(value) : value);
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
		perv = JdbcHelper.queryAsMap(JdbcConnection.getConnection(),
				"SELECT id, name FROM " + tableName + " WHERE id < ? ORDER BY id DESC LIMIT 1", id);
		next = JdbcHelper.queryAsMap(JdbcConnection.getConnection(),
				"SELECT id, name FROM " + tableName + " WHERE id > ? LIMIT 1", id);

		map.put("neighbor_pervInfo", perv);
		map.put("neighbor_nextInfo", next);
	}
}
