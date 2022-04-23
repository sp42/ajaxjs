package com.ajaxjs.util.template;

import java.util.Map;

/**
 * 
 * @author Frank Cheung
 *
 */
public class StrExp extends Exp {
	/**
	 * 
	 */
	private String value;

	/**
	 * 
	 * @param exp
	 */
	StrExp(String exp) {
		this.value = exp;
	}

	@Override
	public String resolve(Map<String, String> context) {
		return this.value;
	}

	@Override
	public String toString() {
		return "StrExp [value=" + value + "]";
	}
}