/**
 * Copyright 2015 Sp42 frank@ajaxjs.com
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
package com.ajaxjs.mvc.controller;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import javax.ws.rs.FormParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import com.ajaxjs.framework.BaseModel;
import com.ajaxjs.keyvalue.MapHelper;
import com.ajaxjs.keyvalue.MappingValue;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.util.logger.LogHelper;

/**
 * 处理请求参数
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class RequestParam {
	private static final LogHelper LOGGER = LogHelper.getLog(RequestParam.class);

	/**
	 * 对控制器的方法进行分析，看需要哪些参数。将得到的参数签名和请求过来的参数相匹配，再传入到方法中去执行。
	 * 
	 * @param request
	 *            请求对象
	 * @param response
	 *            响应对象
	 * @param method
	 *            控制器方法对象
	 * @return 参数列表
	 */
	public static Object[] getArgs(MvcRequest request, HttpServletResponse response, Method method) {
		Annotation[][] annotation = method.getParameterAnnotations(); // 方法所有的注解，length 应该要和参数总数一样
		Class<?>[] parmTypes = method.getParameterTypes();// 反射得到参数列表的各个类型，遍历之

		ArrayList<Object> args = new ArrayList<>();// 参数列表
		for (int i = 0; i < parmTypes.length; i++) {
			Class<?> clazz = parmTypes[i];

			// 适配各种类型的参数，或者注解
			if (clazz.equals(HttpServletRequest.class) || clazz.equals(MvcRequest.class)) {// 常见的 请求/响应 对象，需要的话传入之
				args.add(request);
			} else if (clazz.equals(HttpServletResponse.class)) {
				args.add(response);
			} else if (clazz.equals(Map.class)) { // map 参数，将请求参数转为 map
				Map<String, Object> map;
				if (request.getMethod() != null && request.getMethod().equals("PUT")) {
					map = request.getPutRequestData();
				} else {
					map = MapHelper.asObject(MapHelper.toMap(request.getParameterMap()), true);
				}

				args.add(map);

				if (map.size() == 0)
					LOGGER.info("没有任何请求数据，但控制器方法期望至少一个参数来构成 map。");
			} else if (clazz.equals(ModelAndView.class)) {
				args.add(new ModelAndView()); // 新建 ModeView 对象
			} else if (BaseModel.class.isAssignableFrom(clazz)) {// 实体类参数
				args.add(request.getBean(clazz));
			} else { // 适配注解
				Annotation[] annotations = annotation[i];
				getArgValue(clazz, annotations, request, args, method);
			}
		}

		return args.toArray();
	}

	/**
	 * 根据注解和类型从 request 中取去参数值。 参数名字与 QueryParam 一致 或者 PathParam
	 * 
	 * @param clz
	 *            参数类型
	 * @param annotations
	 *            参数的注解
	 * @param request
	 *            请求对象
	 * @param args
	 *            参数列表
	 * @param method
	 *            控制器方法对象
	 */
	private static void getArgValue(Class<?> clz, Annotation[] annotations, MvcRequest request, ArrayList<Object> args, Method method) {
		if (annotations.length > 0) {
			boolean required = false; // 是否必填字段
			
			for (Annotation a : annotations) {
				if(a instanceof NotNull)
					required = true;
				
				if (a instanceof QueryParam || a instanceof FormParam) { // 找到匹配的参数，这是说控制器上的方法是期望得到一个 url query string 参数的
					getArgValue(clz, args, getArgValue(a, request, required)); // 根据注解的名字，获取 QueryParam 参数实际值，此时是 String 类型，要转为到控制器方法期望的类型。

					break; // 只需要执行一次，参见调用的那个方法就知道了
				} else if (a instanceof PathParam) { // URL 上面的参数
					Path path = method.getAnnotation(Path.class);
					if (path != null) {
						String paramName = ((PathParam) a).value(), value = request.getValueFromPath(path.value(), paramName);
						
						getArgValue2(clz, args, value);
					} else {
						LOGGER.warning(new NullPointerException("控制器方法居然没有 PathParam 注解？？"));
					}

					break;
				}
			}
		} else {
			args.add("nothing to add args"); // 不知道传什么，就空字符串吧
		}
	}
	
	/**
	 * 
	 * @param a
	 * @param request
	 * @param required
	 * @return
	 */
	private static String getArgValue(Annotation a, HttpServletRequest request, boolean required) {
		String key = null, value;
		if (a instanceof QueryParam) {
			key = ((QueryParam) a).value();
		} else {
			key = ((FormParam) a).value();
		}
		
		value = request.getParameter(key);
		
		if(required && value == null)
			throw new NullPointerException("客户端缺少提交的参数 " + key);
		
		return value;
	}
	
	/**
	 * 开始转换为控制器方法上的类型，支持 String、int/Integer、boolean/Boolean
	 * 
	 * @param clz
	 * @param args
	 * @param value
	 */
	private static void getArgValue(Class<?> clz, ArrayList<Object> args, String value) {
		if (clz == String.class) {
			args.add(value);
		} else if (clz == int.class     || clz == Integer.class) {
			args.add(value == null   || "".equals(value) ? 0 : Integer.parseInt(value));
		} else if (clz == long.class    || clz == Long.class) {
			args.add(value == null   || "".equals(value) ? 0L :(Long.parseLong(value)));
		} else if (clz == boolean.class || clz == Boolean.class) {
			args.add(MappingValue.toBoolean(value));
		} else {
			args.add(new Object());// 也不要空的参数，不然反射那里执行不了
			LOGGER.warning("不支持类型");
		}
	}
	
	/**
	 * 
	 * @param clz
	 * @param args
	 * @param value
	 */
	private static void getArgValue2(Class<?> clz, ArrayList<Object> args, String value) {
		if (clz == String.class) {
			args.add(value);
		} else if (clz == int.class  || clz == Integer.class) {
			args.add(Integer.parseInt(value));
		} else if (clz == long.class || clz == Long.class) {
			args.add(Long.parseLong(value));
		} else {
			LOGGER.warning("something wrong!");
			args.add(value); // unknow type
		}		
	}
}
