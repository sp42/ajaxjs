package com.ajaxjs.util.reflect;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * 创建实例的相关反射
 */
@Slf4j
public class NewInstance {
    /**
     * 根据类创建实例，可传入构造器参数。
     * 该函数根据给定的类对象和构造器参数创建一个实例。如果参数中的类是接口，将返回 null。
     * 如果参数中的构造器参数为空或长度为0，则使用类的默认无参构造函数创建实例。
     * 如果构造器参数不为空，将使用反射获取与参数类型匹配的构造函数，并使用构造函数创建实例。
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

        if (ObjectUtils.isEmpty(args)) {
            try {
                return clz.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                log.warn("ERROR>>", e);
            }
        }

        Constructor<T> constructor = getConstructor(clz, Clazz.args2class(args));// 获取构造器

        return constructor == null ? null : newInstance(constructor, args);
    }

    /**
     * 根据构造器创建实例
     * 该函数根据给定的构造器和参数列表创建指定类的实例。它使用反射调用构造函数来实例化对象，并在实例化失败时返回 null。
     *
     * @param constructor 类构造器
     * @param args        获取指定参数类型的构造函数，这里传入我们想调用的构造函数所需的参数。可以不传。
     * @return 对象实例
     */
    public static <T> T newInstance(Constructor<T> constructor, Object... args) {
        try {
            return constructor.newInstance(args); // 实例化
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            log.warn("实例化对象失败：" + constructor.getDeclaringClass(), e);
            return null;
        }
    }

    /**
     * 传入的类是否有带参数的构造器
     * 该函数通过传入的类对象，判断该类是否有带参数的构造函数，若有则返回true，否则返回false。函数首先获取类的所有构造函数，
     * 然后遍历构造函数，判断构造函数的参数列表是否非空，若存在非空的参数列表则返回true。
     * 如果遍历完所有的构造函数都没有找到带参数的构造函数，则返回false。
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
     * 该函数根据给定的类全称和参数，使用反射获取类对象并创建相应类型的对象实例。返回对象实例，类型为 Object。
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
     * 这个函数用于获取类的构造函数。它接受两个参数，一个是类对象，一个是可选的参数类型数组。
     * 如果传入了参数类型数组，则获取与该数组匹配的构造函数；如果没有传入参数类型数组，则获取空参数列表的构造函数。
     * 如果找不到合适的构造函数，会记录日志并返回 null。
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
