/**
 * Copyright 2015 Sp42 frank@ajaxjs.com
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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.UndeclaredThrowableException;

import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.util.logger.LogHelper;

/**
 * 获取方法 getMethod： 获取的是类的所有共有方法，这就包括自身的所有 public 方法，和从基类继承的、从接口实现的所有 public 方法
 * 另外有如何执行的方法
 * 
 * @author Sp42 frank@ajaxjs.com
 *
 */
public class GetMethod {
	private static final LogHelper LOGGER = LogHelper.getLog(GetMethod.class);

	/**
	 * 根据类、方法的字符串和参数列表获取方法对象，支持重载的方法
	 * 
	 * @param obj 可以是实例对象，也可以是类对象
	 * @param methodName 方法名称
	 * @param args 明确的参数类型列表
	 * @return 匹配的方法对象，null 表示找不到
	 */
	public static Method getMethod(Object obj, String methodName, Class<?>... args) {
		Class<?> cls = obj instanceof Class ? (Class<?>) obj : obj.getClass();

		try {
			return CommonUtil.isNull(args) ? cls.getMethod(methodName) : cls.getMethod(methodName, args);
		} catch (NoSuchMethodException | SecurityException e) {
			String str = "";
			for (Class<?> clz : args)
				str += clz.getName();

			LOGGER.warning("类找不到这个方法 {0}.{1}({2})。", cls.getName(), methodName, str.equals("") ? "void" : str);
			return null;
		}
	}

	/**
	 * 
	 * 根据方法名称和参数列表查找方法。注意参数对象类型由于没有向上转型会造成不匹配而找不到方法，这时应使用上一个方法或
	 * getMethodByUpCastingSearch()
	 * 
	 * @param obj 实例对象
	 * @param methodName 方法名称
	 * @param args 对应重载方法的参数列表
	 * @return 匹配的方法对象，null 表示找不到
	 */
	public static Method getMethod(Object obj, String methodName, Object... args) {
		if (!CommonUtil.isNull(args)) {
			return getMethod(obj, methodName, NewInstance.args2class(args));
		} else
			return getMethod(obj, methodName);
	}

	/**
	 * 根据方法名称和参数列表查找方法。自动循环参数类型向上转型。仅支持一个参数。
	 * 
	 * @param cls 实例对象的类对象
	 * @param methodName 方法名称
	 * @param arg 参数对象，可能是子类或接口，所以要在这里找到对应的方法，当前只支持单个参数；且不能传 Class，必须为对象
	 * @return 匹配的方法对象，null 表示找不到
	 */
	public static Method getMethodByUpCastingSearch(Class<?> cls, String methodName, Object arg) {
		for (Class<?> clazz = arg.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
			try {
//				return cls.getDeclaredMethod(methodName, clazz);
				return cls.getMethod(methodName, clazz); // 用 getMethod 代替更好？
			} catch (NoSuchMethodException | SecurityException e) {
				// 这里的异常不能抛出去。 如果这里的异常打印或者往外抛，则就不会执行clazz = clazz.getSuperclass(), 最后就不会进入到父类中了
			}
		}

		return null;
	}

	/**
	 * 循环 object 向上转型（接口）
	 * 
	 * @param cls 主类
	 * @param methodName 方法名称
	 * @param arg 参数对象，可能是子类或接口，所以要在这里找到对应的方法，当前只支持单个参数
	 * @return 方法对象
	 */
	public static Method getDeclaredMethodByInterface(Class<?> cls, String methodName, Object arg) {
		Method methodObj = null;

		for (Class<?> clazz = arg.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
			Type[] intfs = clazz.getGenericInterfaces();

			if (intfs.length != 0) { // 有接口！
				try {
					for (Type intf : intfs) {
						// 旧方法，现在不行，不知道之前怎么可以的 methodObj = hostClazz.getDeclaredMethod(method,
						// (Class<?>)intf);
						// methodObj = cls.getMethod(methodName,
						// ReflectNewInstance.getClassByInterface(intf));
						methodObj = getSuperClassDeclaredMethod(cls, methodName, NewInstance.getClassByInterface(intf));

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

	/**
	 * 查找对象父类身上指定的方法
	 * 
	 * @param cls 主类
	 * @param methodName 方法名称
	 * @param argClazz 参数类引用
	 * @return 匹配的方法对象，null 表示找不到
	 */
	public static Method getSuperClassDeclaredMethod(Class<?> cls, String methodName, Class<?> argClazz) {
		for (; cls != Object.class; cls = cls.getSuperclass()) {
			try {
				return cls.getDeclaredMethod(methodName, argClazz);
			} catch (NoSuchMethodException | SecurityException e) {
			}
		}

		return null;
	}

	/**
	 * 查找对象父类身上指定的方法（注意该方法不需要校验参数类型是否匹配，故有可能不是目标方法，而造成异常，请谨慎使用）
	 * 
	 * @param cls 主类
	 * @param methodName 方法名称
	 * @return 匹配的方法对象，null 表示找不到
	 */
	public static Method getSuperClassDeclaredMethod(Class<?> cls, String methodName) {
		for (; cls != Object.class; cls = cls.getSuperclass()) {
			for (Method m : cls.getDeclaredMethods()) {
				if (m.toString().contains(methodName)) {
					return m;
				}
			}
		}

		return null;
	}

	/**
	 * 调用方法
	 * 
	 * @param instance 对象实例，bean
	 * @param method 方法对象
	 * @param args 参数列表
	 * @return 执行结果
	 * @throws Throwable
	 */
	public static Object executeMethod_Throwable(Object instance, Method method, Object... args) throws Throwable {
		try {
			return args == null || args.length == 0 ? method.invoke(instance) : method.invoke(instance, args);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
			Throwable e;

			if (e1 instanceof InvocationTargetException) {
				e = ((InvocationTargetException) e1).getTargetException();
				System.err.println("反射执行方法异常！所在类：" + instance.getClass().getName() + "方法：" + method.getName());
				throw e;
			}

			throw e1;
		}
	}

	/**
	 * 获取实际抛出的那个异常对象。 InvocationTargetException 太过于宽泛，在 trouble
	 * shouting的时候，不能给人非常直观的信息 AOP 的缘故，不能直接捕获原来的异常，要不断 e.getCause()....
	 * 
	 * @param e 异常对象
	 * @return 实际异常对象
	 */
	public static Throwable getUnderLayerErr(Throwable e) {
		while (e.getClass().equals(InvocationTargetException.class) || e.getClass().equals(UndeclaredThrowableException.class)) {
			e = e.getCause();
		}

		return e;
	}

	/**
	 * 获取实际抛出的那个异常对象，并去掉前面的包名。
	 * 
	 * @param e 异常对象
	 * @return 实际异常对象信息
	 */
	public static String getUnderLayerErrMsg(Throwable e) {
		String msg = getUnderLayerErr(e).toString();

		return msg.replaceAll("^[^:]*:\\s?", "");
	}

	/**
	 * 调用方法，该方法不会抛出异常
	 * 
	 * @param instance 对象实例，bean
	 * @param method 方法对象
	 * @param args 参数列表
	 * @return 执行结果
	 */
	public static Object executeMethod(Object instance, Method method, Object... args) {
		try {
			return executeMethod_Throwable(instance, method, args);
		} catch (Throwable e) {
			return null;
		}
	}

	/**
	 * 调用方法
	 * 
	 * @param instnace 对象实例，bean
	 * @param method 方法对象名称
	 * @param args 参数列表
	 * @return 执行结果
	 */
	public static Object executeMethod(Object instnace, String method, Object... args) {
		// 没有方法对象，先找到方法对象。可以支持方法重载，按照参数列表
		Class<?>[] clazzes = NewInstance.args2class(args);
		Method methodObj = getMethod(instnace.getClass(), method, clazzes);

		return methodObj != null ? executeMethod(instnace, methodObj, args) : null;
	}

	/**
	 * 调用方法。 注意获取方法对象，原始类型和包装类型不能混用，否则得不到正确的方法， 例如 Integer 不能与 int 混用。 这里提供一个
	 * argType 的参数，指明参数类型为何。
	 * 
	 * @param instnace 对象实例
	 * @param methodName 方法名称
	 * @param argType 参数类型
	 * @param argValue 参数值
	 * @return 执行结果
	 */
	public static Object executeMethod(Object instnace, String methodName, Class<?> argType, Object argValue) {
		Method m = getMethod(instnace, methodName, argType);
		if (m != null)
			return executeMethod(instnace, m, argValue);

		return null;
	}
}
