package com.ajaxjs.framework.dao;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.ajaxjs.framework.dao.annotation.Delete;
import com.ajaxjs.framework.dao.annotation.Insert;
import com.ajaxjs.framework.dao.annotation.Select;
import com.ajaxjs.framework.dao.annotation.Update;
import com.ajaxjs.framework.model.PageResult;
import com.ajaxjs.framework.model.Query;
import com.ajaxjs.framework.model.Query.Filter;
import com.ajaxjs.framework.model.Query.Order;
import com.ajaxjs.jdbc.Helper;
import com.ajaxjs.jdbc.SimpleORM;
import com.ajaxjs.util.StringUtil;
import com.ajaxjs.util.reflect.ReflectGeneric;

/**
 * 自动实现接口的实例
 * 
 * @author sp42
 * @param <T>
 */
public class DaoHandler<T extends IDAO> implements InvocationHandler {
	Connection conn;

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Object returnVal = null;
		Class<?> 
				returnType = method.getReturnType(), 
				entryType = getEntryContainerType(method), 
				entryTypeByArgs = null; 
		
		if(args != null && args[0] != null) entryTypeByArgs = args[0].getClass();
		
		returnVal = select(method, args, returnType, entryType);
		if(returnVal != null) return returnVal;

		returnVal = insert(method, args, returnType, entryTypeByArgs);
		if(returnVal != null) return returnVal;
		
		returnVal = update(method, args, returnType, entryTypeByArgs);
		if(returnVal != null) return returnVal;
		
		returnVal = delete(method, args, returnType, entryTypeByArgs);
		return returnVal;
	}

	public Connection getConn() {
		return conn;
	}

	public DaoHandler<T> setConn(Connection conn) {
		this.conn = conn;
		return this;
	}

	@SuppressWarnings("unchecked")
	public T bind(Class<? extends IDAO> clz) {
		return (T) Proxy.newProxyInstance(clz.getClassLoader(), new Class[] { clz }, this);
	}
	
	/**
	 * 获取实体的真实类型，是 map？还是 bean？
	 * @param method
	 * @return
	 */
	private Class<?> getEntryContainerType(Method method) {
		Class<?> type = method.getReturnType();
		if(type == List.class || type == PageResult.class) {
			type = ReflectGeneric.getFirstType(method);// 获取 List<String> 泛型里的 String，而不是 List 类型
		}
		
		return type;
	}
	 
	private <R, B> Object select(Method method, Object[] args, Class<R> returnType, Class<B> entryType) {
		Select select = method.getAnnotation(Select.class); // SQL SELECT 注解
		if (select == null) return null;
		
		String sql = select.value();
		SimpleORM<B> orm = new SimpleORM<>(conn, entryType);

		if (returnType == int.class) {
			return Helper.queryOne(conn, sql, int.class, args);
		} else if (returnType == String.class) {
			return (String) "";
		} else if (returnType == List.class) {
			return orm.queryList(sql, args);
		} else if (returnType == List.class || returnType == PageResult.class) { // 分页
			QueryParam queryParam = getQueryParam(args);
			if (queryParam != null) {
				args = removeItem(args, queryParam); // 删掉数组里的那个特殊的 QueryParam
				
				if(queryParam.query != null) {
					Query query = queryParam.query;
					sql = orderBy(query, sql);
					sql = addWhere(query, sql);
				}
			}
			
			if (returnType == List.class) {
				return orm.queryList(sql, args);
			} else {
				// 分页
				if (queryParam.pageParam.length != 2)
					throw new IllegalArgumentException("没有分页参数！");/* queryParam.pageParam 是必须的 */
				
				// 子查询 select count(*) record_ from ( select * from yourtable t where t.column_ = 'value_' )
				String countSql = sql.replaceAll("SELECT", "SELECT COUNT(\\*) AS count, ");
				int total = Helper.queryOne(conn, countSql, int.class, args);
				
				if (total <= 0) {
					throw new RuntimeException("没有记录");
				} else {
					PageResult<B> result = new PageResult<>();
					result.setStart(queryParam.pageParam[0]);
					result.setPageSize(queryParam.pageParam[1]);
					
					result.setTotalCount(total);// 先查询总数,然后执行分页
					result.page();
					result.setRows(orm.queryList(sql + " LIMIT " + result.getStart() + ", " + result.getPageSize(), args));
					
					return result;
				}
			}
		} else {
			// maybe a bean
			return orm.query(sql, args);
		}
	}

	/**
	 * 排序
	 * @param query
	 * @param sql
	 * @return
	 */
	private String orderBy(Query query, String sql) {
		if (query.getOrder() != null) {
			Order order = query.getOrder();
			
			List<String> orders = new ArrayList<>();
			
			for (String key : order.keySet()) {
				if (!StringUtil.isEmptyString(order.get(key)))
					orders.add(key + " " + order.get(key));
			}
			
			String orderBy = StringUtil.stringJoin(orders, ",");
			
			if(sql.toUpperCase().contains("ORDER BY ")) {
				sql = sql.replaceAll("(?i)ORDER BY ", "ORDER BY " + orderBy + ", ");
			} else {
				sql += "ORDER BY " + orderBy;
			}
		}
		
		return sql;
	}
	
	/**
	 * 添加 WHERE 子语句
	 * 
	 * @param sql
	 *            动态 SqlBuilder 实例
	 * @param query
	 *            Query 查询对象
	 */
	private static String addWhere(Query query, String sql) {
		Map<String, String> map;
		List<String> wheres = new ArrayList<>();
		
		if (query.getFilter() != null) {
			Filter filter = query.getFilter();

			map = (Map<String, String>) filter;

			for (String key : map.keySet()) {
				if (!StringUtil.isEmptyString(map.get(key))) {

					if (filter.isCustomOpeartor()) {
						wheres.add(key + map.get(key));
					} else {
						wheres.add(key + " = " + map.get(key));
					}
				}
			}
		}
		
		if (query.getSearch() != null) {
			map = query.getSearch();
			for (String key : map.keySet()) {
				if (!StringUtil.isEmptyString(map.get(key)))
					wheres.add(key + " LIKE '%" + map.get(key) + "%'");
			}
		}
		
		if (query.getMatch() != null) {
			map = query.getMatch();
			for (String key : map.keySet()) {
				if (!StringUtil.isEmptyString(map.get(key)))
					wheres.add(key + " LIKE '" + map.get(key) + "'");
			}
		}
		
		return sql; // TODO
	}

	/**
	 * 看看是否有特殊的 QueryParam
	 * @param args
	 * @return
	 */
	private QueryParam getQueryParam(Object[] args) {
		QueryParam queryParam = null;
		
		/* 通常 QueryParam 放在最后一个的参数列表，于是我们从最后开始找，这样程序会快点 */
		for(int i = args.length - 1; i >= 0; i--) {
			if (args[i] instanceof QueryParam) {
				queryParam = (QueryParam) args[i];
				break;
			}
		}
		
		return queryParam;
	}

	/**
	 * 删除数组中的某一个元素
	 * 
	 * @param objs
	 *            数组
	 * @param item
	 *            要删除的那个元素
	 * @return 新数组
	 */
	static Object[] removeItem(Object[] objs, Object item) {
		List<Object> list = new ArrayList<>(Arrays.asList(objs));
		list.remove(item);
		return list.size() > 0 ? list.toArray() : null;
	}

	@SuppressWarnings("unchecked")
	private <R, B> Object insert(Method method, Object[] args, Class<R> returnType, Class<B> beanType) {
		Insert insert = method.getAnnotation(Insert.class);
		if (insert == null) 
			return null;

		SimpleORM<B> orm = new SimpleORM<>(conn, beanType);
		Serializable id = null;
		
		if(!insert.value().equals("")) { /* 以 sql 方式创建 */
			id = Helper.create(conn, insert.value(), args);
		} else if (insert.value().equals("") && insert.tableName() != null && args[0] != null) { /* 以 bean 方式创建 */
			id = orm.create((B)args[0], insert.tableName());
		}
		
		if(returnType == Long.class && id.getClass() == Integer.class) {
			return new Long((Integer)id);
		} else {
			return id;
		}
	}
	
	@SuppressWarnings("unchecked")
	private <R, B> Integer update(Method method, Object[] args, Class<R> returnType, Class<B> beanType) {
		Update update = method.getAnnotation(Update.class);
		if (update == null) 
			return null;
		
		SimpleORM<B> orm = new SimpleORM<>(conn, beanType);
		int effectRows = 0;
		
		if(!update.value().equals("")) { /* 以 sql 方式更新 */
			effectRows = Helper.update(conn, update.value(), args);
		} else if (update.value().equals("") && update.tableName() != null && args[0] != null) { /* 以 bean 方式更新 */
			effectRows = orm.update((B)args[0], update.tableName());
		}
		
		return effectRows;
	}
	
	@SuppressWarnings("unchecked")
	private <R, B> Boolean delete(Method method, Object[] args, Class<R> returnType, Class<B> beanType) {
		Delete delete = method.getAnnotation(Delete.class);
		if (delete == null) 
			return null;
		
		SimpleORM<B> orm = new SimpleORM<>(conn, beanType);
		boolean isOk = false;
		
		if(!delete.value().equals("")) { /* 以 sql 方式删除 */
			isOk = Helper.delete(conn, delete.value(), args);
		} else if (delete.value().equals("") && delete.tableName() != null && args[0] != null) { /* 以 bean 方式删除 */
			isOk = orm.delete((B)args[0], delete.tableName());
		}
		
		return isOk;
	}
}
