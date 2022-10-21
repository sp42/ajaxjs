package com.ajaxjs.fast_doc;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.ajaxjs.util.ReflectUtil;

public class DocParser implements Model {
	public static List<Item> parse(Class<?> clz) {
		List<Item> list = makeListByArray(clz.getDeclaredMethods(), method -> {
			Item item = new Item();
			item.methodName = method.getName();

			getReturnType(item, method);

			item.args = makeListByArray(method.getParameters(), param -> {
				Arg arg = new Arg();
				arg.name = param.getName();

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
		bean.values = makeListByArray(real.getDeclaredFields(), field -> {
			Value v = new Value();
			v.name = field.getName();
			v.type = field.getType().getSimpleName();

			return v;
		});

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
