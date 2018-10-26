/**
 * 版权所有 2017 Sp42 frank@ajaxjs.com
 * 
 * 根据 2.0 版本 Apache 许可证("许可证")授权；
 * 根据本许可证，用户可以不使用此文件。
 * 用户可从下列网址获得许可证副本：
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 *    
 * 除非因适用法律需要或书面同意，根据许可证分发的软件是基于"按原样"基础提供，
 * 无任何明示的或暗示的保证或条件。详见根据许可证许可下，特定语言的管辖权限和限制。
 */
package com.ajaxjs.jdbc.sqlbuilder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ajaxjs.keyvalue.BeanUtil;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.util.reflect.GetMethod;

/**
 * 
 * @author Sp42 frank@ajaxjs.com
 *
 */
public class CommonSQL extends SqlBuilder {
	private static final LogHelper LOGGER = LogHelper.getLog(CommonSQL.class);

	public Object[] insert(Object bean, String tableName) {
		INSERT_INTO(tableName);
		List<Object> values = new ArrayList<>();

		if (bean instanceof Map) {
			values = mapToSql(bean, false);
		} else {
			List<String> methodNames = addFieldValues(bean, false, null);

			for (String methodName : methodNames)
				values.add(GetMethod.executeMethod(bean, methodName));
		}

		return values.toArray();
	}

	@SuppressWarnings("unchecked")
	public Object[] update(Object bean, String tableName) {
		UPDATE(tableName);
		List<Object> values = new ArrayList<>();

		if (bean instanceof Map) {
			values = mapToSql(bean, true);
			values.add(((Map<String, Object>) bean).get("id"));// 添加 id 值，在最后
		} else {
			List<String> methodNames = addFieldValues(bean, true, null);
			for (String methodName : methodNames) {
				values.add(GetMethod.executeMethod(bean, methodName));
			}
			values.add(GetMethod.executeMethod(bean, "getId")); // 添加 id 值，在最后
		}

		WHERE("id = ?");
		return values.toArray();
	}

	private List<Object> mapToSql(Object bean, boolean isSet) {
		List<Object> values = new ArrayList<>();
		@SuppressWarnings("unchecked")
		Map<String, Object> map = (Map<String, Object>) bean;

		for (String field : map.keySet()) {
			if (field.equals("id"))
				continue; // 忽略 id 字段
			if (isSet)
				SET(field + " = ?");
			else
				VALUES(field, "?");

			values.add(map.get(field));
		}

		return values;
	}

	private List<String> addFieldValues(Object bean, boolean isSet, Map<String, String> fieldMapping) {
		final List<String> fieldNames = new ArrayList<>();

		for (Method method : bean.getClass().getMethods()) { // 反射获取字段
			String methodName = method.getName();

			if (!isOk_field(methodName))
				continue;

			try {
				if (method.invoke(bean) != null) { // 有值的才进行操作
					fieldNames.add(methodName);// 保存字段顺序		
					String pojoName = BeanUtil.getFieldName(methodName, "get");

					// 字段映射
					if (fieldMapping != null && fieldMapping.size() > 0 && fieldMapping.containsKey(pojoName)) {
						pojoName = fieldMapping.get(pojoName);
					}

					if (isSet)
						SET(pojoName + " = ?");
					else
						VALUES(pojoName, "?");
				}
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				LOGGER.warning(e);
			}
		}

		return fieldNames;
	}

	/**
	 * 不是 pojo 所有的字段都要，这里判断
	 * 
	 * @param methodName
	 *            方法名称
	 * @return 是否需要的字段
	 */
	private static boolean isOk_field(String methodName) {
		return (methodName.startsWith("get") || methodName.startsWith("is")) && !"getId".equals(methodName) && !"getClass".equals(methodName) && !"getService".equals(methodName);
	}
}
