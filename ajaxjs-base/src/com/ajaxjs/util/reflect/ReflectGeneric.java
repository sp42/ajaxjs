/**
 * Copyright 2015 Frank Cheung
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

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

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
				if(typeArgument instanceof sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl) {
					return Map.class; // 写死的
				} else
					return (Class<?>) typeArgument;
			}
		}

		return null;
	}
}
