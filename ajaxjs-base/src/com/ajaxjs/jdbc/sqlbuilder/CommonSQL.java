package com.ajaxjs.jdbc.sqlbuilder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ajaxjs.util.LogHelper;
import com.ajaxjs.util.reflect.BeanUtil;
import com.ajaxjs.util.reflect.Reflect;

public class CommonSQL extends SqlBuilder {
	private static final LogHelper LOGGER = LogHelper.getLog(CommonSQL.class);
	
	public Object[] insert(Object bean, String tableName) {
		INSERT_INTO(tableName);
		List<Object> values = new ArrayList<>();

		if(bean instanceof Map) {
			values = mapToSql(bean, false);
		} else {
			List<String> methodNames =  addFieldValues(bean, false, null);
			
			for(String methodName : methodNames) {
				values.add(Reflect.executeMethod(bean, methodName));
			}
			
		}

		return values.toArray();
	}
	
	@SuppressWarnings("unchecked")
	public Object[] update(Object bean, String tableName) {
		UPDATE(tableName);		
		List<Object> values = new ArrayList<>();
		
		if(bean instanceof Map) {
			values = mapToSql(bean, true);
			values.add(((Map<String, Object>)bean).get("id"));// 添加 id 值，在最后
		} else {
			List<String> methodNames = addFieldValues(bean, true, null);
			for(String methodName : methodNames) {
				values.add(Reflect.executeMethod(bean, methodName));
			}
			values.add(Reflect.executeMethod(bean, "getId")); // 添加 id 值，在最后
		}
		
		WHERE("id = ?");
		return values.toArray();
	}
	
	private List<Object> mapToSql(Object bean, boolean isSet) {
		List<Object> values = new ArrayList<>();
		@SuppressWarnings("unchecked")
		Map<String, Object> map = (Map<String, Object>)bean;
		
		for(String field : map.keySet()) {
			if(field.equals("id"))
				continue; // 忽略 id 字段
			if (isSet)
				SET(field + " = ?");
			else
				VALUES(field, "?");
			
			values.add(map.get(field));
		}
		
		return values;
	}
	
	/**
	 * 通过反射获得 sql 的字段名称
	 * @param sql
	 *            动态 SQL 实例
	 * @param model
	 *            实体
	 * @param methods
	 *            反射获取字段
	 * @param isSet
	 *            true=Update/false=Create
	 */
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
	 * @return
	 */
	private static boolean isOk_field(String methodName) {
		return (methodName.startsWith("get") || methodName.startsWith("is")) && !"getId".equals(methodName)
				&& !"getClass".equals(methodName) && !"getService".equals(methodName);
	}
}
