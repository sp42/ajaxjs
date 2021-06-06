/**
 * Copyright Sp42 frank@ajaxjs.com Licensed under the Apache License, Version
 * 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package com.ajaxjs.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.ajaxjs.util.logger.LogHelper;

/**
 * 反射工具包
 * 
 * @author sp42 frank@ajaxjs.com
 */
public class ReflectUtil {
	private static final LogHelper LOGGER = LogHelper.getLog(ReflectUtil.class);

	/**
	 * 根据类创建实例，可传入构造器参数。
	 * 
	 * @param clz  类对象
	 * @param args 获取指定参数类型的构造函数，这里传入我们想调用的构造函数所需的参数。可以不传。
	 * @return 对象实例
	 */
	public static <T> T newInstance(Class<T> clz, Object... args) {
		if (clz.isInterface()) {
			LOGGER.warning("所传递的class类型参数为接口，无法实例化");
			return null;
		}

		if (args == null || args.length == 0) {
			try {
				return clz.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				LOGGER.warning(e);
			}
		}

		// 获取构造器
		Constructor<T> constructor = getConstructor(clz, args2class(args));
		return newInstance(constructor, args);
	}

	/**
	 * 根据构造器创建实例
	 * 
	 * @param constructor 类构造器
	 * @param args        获取指定参数类型的构造函数，这里传入我们想调用的构造函数所需的参数。可以不传。
	 * @return 对象实例
	 */
	public static <T> T newInstance(Constructor<T> constructor, Object... args) {
		try {
			return constructor.newInstance(args); // 实例化
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			LOGGER.warning(e, "实例化对象失败：" + constructor.getDeclaringClass());
			return null;
		}
	}

	/**
	 * 传入的类是否有带参数的构造器
	 * 
	 * @param clz 类对象
	 * @return true 表示为有带参数
	 */
	public static boolean hasArgsCon(Class<?> clz) {
		Constructor<?>[] constructors = clz.getConstructors();
		for (Constructor<?> constructor : constructors) {
			if (constructor.getParameterTypes().length != 0)
				return true;
		}

		return false;
	}
	/**
	 * 根据类全称创建实例，并转换到其接口的类型
	 * 
	 * @param className 实际类的类型
	 * @param clazz     接口类型
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
	 * @param clzName 类全称
	 * @param args    根据构造函数，创建指定类型的对象,传入的参数个数需要与上面传入的参数类型个数一致
	 * @return 对象实例，因为传入的类全称是字符串，无法创建泛型 T，所以统一返回 Object
	 */
	public static Object newInstance(String clzName, Object... args) {
		Class<?> clazz = getClassByName(clzName);
		return clazz != null ? newInstance(clazz, args) : null;
	}

	/**
	 * 获取类的构造器，可以支持重载的构造器（不同参数的构造器）
	 * 
	 * @param clz    类对象
	 * @param argClz 指定构造函数的参数类型，这里传入我们想调用的构造函数所需的参数类型
	 * @return 类的构造器
	 */
	public static <T> Constructor<T> getConstructor(Class<T> clz, Class<?>... argClz) {
		try {
			return argClz != null ? clz.getConstructor(argClz) : clz.getConstructor();
		} catch (NoSuchMethodException e) {
			LOGGER.warning(e, "找不到这个 {0} 类的构造器。", clz.getName());
		} catch (SecurityException e) {
			LOGGER.warning(e);
		}

		return null;
	}

	/**
	 * 根据类名字符串获取类对象
	 * 
	 * @param clzName 类全称。如果是内部类请注意用法
	 * @return 类对象
	 */
	public static Class<?> getClassByName(String clzName) {
		try {
			return Class.forName(clzName);
		} catch (ClassNotFoundException e) {
			LOGGER.warning(e, "找不到这个类：{0}。", clzName);
		}

		return null;
	}

	/**
	 * 根据类名字符串获取类对象，可强类型转换类型
	 * 
	 * @param clzName 类全称
	 * @param clz     要转换的目标类型
	 * @return 类对象
	 */
	@SuppressWarnings("unchecked")
	public static <T> Class<T> getClassByName(String clzName, Class<T> clz) {
		Class<?> c = getClassByName(clzName);
		return c == null ? null : (Class<T>) getClassByName(clzName);
	}

	/**
	 * 把参数转换为类对象列表
	 * 
	 * @param args 可变参数列表
	 * @return 类对象列表
	 */
	public static Class<?>[] args2class(Object[] args) {
		Class<?>[] clazzes = new Class[args.length];

		for (int i = 0; i < args.length; i++)
			clazzes[i] = args[i].getClass();

		return clazzes;
	}

	/**
	 * 已知接口类型，获取它的 class
	 * 
	 * @param type 接口类型
	 * @return 接口的类对象
	 */
	public static Class<?> getClassByInterface(Type type) {
		String className = type.toString();
		className = className.replaceAll("<.*>$", "").replaceAll("(class|interface)\\s", ""); // 不要泛型的字符

		return getClassByName(className);
	}

	/**
	 * 获取某个类的所有接口
	 * 
	 * @param clz 目标类
	 * @return 类的所有接口
	 */
	public static Class<?>[] getDeclaredInterface(Class<?> clz) {
		List<Class<?>> fields = new ArrayList<>();

		for (; clz != Object.class; clz = clz.getSuperclass()) {
			Class<?>[] currentInterfaces = clz.getInterfaces();
			fields.addAll(Arrays.asList(currentInterfaces));
		}

		return fields.toArray(new Class[fields.size()]);
	}

	/////////////// Methods ///////////////////////

	/**
	 * 根据类、方法的字符串和参数列表获取方法对象，支持重载的方法
	 * 
	 * @param obj    可以是实例对象，也可以是类对象
	 * @param method 方法名称
	 * @param args   明确的参数类型列表
	 * @return 匹配的方法对象，null 表示找不到
	 */
	public static Method getMethod(Object obj, String method, Class<?>... args) {
		Class<?> cls = obj instanceof Class ? (Class<?>) obj : obj.getClass();

		try {
			return CommonUtil.isNull(args) ? cls.getMethod(method) : cls.getMethod(method, args);
		} catch (NoSuchMethodException | SecurityException e) {
			String str = "";
			for (Class<?> clz : args)
				str += clz.getName();

			LOGGER.warning("类找不到这个方法 {0}.{1}({2})。", cls.getName(), method, str.equals("") ? "void" : str);
			return null;
		}
	}

	/**
	 * 
	 * 根据方法名称和参数列表查找方法。注意参数对象类型由于没有向上转型会造成不匹配而找不到方法，这时应使用上一个方法或
	 * getMethodByUpCastingSearch()
	 * 
	 * @param obj    实例对象
	 * @param method 方法名称
	 * @param args   对应重载方法的参数列表
	 * @return 匹配的方法对象，null 表示找不到
	 */
	public static Method getMethod(Object obj, String method, Object... args) {
		if (!CommonUtil.isNull(args)) {
			return getMethod(obj, method, args2class(args));
		} else
			return getMethod(obj, method);
	}

	/**
	 * 根据方法名称和参数列表查找方法。自动循环参数类型向上转型。仅支持一个参数。
	 * 
	 * @param clz    实例对象的类对象
	 * @param method 方法名称
	 * @param arg    参数对象，可能是子类或接口，所以要在这里找到对应的方法，当前只支持单个参数；且不能传 Class，必须为对象
	 * @return 匹配的方法对象，null 表示找不到
	 */
	public static Method getMethodByUpCastingSearch(Class<?> clz, String method, Object arg) {
		for (Class<?> clazz = arg.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
			try {
				// return cls.getDeclaredMethod(methodName, clazz);
				return clz.getMethod(method, clazz); // 用 getMethod 代替更好？
			} catch (NoSuchMethodException | SecurityException e) {
				// 这里的异常不能抛出去。 如果这里的异常打印或者往外抛，则就不会执行clazz = clazz.getSuperclass(), 最后就不会进入到父类中了
			}
		}

		return null;
	}

	/**
	 * 循环 object 向上转型（接口）
	 * 
	 * @param clz    主类
	 * @param method 方法名称
	 * @param arg    参数对象，可能是子类或接口，所以要在这里找到对应的方法，当前只支持单个参数
	 * @return 方法对象
	 */
	public static Method getDeclaredMethodByInterface(Class<?> clz, String method, Object arg) {
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
						methodObj = getSuperClassDeclaredMethod(clz, method, getClassByInterface(intf));

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
	 * @param clz    主类
	 * @param method 方法名称
	 * @param argClz 参数类引用
	 * @return 匹配的方法对象，null 表示找不到
	 */
	public static Method getSuperClassDeclaredMethod(Class<?> clz, String method, Class<?> argClz) {
		for (; clz != Object.class; clz = clz.getSuperclass()) {
			try {
				return clz.getDeclaredMethod(method, argClz);
			} catch (NoSuchMethodException | SecurityException e) {
			}
		}

		return null;
	}

	/**
	 * 查找对象父类身上指定的方法（注意该方法不需要校验参数类型是否匹配，故有可能不是目标方法，而造成异常，请谨慎使用）
	 * 
	 * @param clz    主类
	 * @param method 方法名称
	 * @return 匹配的方法对象，null 表示找不到
	 */
	public static Method getSuperClassDeclaredMethod(Class<?> clz, String method) {
		for (; clz != Object.class; clz = clz.getSuperclass()) {
			for (Method m : clz.getDeclaredMethods()) {
				if (m.toString().contains(method)) {
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
	 * @param method   方法对象
	 * @param args     参数列表
	 * @return 执行结果
	 * @throws Throwable
	 */
	public static Object executeMethod_Throwable(Object instance, Method method, Object... args) throws Throwable {
		if (instance == null || method == null)
			return null;

		try {
			return args == null || args.length == 0 ? method.invoke(instance) : method.invoke(instance, args);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
			Throwable e;

			if (e1 instanceof InvocationTargetException) {
				e = ((InvocationTargetException) e1).getTargetException();
				LOGGER.warning("反射执行方法异常！所在类[{0}] 方法：[{1}]", instance.getClass().getName(), method.getName());
				
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
	 * @param method   方法对象
	 * @param args     参数列表
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
	 * @param method   方法对象名称
	 * @param args     参数列表
	 * @return 执行结果
	 */
	public static Object executeMethod(Object instnace, String method, Object... args) {
		// 没有方法对象，先找到方法对象。可以支持方法重载，按照参数列表
		Class<?>[] clazzes = args2class(args);
		Method methodObj = getMethod(instnace.getClass(), method, clazzes);

		return methodObj != null ? executeMethod(instnace, methodObj, args) : null;
	}

	/**
	 * 调用方法。 注意获取方法对象，原始类型和包装类型不能混用，否则得不到正确的方法， 例如 Integer 不能与 int 混用。 这里提供一个
	 * argType 的参数，指明参数类型为何。
	 * 
	 * @param instnace 对象实例
	 * @param method   方法名称
	 * @param argType  参数类型
	 * @param argValue 参数值
	 * @return 执行结果
	 */
	public static Object executeMethod(Object instnace, String method, Class<?> argType, Object argValue) {
		Method m = getMethod(instnace, method, argType);
		if (m != null)
			return executeMethod(instnace, m, argValue);

		return null;
	}

	/**
	 * 执行静态方法
	 * 
	 * @param method 方法对象
	 * @param args   方法参数列表
	 * @return 执行结果
	 */
	public static Object executeStaticMethod(Method method, Object... args) {
		if (isStaticMethod(method)) {
			try {
				return executeMethod_Throwable(new Object(), method, args);
			} catch (Throwable e) {
				LOGGER.warning(e);
			}
		} else {
			LOGGER.warning("这不是一个静态方法：" + method);
		}

		return null;
	}

	/**
	 * 是否静态方法
	 * 
	 * @param method 方法对象
	 * @return true 表示为静态方法
	 */
	public static boolean isStaticMethod(Method method) {
		return Modifier.isStatic(method.getModifiers());
	}

	// -----------------------------------------------------------------------------------------
	// --------------------------------------BeanUtils------------------------------------------
	// -----------------------------------------------------------------------------------------

	/**
	 * 将第一个字母大写
	 * 
	 * @param str 字符串
	 * @return 字符串
	 */
	public static String firstLetterUpper(String str) {
		// return str.substring(0, 1).toUpperCase() + str.substring(1); // 另外一种写法
		return Character.toString(str.charAt(0)).toUpperCase() + str.substring(1);
	}

	/**
	 * 根据方法名称来截取属性名称，例如把 getter 的 getXxx() 转换为 xxx 的字段名
	 * 
	 * @param method 方法名称
	 * @param action set|get
	 * @return 属性名称
	 */
	public static String getFieldName(String method, String action) {
		method = method.replace(action, "");
		return Character.toString(method.charAt(0)).toLowerCase() + method.substring(1);
	}

	/**
	 * 调用 bean 对象的 setter 方法
	 * 
	 * @param bean  Bean 对象
	 * @param name  属性名称，前缀不要带 set
	 * @param value 要设置的属性值
	 */
	public static void setProperty(Object bean, String name, Object value) {
		String setMethodName = "set" + firstLetterUpper(name);

		Objects.requireNonNull(bean, bean + "执行：" + setMethodName + " 未发现类");
//		Objects.requireNonNull(value, bean + "执行：" + setMethodName + " 未发现参数 value");

		Class<?> clazz = bean.getClass();

		// 要把参数父类的也包括进来
		Method method = getMethodByUpCastingSearch(clazz, setMethodName, value);

		// 如果没找到，那就试试接口的……
		if (method == null)
			method = getDeclaredMethodByInterface(clazz, setMethodName, value);

		// 如果没找到，那忽略参数类型，只要匹配方法名称即可。这会发生在：由于被注入的对象有可能经过了 AOP 的动态代理，所以不能通过上述逻辑找到正确的方法
		if (method == null)
			method = getSuperClassDeclaredMethod(clazz, setMethodName);
		
		// 最终还是找不到
		Objects.requireNonNull(method, "找不到目标方法[" + clazz.getSimpleName() + "." + setMethodName + "(" + value.getClass().getSimpleName() +")]");

		executeMethod(bean, method, value);
	}

	/**
	 * 
	 * @param clz
	 * @return
	 */
	public static Map<String, Integer> getConstantsInt(Class<?> clz) {
		Map<String, Integer> map = new HashMap<>();

		Field[] fields = clz.getDeclaredFields();
		Object instance = newInstance(clz);

		for (Field field : fields) {
			String descriptor = Modifier.toString(field.getModifiers());// 获得其属性的修饰
			if (descriptor.equals("public static final")) {
				try {
					map.put(field.getName(), (int) field.get(instance));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					LOGGER.warning(e);
				}
			}
		}

		return map;
	}
}
