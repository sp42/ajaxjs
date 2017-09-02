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
package com.ajaxjs.util.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.ajaxjs.util.logger.LogHelper;

/**
 * 创建新实例的工具方法
 * 
 * @author Frank Cheung frank@ajaxjs.com
 */
public class ReflectNewInstance {
	private static final LogHelper LOGGER = LogHelper.getLog(ReflectNewInstance.class);

	/**
	 * 根据类创建实例，可传入构造器参数。
	 * 
	 * @param clazz
	 *            类对象
	 * @param args
	 *            获取指定参数类型的构造函数，这里传入我们想调用的构造函数所需的参数。可以不传。
	 * @return 对象实例
	 */
	public static <T> T newInstance(Class<T> clazz, Object... args) {
		if (args.length == 0) {
			try {
				return clazz.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				LOGGER.warning(e);
			}
		}

		Constructor<T> constructor = getConstructor(clazz, args2class(args)); // 获取构造器
		return newInstance(constructor, args);
	}

	/**
	 * 根据构造器创建实例
	 * 
	 * @param constructor
	 *            类构造器
	 * @param args
	 *            获取指定参数类型的构造函数，这里传入我们想调用的构造函数所需的参数。可以不传。
	 * @return 对象实例
	 */
	public static <T> T newInstance(Constructor<T> constructor, Object... args) {
		try {

			return constructor.newInstance(args); // 实例化
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			LOGGER.warning(e, "实例化对象失败：" + constructor.getDeclaringClass());
			return null;
		}
	}

	/**
	 * 传入的类是否有带参数的构造器
	 * 
	 * @param clazz 类对象
	 * @return
	 */
	public static boolean hasArgsCon(Class<?> clazz) {
		Constructor<?>[] constructors = clazz.getConstructors();
		for (Constructor<?> constructor : constructors) {
			if (constructor.getParameterTypes().length != 0)
				return true;
		}

		return false;
	}
	/**
	 * 根据类全称创建实例，并转换到其接口的类型
	 * 
	 * @param className
	 *            实际类的类型
	 * @param clazz
	 *            接口类型
	 * @return 对象实例
	 */
	// @SuppressWarnings("unchecked")
	// public static <T> T newInstance(String className, Class<T> clazz) {
	// Class<?> clz = getClassByName(className);
	// return clazz != null ? (T) newInstance(clz) : null;
	// }

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

	/**
	 * 获取类的构造器，可以支持重载的构造器（不同参数的构造器）
	 * 
	 * @param clazz
	 *            类对象
	 * @param argClasses
	 *            获取指定参数类型的构造函数，这里传入我们想调用的构造函数所需的参数类型
	 * @return 类的构造器
	 */
	public static <T> Constructor<T> getConstructor(Class<T> clazz, Class<?>... argClasses) {
		try {
			return argClasses != null ? clazz.getConstructor(argClasses) : clazz.getConstructor();
		} catch (NoSuchMethodException e) {
			LOGGER.warning(e, "找不到这个 {0} 类的构造器。", clazz.getName());
		} catch (SecurityException e) {
			LOGGER.warning(e);
		}

		return null;
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
			LOGGER.warning(e, "找不到这个类：{0}。", className);
		}

		return null;
	}

	/**
	 * 把参数转换为类对象列表
	 * 
	 * @param args
	 *            可变参数列表
	 * @return 类对象列表
	 */
	public static Class<?>[] args2class(Object[] args) {
		Class<?>[] clazzes = new Class[args.length];

		for (int i = 0; i < args.length; i++)
			clazzes[i] = args[i].getClass();

		return clazzes;
	}
}
