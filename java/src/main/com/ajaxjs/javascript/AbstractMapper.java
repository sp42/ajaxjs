package com.ajaxjs.javascript;

import sun.org.mozilla.javascript.internal.NativeArray;
import sun.org.mozilla.javascript.internal.NativeObject;

public abstract class AbstractMapper implements Mapper {
	@Override
	public Object parseValue(Object value) {
		if (value == null || value instanceof Boolean || value instanceof String) {
			// js 为 null，所以 java hash 也为null
			// nothing but still value;
		} else if (value instanceof Double) {
			value = ((Double) value).intValue();// js number 转换为 INT
		} else if (value instanceof NativeObject) {
			// value = jsValue2java(value);
		} else if (value instanceof NativeArray) {
//			value = toMapArray((NativeArray) value); // 这是规则的情况，数组中每个都是对象，而非
														// string/int/boolean
														// @todo
		} else {
			// LOGGER.info("未知 JS 类型：" + value.getClass().getName());
		}
		
		return value;
	}
}
