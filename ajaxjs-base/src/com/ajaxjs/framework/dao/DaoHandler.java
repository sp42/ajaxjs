package com.ajaxjs.framework.dao;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.ajaxjs.framework.dao.annotation.Insert;
import com.ajaxjs.framework.dao.annotation.Select;
import com.ajaxjs.framework.model.PageResult;
import com.ajaxjs.jdbc.Helper;
import com.ajaxjs.jdbc.SimpleORM;
import com.ajaxjs.util.reflect.ReflectGeneric;

/**
 * 自动实现接口的实例
 * 
 * @author sp42
 * @param <K>
 *            结果的类型，可以是 map 或者 bean
 * @param <T>
 */
public class DaoHandler<K, T extends IDAO> implements InvocationHandler {
	Connection conn;

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Object returnVal = null;
		
		Class<?> entryType = getEntryContainerType(method);
		
		returnVal = select(method, args, entryType);
		if(returnVal != null) return returnVal;

		returnVal = insert(method, args);
		if(returnVal != null) return returnVal;

		return returnVal;
	}

	public Connection getConn() {
		return conn;
	}

	public DaoHandler<K, T> setConn(Connection conn) {
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
	 
	private <R> Object select(Method method, Object[] args, Class<R> entryType) {
		String sql;
		SimpleORM<R> orm = new SimpleORM<>(conn, entryType);
		Class<?> returnType = method.getReturnType();
		Select select = method.getAnnotation(Select.class); // SQL SELECT 注解
		
		if (select != null) {
			sql = select.value();
			// System.out.println(orm.query(select.value(), args));

			if (returnType == int.class) {
				return Helper.queryOne(conn, sql, int.class, args);
			} else if (returnType == String.class) {
				return (String) "";
			} else if (returnType == List.class) {
				return orm.queryList(sql, args);
			} else if (returnType == PageResult.class) { // 分页
				QueryParam queryParam = null;
				boolean hasQueryParam = false; // 是否有特殊的 QueryParam
				
				for (Object o : args) {
					if (o instanceof QueryParam) {
						queryParam = (QueryParam) o;
						hasQueryParam = true;
						break;
					}
				}

				if (hasQueryParam)
					args = removeItem(args, queryParam); // 删掉数组里的那个特殊的 QueryParam

				if (queryParam.pageParam.length != 2) {
					throw new IllegalArgumentException("没有分页参数！");
				}
				int start = queryParam.pageParam[0], limit = queryParam.pageParam[1];

				String countSql = sql.replaceAll("SELECT", "SELECT COUNT(\\*) AS count, ");
				int total = Helper.queryOne(conn, countSql, int.class, args);

				if (total <= 0) {
					throw new RuntimeException("没有记录");
				} else {
					PageResult<R> result = new PageResult<>();
					result.setStart(start);
					result.setPageSize(limit);

					result.setTotalCount(total);// 先查询总数,然后执行分页
					result.page();
					result.setRows(orm.queryList(sql + " LIMIT " + result.getStart() + ", " + result.getPageSize(), args));

					return result;
				}
			} else {
				// maybe a bean
				return orm.query(sql, args);
			}
		}
		
		return null;
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
	private Object insert(Method method, Object[] args) {
		Class<K> returnType = (Class<K>) method.getReturnType();
		SimpleORM<K> orm = new SimpleORM<>(conn, returnType);

		Insert insert = method.getAnnotation(Insert.class);
		if (insert != null) {
			if (insert.value().equals("autoCreate")) {
				if (insert.tableName().equals("")) {
					throw new RuntimeException("如果使用类 autoCreate 那么 tableName 是必填");
				} else {
					Serializable id = orm.create((K) args[0], insert.tableName());
					
					if(returnType == Long.class && id.getClass() == Integer.class) {
						return new Long((Integer)id);
					} else {
						return id;
					}
				}
			}
		}

		return null;
	}
}
