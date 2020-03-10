package com.ajaxjs.ioc.aop;

import java.lang.reflect.Method;

public interface AopHandler<T> {
	/**
	 * 前置调用
	 * 
	 * @param target     本体对象
	 * @param method     方法对象
	 * @param methodName 方法名称
	 * @param args       参数列表
	 * @return 返回任意对象
	 */
	public Object before(T target, Method method, String methodName, Object[] args) throws Throwable;

	/**
	 * 后置调用
	 * 
	 * @param target    本体对象
	 * 
	 * @param method    方法对象
	 * @param args      参数列表
	 * @param returnObj 返回结果
	 */
	public void after(T target, Method method, String methodName, Object[] args, Object returnObj);
}
