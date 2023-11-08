//package com.ajaxjs.web.jsp;
//
//import com.ajaxjs.framework.PageResult;
//import com.ajaxjs.framework.entity.BaseEntityConstants;
//import com.ajaxjs.framework.spring.DiContextUtil;
//import com.ajaxjs.sql.JdbcHelper;
//import com.ajaxjs.util.date.DateUtil;
//import org.springframework.util.CollectionUtils;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.sql.DataSource;
//import java.sql.Connection;
//import java.sql.Date;
//import java.sql.SQLException;
//import java.sql.Timestamp;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class JspHelper {
//	public static Connection init(HttpServletRequest request) {
//		return getJspHelper(request).initDb(request);
//	}
//
//	public static JspHelper getJspHelper(HttpServletRequest request) {
//		JspHelper self = DiContextUtil.getBean(JspHelper.class);
//
//		if (self == null) {
//			DiContextUtil.registryBean(JspHelper.class);
//			self = DiContextUtil.getBean(JspHelper.class);
//		}
//		request.setAttribute("JSP_HELPER", self);
//
//		return self;
//	}
//
//	public Connection initDb(HttpServletRequest request) {
//		DataSource ds = DiContextUtil.getBean(DataSource.class);
//
//		try {
//			Connection conn = ds.getConnection();
//			request.setAttribute("conn", conn);
//
//			return conn;
//		} catch (SQLException e) {
//
//			e.printStackTrace();
//			return null;
//		}
//	}
//
//	/**
//	 * 准备单个实体详情的 SQL，如果有 id 参数是修改，没用则是新建
//	 * 
//	 * @param req
//	 * @param sql
//	 */
//	public static void parepreInfoSql(HttpServletRequest req, String sql) {
//		parepreInfoSql(req, sql, null);
//	}
//
//	/**
//	 * 准备单个实体详情的 SQL，如果有 id 参数是修改，没用则是新建
//	 * 
//	 * @param req
//	 * @param sql
//	 * @param idCol
//	 */
//	public static void parepreInfoSql(HttpServletRequest req, String sql, String idCol) {
//		boolean isCreate = req.getParameter("id") == null;
//		req.setAttribute("isCreate", isCreate);
//
//		idCol = idCol == null ? " WHERE id = " : "WHERE " + idCol + " = ";
//
//		if (!isCreate)
//			sql += idCol + req.getParameter("id");
//
//		req.setAttribute("sql", sql);
//	}
//
//	public static void parepreListSql(HttpServletRequest req, String sql, String namespace, String namespace_chs) {
//		req.setAttribute("sql", sql);
//		req.setAttribute("namespace", namespace);
//		req.setAttribute("namespace_chs", namespace_chs);
//	}
//
//	public static void getInfo(HttpServletRequest req) {
//		boolean isCreate = req.getParameter("id") == null;
//
//		Connection conn = (Connection) req.getAttribute("conn");
//		Map<String, Object> info = isCreate ? new HashMap<>() : JdbcHelper.queryAsMap(conn, req.getAttribute("sql").toString());
//		req.setAttribute("info", info);
//	}
//
//	public static Map<String, Object> getOne(HttpServletRequest req, String sql) {
//		Connection conn = (Connection) req.getAttribute("conn");
//
//		Map<String, Object> info = JdbcHelper.queryAsMap(conn, sql);
//		req.setAttribute("info", info);
//
//		return info;
//	}
//
//	public static void getList(HttpServletRequest req, String sql) {
//		Connection conn = (Connection) req.getAttribute("conn");
//		List<Map<String, Object>> list = JdbcHelper.queryAsMapList(conn, req.getAttribute("sql") == null ? sql : req.getAttribute("sql").toString());
//		req.setAttribute("list", list);
//	}
//
//	public static String getListSize(List<?> list) {
//		if (CollectionUtils.isEmpty(list))
//			return "";
//
//		return String.valueOf(list.size());
//	}
//
//	public static String formatDate(Object date) {
//		return DateUtil.formatDateShorter(DateUtil.object2Date(date));
//	}
//
//	public static String formatDate(Timestamp ts) {
//		if (ts == null)
//			return "";
//
//		Date date = new Date(ts.getTime());
//
//		return DateUtil.formatDateShorter(date);
//	}
//
//	public static PageResult<Map<String, Object>> page(HttpServletRequest req) {
//		String sql = req.getAttribute("sql").toString();
//		String getTotalSql = "SELECT COUNT(*) AS count FROM (" + sql + ") AS t;";
//		Map<String, Object> one = getOne(req, getTotalSql);
////		if(one == null)
////			throw new NullPointerException("");
//		Long total = (Long) one.get("count");
//
//		PageResult<Map<String, Object>> pageList = new PageResult<>();
//		pageList.setTotalCount(total.intValue());
//
//		if (req.getParameter("start") != null)
//			pageList.setStart(Integer.parseInt(req.getParameter("start")));
//
//		if (total == 0) {
//			pageList.setZero(true);
////            Map<String, Object> map = new HashMap<>();
////            map.put("result", null);
////            map.put("msg", "没有数据，查询结果为零");
////            map.put("total", 0);
//		} else {
//			/* 判断分页参数，兼容 MySQL or 页面两者。最后统一使用 start/limit */
//			int limit;
//
//			if (req.getParameter("pageSize") != null)
//				limit = Integer.parseInt(req.getParameter("pageSize"));
//			else if (req.getParameter("limit") != null)
//				limit = Integer.parseInt(req.getParameter("limit"));
//			else
//				limit = PageResult.DEFAULT_PAGE_SIZE;
//
//			pageList.setPageSize(limit);
//
//			int start;
//
//			if (req.getParameter("pageNo") != null) {
//				int pageNo = Integer.parseInt(req.getParameter("pageNo"));
//				start = PageEnhancer.pageNo2start(pageNo, limit);
//			} else if (req.getParameter("start") != null)
//				start = Integer.parseInt(req.getParameter("start"));
//			else
//				start = 0;
//
//			sql += " LIMIT " + start + "," + limit;
//
//			Connection conn = (Connection) req.getAttribute("conn");
//			List<Map<String, Object>> list = JdbcHelper.queryAsMapList(conn, sql);
//
//			if (list != null) {
//				pageList.addAll(list);
//				pageList.page();
//			}
//		}
//
//		req.setAttribute("PAGE_RESULT", pageList);
//
//		return pageList;
//	}
//
//	public static void closeConn(HttpServletRequest req) {
//		Object c = req.getAttribute("conn");
//
//		if (c != null) {
//			Connection conn = (Connection) c;
//
//			try {
//				conn.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}
//	}
//
//	public static boolean bit(int v, int all) {
//		return (v & all) == v;
//	}
//
//	public static String safeGet(HttpServletRequest req, String key) {
//		String value = req.getParameter(key);
//
//		if (value == null)
//			return null;
//		else {
//			if (!preventSQLInject(value))
//				throw new SecurityException("SQL 注入！");
//			else
//				return value;
//		}
//	}
//
//	/**
//	 * 简单检查字符串有否 SQL 关键字
//	 *
//	 * @param str
//	 * @return false 表示为字符串中有 SQL 关键字
//	 */
//	public static boolean preventSQLInject(String str) {
//		str = str.toUpperCase();
//
//		if (str.indexOf("DELETE") >= 0 || str.indexOf("ASCII") >= 0 || str.indexOf("UPDATE") >= 0 || str.indexOf("SELECT") >= 0 || str.indexOf("'") >= 0
//				|| str.indexOf("SUBSTR(") >= 0 || str.indexOf("COUNT(") >= 0 || str.indexOf(" OR ") >= 0 || str.indexOf(" AND ") >= 0
//				|| str.indexOf("DROP") >= 0 || str.indexOf("EXECUTE") >= 0 || str.indexOf("EXEC") >= 0 || str.indexOf("TRUNCATE") >= 0
//				|| str.indexOf("INTO") >= 0 || str.indexOf("DECLARE") >= 0 || str.indexOf("MASTER") >= 0) {
//			return false;
//		}
//
//		return true;
//	}
//
//	public static String getState(int state) {
//		return BaseEntityConstants.STATE.get(state);
//	}
//}
