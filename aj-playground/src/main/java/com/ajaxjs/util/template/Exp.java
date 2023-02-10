package com.ajaxjs.util.template;

import java.util.Map;
import java.util.Objects;

/**
 * 
 * @author Frank Cheung
 *
 */
public abstract class Exp {
	/**
	 * 
	 * @param context
	 * @return
	 */
	abstract String resolve(Map<String, String> context);

	/**
	 * 
	 * @param exp
	 * @return
	 */
	static Exp of(String exp) {
		Objects.requireNonNull(exp);

		if (exp.startsWith("#{") || exp.startsWith("${"))
			return new VarExp(exp);

		return new StrExp(exp);
	}
}