package com.ajaxjs.spring.easy_controller;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import org.springframework.beans.factory.FactoryBean;

/**
 * 接口实例工厂，这里主要是用于提供接口的实例对象
 * 
 * @author Frank Cheung<sp42@qq.com>
 */
public class ControllerFactory implements FactoryBean<Object> {
	/**
	 * 控制器接口类
	 */
	private Class<?> interfaceType;

	/**
	 * 创建一个 ControllerFactory
	 * 
	 * @param interfaceType 控制器接口类
	 */
	public ControllerFactory(Class<?> interfaceType) {
		this.interfaceType = interfaceType;
	}

	@Override
	public Object getObject() throws Exception {
		// 这里主要是创建接口对应的实例，便于注入到 spring 容器中
		InvocationHandler handler = new ControllerProxy(interfaceType);
		return Proxy.newProxyInstance(interfaceType.getClassLoader(), new Class[] { interfaceType }, handler);
	}

	@Override
	public Class<?> getObjectType() {
		return interfaceType;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
}
