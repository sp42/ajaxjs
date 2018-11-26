package com.ajaxjs.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.ajaxjs.util.logger.LogHelper;

public class ReflectUtil {
	private static final LogHelper LOGGER = LogHelper.getLog(ReflectUtil.class);

	/**
	 * 根据类创建实例，可传入构造器参数。
	 * 
	 * @param clazz 类对象
	 * @param args 获取指定参数类型的构造函数，这里传入我们想调用的构造函数所需的参数。可以不传。
	 * @return 对象实例
	 */
	public static <T> T newInstance(Class<T> clazz, Object... args) {
		if (args == null || args.length == 0) {
			try {
				return clazz.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				LOGGER.warning(e);
			}
		}

		Constructor<T> constructor = getConstructor(clazz, ReflectUtil.args2class(args)); // 获取构造器
		return newInstance(constructor, args);
	}

	/**
	 * 根据构造器创建实例
	 * 
	 * @param constructor 类构造器
	 * @param args 获取指定参数类型的构造函数，这里传入我们想调用的构造函数所需的参数。可以不传。
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
	 * @param clazz 类对象
	 * @return true 表示为有带参数
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
	 * @param className 实际类的类型
	 * @param clazz 接口类型
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
	 * @param className 类全称
	 * @param args 根据构造函数，创建指定类型的对象,传入的参数个数需要与上面传入的参数类型个数一致
	 * @return 对象实例，因为传入的类全称是字符串，无法创建泛型 T，所以统一返回 Object
	 */
	public static Object newInstance(String className, Object... args) {
		Class<?> clazz = getClassByName(className);
		return clazz != null ? newInstance(clazz, args) : null;
	}

	/**
	 * 获取类的构造器，可以支持重载的构造器（不同参数的构造器）
	 * 
	 * @param clazz 类对象
	 * @param argClasses 指定构造函数的参数类型，这里传入我们想调用的构造函数所需的参数类型
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
	 * @param className 类全称
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

	@SuppressWarnings("unchecked")
	public static <T> Class<T> getClassByName(String className, Class<T> clz) {
		Class<?> c = getClassByName(className);
		return c == null ? null : (Class<T>) getClassByName(className);
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
	 * @param clazz 目标类
	 * @return 类的所有接口
	 */
	public static Class<?>[] getDeclaredInterface(Class<?> clazz) {
		List<Class<?>> fields = new ArrayList<>();

		for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
			Class<?>[] currentInterfaces = clazz.getInterfaces();
			fields.addAll(Arrays.asList(currentInterfaces));
		}

		return fields.toArray(new Class[fields.size()]);
	}

	/////////////// Methods ///////////////////////

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
			return getMethod(obj, methodName, ReflectUtil.args2class(args));
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
						methodObj = getSuperClassDeclaredMethod(cls, methodName, ReflectUtil.getClassByInterface(intf));

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
		if (instance == null || method == null)
			return null;

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
		Class<?>[] clazzes = ReflectUtil.args2class(args);
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

	/**
	 * 执行静态方法
	 * 
	 * @param method
	 * @param args
	 * @return
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

	public static boolean isStaticMethod(Method method) {
		return Modifier.isStatic(method.getModifiers());
	};
}
