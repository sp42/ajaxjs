package com.ajaxjs.util.reflect;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class BeanUtils {
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
     * 调用 bean 对象的 setter 方法 参考 Spring 的 <code>ReflectionUtils.setField(null, null, null);</code>
     *
     * @param bean  Bean 对象
     * @param name  属性名称，前缀不要带 set
     * @param value 要设置的属性值
     */
    public static void setProperty(Object bean, String name, Object value) {
        String setMethodName = "set" + StringUtils.capitalize(name);
        Objects.requireNonNull(bean, bean + "执行：" + setMethodName + " 未发现类");
//		Objects.requireNonNull(value, bean + "执行：" + setMethodName + " 未发现参数 value");
        Class<?> clazz = bean.getClass();
        // 要把参数父类的也包括进来
        Method method = Methods.getMethodByUpCastingSearch(clazz, setMethodName, value);

        // 如果没找到，那就试试接口的……
        if (method == null)
            method = Methods.getDeclaredMethodByInterface(clazz, setMethodName, value);

        // 如果没找到，那忽略参数类型，只要匹配方法名称即可。这会发生在：由于被注入的对象有可能经过了 AOP 的动态代理，所以不能通过上述逻辑找到正确的方法
        if (method == null)
            method = Methods.getSuperClassDeclaredMethod(clazz, setMethodName);

        // 最终还是找不到
        Objects.requireNonNull(method, "找不到目标方法[" + clazz.getSimpleName() + "." + setMethodName + "(" + value.getClass().getSimpleName() + ")]");

        Methods.executeMethod(bean, method, value);
    }

    /**
     * 常量转换为 Map
     * 获取指定类中的所有 int 类型常量的名称和值，并返回它们构成的 Map 对象。
     *
     * @param clz 常量类，一般为接口
     * @return 常量的 Map 格式
     */
    public static Map<String, Integer> getConstantsInt(Class<?> clz) {
        Map<String, Integer> map = new HashMap<>();// 创建一个空的 HashMap 对象，用于存储常量名称和值的映射关系

        Field[] fields = clz.getDeclaredFields();
        Object instance = NewInstance.newInstance(clz);

        for (Field field : fields) {
            String descriptor = Modifier.toString(field.getModifiers());// 获得其属性的修饰

            // 判断该属性是否为 public static final 修饰的 int 类型常量
            if (descriptor.equals("public static final")) {
                try {
                    map.put(field.getName(), (int) field.get(instance));
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    log.warn("WARN>>>>>", e);
                }
            }
        }

        return map;
    }

    /**
     * 设置 Java Bean 的值
     *
     * @param bean      Bean 实体
     * @param fieldName 字段名
     * @param value     值
     */
    public static void setBeanValue(Object bean, String fieldName, Object value) {
        try {
            Field field = bean.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(bean, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
//            LOGGER.warning(e);
        }
    }
}
