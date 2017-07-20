package com.ajaxjs.json.lexer;
/**
 * 字符串 Token
 * @author Frank Cheung frank@ajaxjs.com
 */
public class StringToken extends Token {
	public StringToken(String value) {
		super(0, "STR", "字符串", null, value);
	}
}
