package com.ajaxjs.fast_doc;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ajaxjs.spring.easy_controller.ControllerMethod;
import com.ajaxjs.util.ReflectUtil;

public class DocParser implements Model {
	public static List<Item> parse(Class<?> clz) {
		RequestMapping rm = clz.getAnnotation(RequestMapping.class);
		String rootUrl = rm.value()[0];

		List<Item> list = makeListByArray(clz.getDeclaredMethods(), method -> {
			Item item = new Item();
			item.methodName = method.getName();

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
				item.url = get.value()[0];

				if (!StringUtils.hasText(item.url)) {
					// 读取 root
					item.url = rootUrl;
				}
			}

			PostMapping post = method.getAnnotation(PostMapping.class);
			if (post != null) {
				item.httpMethod = "POST";
				item.url = post.value()[0];

				if (!StringUtils.hasText(item.url)) {
					// 读取 root
					item.url = rootUrl;
				}
			}

			PutMapping put = method.getAnnotation(PutMapping.class);
			if (put != null) {
				item.httpMethod = "PUT";
				item.url = put.value()[0];

				if (!StringUtils.hasText(item.url)) {
					// 读取 root
					item.url = rootUrl;
				}
			}

			DeleteMapping del = method.getAnnotation(DeleteMapping.class);
			if (del != null) {
				item.httpMethod = "DELETE";
				item.url = del.value()[0];

				if (!StringUtils.hasText(item.url)) {
					// 读取 root
					item.url = rootUrl;
				}
			}

			getReturnType(item, method);

			// 参数 入参
			item.args = makeListByArray(method.getParameters(), param -> {
				Arg arg = new Arg();
				arg.name = param.getName();
				arg.type = param.getType().getSimpleName();
				
				RequestParam queryP = param.getAnnotation(RequestParam.class);
				if (queryP != null) {
					arg.position = "query";
					arg.isRequired = queryP.required();

					if (queryP.value() != null) {
						arg.name = queryP.value();
					}
				}

				return arg;
			});

			return item;
		});

		return list;
	}

	/**
	 * 对一个数组进行迭代，返回一个 list
	 * 
	 * @param <T>
	 * @param arr
	 * @param fn
	 * @return
	 */
	public static <T, K> List<T> makeListByArray(K[] arr, Function<K, T> fn) {
		List<T> values = new ArrayList<>();

		for (K obj : arr) {
			T v = fn.apply(obj);
			values.add(v);
		}

		return values;

	}

	static Map<String, BeanInfo> CACHE = new HashMap<>();

	private static void getReturnType(Item item, Method method) {
		Return r = new Return();
		Class<?> returnType = method.getReturnType();

		if (isSimpleValueType(returnType)) {
			r.isObject = false;
			r.name = returnType.getSimpleName();
			r.fullName = returnType.getName();
		} else if (returnType == Map.class) {
			// TODO
		} else if (returnType == List.class) {
			r.isMany = true;

			Type[] _real = ReflectUtil.getGenericReturnType(method);
			Class<?> real = ReflectUtil.type2class(_real[0]);

			if (isSimpleValueType(real)) {
				r.isObject = false;
				r.name = returnType.getSimpleName();
				r.fullName = returnType.getName();
			} else {
				r.isObject = true;
				r.fullName = real.getName();

				BeanInfo bean;

				if (CACHE.containsKey(r.fullName))
					bean = CACHE.get(r.fullName);
				else {
					bean = getBeanInfo(real);
					CACHE.put(r.fullName, bean);
				}

				r.name = bean.name;
				r.comment = bean.comment;
				r.values = bean.values;
			}
		} else if (returnType.isArray()) {
			r.isMany = true;
		}

		item.returnValue = r;
	}

	private static BeanInfo getBeanInfo(Class<?> real) {
		BeanInfo bean = new BeanInfo();
		bean.name = real.getSimpleName();
		bean.fullName = real.getName();
//		bean.values = makeListByArray(real.getDeclaredFields(), field -> {
//			Value v = new Value();
//			v.name = field.getName();
//			v.type = field.getType().getSimpleName();
//
//			return v;
//		});

		if (bean.fullName.contains("$") || bean.fullName.contains("UAVRouteOutputOfApproach"))
			return bean;

		String root = "C:\\project\\drone\\src\\main\\java\\";
		List<String> sources = Doclet.initDoclet(root);
		sources.add(root + Doclet.className2JavaFileName(bean.fullName));

		Doclet.parseFieldsOfOneBean(root, real, bean, sources);

		return bean;
	}

	static boolean isSimpleValueType(Class<?> type) {
		return type.isPrimitive() || type == Boolean.class || type == Integer.class || type == Long.class || type == String.class || type == Double.class
				|| type == Float.class;
	}
}
