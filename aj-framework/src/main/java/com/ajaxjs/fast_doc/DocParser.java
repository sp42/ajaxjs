package com.ajaxjs.fast_doc;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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

import com.ajaxjs.spring.easy_controller.ControllerMethod;
import com.ajaxjs.spring.easy_controller.Example;
import com.ajaxjs.spring.easy_controller.Info;
import com.ajaxjs.spring.easy_controller.PathForDoc;
import com.ajaxjs.util.ReflectUtil;
import com.ajaxjs.util.StrUtil;
import com.ajaxjs.util.logger.LogHelper;

/**
 * 文档解析器
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public class DocParser implements Model {
	private static final LogHelper LOGGER = LogHelper.getLog(DocParser.class);

	/**
	 * 参数
	 */
	private Params params;

	public DocParser(Params params) {
		this.params = params;
	}

	/**
	 * 解析器入口
	 * 
	 * @param params 参数配置
	 * @param clzs   控制器类
	 * @return 解析结果，key 是类的名称
	 */
	public static Map<String, ControllerInfo> run(Params params, Class<?>... clzs) {
		LOGGER.info("Fast Doc's getting started.");
		Map<String, ControllerInfo> map = new HashMap<>();
		DocParser docParser = new DocParser(params);

		for (Class<?> clz : clzs) {
			ControllerInfo ci = new ControllerInfo();
			ci.name = clz.getSimpleName();
			ci.type = clz.getName();
			ci.items = docParser.parse(clz);

			Info info = clz.getAnnotation(Info.class);
			if (info != null)
				ci.description = info.value();

			map.put(ci.name, ci);
		}

		return map;
	}

	/**
	 * 解析一个接口类
	 * 
	 * @param clz
	 * @return
	 */
	public List<Item> parse(Class<?> clz) {
		String rootUrl = null;// 根 url，可以为空
		RequestMapping rm = clz.getAnnotation(RequestMapping.class);

		if (rm != null && !ObjectUtils.isEmpty(rm.value()))
			rootUrl = rm.value()[0];
		else {
			PathForDoc pd = clz.getAnnotation(PathForDoc.class);
			if (pd != null)
				rootUrl = pd.value();
		}

		final String _rootUrl = rootUrl;

		// 遍历每个方法
		List<Item> list = Util.makeListByArray(clz.getDeclaredMethods(), method -> {
			Item item = new Item();

			getInfo(item, method, _rootUrl);
			getReturnType(item, method);
			getArgs(item, method);

			return item;
		});

		list = list.stream().sorted().collect(Collectors.toList());// 按 url 排序

		return list;
	}

	/**
	 * 获取方法的一些基本信息
	 * 
	 * @param item
	 * @param method
	 * @param rootUrl
	 */
	private void getInfo(Item item, Method method, String rootUrl) {
		item.methodName = method.getName();
		item.id = UUID.randomUUID().toString(); // 雪花算法会重复，改用 uuid

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
			item.url = setUrl(get.value(), rootUrl);
		}

		PostMapping post = method.getAnnotation(PostMapping.class);
		if (post != null) {
			item.httpMethod = "POST";
			item.url = setUrl(post.value(), rootUrl);
		}

		PutMapping put = method.getAnnotation(PutMapping.class);
		if (put != null) {
			item.httpMethod = "PUT";
			item.url = setUrl(put.value(), rootUrl);
		}

		DeleteMapping del = method.getAnnotation(DeleteMapping.class);
		if (del != null) {
			item.httpMethod = "DELETE";
			item.url = setUrl(del.value(), rootUrl);
		}
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

	/**
	 * 获取 URL。若注解上有则获取之，没有则表示是类定义的 rootUrl
	 * 
	 * @param arr
	 * @param rootUrl
	 * @return
	 */
	private String setUrl(String[] arr, String rootUrl) {
		if (!ObjectUtils.isEmpty(arr) && StringUtils.hasText(arr[0])) {

			String s = StrUtil.concatUrl(rootUrl, arr[0]);
			return s;
		}

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

		if (Util.isSimpleValueType(returnType)) {
			r.isObject = false;
		} else if (returnType == Map.class) {
			// TODO
		} else if (returnType == List.class || returnType == ArrayList.class || returnType == AbstractList.class) {
			r.isMany = true;

			Type[] _real = ReflectUtil.getGenericReturnType(method);
			Class<?> real = ReflectUtil.type2class(_real[0]);

			if (!Util.isSimpleValueType(real)) {
				r.isObject = true;
				getBeanInfo(real, r);
			} else
				r.isObject = false;
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

	private void getBeanInfo(Class<?> clz, Return r) {
		BeanInfo bean = BeanParser.getBeanInfo(clz, params);
		if (bean != null) {

			r.name = bean.name;
			r.comment = bean.description;
			r.values = bean.values;
			r.beans = bean.beans;
		}
	}
}
