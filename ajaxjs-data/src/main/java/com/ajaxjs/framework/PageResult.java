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

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.ajaxjs.orm.JdbcHelper;
import com.ajaxjs.orm.annotation.Select;
import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.util.logger.LogHelper;

/**
 * 分页信息 bean
 * 
 * @author Sp42 frank@ajaxjs.com
 *
 * @param <T> Bean 对象，也可以是 Map
 */
public class PageResult<T> extends ArrayList<T> {
	private static final long serialVersionUID = 543109149479031294L;

	private int totalCount; // 总记录数
	private int start; // 从第几笔记录开始
	private int pageSize; // 每页大小
	private int totalPage; // 总页数
	private int currentPage; // 当前第几页

	private boolean isZero; // 是否没有数据，就是查询了之后，一条记录符合都没有

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	/**
	 * 分页的逻辑运算
	 */
	public void page() {
		int totalPage = getTotalCount() / getPageSize(), yushu = getTotalCount() % getPageSize();

		totalPage = (yushu == 0 ? totalPage : totalPage + 1);
		setTotalPage(totalPage);

		int currentPage = (getStart() / getPageSize()) + 1;
		setCurrentPage(currentPage);
	}

	public boolean isZero() {
		return isZero;
	}

	public void setZero(boolean isZero) {
		this.isZero = isZero;
	}

	/**
	 * 分页操作：一、先查询有没有记录（不用查 列 column）；二、实际分页查询（加入 LIMIT ?, ? 语句，拼凑参数）
	 * 
	 * @param conn
	 * @param entryType
	 * @param select
	 * @param sql
	 * @param method
	 * @param args
	 * @return 分页列表，如果找不到数据，仍返回一个空的 PageList，但可以通过 getZero() 得知是否为空
	 */
	@SuppressWarnings("unchecked")
	public static <B> PageResult<B> doPage(Connection conn, Class<B> entryType, Select select, String sql, Method method, Repository dao, Object[] args, Object _sqlHandler) {
		P p = getPageParameters(method, args);

		System.out.println("::::::::::::::::::::::::::::::::::::" + Arrays.toString(p.args));

		int total = countTotal(select, sql, p.args, dao, conn, (Function<String, String>) _sqlHandler);

		System.out.println("::::::::::::::::::::::::::::::::::::" + total);
		PageResult<B> result = new PageResult<>();

		if (total <= 0) {
			LogHelper.p(sql + "查询完毕，没有符合条件的记录");
			result.setZero(true); // 查询完毕，没有符合条件的记录
		} else {
			int start = p.pageParams[0];
			int limit = p.pageParams[1];

			List<B> list;
			if (entryType == Map.class) {
				list = (List<B>) JdbcHelper.queryAsMapList(conn, sql + " LIMIT ?, ?", args);
			} else {
				list = JdbcHelper.queryAsBeanList(entryType, conn, sql + " LIMIT ?, ?", args);
			}

			result.setStart(start);
			result.setPageSize(limit);
			result.setTotalCount(total);// 先查询总数,然后执行分页
			result.page();

			if (list != null)
				result.addAll(list);

		}

		return result;
	}

	/**
	 * 获取统计行数
	 * 
	 * @param select 业务逻辑 SQL 所在的注解
	 * @param sql 业务逻辑 SQL
	 * @param args DAO 方法参数，不要包含 start/limit 参数
	 * @param conn 连接对象，判断是否 MySQL or SQLite
	 * @return 统计行数
	 */
	private static int countTotal(Select select, String sql, Object[] args, Repository dao, Connection conn, Function<String, String> sqlHandler) {
		String countSql;

		if (CommonUtil.isEmptyString(select.countSql())) {
			// 另外一种统计方式，但较慢 子查询
			// 这是默认的实现，你可以通过增加 sqlCount 注解给出特定的 统计行数 之 SQL
			countSql = "SELECT COUNT(*) AS count FROM (" + sql + ") AS t;";
//			countSql = sql.replaceAll("SELECT.*FROM", "SELECT COUNT(\\*) AS count FROM");
		} else {
			countSql = Repository.isSqlite(select.sqliteCountSql(), conn) ? select.sqliteCountSql() : select.countSql();
		}

		if (sqlHandler != null) {
			countSql = sqlHandler.apply(countSql);
		}

		countSql = dao.handleSql(countSql, null);

		if (conn.toString().contains("sqlite")) {
			return JdbcHelper.queryOne(conn, countSql, Integer.class, args);
		} else {// mysql 返回 long，转换一下
			Long total = JdbcHelper.queryOne(conn, countSql, Long.class, args);
			return total == null ? 0 : total.intValue();
		}
	}

	static final int defaultPageSize = 8;

	static class P {
		/**
		 * 分页参数
		 */
		public int[] pageParams;

		/**
		 * 不包含分页参数的参数列表
		 */
		public Object[] args;
	}

	/**
	 * 获取分页参数，利用反射 DAO 方法参数列表来定位分页的 start/limit
	 * 
	 * @param method 方法对象
	 * @param args 包含分页参数 start/limit 的参数列表
	 * @return
	 */
	private static P getPageParameters(Method method, Object[] args) {
		P p = new P();
		int[] pageParams = new int[2];
		Parameter[] parameters = method.getParameters();

		if (parameters == null || parameters.length == 0) {
			pageParams[0] = 0;
			pageParams[1] = defaultPageSize;
		}

		int removeStartIndex = 0, removeLimitIndex = 0, sqlHandlerIndex = 0;

		for (int i = 0; i < parameters.length; i++) {
			Parameter param = parameters[i];
			if ("arg0".equals(param.getName()) || "arg1".equals(param.getName()))
				throw new Error(" Java 8 支持反射获取 参数 具体名称，但要打开编译开关。例如 Eclipse 须在 Store information about method parameters (usable via reflection) 打勾，或者编译时加入参数 -parameters。");

			if (param.getName().equalsIgnoreCase("start")) {

				pageParams[0] = (int) args[i];
				removeStartIndex = i;

			} else if (param.getName().equalsIgnoreCase("limit")) {
				int l = (int) args[i];

				if (l == 0) {// limit 不能为 0
					pageParams[1] = defaultPageSize;
					args[i] = defaultPageSize;
				} else {
					pageParams[1] = l;
				}

				removeLimitIndex = i;
			} else if (Function.class.equals(param.getType())) {
				sqlHandlerIndex = i;
			}
		}

		List<Object> list = new ArrayList<>(); // 移除分页参数，形成新的参数列表

		for (int i = 0; i < args.length; i++) {
			if (i == removeStartIndex || i == removeLimitIndex || i == sqlHandlerIndex) {

			} else {
				list.add(args[i]);
			}
		}

		p.pageParams = pageParams;
		p.args = list.toArray();

		return p;
	}

	/**
	 * 列表不分页，转换为 PageResult
	 * 
	 * @param list
	 * @return
	 */
	public static <X> PageResult<X> list2PageList(List<X> list) {
		PageResult<X> pageResult = new PageResult<>();
		pageResult.addAll(list);
		pageResult.setPageSize(list.size());
		pageResult.setTotalCount(list.size());

		return pageResult;
	}

	/*
	 * 分页时高效的总页数计算 我们一般分页是这样来计算页码的： int row=200; //记录总数 int page=5;//每页数量 int
	 * count=row%5==0?row/page:row/page+1; 上面这种是用的最多的! 那么下面我们来一种最简单的，不用任何判断！ 看代码：
	 * int row=21; int pageCount=5; int sum=(row-1)/pageCount+1;//这样就计算好了页码数量，逢1进1
	 */
}