package com.ajaxjs.json.lexer;

public class StringToken extends Token {
	public StringToken(String value) {
		super(0, "STR", "字符串", null, value);
	}
}
