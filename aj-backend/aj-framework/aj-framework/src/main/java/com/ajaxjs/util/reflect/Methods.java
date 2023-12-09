package com.ajaxjs.util.reflect;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 方法相关的反射
 */
@Slf4j
public class Methods {
    /**
     * 根据类和方法名获取该类声明的方法
     *
     * @param clz        要查找方法的类
     * @param methodName 方法名
     * @return 声明的方法，如果方法不存在则返回 null
     */
    public static Method getDeclaredMethod(Class<?> clz, String methodName) {
        try {
            return clz.getDeclaredMethod(methodName);
        } catch (NoSuchMethodException e) {
            log.warn("ERROR>>", e);
            return null;
        }
    }

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
            return ObjectUtils.isEmpty(args) ? cls.getMethod(method) : cls.getMethod(method, args);
        } catch (NoSuchMethodException | SecurityException e) {
            StringBuilder str = new StringBuilder();

            for (Class<?> clz : args)
                str.append(clz.getName());

            log.warn("类找不到这个方法 {}.{}({})。", cls.getName(), method, str.toString().equals("") ? "void" : str.toString());
            return null;
        }
    }

    /**
     * 根据方法名称和参数列表查找方法。注意参数对象类型由于没有向上转型会造成不匹配而找不到方法，这时应使用上一个方法或
     * getMethodByUpCastingSearch()
     *
     * @param obj    实例对象
     * @param method 方法名称
     * @param args   对应重载方法的参数列表
     * @return 匹配的方法对象，null 表示找不到
     */
    public static Method getMethod(Object obj, String method, Object... args) {
        if (!ObjectUtils.isEmpty(args))
            return getMethod(obj, method, Clazz.args2class(args));
        else
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
        Method methodObj;

        for (Class<?> clazz = arg.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
            Type[] interfaces = clazz.getGenericInterfaces();

            if (interfaces.length != 0) { // 有接口！
                try {
                    for (Type _interface : interfaces) {
                        // 旧方法，现在不行，不知道之前怎么可以的 methodObj = hostClazz.getDeclaredMethod(method,
                        // (Class<?>)_interface);
                        // methodObj = cls.getMethod(methodName,
                        // ReflectNewInstance.getClassByInterface(_interface));
                        methodObj = getSuperClassDeclaredMethod(clz, method, Clazz.getClassByInterface(_interface));

                        if (methodObj != null)
                            return methodObj;
                    }
                } catch (Exception e) {
                    log.warn("ERROR>>", e);
                }
            }
            //			else {
            // 无实现的接口
            //			}
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
            } catch (NoSuchMethodException | SecurityException ignored) {
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
                if (m.toString().contains(method))
                    return m;
            }
        }

        return null;
    }

    /**
     * 获取本类及其父类的字段属性（包括 private 的）
     *
     * @param clz 当前类对象
     * @return 字段数组
     */
    public static Field[] getSuperClassDeclaredFields(Class<?> clz) {
        List<Field> fieldList = new ArrayList<>();

        while (clz != null) {
            fieldList.addAll(new ArrayList<>(Arrays.asList(clz.getDeclaredFields())));
            clz = clz.getSuperclass();
        }

        return fieldList.toArray(new Field[0]);
    }

    public static Class<?>[] getAllSuperClazz(Class<?> clz) {
        List<Class<?>> clzList = new ArrayList<>();

        //		while (clz != null) {
        //			clz = clazz.getSuperclass();
        //
        //			if (clz != null && clz != Object.class)
        //				clzList.add(clz);
        //		}
        for (; clz != Object.class; clz = clz.getSuperclass()) {
            if (clz == null)
                break;
            else
                clzList.add(clz);
        }

        if (clzList.size() > 0)
            clzList.remove(0); // 排除自己

        return clzList.toArray(new Class[0]);
    }

    /**
     * 调用方法
     *
     * @param instance 对象实例，bean
     * @param method   方法对象
     * @param args     参数列表
     * @return 执行结果
     * @throws Throwable 任何异常
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
                log.warn("反射执行方法异常！所在类[{}] 方法：[{}]", instance.getClass().getName(), method.getName());

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
     * @param instance 对象实例，bean
     * @param method   方法对象名称
     * @param args     参数列表
     * @return 执行结果
     */
    public static Object executeMethod(Object instance, String method, Object... args) {
        // 没有方法对象，先找到方法对象。可以支持方法重载，按照参数列表
        Class<?>[] clazz = Clazz.args2class(args);
        Method methodObj = getMethod(instance.getClass(), method, clazz);

        return methodObj != null ? executeMethod(instance, methodObj, args) : null;
    }

    /**
     * 调用方法。 注意获取方法对象，原始类型和包装类型不能混用，否则得不到正确的方法， 例如 Integer 不能与 int 混用。 这里提供一个
     * argType 的参数，指明参数类型为何。
     *
     * @param instance 对象实例
     * @param method   方法名称
     * @param argType  参数类型
     * @param argValue 参数值
     * @return 执行结果
     */
    public static Object executeMethod(Object instance, String method, Class<?> argType, Object argValue) {
        Method m = getMethod(instance, method, argType);
        if (m != null)
            return executeMethod(instance, m, argValue);

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
                log.warn("ERROR>>", e);
            }
        } else
            log.warn("这不是一个静态方法：" + method);

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

    /**
     * 调用 Interface 的 default 方法
     * <a href="https://www.jianshu.com/p/63691220f81f">...</a>
     * <a href="https://link.jianshu.com/?t=http://stackoverflow.com/questions/22614746/how-do-i-invoke-java-8-default-methods-refletively">...</a>
     */
    public static Object executeDefault(Object proxy, Method method, Object[] args) {
        try {
            Constructor<MethodHandles.Lookup> constructor = MethodHandles.Lookup.class.getDeclaredConstructor(Class.class, int.class);
            constructor.setAccessible(true);

            Class<?> declaringClass = method.getDeclaringClass();
            int allModes = MethodHandles.Lookup.PUBLIC | MethodHandles.Lookup.PRIVATE | MethodHandles.Lookup.PROTECTED | MethodHandles.Lookup.PACKAGE;

            return constructor.newInstance(declaringClass, allModes)
                    .unreflectSpecial(method, declaringClass)
                    .bindTo(proxy)
                    .invokeWithArguments(args);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
