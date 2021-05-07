/**
 * Copyright 2015 Sp42 frank@ajaxjs.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations
 * under the License.
 */
package com.ajaxjs.web.mvc;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import com.ajaxjs.framework.BaseModel;
import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.util.MappingValue;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.util.map.MapTool;

/**
 * 处理请求参数，多为静态的工具方法
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class RequestParam {
	private static final LogHelper LOGGER = LogHelper.getLog(RequestParam.class);

	/**
	 * 对控制器的方法进行分析，看需要哪些参数。将得到的参数签名和请求过来的参数相匹配，再传入到方法中去执行。
	 * 
	 * @param controller 控制器实例
	 * @param req        请求对象
	 * @param resp       响应对象
	 * @param method     控制器方法对象
	 * @return 参数列表
	 */
	public static Object[] getArgs(IController controller, MvcRequest req, HttpServletResponse resp, Method method) {
		Annotation[][] annot = method.getParameterAnnotations(); // 方法所有的注解，length 应该要和参数总数一样
		Class<?>[] parmTypes = method.getParameterTypes();// 反射得到参数列表的各个类型，遍历之
		ArrayList<Object> args = new ArrayList<>();// 参数列表

		for (int i = 0; i < parmTypes.length; i++) {
			Class<?> clazz = parmTypes[i];

			// 适配各种类型的参数，或者注解
			if (clazz.equals(HttpServletRequest.class) || clazz.equals(MvcRequest.class)) // 常见的 请求/响应 对象，需要的话传入之
				args.add(req);
			else if (clazz.equals(HttpServletResponse.class))
				args.add(resp);
			else if (clazz.equals(HttpSession.class))
				args.add(req.getSession());
			else if (clazz.equals(Map.class)) { // map 参数，将请求参数转为 map
				Map<String, Object> map;

				if (req.getMethod() != null && req.getMethod().equals("PUT"))
					map = req.getPutRequestData();
				else
					map = MapTool.as(req.getParameterMap(), arr -> MappingValue.toJavaValue(arr[0]));

				args.add(map);

				if (map.size() == 0)
					LOGGER.info("没有任何请求数据，但控制器方法期望至少一个参数来构成 map。");
			} else if (clazz.equals(ModelAndView.class))
				args.add(new ModelAndView()); // 新建 ModeView 对象
			else if (BaseModel.class.isAssignableFrom(clazz)) {// 实体类参数
				Annotation[] annotations = annot[i];

				// 写死第一个的注解
				if (annotations != null && annotations.length >= 1 && annotations[0] instanceof SubBean) {
					Map<String, Object> map = req.getMap(), _map = new HashMap<>();

					SubBean s = (SubBean) annotations[0];
					String perfix = s.value() + ".";

					map.forEach((key, v) -> {
						if (key.startsWith(perfix)) {
							_map.put(key.replace(perfix, ""), v);
						}
					});

					Object bean = MapTool.map2Bean(_map, clazz, true);
					args.add(bean);
				} else
					args.add(req.getBean(clazz));

			} else { // 适配注解
				Annotation[] annotations = annot[i];
				getArgValue(clazz, annotations, req, args, method, controller);
			}
		}

		return args.toArray();
	}

	/**
	 * 根据注解和类型从 request 中取去参数值。 参数名字与 QueryParam 一致 或者 PathParam
	 * 
	 * @param clz        参数类型
	 * @param annots     参数的注解
	 * @param req        请求对象
	 * @param args       参数列表
	 * @param method     控制器方法对象
	 * @param controller 控制器实例
	 */
	private static void getArgValue(Class<?> clz, Annotation[] annots, MvcRequest req, ArrayList<Object> args, Method method, IController controller) {
		if (annots.length > 0) {
			boolean required = false; // 是否必填字段
			String defaultValue = null; // 默认值

			for (Annotation annot : annots) {
				if (annot instanceof NotNull)
					required = true;

				if (annot instanceof DefaultValue)
					defaultValue = ((DefaultValue) annot).value();

				// 找到匹配的参数，这是说控制器上的方法是期望得到一个 url query string
				if (annot instanceof QueryParam || annot instanceof FormParam || annot instanceof HeaderParam) {
					// 参数的
					// 根据注解的名字，获取 QueryParam 参数实际值，此时是 String 类型，要转为到控制器方法期望的类型。
					getArgValue(clz, args, getArgValue(annot, req, required), defaultValue);

					break; // 只需要执行一次，参见调用的那个方法就知道了
				} else if (annot instanceof PathParam) { // URL 上面的参数
					Path path = method.getAnnotation(Path.class);

					if (path != null) {
						String paramName = ((PathParam) annot).value();
						String value = req.getValueFromPath(parseRoot(path, controller == null ? null : controller.getClass()), paramName);
						getArgValue2(clz, args, value);
					} else
						LOGGER.warning(new NullPointerException("控制器方法居然没有 PathParam 注解？？"));

					break;
				} else if (annot instanceof SubBean) {// 子实体
					//String name = ((SubBean) annot).value();
				}
			}
		} else
			args.add("Nothing to add args"); // 不知道传什么，就这个字符串吧
	}

	/**
	 * 
	 * @param path
	 * @param clz
	 * @return
	 */
	private static String parseRoot(Path path, Class<? extends IController> clz) {
		String pathValue = path.value();

		if (clz != null && pathValue.contains("{root}")) { // 顶部路径
			String topPath = Action.getRootPath(clz);
			pathValue = pathValue.replaceAll("\\{root\\}", topPath);
		}

		return pathValue;
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

		if (a instanceof QueryParam)
			key = ((QueryParam) a).value();
		else if (a instanceof FormParam)
			key = ((FormParam) a).value();
		else if (a instanceof HeaderParam)
			key = ((HeaderParam) a).value();

		value = a instanceof HeaderParam ? request.getHeader(key) : request.getParameter(key);

		if (required && CommonUtil.isEmptyString(value))
			throw new NullPointerException("客户端缺少提交的参数[ " + key + "]");

		return value != null ? value.trim() : value; // 去掉空格
	}

	/**
	 * 开始转换为控制器方法上的类型，支持 String、int/Integer、boolean/Boolean
	 * 
	 * @param clz          方法目标类型
	 * @param args         保存转换后值的列表容器
	 * @param value        请求过来的原始值，都是字符串
	 * @param defaultValue 默认值
	 */
	private static void getArgValue(Class<?> clz, ArrayList<Object> args, String value, String defaultValue) {
		if (clz == String.class)
			args.add(value == null ? defaultValue : value);
		else if (clz == int.class || clz == Integer.class) {
			boolean isNull = value == null || "".equals(value) || "null".equals(value);
			int v = 0;
			if (isNull && defaultValue != null)
				v = Integer.parseInt(defaultValue);
			else if (!isNull)
				v = Integer.parseInt(value);

			args.add(v);
		} else if (clz == long.class || clz == Long.class)
			args.add(value == null || "".equals(value) ? 0L : (Long.parseLong(value)));
		else if (clz == boolean.class || clz == Boolean.class)
			args.add(MappingValue.toBoolean(value));
		else {
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
		} else if (clz == int.class || clz == Integer.class) {
			args.add(Integer.parseInt(value));
		} else if (clz == long.class || clz == Long.class) {
			args.add(Long.parseLong(value));
		} else {
			LOGGER.warning("something wrong!");
			args.add(value); // unknow type
		}
	}
}
