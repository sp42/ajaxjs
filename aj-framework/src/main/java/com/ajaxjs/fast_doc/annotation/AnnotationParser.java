package com.ajaxjs.fast_doc.annotation;

import java.lang.reflect.Method;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ValueConstants;

import com.ajaxjs.fast_doc.BeanParser;
import com.ajaxjs.fast_doc.Model.ArgInfo;
import com.ajaxjs.fast_doc.Model.ControllerInfo;
import com.ajaxjs.fast_doc.Model.Item;
import com.ajaxjs.fast_doc.Model.Return;
import com.ajaxjs.fast_doc.Util;
import com.ajaxjs.util.ReflectUtil;
import com.ajaxjs.util.StrUtil;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.spring.easy_controller.ControllerMethod;
import com.ajaxjs.spring.easy_controller.Example;
import com.ajaxjs.spring.easy_controller.PathForDoc;

/**
 * 基于注解的提取，以 Spring MVC 的为主
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public class AnnotationParser {
	private static final LogHelper LOGGER = LogHelper.getLog(AnnotationParser.class);

	/**
	 * 
	 * @param clz
	 */
	public AnnotationParser(Class<?> clz) {
		this.clz = clz;
	}

	/**
	 * 
	 */
	private Class<?> clz;

	/**
	 * 根 url，可以为空
	 */
	private String rootUrl;

	public ControllerInfo parser() {
		rootUrl = getRootUrl();

		ControllerInfo ci = new ControllerInfo();
		ci.name = clz.getSimpleName();
		ci.type = clz.getName();
		ci.items = parseControllerMethod();

		return ci;
	}

	/**
	 * 遍历每个方法
	 * 
	 * @return
	 */
	private List<Item> parseControllerMethod() {
		List<Item> list = Util.makeListByArray(clz.getDeclaredMethods(), method -> {
			Item item = new Item();

			getInfo(item, method);
			getReturnType(item, method);
			getArgs(item, method);

			return item;
		});

		return list.stream().sorted().collect(Collectors.toList());// 按 url 排序
	}

	/**
	 * 获取根 url
	 * 
	 * @return
	 */
	private String getRootUrl() {
		String rootUrl = null;
		RequestMapping rm = clz.getAnnotation(RequestMapping.class);

		if (rm != null && !ObjectUtils.isEmpty(rm.value()))
			rootUrl = rm.value()[0];
		else {
			PathForDoc pd = clz.getAnnotation(PathForDoc.class);
			if (pd != null)
				rootUrl = pd.value();
		}

		return rootUrl;
	}

	/**
	 * 获取方法的一些基本信息
	 * 
	 * @param item
	 * @param method
	 */
	private void getInfo(Item item, Method method) {
		item.methodName = method.getName();
		item.id = StrUtil.uuid(); // 雪花算法会重复，改用 uuid

		ControllerMethod cm = method.getAnnotation(ControllerMethod.class);

		if (cm != null) { // 方法的一些信息，读取注解
			item.name = cm.value();
			item.description = cm.descrition();
			item.image = cm.image();
		}

		// HTTP 方法
		GetMapping get = method.getAnnotation(GetMapping.class);
		if (get != null) {
			item.httpMethod = "GET";
			item.url = setUrl(get.value());
		}

		PostMapping post = method.getAnnotation(PostMapping.class);
		if (post != null) {
			item.httpMethod = "POST";
			item.url = setUrl(post.value());
		}

		PutMapping put = method.getAnnotation(PutMapping.class);
		if (put != null) {
			item.httpMethod = "PUT";
			item.url = setUrl(put.value());
		}

		DeleteMapping del = method.getAnnotation(DeleteMapping.class);
		if (del != null) {
			item.httpMethod = "DELETE";
			item.url = setUrl(del.value());
		}
	}

	/**
	 * 获取 URL。若注解上有则获取之，没有则表示是类定义的 rootUrl
	 * 
	 * @param arr
	 * @return
	 */
	private String setUrl(String[] arr) {
		if (!ObjectUtils.isEmpty(arr) && StringUtils.hasText(arr[0]))
			return StrUtil.concatUrl(rootUrl, arr[0]);

		return rootUrl;
	}

	/**
	 * 生成返回值信息
	 * 
	 * @param item
	 * @param method
	 */
	private void getReturnType(Item item, Method method) {
		Class<?> returnType = method.getReturnType();
		Return r = new Return();
		r.name = returnType.getSimpleName();
		r.type = returnType.getName();

		Example eg = method.getAnnotation(Example.class);
		if (eg != null)
			r.example = eg.value();

		if (Util.isSimpleValueType(returnType))
			r.isObject = false;
		else if (returnType == Map.class) {
			// TODO
		} else if (returnType == List.class || returnType == ArrayList.class || returnType == AbstractList.class) {
			r.isMany = true;

			Class<?> real = ReflectUtil.getGenericFirstReturnType(method);

			if (Util.isSimpleValueType(real))
				r.isObject = false;
			else {
				r.isObject = true;
				getBeanInfo(real, r);
			}
		} else if (returnType.isArray()) {
			r.isMany = true;
			// TODO
		} else { // it's single bean
			r.isMany = false;
			r.isObject = true;

			getBeanInfo(returnType, r);
		}

		item.returnValue = r;
	}

	/**
	 * 参数 入参
	 * 
	 * @param item
	 * @param method
	 */
	private void getArgs(Item item, Method method) {
		item.args = Util.makeListByArray(method.getParameters(), param -> {
			Class<?> clz = param.getType();

			ArgInfo arg = new ArgInfo();
			arg.name = param.getName();
			arg.type = clz.getSimpleName();

			RequestParam queryP = param.getAnnotation(RequestParam.class);

			if (queryP != null) {
				arg.position = "query";
				arg.isRequired = queryP.required();

				if (StringUtils.hasText(queryP.value()))
					arg.name = queryP.value();

				if (!queryP.defaultValue().equals(ValueConstants.DEFAULT_NONE))
					arg.defaultValue = queryP.defaultValue();

				return arg;
			}

			PathVariable pv = param.getAnnotation(PathVariable.class);

			if (pv != null) {
				arg.position = "path";
				arg.isRequired = true;

				if (StringUtils.hasText(pv.value()))
					arg.name = pv.value();

				return arg;
			}

			RequestBody rb = param.getAnnotation(RequestBody.class);
			if (rb != null) {
				arg.position = "body";
				arg.isRequired = rb.required();
			}

			Example eg = param.getAnnotation(Example.class);
			if (eg != null)
				arg.example = eg.value();

			LOGGER.info(">>>>>>>>>" + clz + !Util.isSimpleValueType(clz));

			if (!Util.isSimpleValueType(clz))
				arg.bean = BeanParser.getBeanInfo(clz, params);

			return arg;
		});
	}

}
