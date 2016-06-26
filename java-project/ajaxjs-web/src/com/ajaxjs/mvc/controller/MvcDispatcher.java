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

import com.ajaxjs.mvc.AnnotationUtils;
import com.ajaxjs.mvc.ActionAndView;
import com.ajaxjs.util.LogHelper;
import com.ajaxjs.util.Reflect;
import com.ajaxjs.web.Requester;
import com.ajaxjs.web.Responser;
import com.ajaxjs.web.ServletPatch;

/**
 * MVC框架的核心是一个 Dispatcher，用于接收所有的HTTP请求，并根据URL选择合适的Action对其进行处理
 * @author frank
 *
 */
public class MvcDispatcher implements Filter {
	private static final LogHelper LOGGER = LogHelper.getLog(MvcDispatcher.class);

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
		Requester request = new Requester(req);
		Responser response = new Responser(resp);
		response.setRequest(request);
		
		String uri = request.getRoute(), httpMethod = request.getMethod();
		
		Object[] obj = getMethod(uri, httpMethod); // 返回两个对象
		Method method = (Method)obj[1];// 要执行的方法
		
		if(method == null) {
			@SuppressWarnings("unused")
			String msg = httpMethod + uri + " 控制器没有这个方法！";// Let go
		} else {
			IController controller = (IController)obj[0];
			Object result;
			
            if(method.getParameterTypes().length > 0){
            	Object[] agrs = getArgs(request, response, method);
            	result = Reflect.executeMethod(controller, method, agrs);// 通过反射执行方法
            }else {
            	// 方法没有参数
            	result = Reflect.executeMethod(controller, method);
            }
            
            
            resultHandler(result, response);
			
            return;
		}

		chain.doFilter(request, response);
	}

	private static void resultHandler(Object result, Responser response) {
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
	 * @param response
	 * @param method
	 * @return
	 */
	private static Object[] getArgs(Requester request, Responser response, Method method) {
		ArrayList<Object> args = new ArrayList<>();// 参数列表
		
		Class<?>[] parmTypes = method.getParameterTypes();
		for (Class<?> clazz : parmTypes) {
			if (clazz.equals(HttpServletRequest.class)) {
				args.add(request);
			}
			if (clazz.equals(Requester.class)) {
				args.add(request);
			}
			if (clazz.equals(HttpServletResponse.class)) {
				args.add(response);
			}
			if (clazz.equals(Responser.class)) {
				args.add(response);
			}
		}
		
		return args.toArray();
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
			if (uri.startsWith(path)) {
				LOGGER.info(path + "命中！！！");
				
				ActionAndView controllerInfo = AnnotationUtils.controllers.get(path);
				objs[0] = controllerInfo.controller; // 返回 controller
				
				String subUri = uri.replace(path, "");
				
				boolean isSub = false; // 如果执行了 sub 就不用执行类 path 本身的 
				
				System.out.println("----"+path);
				for(String subPath : controllerInfo.subPath.keySet()) {
					if(subUri.startsWith("/" + subPath)) {
						method = getMethod(controllerInfo.subPath.get(subPath), httpMethod);
						break;
					}
				}
				
				if(!isSub) {
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
	@SuppressWarnings("unchecked")
	@Override
	public void init(FilterConfig fConfig) throws ServletException {
		Map<String, String> config = ServletPatch.parseInitParams(null, fConfig);// 获取 web.xml 配置
		
		if(config != null && config.get("controller") != null) {
			String str = config.get("controller");
			try {
				System.out.println(str);
				for(String controllerClz : str.split(",")) {
					AnnotationUtils.scan((Class<? extends IController>)Class.forName(controllerClz));
				}
			} catch (ClassNotFoundException e) {
				LOGGER.warning(e);
			}
		} else {
			LOGGER.warning("web.xml 没有配置 MVC 过滤器或者 配置没有定义 controller");
		}

	}

	@Override
	public void destroy() {}
}
