package com.ajaxjs.json.lexer;

public class NumberToken extends Token {
	public NumberToken(String value) {
		super(1, "NUM", "数字", null, value);
	}
}
