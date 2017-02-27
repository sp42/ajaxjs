package com.ajaxjs.util.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import com.ajaxjs.util.reflect.Reflect;

public abstract class Aop<T> implements InvocationHandler {
	private T proxied;

	public T getProxied() {
		return proxied;
	}

	public void setProxied(T proxied) {
		this.proxied = proxied;
	}

	/**
	 * 
	 * @param method
	 * @param args
	 */
	protected abstract Object before(Method method, Object[] args);

	protected abstract void after(Method method, Object[] args, Object returnObj);

	@SuppressWarnings("unchecked")
	public T bind(T proxied) {
		if(proxied == null) throw new NullPointerException("proxy 对象不能为空！");
		
		this.proxied = proxied;
		
		Object obj = Proxy.newProxyInstance(proxied.getClass().getClassLoader(), proxied.getClass().getInterfaces(), this);
		return (T)obj ;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		Object beforeReturn = before(method, args);
		
		if (beforeReturn instanceof StopAop) {
			System.out.println("中止运行 aop");
			return null; // 中止运行 aop
		}
		
		if (beforeReturn instanceof ReturnBefore) {
			System.out.println("beforeReturn");
			return ((ReturnBefore)beforeReturn).getReturnValue(); // 中止运行 aop
		}

		Object returnObj = method.invoke(proxied, args);
		after(method, args, returnObj);

		return returnObj;
	}
	
	@SafeVarargs
	public static <K> K chain(K obj, Aop<K>... proxyHandlers) {
		for(Aop<K> proxyHandler : proxyHandlers) {
			obj = proxyHandler.bind(obj);
		}
		return obj;
	}
	
	// ------------------------------- 简单版 ------------------------------

	/**
	 * 通过动态代理实现 AOP
	 * 
	 * @param obj
	 *            对象
	 * @param _interface
	 *            必须是接口类
	 * @param cb
	 *            回调
	 * @return 指定类型的实例
	 */
	@SuppressWarnings("unchecked")
	public static <T> T proxy(final Object obj, Class<T> _interface, final ProxyCallback cb) {
		Object _obj = Proxy.newProxyInstance(
			Reflect.class.getClassLoader(), 
			new Class[] { _interface },
			new InvocationHandler() {
				public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
					Object __obj = null;
	
					String methodName = method.getName();
	
					if (cb.before(obj, methodName, args) != false) { // 前置调用，可拦截
						__obj = method.invoke(obj, args);
						__obj = cb.after(obj, methodName, __obj, args); // 后置调用
					}
					// if ("add".equals(method.getName())) {
					//// throw new UnsupportedOperationException();
					// }
	
					return __obj;
				}
			}
		);

		return (T) _obj;
	}

	/**
	 * AOP 回调
	 * 
	 * @author frank
	 *
	 */
	public static interface ProxyCallback {
		/**
		 * 前置调用，可拦截
		 * 
		 * @param instance
		 *            实例
		 * @param methodName
		 *            方法名称
		 * @param objects
		 *            对象列表
		 * @return 返回 false 不继续
		 */
		boolean before(Object instance, String methodName, Object... objects);

		/**
		 * 后置调用
		 * 
		 * @param instance
		 *            实例
		 * @param methodName
		 *            方法名称
		 * @param returnValue
		 *            返回值
		 * @param objects
		 *            对象列表
		 * @return 任意对象
		 */
		Object after(Object instance, String methodName, Object returnValue, Object... objects);
	}
}