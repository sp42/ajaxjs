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
package com.ajaxjs.js.jsonparser.lexer;

/**
 * 列出常见的 Token 类型。字符串类型 Token 和 数字类型 Token 两个比较特殊，须另外设类来表示。
 * 
 * @author Sp42 frank@ajaxjs.com
 */
public interface Tokens {
	/**
	 * 注意：Token 0 为 字符串类型 Token，参见 StringToken 类，这里不写
	 */

	/**
	 * 注意：Token 1 为 数字类型 Token，参见 NumberToken 类，这里不写
	 */

	/**
	 * 对象的值
	 */
	public static final Token DESC = new Token(2, "DESC", ":");

	/**
	 * 多个元素之间的分隔符
	 */
	public static final Token SPLIT = new Token(3, "SPLIT", ",");

	/**
	 * 数组开始
	 */
	public static final Token ARRS = new Token(4, "ARRS", "[");

	/**
	 * 对象开始
	 */
	public static final Token OBJS = new Token(5, "OBJS", "{");

	/**
	 * 数组结束
	 */
	public static final Token ARRE = new Token(6, "ARRE", "]");

	/**
	 * 对象结束
	 */
	public static final Token OBJE = new Token(7, "OBJE", "}");

	/**
	 * FALSE 值
	 */
	public static final Token FALSE = new Token(8, "FALSE", "false", false);

	/**
	 * TRUE 值
	 */
	public static final Token TRUE = new Token(9, "TRUE", "true", true);

	/**
	 * NULL 空值
	 */
	public static final Token NIL = new Token(10, "NIL", "null", null);

	/**
	 * 开始
	 */
	public static final Token BGN = new Token(11, "BGN", "开始");

	/**
	 * 结束
	 */
	public static final Token EOF = new Token(12, "EOF", "结束");
}
