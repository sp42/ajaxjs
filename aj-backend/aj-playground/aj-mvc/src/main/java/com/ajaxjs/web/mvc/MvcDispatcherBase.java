package com.ajaxjs.web.mvc;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.ajaxjs.framework.GetConfig;
import com.ajaxjs.framework.config.ConfigService;
import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.util.ReflectUtil;
import com.ajaxjs.util.ioc.ComponentMgr;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.util.map.JsonHelper;
import com.ajaxjs.web.mvc.filter.Authority;
import com.ajaxjs.web.mvc.filter.FilterAction;
import com.ajaxjs.web.mvc.filter.FilterAfterArgs;
import com.ajaxjs.web.mvc.filter.FilterContext;
import com.ajaxjs.web.mvc.filter.MvcFilter;
import com.ajaxjs.web.secuity.SecurityRequest;
import com.ajaxjs.web.secuity.SecurityResponse;

public class MvcDispatcherBase {
	private static final LogHelper LOGGER = LogHelper.getLog(MvcDispatcherBase.class);

	/**
	 * 初始化这个过滤器
	 * 
	 * @param ctx
	 */
	@SuppressWarnings("unchecked")
	public static void init(ServletContext ctx) {
		for (Class<?> clz : ComponentMgr.clzs) {
			if (IController.class.isAssignableFrom(clz)) {
				add((Class<? extends IController>) clz);// 添加到集合中去
			}
		}
	};

	private static Boolean isEnableSecurityIO;

	public static final BiFunction<HttpServletRequest, HttpServletResponse, Boolean> dispatcher = (req, resp) -> {
		if (isEnableSecurityIO == null)
			isEnableSecurityIO = "true".equals(ComponentMgr.getByInterface(GetConfig.class).getString("security.isEnableSecurityIO"));

		MvcRequest request = new MvcRequest(isEnableSecurityIO ? new SecurityRequest(req) : req);
		MvcOutput response = new MvcOutput(isEnableSecurityIO ? new SecurityResponse(resp) : resp);

		String uri = request.getFolder(), httpMethod = request.getMethod();
		Action action = null;

//		LOGGER.info("uri: {0}", uri);
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
				return false; // 终止当前 servlet 请求
			} else {
//				LOGGER.info("{0} {1} 控制器没有这个方法！", httpMethod, request.getRequestURI());
			}
		} else {
		}

		return true;
	};

	/**
	 * 虽然 REST 风格的 URL 一般不含后缀，我们只能将 DispatcherServlet 映射到“/”，使之变为一个默认的 Servlet， 在处理
	 * js/css 等静态文件有后缀，这样的话我们需要区分对待。
	 */

	/**
	 * 解析一个控制器。Parsing a Controller class.
	 * 
	 * @param clz 控制器类 a Controller class
	 */
	public static void add(Class<? extends IController> clz) {
		if (Modifier.isAbstract(clz.getModifiers())) // 忽略抽象类
			return;

		String topPath = Action.getRootPath(clz);
		if (CommonUtil.isEmptyString(topPath))
			return;

//		LOGGER.info("正在解析 [{0}]控制器", topPath);
		Action action = IController.findTreeByPath(IController.urlMappingTree, topPath, "", true);
		action.createControllerInstance(clz);
		action.parseMethod();

		// 会打印控制器的总路径信息，不会打印各个方法的路径，那太细了，日志也会相应地多
		// LOGGER.info("控制器已登记成功！The controller [{0}] (\"/{1}\") was parsed and
		// registered", clz.toString().replaceAll("class\\s", ""), topPath); // 控制器 {0}
		// 所有路径（包括子路径）注册成功！
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

				FilterContext ctx = new FilterContext();
				ctx.model = model;
				ctx.request = request;
				ctx.response = response;
				ctx.method = method;
				ctx.args = args;

				for (FilterAction filterAction : filterActions) {
					isbeforeSkip = !filterAction.before(ctx); // 相当于 AOP 前置
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
		} else
			LOGGER.warning("一般情况下不应执行到这一步。Should not be executed in this step.");

		MvcRequest.clean();
	}

	/**
	 * 
	 * @param err
	 * @param method
	 * @param req
	 * @param response
	 * @param model
	 */
	private static void handleErr(Throwable err, Method method, MvcRequest req, MvcOutput response, ModelAndView model) {
		Throwable _err = ReflectUtil.getUnderLayerErr(err);

		if (model != null && model.containsKey(FilterAction.NOT_LOG_EXCEPTION) && ((boolean) model.get(FilterAction.NOT_LOG_EXCEPTION))) {
			_err.printStackTrace(); // 打印异常
		} else
			LOGGER.warning(_err);

		String errMsg = ReflectUtil.getUnderLayerErrMsg(err);
		Produces a = method.getAnnotation(Produces.class);

		if (a != null && MediaType.APPLICATION_JSON.equals(a.value()[0])) {// 返回 json
			response.resultHandler(String.format(MvcConstant.JSON_NOT_OK, JsonHelper.jsonString_covernt(errMsg)), req, model, method);
		} else {
			if (err instanceof IllegalAccessError && ConfigService.getValueAsString("page.onNoLogin") != null) {
				// 没有权限时跳转的地方
				model.put("title", "非法请求");
				model.put("msg", errMsg);
				model.put("redirect", req.getContextPath() + "/user/login/");
				req.saveToReuqest(model);
				response.resultHandler("/WEB-INF/jsp/pages/msg.jsp", req, model, method);
//				response.resultHandler("redirect::" + r.getContextPath() + ConfigService.getValueAsString("page.onNoLogin"), r, model,
//						method);
			} else {
				req.setAttribute("javax.servlet.error.status_code", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				req.setAttribute("javax.servlet.error.exception_type", err.getClass());
				req.setAttribute("javax.servlet.error.exception", err);
				response.resultHandler("/WEB-INF/jsp/error.jsp", req, model, method);
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
