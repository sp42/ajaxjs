package com.ajaxjs.util.reflect;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Bean 工具集
 * @author sp42
 *
 */
public class BeanUtil {

	/**
	 * BEAN SETXXX
	 * 
	 * @param bean
	 *            JAVA Bean 对象，也可以是
	 * @param name
	 *            属性名称
	 * @param value
	 *            要设置的属性值
	 */
	public static void setProperty(Object bean, String name, Object value) {
		if (bean == null)
			throw new NullPointerException("未发现类");

		String setMethodName = "set" + firstLetterUpper(name);

		Class<?> clazz = bean.getClass();

		if (clazz == null)
			throw new NullPointerException("执行：" + setMethodName + "未发现类");
		if (value == null)
			throw new NullPointerException("执行：" + setMethodName + "未发现参数 value");

		// 要把父类的也包括进来
		Method method = Reflect.getDeclaredMethod(clazz, setMethodName, value);

		// 如果没找到，那就试试接口的……
		if (method == null)
			method = Reflect.getDeclaredMethodByInterface(clazz, setMethodName, value);

		if (method == null) {
			throw new NullPointerException(clazz.getName() + "找不到目标方法！" + setMethodName);
		}

		Reflect.executeMethod(bean, method, value);
	}

	/**
	 * 将第一个字母大写
	 * 
	 * @param str
	 *            字符串
	 * @return 字符串
	 */
	public static String firstLetterUpper(String str) {
		// return str.substring(0, 1).toUpperCase() + str.substring(1); //
		// 另外一种写法
		return Character.toString(str.charAt(0)).toUpperCase() + str.substring(1);
	}

	/**
	 * 把 getter 的 getXX() 转换为 xX 的字段名
	 * 
	 * @param methodName
	 *            方法名称
	 * @return
	 */
	public static String getFieldName(String methodName) {
		methodName = methodName.replace("get", "");
		return Character.toString(methodName.charAt(0)).toLowerCase() + methodName.substring(1);
	}

	/**
	 * map转实体
	 * 
	 * @param map
	 * @return
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws IntrospectionException
	 */
	public static <T> T mapTransformToBean(Map<String, Object> map, Class<T> clz) {
		T bean = ReflectNewInstance.newInstance(clz);
		try {

			BeanInfo beanInfo = Introspector.getBeanInfo(clz);

			for (PropertyDescriptor property : beanInfo.getPropertyDescriptors()) {
				String key = property.getName();

				if (map.containsKey(key)) {
					Object value = map.get(key);
					// 得到property对应的setter方法
					Method setter = property.getWriteMethod();
					setter.invoke(bean, value);
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return bean;
	}

	/**
	 * 实体转map
	 * 
	 * @param user
	 * @return
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws IntrospectionException
	 */
	public static <T> Map<String, Object> beanTransformToMap(T user) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(user.getClass());

			for (PropertyDescriptor property : beanInfo.getPropertyDescriptors()) {
				String key = property.getName();
				// 过滤class属性
				if (!key.equals("class")) {
					// 得到property对应的getter方法
					Method getter = property.getReadMethod();
					Object value = getter.invoke(user);
					map.put(key, value);
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return map;
	}
}
