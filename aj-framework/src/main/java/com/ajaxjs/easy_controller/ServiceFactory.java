package com.ajaxjs.easy_controller;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;

/**
 * 接口实例工厂，这里主要是用于提供接口的实例对象
 */
public class ServiceFactory<T> implements FactoryBean<T> {
	/**
	 * 
	 */
	private Class<T> interfaceType;

	/**
	 * 
	 */
	private ApplicationContext applicationContext;

	public ServiceFactory(Class<T> interfaceType, ApplicationContext applicationContext) {
		this.interfaceType = interfaceType;
		this.applicationContext = applicationContext;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T getObject() throws Exception {
		// 这里主要是创建接口对应的实例，便于注入到 spring 容器中
		InvocationHandler handler = new ServiceProxy<>(interfaceType, applicationContext);
		return (T) Proxy.newProxyInstance(interfaceType.getClassLoader(), new Class[] { interfaceType }, handler);
	}

	@Override
	public Class<T> getObjectType() {
		return interfaceType;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
}
