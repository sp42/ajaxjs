package com.ajaxjs.framework.dao;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

import com.ajaxjs.framework.dao.annotation.Insert;
import com.ajaxjs.framework.dao.annotation.Select;
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
		
		returnVal = select(method, args);
		if(returnVal != null)
			return returnVal;
		else{
			returnVal = insert(method, args);
			if(returnVal != null)
				return returnVal;
			else {
//				returnVal = insert(method, args);
//				if(returnVal != null)
//					return returnVal;
			}
		}
		
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
	
	private Object select(Method method, Object[] args) {
		Class<?> returnType = method.getReturnType();
		String sql;
		SimpleORM<?> orm = new SimpleORM<>(conn, returnType);

		Select select = method.getAnnotation(Select.class); // SQL SELECT 注解
		
		if (select != null) {
			sql = select.value();
			// System.out.println(orm.query(select.value(), args));

			if (returnType == int.class) {
				Map<String, Object> map = Helper.query(conn, sql);
				for(String key : map.keySet())
					return (int)map.get(key); // 有且只有一个记录
				
			} else if (returnType == String.class) {
				return (String) "";
			} else if (returnType == List.class) {
				orm = new SimpleORM<>(conn, ReflectGeneric.getFirstType(method)); // 获取 List<String> 泛型里的 String，而不是 List 类型

				return orm.queryList(sql, args);
			} else {
				// maybe a bean
				return orm.query(sql, args);
			}
		}
		
		return null;
		
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
