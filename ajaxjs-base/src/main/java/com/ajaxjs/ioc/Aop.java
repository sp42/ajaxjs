/**
 * Copyright Sp42 frank@ajaxjs.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ajaxjs.ioc;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Objects;

import com.ajaxjs.ioc.annotation.ReturnAsArg;
import com.ajaxjs.ioc.annotation.ReturnBefore;

/**
 * AOP 抽象类
 * 
 * @author sp42 frank@ajaxjs.com
 * @param <T> T 必须是目标对象的其中一个接口类型
 */
public abstract class Aop<T> implements InvocationHandler {
	/**
	 * 原目标对象，未被 AOP 包装过的
	 */
	private T target;

	/**
	 * 前置调用
	 * 
	 * @param target     本体对象
	 * @param method     方法对象
	 * @param methodName 方法名称
	 * @param args       参数列表
	 * @return 返回任意对象
	 */
	public abstract Object before(T target, Method method, String methodName, Object[] args) throws Throwable;

	/**
	 * 后置调用
	 * 
	 * @param target    本体对象
	 * 
	 * @param method    方法对象
	 * @param args      参数列表
	 * @param returnObj 返回结果
	 * @return 返回任意对象
	 */
	public abstract void after(T target, Method method, String methodName, Object[] args, Object returnObj);

	/**
	 * 对目标对象进行 AOP 处理，AOP所在的模块，就是本类！
	 * 
	 * @param target 目标对象，被 AOP 接管的对象
	 * @return AOP 代理对象，已经不是原来目标对象了，但是同一接口的
	 */
	@SuppressWarnings("unchecked")
	public T bind(T target) {
		Objects.toString(target, "proxy 对象不能为空！");

		this.target = target;

		Object obj = Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), this);
		return (T) obj;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Object beforeReturn = before(target, method, method == null ? null : method.getName(), args);

		if (beforeReturn instanceof ReturnBefore)
			return ((ReturnBefore) beforeReturn).getReturnValue(); // 中止运行 aop

		// 如果作为参数
		boolean isBeforeReturn = beforeReturn != null && beforeReturn instanceof ReturnAsArg;

		if (isBeforeReturn)
			args = ((ReturnAsArg) beforeReturn).getArgs();

		Object returnObj = method.invoke(target, args);
		String methodName = method == null ? null : method.getName();

		after(target, method, methodName, args, returnObj);

		return returnObj;
	}

	/**
	 * 绑定多个代理
	 * 
	 * @param target        目标对象
	 * @param proxyHandlers 代理处理器
	 * @return AOP 链包装过的目标对象
	 */
	@SafeVarargs
	public static <K> K chain(K target, Aop<K>... proxyHandlers) { // 链式
		for (Aop<K> proxyHandler : proxyHandlers)
			target = proxyHandler.bind(target);

		return target;
	}

	public T getProxied() {
		return target;
	}

	public void setProxied(T proxied) {
		this.target = proxied;
	}
}