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
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.framework.BaseModel;
import com.ajaxjs.mvc.ActionAndView;
import com.ajaxjs.util.ClassScaner;
import com.ajaxjs.util.reflect.ExecuteMethod;
import com.ajaxjs.util.StringUtil;
import com.ajaxjs.util.collection.MapHelper;
import com.ajaxjs.ioc.BeanContext;
import com.ajaxjs.ioc.Scanner;
import com.ajaxjs.util.logger.LogHelper;

/**
 * MVC 分发器
 * 
 * @author Sp42 frank@ajaxjs.com
 */
public class MvcDispatcher implements Filter {
	private static final LogHelper LOGGER = LogHelper.getLog(MvcDispatcher.class);

	/**
	 * 初始化这个过滤器
	 */
	@Override
	public void init(FilterConfig fConfig) throws ServletException {
		LOGGER.info("AJAXJS MVC 服务启动之中……");

		// 读取 web.xml 配置，如果有 controller 那一项就获取指定包里面的内容，看是否有属于 IController 接口的控制器，有就加入到 AnnotationUtils.controllers 集合中
		Map<String, String> config = MvcRequest.parseInitParams(null, fConfig);

		if (config != null && config.get("doIoc") != null) {
			BeanContext.me().init(new Scanner().scanPackage(config.get("doIoc")));
		}

		if (config != null && config.get("controller") != null) {
			String str = config.get("controller");

			ClassScaner<IController> scaner = new ClassScaner<>(IController.class);// 定义一个扫描器，专门扫描 IController

			for (String packageName : StringUtil.split(str)) {
				for (Class<IController> clz : scaner.scan(packageName)) {
					LOGGER.info("正在初始化控制器：{0}", clz.getName());
					AnnotationUtils.scan(clz);
				}
			}
		} else {
			LOGGER.warning("web.xml 没有配置 MVC 过滤器或者 配置没有定义 controller");
		}
	}

	/**
	 * 虽然 REST 风格的 URL 一般不含后缀，我们只能将 DispatcherServlet 映射到“/”，使之变为一个默认的 Servlet，
	 * 这样，就可以对任意的 URL 进行处理，但是在处理 js/css 等静态文件十分不方便， 于是我们约定 *.do：后缀模式匹配。
	 */
	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
		MvcRequest request = new MvcRequest((HttpServletRequest) req);
		HttpServletResponse response = (HttpServletResponse) resp;

		String uri = request.getRoute(), httpMethod = request.getMethod();

		Object[] obj = getMethod(uri, httpMethod); // 返回两个对象
		Method method = (Method) obj[1];// 要执行的方法

		if (method == null) {
			@SuppressWarnings("unused")
			String msg = httpMethod + uri + " 控制器没有这个方法！";// Let it go
		} else {
			IController controller = (IController) obj[0];
			Object result;

			ModelAndView model = null;

			MvcRequest.setHttpServletRequest(request);
			MvcRequest.setHttpServletResponse(response);

			if (method.getParameterTypes().length > 0) {
				Object[] args = getArgs(request, response, method);
				model = findModel(args);

				// 调用反射的 Reflect.executeMethod 方法就可以执行目标方法，并返回一个结果。
				result = ExecuteMethod.executeMethod(controller, method, args);// 通过反射执行控制器方法
			} else {
				// 方法没有参数
				result = ExecuteMethod.executeMethod(controller, method);
			}

			resultHandler(result, request, response, model);
			MvcRequest.clean();

			return; // 终止当前 servlet 请求
		}

		chain.doFilter(req, resp);// 不要传 MvcRequest，以免入侵其他框架
	}

	/**
	 * 返回要执行的方法
	 * 
	 * @param userPath
	 *            用户请求过来的网址，已经去掉项目前缀的 URL 路径
	 * @param httpMethod
	 *            HTTP 请求的方法
	 * @return 控制器方法
	 */
	private static Object[] getMethod(String userPath, String httpMethod) {
		Object[] objs = new Object[2];
		Method method = null;

		/*
		 * 遍历在 init(FilterConfig fConfig) 时收集的所有控制器
		 */
		for (String path : AnnotationUtils.controllers.keySet()) {
			if (userPath.startsWith(path)) { // 匹配对应的控制器
				LOGGER.info(path + " 控制器命中！！！");
				
				// 遇到 aa/bb、aa 的情形 优先 aa/bb
				ActionAndView controllerInfo = AnnotationUtils.controllers.containsKey(userPath) ? AnnotationUtils.controllers.get(userPath) : AnnotationUtils.controllers.get(path);
				objs[0] = controllerInfo.controller; // 返回 controller

				String userSubPath = userPath.replaceAll(path + "/?", "");  
				String key = userSubPath.replaceAll("\\d+$", "{id}");

				ActionAndView av = null;
				if (controllerInfo.subPath.containsKey(userSubPath)) { // 相同的业务，如  xx/login
					LOGGER.info(userSubPath + " 子路径命中，相同的业务！！！");
					av = controllerInfo.subPath.get(userSubPath);
				} else if (controllerInfo.subPath.containsKey(key)) { // 单个实体，如  xx/1
					LOGGER.info(key + " 子路径命中，单个实体！！！");
					av = controllerInfo.subPath.get(key);
				} else {
					av = controllerInfo;// 类本身，不是子路径
				}

				if (av == null)
					throw new NullPointerException(userSubPath + " ActionAndView 对象不存在！");
				method = getMethod(av, httpMethod);

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
		if (controllerInfo == null)
			throw new NullPointerException(" ActionAndView 对象不存在！");

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
	 * 查找是 ModelAndView 类型的参数，返回之
	 * 
	 * @param args
	 *            参数列表
	 * @return ModelAndView
	 */
	private static ModelAndView findModel(Object[] args) {
		for (Object obj : args) {
			if (obj instanceof ModelAndView)
				return (ModelAndView) obj;
		}
		return null;
	}

	/**
	 * 一般一个请求希望返回一个页面，这时就需要控制器返回一个模板渲染输出了。 中间执行逻辑完成，最后就是控制输出（响应）
	 * 可以跳转也可以输出模板渲染器（即使是 json 都是 模板渲染器 ）
	 * 
	 * @param result
	 *            模板路径是指页面模板（比如 jsp，velocity模板等）的目录文件名
	 * @param request
	 *            请求对象
	 * @param response
	 *            响应对象
	 * @param model
	 *            所有渲染数据都要放到一个 model 对象中（本质 是 map或者 bean），这样使用者就可以在模板内用 Map 对象的
	 *            key/getter 获取到对应的数据。
	 */
	private static void resultHandler(Object result, MvcRequest request, HttpServletResponse response, ModelAndView model) {
		if (model != null)
			request.saveToReuqest(model);

		if (result != null && result instanceof String) {
			String str = (String) result, html = "html::";
			MvcOutput o = new MvcOutput(response);

			if (str.startsWith(html)) {
				o.setSimpleHTML(true).setOutput(str.replace(html, "")).go();
			} else if (str.startsWith("redirect::")) {
				o.setRedirect(str.replace("redirect::", "")).go();

			} else if (str.startsWith("json::")) {
				String jsonpToken = request.getParameter(MvcRequest.callback_param); // 由参数决定是否使用 jsonp

				if (StringUtil.isEmptyString(jsonpToken)) {
					o.setJson(true).setOutput(str.replace("json::", "")).go();
				} else {
					o.setJsonpToken(jsonpToken).setOutput(str.replace("json::", "")).go();
				}
			} else { // JSP
				if (!str.endsWith(".jsp")) // 自动补充 .jsp 扩展名
					str += ".jsp";

				LOGGER.info("执行逻辑完成，现在控制输出（响应 JSP）" + result);
				o.setTemplate(str).go(request);
			}
		}
	}

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
	private static Object[] getArgs(MvcRequest request, HttpServletResponse response, Method method) {
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
				Map<String, Object> map = request.getMethod().equals("PUT") ? request.getPutRequestData() : MapHelper.asObject(MapHelper.toMap(request.getParameterMap()), true);

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
	 */
	private static void getArgValue(Class<?> clz, Annotation[] annotations, MvcRequest request, ArrayList<Object> args, Method method) {
		if (annotations.length > 0) {
			boolean isGot = false; // 是否有 QueryParam 注解写好了

			for (Annotation a : annotations) {
				if (a instanceof QueryParam) { // 找到匹配的参数，这是说控制器上的方法是期望得到一个 url query string 参数的
					isGot = true;

					/*
					 * 根据注解的名字，获取 QueryParam 参数实际值，此时是 String 类型，要转为到控制器方法期望的类型。
					 */
					String argValue = request.getParameter(((QueryParam) a).value());

					/*
					 * 开始转换为控制器方法上的类型，支持 String、int/Integer、boolean/Boolean
					 */
					if (clz == String.class) {
						args.add(argValue);
					} else if (clz == int.class || clz == Integer.class) {
						args.add(argValue == null || "".equals(argValue) ? 0 : Integer.parseInt(argValue));
					} else if (clz == long.class || clz == Long.class) {
						args.add(Long.parseLong(argValue));
					} else if (clz == boolean.class || clz == Boolean.class) {
						if (argValue.equals("true") || argValue.equals("1") || argValue.equals("on"))
							args.add(true);
						else if (argValue.equals("false") || argValue.equals("0") || argValue.equals("off"))
							args.add(false);
					} else {
						args.add(new Object());// 也不要空的参数，不然反射那里执行不了
						LOGGER.warning("不支持类型");
					}

					break; // 只需要执行一次，参见调用的那个方法就知道了
				} else if (a instanceof PathParam) { // URL 上面的参数
					Path path = method.getAnnotation(Path.class);
					if (path != null) {
						String paramName = ((PathParam) a).value(), value = request.getValueFromPath(path.value(), paramName);

						//						if(paramName.contains("{"))
						//							throw new RuntimeException("这里的参数不应该有 {xx} 尖括号");

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

	@Override
	public void destroy() {
	} // 暂时不需要这个逻辑
}
