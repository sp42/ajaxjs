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
package com.ajaxjs.js.json.lexer;

/**
 * 数字
 * 
 * @author Frank Cheung frank@ajaxjs.com
 */
public class NumberToken extends Token {
	/**
	 * 
	 * @param value
	 */
	public NumberToken(String value) {
		super(1, "NUM", "数字", null, value);
	}
}
