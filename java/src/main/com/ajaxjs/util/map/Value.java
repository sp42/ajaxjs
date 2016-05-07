/**
 * Copyright 2015 Frank Cheung
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ajaxjs.util.map;

import java.util.Date;

import com.ajaxjs.util.DateTools;


public class Value implements IValue{
	private Object rawValue;

	public Value() {}
	
	/**
	 * 
	 * @param rawValue 輸入值
	 */
	public Value(Object rawValue) {
		this.rawValue = rawValue;
		// 马上进行初始化工作
		toJavaValue(); // 必须先获取真实值，才可以转换为其他类型，否则都是 String没意义
		toSqlType();
		toJSONType();
	}

	@Override
	public void setRawValue(Object rawValue) {
		this.rawValue = rawValue;
	}

	@Override
	public Object getRawValue() {
		return rawValue;
	}
	
	//-------------------------------------------------
	private Object valueOf;
	
	@Override
	@SuppressWarnings("unchecked")
	public <T> T valueOf(Class<T> clz) {
		return (T) valueOf;
	}
	
	public void setValueOf(Object valueOf) {
		this.valueOf = valueOf;
	}

	@Override
	public Object getValueOf() {
		return valueOf;
	}
	
	public void toJavaValue(){
		if (rawValue != null) {
			if (rawValue instanceof String)
				setValueOf(toJavaValue((String) rawValue));
			else 
				setValueOf(rawValue);
		} 
	}

	/**
	 * 从字符串还原真实值，
	 * 空字符串"" 会被视为 null
	 * 获取参数，并自动从字符串转换为 Java 类型，如 "true"--> true,"123"--> 123,"null"-->null
	 * @param value 字符串的值
	 * @return
	 */
	public static Object toJavaValue(String value) {
		Object _return = value;

		if (value == null) return null;
		value = value.trim();

		if ("".equals(value) || "null".equals(value))
			return null;

		if ("true".equalsIgnoreCase(value))
			return true;
		if ("false".equalsIgnoreCase(value))
			return false;

		try {
			int int_value = Integer.parseInt(value); 
			if ((int_value + "").equals(value)) // 判断为整形
				return int_value;
		} catch (NumberFormatException e) {// 不能转换为数字
			_return = value;
		}

		return _return;
	}
	
	//------------------------------------------------
	private String sqlType;
	@Override
	public String getSqlType() {
		return sqlType;
	}

	public void setSqlType(String sqlType) {
		this.sqlType = sqlType;
	}
	
	public String getSqlTypeClassName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toSqlType() {
		String sqlType = toSqlType(getValueOf());
		setSqlType(sqlType);
		return sqlType;
	}
	
	/**
	 * 转换为符合 SQL 语句的字符串
	 * @param value
	 * @return
	 */
	public static String toSqlType(Object value) {
		if (value == null) {
			return "NULL";
		} else if (value instanceof String) {
			return makeString(value.toString());
		} else if (value instanceof Integer) {
			return value + "";
		} else if (value instanceof Date) {
			return makeString(DateTools.formatDate((Date) value)); // SQL 标准日期
		} else {
			throw new UnsupportedOperationException("不支持类型：" + value.getClass().getName());
		}
	}
	
	private static String makeString(String str) {
		return "'" + str + "'";
	}
	
	//------------------------------------------------
	private String jsonType;
	
	public String getJsonType() {
		return jsonType;
	}

	public void setJsonType(String jsonType) {
		this.jsonType = jsonType;
	}
	
	@Override
	public String toJSONType() {
		return com.ajaxjs.javascript.Util.obj2jsonVaule(getValueOf());
	}

}
