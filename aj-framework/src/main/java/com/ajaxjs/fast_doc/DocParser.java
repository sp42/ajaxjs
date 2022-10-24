package com.ajaxjs.fast_doc;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ValueConstants;

import com.ajaxjs.fast_doc.Doclet.Params;
import com.ajaxjs.spring.easy_controller.ControllerMethod;
import com.ajaxjs.spring.easy_controller.Example;
import com.ajaxjs.util.ReflectUtil;
import com.ajaxjs.util.StrUtil;
import com.ajaxjs.util.logger.LogHelper;

public class DocParser implements Model {
	private static final LogHelper LOGGER = LogHelper.getLog(DocParser.class);

	private Params params;

	public DocParser(Params params) {
		this.params = params;
	}

	public List<Item> parse(Class<?> clz) {
		RequestMapping rm = clz.getAnnotation(RequestMapping.class);
		String rootUrl = rm.value()[0];

		List<Item> list = Util.makeListByArray(clz.getDeclaredMethods(), method -> {
			Item item = new Item();
			item.methodName = method.getName();
//			item.id = SnowflakeId.get();
//			item.id = new IdWorker(1, 1, 1).nextId();
			item.id = UUID.randomUUID().toString();

			ControllerMethod cm = method.getAnnotation(ControllerMethod.class);
			if (cm != null) {
				item.name = cm.value();

				if (cm.descrition() != null)
					item.description = cm.descrition();

				if (cm.image() != null)
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

			getReturnType(item, method);

			// 参数 入参
			item.args = Util.makeListByArray(method.getParameters(), param -> {
				Arg arg = new Arg();
				arg.name = param.getName();
				arg.type = param.getType().getSimpleName();

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

				return arg;
			});
			
			LOGGER.info(">>item.description>>>>>>" + item.description);

			return item;
		});

		return list;
	}

	/**
	 * 获取 URL。若注解上有则获取之，没有则表示是类定义的 rootUrl
	 * 
	 * @param arr
	 * @param rootUrl
	 * @return
	 */
	private String setUrl(String[] arr, String rootUrl) {
		if (!ObjectUtils.isEmpty(arr) && StringUtils.hasText(arr[0]))
			return arr[0];

		return rootUrl;
	}

	public static Map<String, BeanInfo> CACHE = new HashMap<>();

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
		} else { // it's single bean
			r.isMany = false;
			r.isObject = true;

			getBeanInfo(returnType, r);
		}

		item.returnValue = r;
	}

	private void getBeanInfo(Class<?> clz, Return r) {
		String fullName = clz.getName();
		BeanInfo bean;

//		LOGGER.info("want clz: " + fullName);
		if (CACHE.containsKey(fullName))
			bean = CACHE.get(fullName);
		else {
			bean = getBeanInfo(clz);
			CACHE.put(fullName, bean);
		}

		r.name = bean.name;
		r.comment = bean.description;
		r.values = bean.values;
		r.beans = bean.beans;
	}

	private BeanInfo getBeanInfo(Class<?> real) {
		BeanInfo bean = new BeanInfo();
		bean.name = real.getSimpleName();
		bean.type = real.getName();

		if (bean.type.contains("CloseResourcePlan"))
			LOGGER.info(">>>>>>>>>>>" + bean.type);

		if (bean.type.contains("UAVRouteOutputOfApproach") || bean.type.contains("PageResult"))
			return bean;

//		Params params = new Params();
////		params.root = "C:\\code\\drone\\src\\main\\java\\";
//		params.root = "C:\\project\\drone\\src\\main\\java\\";
//		// C:\\code\\drone\\jar\\lbsalgo-0.0.1-SNAPSHOT.jar
////		params.classPath = getClzPath("C:\\sp42\\profile\\eclipse\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\wtpwebapps\\aj-sso\\WEB-INF\\lib");
//		params.classPath = getClzPath("C:\\sp42\\profile\\eclipse\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\wtpwebapps\\aj-sso\\WEB-INF\\lib");
////		params.sourcePath = params.root + ";C:\\code\\aj\\aj-framework\\src\\main\\java;C:\\code\\aj\\aj-util\\src\\main\\java";
//		params.sourcePath = params.root + ";C:\\code\\aj-framework\\src\\main\\java;C:\\code\\aj-util\\src\\main\\java";

		Doclet.parseFieldsOfOneBean(params, real, bean);

		return bean;
	}

	public static String getClzPath(String dir) {
		File file = new File(dir); // 获取其file对象
		File[] fs = file.listFiles(); // 遍历path下的文件和目录，放在File数组中

		List<String> list = new ArrayList<>();

		if (fs != null)
			for (File f : fs) { // 遍历File[]数组
				if (!f.isDirectory() && f.getName().contains("jar")) {
					list.add(dir + "//" + f.getName());
				}
			}

		return StrUtil.join(list, ";");
	}

}
