package com.ajaxjs.validator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.ajaxjs.util.logger.LogHelper;

public class BeanValidator {
	private static final LogHelper LOGGER = LogHelper.getLog(BeanValidator.class);

	private static final Map<Class<?>, Validator> cache = new HashMap<>();

	/**
	 * 
	 * @param clzs
	 * @param validator
	 */
	public static void register(Class<?> clzs, Validator validator) {
		cache.put(clzs, validator);
	}

	static {
		register(NotNull.class, BuiltinValidator.NOT_NULL_VALIDATOR);
		register(NotBlank.class, BuiltinValidator.NOT_BLANK_VALIDATOR);
	}

	/**
	 * 校验实体
	 * 
	 * @param bean 实体
	 * @return 错误集合，若数组为 length=0表示完全通过
	 */
	public static String[] validate(Object bean) {
		List<String> list = new ArrayList<>();
		Class<?> cls = bean.getClass();

		Field[] fields = cls.getDeclaredFields();
		try {
			// 获取实体字段集合
			for (Field f : fields) {// 通过反射获取该属性对应的值
				f.setAccessible(true);

				Object value = f.get(bean);// 获取字段值
				Annotation[] arrayAno = f.getAnnotations();// 获取字段上的注解集合

				for (Annotation annotation : arrayAno) {
					Class<?> clazz = annotation.annotationType();// 获取注解类型（注解类的Class）

					Validator validator = cache.get(clazz);
					if (validator == null) // 不是验证器的注解
						continue;

					String result = validator.valid(value, f, annotation);
					if (result != null)
						list.add(result);
				}
			}
		} catch (Exception e) {
			LOGGER.warning(e, "验证出错");
		}

		return list.toArray(new String[list.size()]);
	}

}