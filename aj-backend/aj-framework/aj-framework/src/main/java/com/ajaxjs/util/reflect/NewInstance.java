package com.ajaxjs.util.reflect;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * 创建实例的相关反射
 */
@Slf4j
public class NewInstance {
    /**
     * 根据类创建实例，可传入构造器参数。
     *
     * @param clz  类对象
     * @param args 获取指定参数类型的构造函数，这里传入我们想调用的构造函数所需的参数。可以不传。
     * @return 对象实例
     */
    public static <T> T newInstance(Class<T> clz, Object... args) {
        if (clz.isInterface()) {
            log.warn("所传递的 class 类型参数为接口 {}，无法实例化", clz);
            return null;
        }

        if (args == null || args.length == 0) {
            try {
                return clz.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                log.warn("ERROR>>", e);
            }
        }

        assert args != null;
        Constructor<T> constructor = getConstructor(clz, Clazz.args2class(args));// 获取构造器

        assert constructor != null;
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
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException |
                 InvocationTargetException e) {
            log.warn("实例化对象失败：" + constructor.getDeclaringClass(), e);
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
//	/**
//	 * 根据类全称创建实例，并转换到其接口的类型
//	 *
//	 * @param className 实际类的类型
//	 * @param clazz     接口类型
//	 * @return 对象实例
//	 */
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
        Class<?> clazz = Clazz.getClassByName(clzName);

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
            log.warn("找不到这个 " + clz.getName() + " 类的构造器。", e);
        } catch (SecurityException e) {
            log.warn("ERROR>>", e);
        }

        return null;
    }
}
