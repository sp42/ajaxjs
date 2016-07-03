/**
 * Copyright 2015 Frank Cheung
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
package com.ajaxjs.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 反射工具类
 * 
 * @author frank
 */
public class Reflect {
	private static final LogHelper LOGGER = LogHelper.getLog(Reflect.class);

	/**
	 * 根据类创建实例
	 * 
	 * @param clazz
	 *            类对象
	 * @return 对象实例
	 */
	public static <T> T newInstance(Class<T> clazz) {
		try {
			return clazz.newInstance(); // 实例化 bean
		} catch (InstantiationException | IllegalAccessException e) {
			LOGGER.warning(e);
			return null;
		}
	}

	/**
	 * 根据构造器创建实例
	 * 
	 * @param constructor
	 *            类构造器
	 * @param args
	 *            获取指定参数类型的构造函数，这里传入我们想调用的构造函数所需的参数
	 * @return 对象实例
	 */
	public static <T> T newInstance(Constructor<T> constructor, Object... args) {
		try {
			return constructor.newInstance(args);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			LOGGER.warning("实例化对象失败：" + constructor.getDeclaringClass(), e);
			return null;
		}

	}

	/**
	 * 获取类的构造器，可以支持重载的构造器（不同参数的构造器）
	 * 
	 * @param clazz
	 *            类对象
	 * @param classes
	 *            获取指定参数类型的构造函数，这里传入我们想调用的构造函数所需的参数类型
	 * @return 类的构造器
	 */
	public static <T> Constructor<T> getConstructor(Class<T> clazz, Class<?>... classes) {
		try {
			return classes != null ? clazz.getConstructor(classes) : clazz.getConstructor();
		} catch (NoSuchMethodException e) {
			LOGGER.warning("找不到这个 {0} 类的构造器。", clazz.getName());
			return null;
		} catch (SecurityException e) {
			LOGGER.warning(e);
			return null;
		}
	}

	/**
	 * 根据类对象创建实例
	 * 
	 * @param clazz
	 *            类对象
	 * @param args
	 *            获取指定参数类型的构造函数，这里传入我们想调用的构造函数所需的参数
	 * @return 对象实例
	 */
	public static <T> T newInstance(Class<T> clazz, Object... args) {
		T obj = null;
		Constructor<T> constructor = getConstructor(clazz, args2class(args)); // 获取构造器

		if (constructor != null)
			obj = newInstance(constructor, args);

		if (obj == null)
			LOGGER.warning("newInstanceByClassName 对象 {0} 失败：", clazz.getName());
		return obj;
	}

	/**
	 * 根据类名字符串获取类对象
	 * 
	 * @param className
	 *            类全称
	 * @return 类对象
	 */
	public static Class<?> getClassByName(String className) {
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			LOGGER.warning("找不到这个类：{0}。", className);
			return null;
		}
	}

	/**
	 * 根据类全称创建实例
	 * 
	 * @param className
	 *            类全称
	 * @param args
	 *            根据构造函数，创建指定类型的对象,传入的参数个数需要与上面传入的参数类型个数一致
	 * @return 对象实例，因为传入的类全称是字符串，无法创建泛型 T，所以统一返回 Object
	 */
	public static Object newInstance(String className, Object... args) {
		Class<?> clazz = getClassByName(className);
		return clazz != null ? newInstance(clazz, args) : null;
	}

	// -------------------------------------- 方法
	// ----------------------------------

	/**
	 * 根据类和参数列表获取方法对象，支持重载的方法
	 * 获取的是类的所有共有方法，这就包括自身的所有public方法，和从基类继承的、从接口实现的所有public方法
	 * 
	 * @param clazz
	 *            类对象
	 * @param method
	 *            方法名称
	 * @param args
	 *            对应重载方法的参数列表
	 * @return 匹配的方法对象，null 表示找不到
	 */
	public static Method getMethod(Class<?> clazz, String method, Class<?>... args) {
		try {
			return clazz.getMethod(method, args);
		} catch (NoSuchMethodException e) {
			LOGGER.warning("类找不到这个方法 {0}.{1}。", clazz.getName(), method);
		} catch (SecurityException e) {
			LOGGER.warning(e);
		}

		return null;
	}

	/**
	 * 调用方法
	 * 
	 * @param instance
	 *            对象实例，bean
	 * @param method
	 *            方法对象
	 * @param args
	 *            参数列表
	 * @return 执行结果
	 */
	public static Object executeMethod(Object instance, Method method, Object... args) {
		try {
			return method.invoke(instance, args);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			System.err.println("method.getName():" + method.getName());
			LOGGER.warning(e);
			return null;
		}
	}
	/**
	 * 调用方法，方法没有参数的
	 * 
	 * @param instance
	 *            对象实例，bean
	 * @param method
	 *            方法对象

	 * @return 执行结果
	 */
	public static Object executeMethod(Object instance, Method method) {
		try {
			return method.invoke(instance);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			System.err.println(method.getName());
			LOGGER.warning(e);
			return null;
		}
	}
	
	/**
	 * 调用方法。 注意获取方法对象，原始类型和包装类型不能混用，否则得不到正确的方法，例如 Integer 不能与 int 混用。 这里提供一个
	 * argType 的参数，指明参数类型为何。
	 * 
	 * @param instnace
	 *            对象实例，bean
	 * @param methodName
	 *            方法名称
	 * @param argType
	 *            参数类型
	 * @param value
	 *            参数值
	 */
	public static void executeMethod(Object instnace, String methodName, Class<?> argType, Object value) {
		Method m = Reflect.getMethod(instnace.getClass(), methodName, argType);
		Reflect.executeMethod(instnace, m, value);
	}

	/**
	 * 调用方法
	 * 
	 * @param instnace
	 *            对象实例，bean
	 * @param method
	 *            方法对象名称
	 * @param args
	 *            参数列表
	 * @return 执行结果
	 */
	public static Object executeMethod(Object instnace, String method, Object... args) {
		// 没有方法对象，先找到方法对象。可以支持方法重载，按照参数列表
		Class<?>[] clazzes = args2class(args);
		Method methodObj = getMethod(instnace.getClass(), method, clazzes);

		return methodObj != null ? executeMethod(instnace, methodObj, args) : null;
	}

	/**
	 * 把参数转换为类对象列表
	 * 
	 * @param args
	 *            可变参数列表
	 * @return 类对象列表
	 */
	private static Class<?>[] args2class(Object[] args) {
		// 把参数转换为类对象列表
		Class<?>[] clazzes = new Class[args.length];
		for (int i = 0; i < args.length; i++)
			clazzes[i] = args[i].getClass();

		return clazzes;
	}

	/**
	 * 用 getMethod 代替更好？ 循环 object 向上转型, 获取 hostClazz 对象的 DeclaredMethod
	 * getDeclaredMethod()获取的是类自身声明的所有方法，包含public、protected和private方法。
	 * 
	 * @param hostClazz
	 *            主类
	 * @param method
	 *            方法名称
	 * @param arg
	 *            参数对象，可能是子类或接口，所以要在这里找到对应的方法，当前只支持单个参数
	 * @return 匹配的方法对象，null 表示找不到
	 */
	public static Method getDeclaredMethod(Class<?> hostClazz, String method, Object arg) {
		for (Class<?> clazz = arg.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
			try {
				return hostClazz.getDeclaredMethod(method, clazz);
			} catch (Exception e) {
				/*
				 * 这里的异常不能抛出去。 如果这里的异常打印或者往外抛，则就不会执行clazz =
				 * clazz.getSuperclass(),最后就不会进入到父类中了
				 */
			}
		}

		return null;
	}

	/**
	 * 获取某个类的所有字段
	 * 
	 * @param clazz
	 *            目标类
	 * @return 类的所有字段
	 */
	public static List<Field> getDeclaredField(Class<?> clazz) {
		List<Field> fields = new ArrayList<>();

		for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
			Field[] currentFields = clazz.getDeclaredFields();
			fields.addAll(Arrays.asList(currentFields));
		}

		return fields;
	}
	
	/**
	 * 获取某个类的所有接口
	 * 
	 * @param clazz
	 *            目标类
	 * @return 类的所有接口
	 */
	public static Class<?>[] getDeclaredInterface(Class<?> clazz) {
		List<Class<?>> fields = new ArrayList<>();
		
		for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
			Class<?>[] currentInterfaces = clazz.getInterfaces();
			fields.addAll(Arrays.asList(currentInterfaces));
		}
		Class<?>[] clz = new Class[fields.size()];
		
		return fields.toArray(clz);
	}

	/**
	 * 循环 object 向上转型（接口）, 获取 hostClazz 对象的 DeclaredMethod
	 * 
	 * @param hostClazz
	 * @param method
	 *            方法名称
	 * @param arg
	 *            参数对象，可能是子类或接口，所以要在这里找到对应的方法，当前只支持单个参数
	 * @return 方法对象
	 */
	public static Method getDeclaredMethodByInterface(Class<?> hostClazz, String method, Object arg) {
		Method methodObj = null;

		for (Class<?> clazz = arg.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
			Type[] intfs = clazz.getGenericInterfaces();

			if (intfs.length != 0) { // 有接口！
				try {
					for (Type intf : intfs) {
						methodObj = hostClazz.getDeclaredMethod(method, (Class<?>) intf);
						if (methodObj != null)
							return methodObj;
					}
				} catch (Exception e) {
				}
			} else {
				// 无实现的接口
			}
		}

		return null;
	}

	// ------------------------------- AOP------------------------------

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

	/**
	 * BEAN SETXXX
	 * 
	 * @param bean
	 *            JAVA Bean 对象，也可以是
	 * @param name
	 *            属性名称
	 * @param value
	 *            要设置的属性值
	 */
	public static void setProperty(Object bean, String name, Object value) {
		if (bean == null)
			throw new NullPointerException("未发现类");
		String setMethodName = "set" + firstLetterUpper(name);

		Class<?> clazz = bean.getClass();
		if (clazz == null)
			throw new NullPointerException("执行：" + setMethodName + "未发现类");
		if (value == null)
			throw new NullPointerException("执行：" + setMethodName + "未发现参数 value");

		// 要把父类的也包括进来
		Method method = getDeclaredMethod(clazz, setMethodName, value);

		// 如果没找到，那就试试接口的……
		if (method == null)
			method = getDeclaredMethodByInterface(clazz, setMethodName, value);
		if (method == null) {
			throw new NullPointerException(clazz.getName() + "找不到目标方法！" + setMethodName);
		}

		executeMethod(bean, method, value);
	}
	
	/**
	 * 将第一个字母大写
	 * 
	 * @param str
	 *            字符串
	 * @return 字符串
	 */
	public static String firstLetterUpper(String str) {
//		return str.substring(0, 1).toUpperCase() + str.substring(1); // 另外一种写法
		return Character.toString(str.charAt(0)).toUpperCase() + str.substring(1);
	}
}
