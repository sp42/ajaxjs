/**
 * Copyright Sp42 frank@ajaxjs.com <frank@ajaxjs.com>
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
package com.ajaxjs.jsonparser.lexer;

/**
 * Token 类型
 */
public class Token {
	/**
	 * 创建一个 Token
	 * 
	 * @param type   TokenId
	 * @param name   Token 名称
	 * @param cnName Token 名称（中文名）
	 */
	public Token(int type, String name, String cnName) {
		this.type = type;
		this.typeName = name;
		this.typeNameChinese = cnName;
	}

	/**
	 * 创建一个 Token
	 * 
	 * @param type   TokenId
	 * @param name   Token 名称
	 * @param cnName Token 名称（中文名）
	 * @param value  Java 类型的值，通常是已知的
	 */
	public Token(int type, String name, String cnName, Object value) {
		this(type, name, cnName);
		this.javaValue = value;
	}

	/**
	 * 创建一个 Token
	 * 
	 * @param type      TokenId
	 * @param name      Token 名称
	 * @param cnName    Token 名称（中文名）
	 * @param value     Java 类型的值，通常是已知的
	 * @param jsonValue JSON 值（String|Number）
	 */
	public Token(int type, String name, String cnName, Object value, String jsonValue) {
		this(type, name, cnName, value);
		this.value = jsonValue;
	}

	/**
	 * Token 类型，也可以理解为 TokenId、TokenIndex
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

//	/**
//	 * 根据 value（从 JSON 得来的） 转换为 Java 可读取的值
//	 * 
//	 * @return Java 中的类型
//	 */
//	public Object toJavaValue() {
//		if (this == Tokens.TRUE || this == Tokens.FALSE || this == Tokens.NIL)
//			return getJavaValue();
//		else if (this instanceof StringToken)
//			return StringToken.unescape(value);
//		else if (this instanceof NumberToken) {
//			// System.out.println(value.indexOf('.') != -1);
//			// 奇葩问题
//			// System.out.println(false ? Double.parseDouble(value) :
//			// Integer.parseInt(value));
//			if (value.indexOf('.') != -1) {
//				return Double.parseDouble(value);
//			} else {
//				return Integer.parseInt(value);
//			}
//			// return value.indexOf('.') != -1 ? Double.parseDouble(value) :
//			// Integer.parseInt(value);
//		} else
//			throw new JsonParseException("获取 Java 值失败！");
//	}

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
