/**
 * Copyright 2015 sp42 frank@ajaxjs.com
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
package com.ajaxjs.mvc.controller;

import java.beans.beancontext.BeanContext;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.ajaxjs.framework.Application;
import com.ajaxjs.framework.Component;
import com.ajaxjs.framework.config.ConfigService;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.MvcConstant;
import com.ajaxjs.mvc.filter.Authority;
import com.ajaxjs.mvc.filter.FilterAction;
import com.ajaxjs.mvc.filter.FilterAfterArgs;
import com.ajaxjs.mvc.filter.MvcFilter;
import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.util.ReflectUtil;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.util.map.JsonHelper;
import com.ajaxjs.web.secuity.SecurityRequest;
import com.ajaxjs.web.secuity.SecurityResponse;

/**
 * MVC 分发器，控制器核心类
 * 
 * @author sp42 frank@ajaxjs.com
 */
public class MvcDispatcher implements Component {
	private static final LogHelper LOGGER = LogHelper.getLog(MvcDispatcher.class);

	{
		LOGGER.infoYellow("\n     ___       _       ___  __    __      _   _____        _          __  _____   _____  \n"
				+ "     /   |     | |     /   | \\ \\  / /     | | /  ___/      | |        / / | ____| |  _  \\ \n"
				+ "    / /| |     | |    / /| |  \\ \\/ /      | | | |___       | |  __   / /  | |__   | |_| |  \n"
				+ "   / / | |  _  | |   / / | |   }  {    _  | | \\___  \\      | | /  | / /   |  __|  |  _  {  \n"
				+ "  / /  | | | |_| |  / /  | |  / /\\ \\  | |_| |  ___| |      | |/   |/ /    | |___  | |_| |  \n"
				+ " /_/   |_| \\_____/ /_/   |_| /_/  \\_\\ \\_____/ /_____/      |___/|___/     |_____| |_____/ \n");
	}

	/**
	 * 初始化这个过滤器
	 * 
	 *
	 */
	private static final Consumer<ServletContext> init = ctx -> {
		String[] packageNames = { "com.ajaxjs", "com.ibm" };
//		String[] packageNames = { "com.ajaxjs.user", "com.ajaxjs.shop", "com.ajaxjs.website", "com.ajaxjs.app", "com.ajaxjs.weixin", "com.ibm" };

		for (String packageName : packageNames)
			BeanContext.init(packageName);

		BeanContext.injectBeans(); // 依赖注射扫描

		ControllerScanner scanner;// 定义一个扫描器，专门扫描 IController
		for (String packageName : packageNames) {
			scanner = new ControllerScanner();
			Set<Class<IController>> IControllers = scanner.scan(packageName);

			for (Class<IController> clz : IControllers)
				ControllerScanner.add(clz);
		}

//		if (config != null && config.get("controller") == null)
//			LOGGER.info("web.xml 没有配置 MVC 过滤器或者 配置没有定义 controller");
	};

	private static Boolean isEnableSecurityIO;

	private static final BiFunction<HttpServletRequest, HttpServletResponse, Boolean> dispatcher = (req, resp) -> {
		req.setAttribute("requestTimeRecorder", System.currentTimeMillis()); // 每次 servlet 都会执行的记录时间

		if (isEnableSecurityIO == null)
			isEnableSecurityIO = ConfigService.getValueAsBool("security.isEnableSecurityIO");

		MvcRequest request = new MvcRequest(isEnableSecurityIO ? new SecurityRequest(req) : req);
		MvcOutput response = new MvcOutput(isEnableSecurityIO ? new SecurityResponse(resp) : resp);

		String uri = request.getFolder(), httpMethod = request.getMethod();
		Action action = null;

		try {
			action = IController.findTreeByPath(uri);
		} catch (Throwable e) {
			LOGGER.warning(e);
		}

		if (action != null) {
			Method method = action.getMethod(httpMethod);// 要执行的方法
			IController controller = action.getController(httpMethod);
			LOGGER.info("uri: {0}, action: {1}, method: {2}", uri, action, method);

			if (method != null && controller != null) {
				execute(request, response, controller, method);
				return false; // 终止当前 servlet 请求
			} else {
//				LOGGER.info("{0} {1} 控制器没有这个方法！", httpMethod, request.getRequestURI());
			}
		}

		return true;
	};

	static {
		Application.onServletStartUp.add(init);
		Application.onRequest.add(1, dispatcher);
	}

	/**
	 * 虽然 REST 风格的 URL 一般不含后缀，我们只能将 DispatcherServlet 映射到“/”，使之变为一个默认的 Servlet， 在处理
	 * js/css 等静态文件有后缀，这样的话我们需要区分对待。
	 */
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest _request = (HttpServletRequest) req;
		HttpServletResponse _response = (HttpServletResponse) resp;

//		if (ServletHelper.isStaticAsset(_request.getRequestURI())) {
//			chain.doFilter(req, resp);
//			return;
//		}

		_request.setAttribute("requestTimeRecorder", System.currentTimeMillis()); // 每次 servlet 都会执行的记录时间
		boolean isEnableSecurityIO = ConfigService.getValueAsBool("security.isEnableSecurityIO");
		MvcRequest request = new MvcRequest(isEnableSecurityIO ? new SecurityRequest(_request) : _request);
		MvcOutput response = new MvcOutput(isEnableSecurityIO ? new SecurityResponse(_response) : _response);

		String uri = request.getFolder(), httpMethod = request.getMethod();
		Action action = null;

		try {
			action = IController.findTreeByPath(uri);
		} catch (Throwable e) {
			LOGGER.warning(e);
		}

		if (action != null) {
			Method method = action.getMethod(httpMethod);// 要执行的方法
			IController controller = action.getController(httpMethod);
//			LOGGER.info("uri: {0}, action: {1}, method: {2}", uri, action, method);

			if (method != null && controller != null) {
				execute(request, response, controller, method);
				return; // 终止当前 servlet 请求
			} else {
//				LOGGER.info("{0} {1} 控制器没有这个方法！", httpMethod, request.getRequestURI());
			}
		}

		chain.doFilter(req, resp);// 不用传 MvcRequest，以免入侵其他框架
	}

	/**
	 * 执行控制器方法
	 * 
	 * @param request    请求对象
	 * @param response   响应对象
	 * @param controller 控制器对象
	 * @param method     控制器方法
	 */
	private static void execute(MvcRequest request, MvcOutput response, IController controller, Method method) {
		MvcRequest.setHttpServletRequest(request);
		MvcRequest.setHttpServletResponse(response);

		Throwable err = null; // 收集错误信息
		Object result = null;
		ModelAndView model = null;

		FilterAction[] filterActions = getFilterActions(method);
		boolean isDoFilter = !CommonUtil.isNull(filterActions), isbeforeSkip = false; // 是否中止控制器方法调用，由拦截器决定
		Object[] args = null;// 方法没有参数
		boolean hasArgs = method.getParameterTypes().length > 0;

		try {
			if (hasArgs) {
				args = RequestParam.getArgs(controller, request, response, method);
				model = findModel(args);
				// 通过反射执行控制器方法:调用反射的 Reflect.executeMethod 方法就可以执行目标方法，并返回一个结果。
			}

			if (isDoFilter) {
				if (model == null) // 如果方法没参数则不会出现 mv。这里补一个
					model = new ModelAndView();

				for (FilterAction filterAction : filterActions) {
					isbeforeSkip = !filterAction.before(model, request, response, method, args); // 相当于 AOP 前置
					if (isbeforeSkip)
						break;
				}
			}

			if (!isbeforeSkip) {
				result = hasArgs ? ReflectUtil.executeMethod_Throwable(controller, method, args) : ReflectUtil.executeMethod_Throwable(controller, method);
			}

		} catch (Throwable e) {
			err = e;

			if (e instanceof IllegalArgumentException && e.getMessage().contains("object is not an instance of declaring class"))
				LOGGER.warning("异常可能的原因：@Bean注解的名称重复，请检查 IOC 中的是否重名");
		}

		if (model != null)
			request.saveToReuqest(model);

		boolean isDoOldReturn = true; // 是否执行默认的处理 response 方法

		if (isDoFilter) {
			FilterAfterArgs argsHolder = new FilterAfterArgs();
			argsHolder.model = model;
			argsHolder.result = result;
			argsHolder.method = method;
			argsHolder.err = err;
			argsHolder.request = request;
			argsHolder.response = response;
			argsHolder.isbeforeSkip = isbeforeSkip;

			try { // 一有异常则退出，未执行的 afterFilter 都不执行了
				for (FilterAction filterAction : filterActions) {
					isDoOldReturn = filterAction.after(argsHolder); // 后置调用

					if (argsHolder.isAfterSkip)
						break;
				}
			} catch (Throwable e) {
				err = e; // 让异常统一处理
			}
		}

		if (err != null) { // 有未处理的异常
			handleErr(err, method, request, response, model);
		} else if (!isbeforeSkip) {// 前置既然不执行了，后续的当然也不执行
			if (isDoOldReturn)
				response.resultHandler(result, request, model, method);
		} else {
			LOGGER.warning("一般情况下不应执行到这一步。Should not be executed in this step.");
		}

		MvcRequest.clean();
	}

	/**
	 * 
	 * @param err
	 * @param method
	 * @param r
	 * @param response
	 * @param model
	 */
	private static void handleErr(Throwable err, Method method, MvcRequest r, MvcOutput response, ModelAndView model) {
		Throwable _err = ReflectUtil.getUnderLayerErr(err);

		if (model != null && model.containsKey(FilterAction.NOT_LOG_EXCEPTION) && ((boolean) model.get(FilterAction.NOT_LOG_EXCEPTION))) {
			_err.printStackTrace(); // 打印异常
		} else
			LOGGER.warning(_err);

		String errMsg = ReflectUtil.getUnderLayerErrMsg(err);
		Produces a = method.getAnnotation(Produces.class);

		if (a != null && MediaType.APPLICATION_JSON.equals(a.value()[0])) {// 返回 json
			response.resultHandler(String.format(MvcConstant.JSON_NOT_OK, JsonHelper.jsonString_covernt(errMsg)), r, model, method);
		} else {
			if (err instanceof IllegalAccessError && ConfigService.getValueAsString("page.onNoLogin") != null) {
				// 没有权限时跳转的地方
				model.put("title", "非法请求");
				model.put("msg", errMsg);
				response.resultHandler("/WEB-INF/jsp/pages/msg.jsp", r, model, method);
//				response.resultHandler("redirect::" + r.getContextPath() + ConfigService.getValueAsString("page.onNoLogin"), r, model,
//						method);
			} else {
				r.setAttribute("javax.servlet.error.status_code", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				r.setAttribute("javax.servlet.error.exception_type", err.getClass());
				r.setAttribute("javax.servlet.error.exception", err);
				response.resultHandler("/WEB-INF/jsp/error.jsp", r, model, method);
			}
//			response.resultHandler(String.format("redirect::%s/showMsg?msg=%s", request.getContextPath(), Encode.urlEncode(errMsg)), request, model, method);
		}
	}

	/**
	 * 初始化拦截器 TODO 改为 IOC 更节省资源
	 * 
	 * @param method
	 * @return
	 */
	private static FilterAction[] getFilterActions(Method method) {
		List<FilterAction> list = new ArrayList<>();// 拦截器

		if (method.getAnnotation(MvcFilter.class) != null) {
			Class<? extends FilterAction>[] clzs = method.getAnnotation(MvcFilter.class).filters();

			for (Class<? extends FilterAction> clz : clzs) {
				list.add(ReflectUtil.newInstance(clz));
			}
		}

		if (method.getAnnotation(Authority.class) != null) {
			Authority a = method.getAnnotation(Authority.class);
			Class<? extends FilterAction> clz = a.filter();
			list.add(ReflectUtil.newInstance(clz, a.value()));
		}

		return list.toArray(new FilterAction[list.size()]);
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
}
