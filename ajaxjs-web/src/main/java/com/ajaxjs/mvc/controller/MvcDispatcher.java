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

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

import com.ajaxjs.config.ConfigService;
import com.ajaxjs.ioc.BeanContext;
import com.ajaxjs.mvc.Constant;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.filter.Authority;
import com.ajaxjs.mvc.filter.FilterAction;
import com.ajaxjs.mvc.filter.FilterAfterArgs;
import com.ajaxjs.mvc.filter.MvcFilter;
import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.util.ReflectUtil;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.util.map.JsonHelper;
import com.ajaxjs.util.map.MapTool;
import com.ajaxjs.web.ServletHelper;

/**
 * MVC 分发器，控制器核心类
 * 
 * @author sp42 frank@ajaxjs.com
 */
public class MvcDispatcher implements Filter {
	private static final LogHelper LOGGER = LogHelper.getLog(MvcDispatcher.class);

	private static final String t = "     ___       _       ___  __    __      _   _____        _          __  _____   _____  \n"
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

		MapTool.getValue(config, "doIoc", (String doIoc) -> {
			for (String packageName : CommonUtil.split(doIoc))
				BeanContext.init(packageName);

			BeanContext.injectBeans(); // 依赖注射扫描
		});

		MapTool.getValue(config, "controller", ControllerScanner::scannController);

		if (config != null && config.get("controller") == null)
			LOGGER.info("web.xml 没有配置 MVC 过滤器或者 配置没有定义 controller");
	}

	/**
	 * 虽然 REST 风格的 URL 一般不含后缀，我们只能将 DispatcherServlet 映射到“/”，使之变为一个默认的 Servlet， 在处理
	 * js/css 等静态文件有后缀，这样的话我们需要区分对待。
	 */
	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
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
				args = RequestParam.getArgs(request, response, method);
				model = findModel(args);
				// 通过反射执行控制器方法:调用反射的 Reflect.executeMethod 方法就可以执行目标方法，并返回一个结果。
			}

			if (isDoFilter) {
				for (FilterAction filterAction : filterActions) {
					isbeforeSkip = !filterAction.before(model, request, response, method, args); // 相当于 AOP 前置
					if (isbeforeSkip)
						break;
				}
			}

			if (!isbeforeSkip) {
				result = hasArgs ? ReflectUtil.executeMethod_Throwable(controller, method, args)
						: ReflectUtil.executeMethod_Throwable(controller, method);
			}

		} catch (Throwable e) {
			err = e;

			if (e instanceof IllegalArgumentException
					&& e.getMessage().contains("object is not an instance of declaring class"))
				LOGGER.warning("异常可能的原因：@Bean注解的名称重复，请检查 IOC 中的是否重名");
		}

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
	 * @param request
	 * @param response
	 * @param model
	 */
	private static void handleErr(Throwable err, Method method, MvcRequest request, MvcOutput response,
			ModelAndView model) {
		ReflectUtil.getUnderLayerErr(err).printStackTrace(); // 打印异常

		String errMsg = ReflectUtil.getUnderLayerErrMsg(err);
		Produces a = method.getAnnotation(Produces.class);

		if (a != null && MediaType.APPLICATION_JSON.equals(a.value()[0])) {// 返回 json
			response.resultHandler(String.format(Constant.json_not_ok, JsonHelper.jsonString_covernt(errMsg)), request,
					model, method);
		} else {
			if (err instanceof IllegalAccessError && ConfigService.getValueAsString("page.onNoLogin") != null) {
				response.resultHandler(
						"redirect::" + request.getContextPath() + ConfigService.getValueAsString("page.onNoLogin"),
						request, model, method);
			} else {
				request.setAttribute("javax.servlet.error.status_code", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				request.setAttribute("javax.servlet.error.exception_type", err.getClass());
				request.setAttribute("javax.servlet.error.exception", err);
				response.resultHandler("/WEB-INF/jsp/error.jsp", request, model, method);
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

	@Override
	public void destroy() {
	}
}
