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

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.ajaxjs.util.LogHelper;

/**
 * 反射工具类（方法）
 * 
 * @author frank
 */
public class Reflect {
	private static final LogHelper LOGGER = LogHelper.getLog(Reflect.class);
	
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
			return args.length == 0 ? method.invoke(instance) : method.invoke(instance, args);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// 这里的异常对象不要传到日志中，而是真正的异常
			LOGGER.warning(e.getCause(), "反射执行方法异常！所在类：{0} 方法：{1}", instance.getClass().getName(), method.getName());
			
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

	 * @return 执行结果s
	 */
	public static Object executeMethod(Object instance, Method method) {
		return executeMethod(instance, method, new Object[]{});
	}
	
	/**
	 * 调用方法。
	 * 注意获取方法对象，原始类型和包装类型不能混用，否则得不到正确的方法，
	 * 例如 Integer 不能与 int 混用。
	 * 这里提供一个 argType 的参数，指明参数类型为何。
	 * 
	 * @param instnace
	 *            对象实例
	 * @param methodName
	 *            方法名称
	 * @param argType
	 *            参数类型
	 * @param argValue
	 *            参数值
	 */
	public static void executeMethod(Object instnace, String methodName, Class<?> argType, Object argValue) {
		Method m = getMethod(instnace.getClass(), methodName, argType);
		executeMethod(instnace, m, argValue);
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
		Class<?>[] clazzes = ReflectNewInstance.args2class(args);
		Method methodObj = getMethod(instnace.getClass(), method, clazzes);

		return methodObj != null ? executeMethod(instnace, methodObj, args) : null;
	}
	
	/**
	 * 根据类、方法的字符串和参数列表获取方法对象，支持重载的方法
	 * 获取的是类的所有共有方法，这就包括自身的所有 public 方法，和从基类继承的、从接口实现的所有 public 方法
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
			String str = "";
			for(Class<?> clz : args) 
				str += clz.getName();
			
			LOGGER.warning("类找不到这个方法 {0}.{1}({2})。", clazz.getName(), method, str.equals("") ? "void" : str);
		} catch (SecurityException e) {
			LOGGER.warning(e);
		}

		return null;
	}

	/**
	 * 用 getMethod 代替更好？ 循环 object 向上转型, 获取 hostClazz 对象的 DeclaredMethod
	 * getDeclaredMethod() 获取的是类自身声明的所有方法，包含 public、protected 和 private 方法。
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
			} catch (NoSuchMethodException | SecurityException e) {
				/*
				 * 这里的异常不能抛出去。 如果这里的异常打印或者往外抛，则就不会执行clazz = clazz.getSuperclass(),最后就不会进入到父类中了
				 */
			}
		}

		return null;
	}
	
	public static Method getSuperClassDeclaredMethod(Class<?> hostClazz, String method, Class<?> argClazz) {
		for (; hostClazz != Object.class; hostClazz = hostClazz.getSuperclass()) {
			try {
				return hostClazz.getDeclaredMethod(method, argClazz);
			} catch (NoSuchMethodException | SecurityException e) {
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
	
	private static final String TYPE_NAME_PREFIX = "class ";

	/**
	 * 已知接口类型，获取它的 class
	 * 
	 * @param type
	 *            接口类型
	 * @return 接口的类对象
	 */
	public static Class<?> getClassByInterface(Type type) {
	    String className = type.toString();
	    
	    if (className.startsWith(TYPE_NAME_PREFIX)) 
	        className = className.substring(TYPE_NAME_PREFIX.length());
	    
	    className = className.replaceAll("<.*>$", ""); // 不要泛型的字符

	    return ReflectNewInstance.getClassByName(className);
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
						// 旧方法，现在不行，不知道之前怎么可以的 methodObj = hostClazz.getDeclaredMethod(method, (Class<?>)intf);
						methodObj = getSuperClassDeclaredMethod(hostClazz, method, getClassByInterface(intf));
						
						if (methodObj != null)
							return methodObj;
					}
				} catch (Exception e) {
					LOGGER.warning(e);
				}
			} else {
				// 无实现的接口
			}
		}
		
		return null;
	}
}
