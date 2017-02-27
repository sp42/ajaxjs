package com.ajaxjs.framework.dao;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.util.List;

import com.ajaxjs.framework.dao.annotation.Select;
import com.ajaxjs.jdbc.SimpleORM;
import com.ajaxjs.util.reflect.ReflectGeneric;

/**
 * 自动实现接口的实例
 * 
 * @author sp42
 *
 * @param <T>
 */
public class DaoHandler<T extends IDAO> implements InvocationHandler {
	Connection conn;

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Class<?> returnType = method.getReturnType();
		String sql;
		SimpleORM<?> orm = new SimpleORM<>(conn, returnType);

		Select select = method.getAnnotation(Select.class);
		
		if (select != null) {
			sql = select.value();
			// System.out.println(orm.query(select.value(), args));

			if (returnType == int.class) {
				return 8;
			} else if (returnType == String.class) {
				return (String) "";
			} else if (returnType == List.class) {
				orm = new SimpleORM<>(conn, ReflectGeneric.getFirstType(method)); // 获取 List<String> 的 String，而不是 List 假设

				return orm.queryList(sql, args);
			} else {
				System.out.println("unknow return type: " + returnType.getName());
			}
		}

		return null;

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
}
