/**
 * Copyright Sp42 frank@ajaxjs.com Licensed under the Apache License, Version
 * 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package com.ajaxjs.jsonparser.lexer;

public class BaseLexer {
	/**
	 * 是否空格字符
	 * 
	 * @param c 传入的字符
	 * @return 是否空格字符
	 */
	public static boolean isSpace(char c) {
		return c == ' ' || c == '\t' || c == '\n';
	}

	/**
	 * 是否字符或者下划线
	 * 
	 * @param c 传入的字符
	 * @return 是否字符或者下划线
	 */
	public static boolean isLetterUnderline(char c) {
		return (c >= 'a' && c <= 'z') || c == '_';
	}

	/**
	 * 是否数字字符
	 * 
	 * @param c 传入的字符
	 * @return 是否数字字符
	 */
	public static boolean isNum(char c) {
		return c >= '0' && c <= '9';
	}

	/**
	 * 是否数字字符或小数
	 * 
	 * @param c 传入的字符
	 * @return 是否数字字符或小数
	 */
	public static boolean isDecimal(char c) {
		return isNum(c) || c == '.';
	}

	/**
	 * 是否字符或者数字或下划线
	 * 
	 * @param c 传入的字符
	 * @return 是否字符或者数字或下划线
	 */
	public static boolean isNumLetterUnderline(char c) {
		return isLetterUnderline(c) || isNum(c) || c == '_';
	}

}
