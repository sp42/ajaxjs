package com.ajaxjs.util.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.ajaxjs.util.LogHelper;

public class ReflectNewInstance {
	private static final LogHelper LOGGER = LogHelper.getLog(ReflectNewInstance.class);

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
	 * 根据类全称创建实例，并转换到其接口的类型
	 * @param className 实际类的类型
	 * @param clazz 接口类型
	 * @return 对象实例
	 */
	@SuppressWarnings("unchecked")
	public static <T> T newInstance(String className, Class<T> clazz) {
		Class<?> clz = getClassByName(className);
		return clazz != null ? (T)newInstance(clz) : null;
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
