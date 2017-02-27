package com.ajaxjs.util.reflect;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 关于泛型的反射收集于此
 * @author sp42
 *
 */
public class ReflectGeneric {
	/**
	 * 获取 List<String> 的 String，而不是 List 假设
	 * 
	 * @param method
	 *            方法对象
	 * @return 泛型类型
	 */
	public static Class<?> getFirstType(Method method) {
		Type returnType = method.getGenericReturnType();

		if (returnType instanceof ParameterizedType) {
			ParameterizedType type = (ParameterizedType) returnType;

			for (Type typeArgument : type.getActualTypeArguments()) {
				return (Class<?>) typeArgument;
			}
		}

		return null;
	}
}
