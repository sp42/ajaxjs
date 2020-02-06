package com.ajaxjs.orm;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import com.ajaxjs.orm.annotation.Select;
import com.ajaxjs.orm.annotation.Update;

public class TestDynamicProxy {
	// 可以创建实例的接口，但不用写实现的类
	static interface Foo {
		@Select("SELECT * FROM news")
		void foo(String arg);

		@Update("UPDATE news SET title = 'Test' WHERE id = 1")
		String bar();
	}

	// 代理类
	static class DynamicProxy implements InvocationHandler {
		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			if (method.getAnnotation(Select.class) != null) {
				// 处理查询的方法……
			} else if (method.getAnnotation(Update.class) != null) {
				// 处理更新的方法……
			}

			return "我是返回值";
		}
	}

	/**
	 * 创建代理 
	 * @param clz 这个参数必须是接口类型
	 * @return 代理执行之后的结果
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Foo> T getProxy(Class<T> clz) {
		if (!clz.isInterface())
			throw new IllegalArgumentException("这不是一个接口参数");

		Object obj = Proxy.newProxyInstance(clz.getClassLoader(), new Class[] { clz }, new DynamicProxy());
		return (T) obj;
	}

	public static void main(String[] args) {
		Foo foo = getProxy(Foo.class);
	}
}
