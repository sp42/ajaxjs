package com.ajaxjs.util.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

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
}