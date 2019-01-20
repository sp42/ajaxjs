/**
 * Copyright 2015 sp42 frank@ajaxjs.com
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
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.ajaxjs.ioc.BeanContext;
import com.ajaxjs.keyvalue.MapHelper;
import com.ajaxjs.keyvalue.MappingHelper;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.filter.FilterAction;
import com.ajaxjs.mvc.filter.MvcFilter;
import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.util.Encode;
import com.ajaxjs.util.ReflectUtil;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.web.ServletHelper;

/**
 * MVC 分发器，控制器核心类
 * 
 * @author Sp42 frank@ajaxjs.com
 */
public class MvcDispatcher implements Filter {
	private static final LogHelper LOGGER = LogHelper.getLog(MvcDispatcher.class);

	private static final String t = 
			  "     ___       _       ___  __    __      _   _____        _          __  _____   _____  \n"
			+ "     /   |     | |     /   | \\ \\  / /     | | /  ___/      | |        / / | ____| |  _  \\ \n" 
			+ "    / /| |     | |    / /| |  \\ \\/ /      | | | |___       | |  __   / /  | |__   | |_| |  \n"
			+ "   / / | |  _  | |   / / | |   }  {    _  | | \\___  \\      | | /  | / /   |  __|  |  _  {  \n" 
			+ "  / /  | | | |_| |  / /  | |  / /\\ \\  | |_| |  ___| |      | |/   |/ /    | |___  | |_| |  \n"
			+ " /_/   |_| \\_____/ /_/   |_| /_/  \\_\\ \\_____/ /_____/      |___/|___/     |_____| |_____/ \n";

	{
		LOGGER.infoYellow("\n" + t);
	}

	/**
	 * 初始化这个过滤器
	 * 
	 * @param _config 过滤器配置，web.xml 中定义
	 */
	@Override
	public void init(FilterConfig _config) {
		// 读取 web.xml 配置，如果有 controller 那一项就获取指定包里面的内容，看是否有属于 IController 接口的控制器，有就加入到
		// AnnotationUtils.controllers 集合中
		Map<String, String> config = ServletHelper.initFilterConfig2Map(_config);

		MapHelper.getValue(config, "doIoc", (String doIoc) -> {
			for (String packageName : CommonUtil.split(doIoc))
				BeanContext.init(packageName);

			BeanContext.injectBeans(); // 依赖注射扫描
		});

		MapHelper.getValue(config, "controller", ControllerScanner::scannController);

		if (config != null && config.get("controller") == null)
			LOGGER.info("web.xml 没有配置 MVC 过滤器或者 配置没有定义 controller");
	}
 

	/**
	 * 虽然 REST 风格的 URL 一般不含后缀，我们只能将 DispatcherServlet 映射到“/”，使之变为一个默认的 Servlet， 在处理
	 * js/css 等静态文件有后缀，这样的话我们需要区分对待。
	 */
	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest _request = (HttpServletRequest) req;
		HttpServletResponse _response = (HttpServletResponse) resp;

		if (ServletHelper.isStaticAsset(_request.getRequestURI())) {
			chain.doFilter(req, resp);
			return;
		}

		_request.setAttribute("requestTimeRecorder", System.currentTimeMillis()); // 每次 servlet 都会执行的。记录时间

		MvcRequest request = new MvcRequest(_request);
		MvcOutput response = new MvcOutput(_response);

		String uri = request.getFolder(), httpMethod = request.getMethod();

//		System.out.println(">>>>>>>" + uri);
		Matcher match = id.matcher(uri);
		if (match.find()) {
			uri = match.replaceAll("/{id}");
		}

		Action action = ControllerScanner.find(uri);

		if (action != null) {
			Method method = getMethod(action, httpMethod);// 要执行的方法
			IController controller = getController(action, httpMethod);
//			System.out.println(">>>>>>>" + action);
//			System.out.println(">>>>>>>" + method);

			if (method != null && controller != null) {
				dispatch(request, response, controller, method);
				return; // 终止当前 servlet 请求
			} else {
//				LOGGER.info("{0} {1} 控制器没有这个方法！", httpMethod, request.getRequestURI());
			}
		}

		chain.doFilter(req, resp);// 不用传 MvcRequest，以免入侵其他框架
	}

	/**
	 * 分发
	 * 
	 * @param request
	 * @param response
	 * @param controller
	 * @param method
	 */
	private static void dispatch(MvcRequest request, MvcOutput response, IController controller, Method method) {
		MvcRequest.setHttpServletRequest(request);
		MvcRequest.setHttpServletResponse(response);

		Throwable err = null; // 收集错误信息
		Object result = null;
		ModelAndView model = null;

		FilterAction[] filterActions = getFilterActions(method);
		boolean isDoFilter = !CommonUtil.isNull(filterActions), isSkip = false; // 是否中止控制器方法调用，由拦截器决定

		try {
			if (isDoFilter) {
				for (FilterAction filterAction : filterActions) {
					isSkip = !filterAction.before(request, response, method); // 相当于 AOP 前置
					if (isSkip)
						break;
				}
			}

			if (!isSkip) {
				if (method.getParameterTypes().length > 0) {
					Object[] args = RequestParam.getArgs(request, response, method);
					model = findModel(args);
					// 通过反射执行控制器方法:调用反射的 Reflect.executeMethod 方法就可以执行目标方法，并返回一个结果。
					
					System.out.println(Arrays.toString(args));
					System.out.println(method);
					result = ReflectUtil.executeMethod_Throwable(controller, method, args);
				} else {
					result = ReflectUtil.executeMethod_Throwable(controller, method);// 方法没有参数
				}
			}

		} catch (Throwable e) {
			err = e;

			if (e instanceof IllegalArgumentException && e.getMessage().contains("object is not an instance of declaring class")) {
				LOGGER.warning("异常可能的原因：@Bean注解的名称重复，请检查 IOC 中的是否重名");
			}
		} finally {
			if (isDoFilter) {
				for (FilterAction filterAction : filterActions)
					filterAction.after(request, response, method, isSkip); // 后置调用
			}
		}

		if (err != null) { // 有未处理的异常
			handleErr(err, method, request, response, model);
		} else if (!isSkip) {
			response.resultHandler(result, request, model, method);
		} else {
			LOGGER.warning("一般情况下不应执行到这一步。Should not be executed in this step.");
		}

		MvcRequest.clean();
	}

	private static void handleErr(Throwable err, Method method, MvcRequest request, MvcOutput response, ModelAndView model) {
		ReflectUtil.getUnderLayerErr(err).printStackTrace(); // 打印异常

		String errMsg = ReflectUtil.getUnderLayerErrMsg(err);
		Produces a = method.getAnnotation(Produces.class);

		if (a != null && MediaType.APPLICATION_JSON.equals(a.value()[0])) {// 返回 json
			response.resultHandler(String.format(MappingHelper.json_not_ok, errMsg), request, model, method);
		} else {
			response.resultHandler(String.format("redirect::%s/showMsg?msg=%s", request.getContextPath(), Encode.urlEncode((errMsg))), request, model, method);
		}
	}

	/**
	 * 初始化拦截器 TODO 改为 IOC 更节省资源
	 * 
	 * @param method
	 * @return
	 */
	private static FilterAction[] getFilterActions(Method method) {
		FilterAction[] filterActions = null; // 拦截器

		if (method.getAnnotation(MvcFilter.class) != null) {
			Class<? extends FilterAction>[] clzs = method.getAnnotation(MvcFilter.class).filters();
			filterActions = new FilterAction[clzs.length];

			int i = 0;
			for (Class<? extends FilterAction> clz : clzs) {
				filterActions[i++] = ReflectUtil.newInstance(clz);
			}
		}

		return filterActions;
	}

	private static final Pattern id = Pattern.compile("/\\d+");

	/**
	 * 根据 httpMethod 请求方法返回控制器类身上的方法。
	 * 
	 * @param controllerInfo 控制器类信息
	 * @param httpMethod HTTP 请求的方法
	 * @return 控制器方法
	 */
	private static Method getMethod(Action action, String httpMethod) {
		Objects.requireNonNull(action, "Action 对象不存在！");

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
	 * 根据 httpMethod 请求方法返回控制器
	 * 
	 * @param action
	 * @param httpMethod HTTP 请求的方法
	 * @return 控制器
	 */
	private static IController getController(Action action, String httpMethod) {
		switch (httpMethod.toUpperCase()) {
		case "GET":
			return action.getMethodController;
		case "POST":
			return action.postMethodController;
		case "PUT":
			return action.putMethodController;
		case "DELETE":
			return action.deleteMethodController;
		default:
			return action.controller;
		}
	}

	/**
	 * 查找是 ModelAndView 类型的参数，返回之
	 * 
	 * @param args 参数列表
	 * @return ModelAndView
	 */
	private static ModelAndView findModel(Object[] args) {
		Optional<Object> mv = Arrays.stream(args).filter(obj -> obj instanceof ModelAndView).findAny();
		return mv.isPresent() ? (ModelAndView) mv.get() : null;
	}

	@Override
	public void destroy() {
	}
}
