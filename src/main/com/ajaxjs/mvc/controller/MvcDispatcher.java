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

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.framework.BaseModel;
import com.ajaxjs.util.reflect.ExecuteMethod;
import com.ajaxjs.util.reflect.NewInstance;
import com.ajaxjs.util.StringUtil;
import com.ajaxjs.util.collection.MapHelper;
import com.ajaxjs.util.io.resource.Scan;
import com.ajaxjs.util.io.resource.ScanClass;
import com.ajaxjs.util.io.resource.Scanner;
import com.ajaxjs.ioc.BeanContext;
import com.ajaxjs.util.logger.LogHelper;

/**
 * MVC 分发器
 * 
 * @author Sp42 frank@ajaxjs.com
 */
public class MvcDispatcher implements Filter {
	private static final LogHelper LOGGER = LogHelper.getLog(MvcDispatcher.class);

	public static class IControllerScanner extends ScanClass {
		@SuppressWarnings({ "rawtypes", "unchecked" })
		@Override
		public void onFileAdding(Set target, File resourceFile, String packageJavaName) {
			String className = getClassName(resourceFile, packageJavaName);
			Class<?> clazz = NewInstance.getClassByName(className);
			
			LOGGER.info("正在检查类：{0}, 如果该类是 IController 的实例，那么将被收集起来。", className);
			if (IController.class.isAssignableFrom(clazz)) {
				target.add(clazz);// 添加到集合中去
			}
		}

		@SuppressWarnings({ "rawtypes", "unchecked" })
		@Override
		public void onJarAdding(Set target, String resourcePath) {
			Class<?> clazz = NewInstance.getClassByName(resourcePath);
			
			LOGGER.info("正在检查类：{0}, 如果该类是 IController 的实例，那么将被收集起来。", resourcePath);
			if (IController.class.isAssignableFrom(clazz)) {
				target.add(clazz);// 添加到集合中去
			}
		}
	}

	/**
	 * 初始化这个过滤器
	 */
	@Override
	public void init(FilterConfig fConfig) throws ServletException {
		LOGGER.info("AJAXJS MVC 服务启动之中……");

		// 读取 web.xml 配置，如果有 controller 那一项就获取指定包里面的内容，看是否有属于 IController 接口的控制器，有就加入到 AnnotationUtils.controllers 集合中
		Map<String, String> config = MvcRequest.parseInitParams(null, fConfig);

		//		if (config != null && config.get("doIoc") != null)
		//			BeanContext.me().init(new Scanner(new IControllerScanner()).scan(config.get("doIoc")));

		if (config != null && config.get("controller") != null) {
			String str = config.get("controller");

			Scanner scaner = new Scanner(new IControllerScanner());// 定义一个扫描器，专门扫描 IController

			for (String packageName : StringUtil.split(str)) {
				@SuppressWarnings("unchecked")
				Set<Class<IController>> IControllers = (Set<Class<IController>>) scaner.scan(packageName);

				for (Class<IController> clz : IControllers)
					ControllerScanner.add(clz);
			}
		} else {
			LOGGER.info("web.xml 没有配置 MVC 过滤器或者 配置没有定义 controller");
		}
	}

	/**
	 * 虽然 REST 风格的 URL 一般不含后缀，我们只能将 DispatcherServlet 映射到“/”，使之变为一个默认的 Servlet，
	 * 这样，就可以对任意的 URL 进行处理，但是在处理 js/css 等静态文件十分不方便， 于是我们约定 *.do：后缀模式匹配。
	 */
	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
		MvcRequest request = new MvcRequest(req);
		MvcOutput response = new MvcOutput(resp);

		String uri = request.getRoute(), httpMethod = request.getMethod();

		//		Object[] obj = getMethod(uri, httpMethod); // 返回两个对象
		//		Method method = (Method) obj[1];// 要执行的方法
		//
		//		if (method != null) {
		//			IController controller = (IController) obj[0];
		//			Object result;
		//
		//			ModelAndView model = null;
		//
		//			MvcRequest.setHttpServletRequest(request);
		//			MvcRequest.setHttpServletResponse(response);
		//
		//			if (method.getParameterTypes().length > 0) {
		//				Object[] args = RequestParam.getArgs(request, response, method);
		//				model = findModel(args);
		//
		//				// 调用反射的 Reflect.executeMethod 方法就可以执行目标方法，并返回一个结果。
		//				result = ExecuteMethod.executeMethod(controller, method, args);// 通过反射执行控制器方法
		//			} else {
		//				// 方法没有参数
		//				result = ExecuteMethod.executeMethod(controller, method);
		//			}
		//
		//			response.resultHandler(result, request, model);
		//			MvcRequest.clean();
		//
		//			return; // 终止当前 servlet 请求
		//		}
		//		//		else {
		//		//			LOGGER.info(httpMethod + uri + " 控制器没有这个方法！"); // Let it go, may be html/js/css/jpg..
		//		//		}

		chain.doFilter(req, resp);// 不要传 MvcRequest，以免入侵其他框架
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
	private static Method getMethod(Action action, String httpMethod) {
		if (action == null)
			throw new NullPointerException(" Action 对象不存在！");

		switch (httpMethod.toUpperCase()) {
		case "GET":
			return action.getMethod;
		case "POST":
			return action.postMethod;
		case "PUT":
			return action.putMethod;
		case "DELETE":
			return action.deleteMethod;
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

	@Override
	public void destroy() {
	}
}
