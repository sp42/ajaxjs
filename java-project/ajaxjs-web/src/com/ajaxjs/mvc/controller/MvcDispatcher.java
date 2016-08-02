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
package com.ajaxjs.mvc.controller;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import com.ajaxjs.mvc.AnnotationUtils;
import com.ajaxjs.framework.model.BaseModel;
import com.ajaxjs.framework.model.ModelAndView;
import com.ajaxjs.mvc.ActionAndView;
import com.ajaxjs.util.ClassScaner;
import com.ajaxjs.util.LogHelper;
import com.ajaxjs.util.Reflect;
import com.ajaxjs.util.StringUtil;
import com.ajaxjs.web.Requester;
import com.ajaxjs.web.Responser;
import com.ajaxjs.web.ServletPatch;

/**
 * MVC框架的核心是一个 Dispatcher，用于接收所有的HTTP请求，并根据URL选择合适的Action对其进行处理
 * 
 * @author frank
 *
 */
public class MvcDispatcher implements Filter {
	private static final LogHelper LOGGER = LogHelper.getLog(MvcDispatcher.class);

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		Requester request = new Requester(req);
		Responser response = new Responser(resp);
		response.setRequest(request);

		String uri = request.getRoute(), httpMethod = request.getMethod();

		Object[] obj = getMethod(uri, httpMethod); // 返回两个对象
		Method method = (Method) obj[1];// 要执行的方法

		if (method == null) {
			@SuppressWarnings("unused")
			String msg = httpMethod + uri + " 控制器没有这个方法！";// Let go
		} else {
			IController controller = (IController) obj[0];
			Object result;

			ModelAndView model = null;
			
			RequestHelper.setHttpServletRequest(request);
			RequestHelper.setHttpServletResponse(response);
			
			if (method.getParameterTypes().length > 0) {
				Object[] args = getArgs(request, response, method);
				model = findModel(args);
				result = Reflect.executeMethod(controller, method, args);// 通过反射执行方法
			} else {
				// 方法没有参数
				result = Reflect.executeMethod(controller, method);
			}

			resultHandler(result, request, response, model);
			
			RequestHelper.clean();
			return;
		}

		chain.doFilter(req, resp);// 不要传 ServletRequest，以免影响其他框架
	}

	/**
	 * 查找是 ModelAndView 类型的参数，返回之
	 * 
	 * @param args
	 * @return
	 */
	private static ModelAndView findModel(Object[] args) {
		for (Object obj : args) {
			if (obj instanceof ModelAndView)
				return (ModelAndView) obj;
		}
		return null;
	}

	/**
	 * 执行逻辑完成，现在控制输出（响应）
	 * @param result
	 * @param request
	 * @param response
	 * @param model
	 */
	private static void resultHandler(Object result, Requester request, Responser response, ModelAndView model) {
		LOGGER.info("执行逻辑完成，现在控制输出（响应）" + result);
		
		if (model != null)
			model.saveToReuqest(request);

		if (result != null) {
			if (result instanceof String) {
				String str = (String) result, html = "html::";

				if (str.startsWith(html)) {
					response.outputSimpleHTML(str.replace(html, ""));
				} else if (str.startsWith("redirect::")) {
					try {
						response.sendRedirect(str.replace("redirect::", ""));
					} catch (IOException e) {
						LOGGER.warning(e);
					}
				} else { // JSP
					response.sendRequestDispatcher(str);
				}
			}
		}
	}

	/**
	 * 
	 * @param request
	 *            请求对象
	 * @param response
	 *            响应对象
	 * @param method
	 *            控制器方法对象
	 * @return 参数列表
	 */
	private static Object[] getArgs(Requester request, Responser response, Method method) {
		ArrayList<Object> args = new ArrayList<>();// 参数列表
		Annotation[][] annotation = method.getParameterAnnotations(); /* 方法所有的注解，length和参数总数一样 */

		Class<?>[] parmTypes = method.getParameterTypes();
		for (int i = 0; i < parmTypes.length; i++) {
			Class<?> clazz = parmTypes[i];
			if (clazz.equals(HttpServletRequest.class)) {
				args.add(request);
			} else if (clazz.equals(Requester.class)) {
				args.add(request);
			} else if (clazz.equals(HttpServletResponse.class)) {
				args.add(response);
			} else if (clazz.equals(Responser.class)) {
				args.add(response);
			} else if (clazz.equals(ModelAndView.class)) {
				args.add(new ModelAndView());
			} else if(BaseModel.class.isAssignableFrom(clazz)){
				args.add(Reflect.newInstance(clazz)); // 实体类参数
 			}else {
				Annotation[] annotations = annotation[i];
				getArgValue(clazz, annotations, request, args, method);
			}
		}

		return args.toArray();
	}

	/**
	 * 根据注解和类型从 request 中取去参数值
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
	 */
	private static void getArgValue(Class<?> clz, Annotation[] annotations, Requester request, ArrayList<Object> args,
			Method method) {
		if (annotations.length > 0) {
			boolean isGot = false; // 是否有 QueryParam 注解写好了
			for (Annotation a : annotations) {
				if (a instanceof QueryParam) { // 找到匹配的参数
					isGot = true;

					String argValue = request.getParameter(((QueryParam) a).value());

					// 支持 String、int/Integer、boolean/Boolean
					if (clz == String.class) {
						args.add(argValue);
					} else if (clz == int.class || clz == Integer.class) {
						args.add(Integer.parseInt(argValue));
					} else if (clz == long.class || clz == Long.class) {
						args.add(Long.parseLong(argValue));
					} else if (clz == boolean.class || clz == Boolean.class) {
						if (argValue.equals("true"))
							args.add(true);
						else if (argValue.equals("false"))
							args.add(true);
					} else {
						args.add(new Object());// 也不要空的参数，不然反射那里执行不了
						LOGGER.warning("不支持类型");
					}
					break; // 只需要执行一次，参见调用的那个方法就知道了
				} else if (a instanceof PathParam) {
					Path path = method.getAnnotation(Path.class);
					if (path != null) {
						String paramName = ((PathParam) a).value();
						String value = getValueFromPath(request.getRequestURI(), path.value(), paramName);
						if (clz == String.class) {
							args.add(value);
						} else if (clz == int.class || clz == Integer.class) {
							args.add(Integer.parseInt(value));
						} else if (clz == long.class || clz == Long.class) {
							args.add(Long.parseLong(value));
						}
					} else {
						LOGGER.warning(new NullPointerException("控制器方法居然没有 PathParam 注解？？"));
					}
					break;
				}
			}
			// 还是没有适合注解呢？
			if (!isGot) {
				// "是否有 QueryParam 注解写好了??"
				// 当然也可能是 PathParam
			}
		} else {
			args.add("nothing to add args"); // 不知道传什么，就空字符串吧
		}
	}

	/**
	 * 取去 url 上的值
	 * 
	 * @param requestURI
	 * @param value
	 * @param paramName
	 * @return
	 */
	private static String getValueFromPath(String requestURI, String value, String paramName) {
		// System.out.println(requestURI);
		// System.out.println(value);
		// System.out.println(paramName);
		String regExp = "(?!" + value.replace("{" + paramName + "}",
				")(\\d+)");/* 获取正则 暂时写死 数字 TODO */
		 System.out.println(regExp);
		String result = StringUtil.regMatch(regExp, requestURI);
		if (result == null)
			throw new IllegalArgumentException("在 " + requestURI + "不能获取 " + paramName + "参数");
		return result;
	}

	/**
	 * 返回要执行的方法
	 * 
	 * @param uri
	 *            去掉项目前缀的 URL 路径
	 * @param httpMethod
	 *            HTTP 请求的方法
	 * @return 控制器方法
	 */
	private static Object[] getMethod(String uri, String httpMethod) {
		Object[] objs = new Object[2];
		Method method = null;

		for (String path : AnnotationUtils.controllers.keySet()) {
			if (uri.startsWith(path)) { // 匹配对应的控制器
				LOGGER.info(path + " 控制器命中！！！");

				ActionAndView controllerInfo = AnnotationUtils.controllers.get(path);
				objs[0] = controllerInfo.controller; // 返回 controller

				String subUri = uri.replace(path, "");

				boolean isSub = false; // 如果执行了 sub 就不用执行类 path 本身的

				for (String subPath : controllerInfo.subPath.keySet()) {
					UriInfo ui = new UriInfo(subUri, subPath);

					if (subUri.equals("/{id}") || ui.isInfo() || ui.isNotInfo()) {
						method = getMethod(controllerInfo.subPath.get(subPath), httpMethod);
						LOGGER.info(subPath + "子路径命中！！！");
						isSub = true;
						break;
					}
				}

				if (!isSub) {
					// 类本身
					method = getMethod(controllerInfo, httpMethod);
				}

				break;
			}
		}

		objs[1] = method;// 返回 方法对象

		return objs;
	}

	/**
	 * 根据 httpMethod 请求方法返回控制器类身上的方法。
	 * 
	 * @param controllerInfo
	 *            控制器类信息
	 * @param httpMethod
	 *            HTTP 请求的方法
	 * @return 控制器方法
	 */
	private static Method getMethod(ActionAndView controllerInfo, String httpMethod) {
		switch (httpMethod) {
		case "GET":
			return controllerInfo.GET_method;
		case "POST":
			return controllerInfo.POST_method;
		case "PUT":
			return controllerInfo.PUT_method;
		case "DELETE":
			return controllerInfo.DELETE_method;
		}

		return null;
	}

	/**
	 * 获取配置信息
	 */
	@Override
	public void init(FilterConfig fConfig) throws ServletException {
		LOGGER.info("Ajaxjs MVC 服务启动之中……");
		/* 获取web.xml 配置 */
		Map<String, String> config = ServletPatch.parseInitParams(null, fConfig);

		if (config != null && config.get("controller") != null) {
			String str = config.get("controller");
			
			ClassScaner<IController> scaner = new ClassScaner<>(IController.class);
			for (String packageName : str.split(",")) {
				for(Class<IController> clz : scaner.scan(packageName)){
					LOGGER.info("创建控制器：:" + clz.getName());
					AnnotationUtils.scan(clz);
				}
			}
		} else {
			LOGGER.warning("web.xml 没有配置 MVC 过滤器或者 配置没有定义 controller");
		}

	}

	@Override
	public void destroy() {
	}

	static class UriInfo {
		private String subUri;
		private String subPath;
		private String info_id;

		public UriInfo(String subUri, String subPath) {
			this.subUri = subUri;
			this.subPath = subPath;
			/* like foo/123 means info */
			String reg = "(?!/" + subPath.replaceAll("(\\{|\\})", "") + "/)(\\d+)";
			// System.out.println(subUri);
			// System.out.println(reg);
			info_id = StringUtil.regMatch(reg, subUri);
			// System.out.println(info_id);

		}

		/**
		 * 是否详情的
		 * 
		 * @return
		 */
		public boolean isInfo() {
			return info_id != null && subPath.contains("{id}");
		}

		/**
		 * 是否非详情的
		 * 
		 * @return
		 */
		public boolean isNotInfo() {
			return info_id == null && subUri.startsWith(subPath);
		}
	}
}
