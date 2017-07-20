package com.ajaxjs.json.lexer;

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
