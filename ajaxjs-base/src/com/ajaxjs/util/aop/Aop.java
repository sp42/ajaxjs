/**
 * Copyright Frank Cheung frank@ajaxjs.com
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
package com.ajaxjs.util.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import com.ajaxjs.util.reflect.Reflect;

/**
 * AOP 抽象类
 * 
 * @author Frank Cheung frank@ajaxjs.com
 * @param <T>
 */
public abstract class Aop<T> implements InvocationHandler {
	/**
	 * 
	 */
	private T proxied;

	/**
	 * 前置调用
	 * @param method
	 * @param args
	 */
	protected abstract Object before(Method method, Object[] args) throws Throwable;

	/**
	 * 后置调用
	 * @param method
	 * @param args
	 * @param returnObj
	 */
	protected abstract void after(Method method, Object[] args, Object returnObj);

	/**
	 * 
	 * @param proxied
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public T bind(T proxied) {
		if (proxied == null)
			throw new NullPointerException("proxy 对象不能为空！");

		this.proxied = proxied;

		Object obj = Proxy.newProxyInstance(proxied.getClass().getClassLoader(), proxied.getClass().getInterfaces(), this);
		return (T) obj;
	}

	/**
	 * 
	 */
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

		Object beforeReturn = before(method, args);

		if (beforeReturn instanceof ReturnBefore) {
			System.out.println("beforeReturn");
			return ((ReturnBefore) beforeReturn).getReturnValue(); // 中止运行 aop
		}

		Object returnObj = method.invoke(proxied, args);
		after(method, args, returnObj);

		return returnObj;
	}

	@SafeVarargs
	public static <K> K chain(K obj, Aop<K>... proxyHandlers) {
		for (Aop<K> proxyHandler : proxyHandlers) {
			obj = proxyHandler.bind(obj);
		}
		return obj;
	}
	
	public T getProxied() {
		return proxied;
	}

	public void setProxied(T proxied) {
		this.proxied = proxied;
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
		Object _obj = Proxy.newProxyInstance(Reflect.class.getClassLoader(), new Class[] { _interface },
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
				});

		return (T) _obj;
	}
	
	/**
	 * AOP 回调
	 * 
	 * @author Frank Cheung frank@ajaxjs.com
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