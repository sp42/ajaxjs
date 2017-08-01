/**
 * Copyright Frank Cheung <frank@ajaxjs.com>
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
package com.ajaxjs.js.jsonparser.lexer;

import com.ajaxjs.js.jsonparser.JsonParseException;

/**
 * 
 */
public class Token {
	/**
	 * 
	 * @param type
	 * @param typeName
	 * @param typeNameChinese
	 */
	public Token(int type, String typeName, String typeNameChinese) {
		this.type = type;
		this.typeName = typeName;
		this.typeNameChinese = typeNameChinese;
		
	}
	
	/**
	 * 
	 * @param type
	 * @param typeName
	 * @param typeNameChinese Java 类型的值，通常是已知的
	 * @param javaValue
	 */
	public Token(int type, String typeName, String typeNameChinese, Object javaValue) {
		this(type, typeName, typeNameChinese);
		this.javaValue = javaValue;
	}
	
	/**
	 * 
	 * @param type
	 * @param typeName
	 * @param typeNameChinese
	 * @param javaValue
	 *            Java 类型的值，通常是已知的
	 * @param value
	 *            JSON 值（String|Number）
	 */
	public Token(int type, String typeName, String typeNameChinese, Object javaValue, String value) {
		this(type, typeName, typeNameChinese, javaValue);
		this.value = value;
	}

	/**
	 * Token 类型，也可以理解为 id、index
	 */
	private int type;
	
	/**
	 * 类型名称
	 */
	private String typeName;
	
	/**
	 * 类型名称（中文版本）
	 */
	private String typeNameChinese;

	/**
	 * Token 值，一般指数字和字符串。这是 JSON 字符串上的那个原始值。
	 */
	private String value;
	
	/**
	 * Java 的值，由 value 转换而来。 
	 */
	private Object javaValue;
	
	/**
	 * 根据 value（从 JSON 得来的） 转换为 Java 可读取的值 
	 * @return Java 中的类型
	 */
	public Object toJavaValue() {
		if(this == Tokens.TRUE || this == Tokens.FALSE || this == Tokens.NIL)
			return getJavaValue();
		else if(this instanceof StringToken) 
			return StringToken.unescape(value);
		else if(this instanceof NumberToken) {
			//System.out.println("vv:" +( (false) ? Double.parseDouble(value) :  Integer.parseInt(value)));
			if (value.indexOf('.') != -1)
				return Double.parseDouble(value);
			else
				return Integer.parseInt(value);
		}else 
			throw new JsonParseException("获取 Java 值失败！");
	}
	
	private static String strTpl = "[ %s | %s : %s]";

	@Override
	public String toString() {
		return type > 1 ? "[" + getTypeName() + "]" : String.format(strTpl, getTypeName(), getTypeNameChinese(), value);
	}
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the javaValue
	 */
	public Object getJavaValue() {
		return javaValue;
	}

	/**
	 * @param javaValue the javaValue to set
	 */
	public void setJavaValue(Object javaValue) {
		this.javaValue = javaValue;
	}

	/**
	 * @return the typeName
	 */
	public String getTypeName() {
		return typeName;
	}

	/**
	 * @param typeName the typeName to set
	 */
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	/**
	 * @return the typeNameChinese
	 */
	public String getTypeNameChinese() {
		return typeNameChinese;
	}

	/**
	 * @param typeNameChinese the typeNameChinese to set
	 */
	public void setTypeNameChinese(String typeNameChinese) {
		this.typeNameChinese = typeNameChinese;
	}
}
