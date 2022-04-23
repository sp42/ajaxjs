package com.ajaxjs.util.template;

import java.util.Map;
import java.util.Objects;

/**
 * 
 * @author Frank Cheung
 *
 */
public class VarExp extends Exp {
	/**
	 * 
	 */
	private String varName;

	/**
	 * 
	 */
	private String defaultValue;

	/**
	 * 
	 */
	private Boolean nullable = false;

	/**
	 * 
	 * @param varName
	 * @param defaultValue
	 * @param nullable
	 */
	VarExp(String varName, String defaultValue, Boolean nullable) {
		this.varName = varName;
		this.defaultValue = defaultValue;
		this.nullable = nullable;
	}

	/**
	 * 
	 * @param exp
	 */
	VarExp(String exp) {
		Objects.requireNonNull(exp);

		if (!(exp.startsWith("#{") || exp.startsWith("${")) || !exp.endsWith("}"))
			throw new IllegalArgumentException("表达式[" + exp + "]必须类似于#{}或${}");

		String[] nodes = exp.substring(2, exp.length() - 1).split(",");

		if (nodes.length > 2)
			throw new IllegalArgumentException("表达式[" + exp + "]只能出现一个','");

		this.varName = nodes[0].trim();
		this.defaultValue = nodes.length == 2 ? nodes[1].trim() : "";
		this.nullable = exp.startsWith("$");
	}

	@Override
	public String resolve(Map<String, String> context) {
		String value = context.get(varName);
		if (value == null && nullable)
			value = defaultValue == null ? "" : defaultValue;

		if (value == null)
			throw new NullPointerException("上下文中没有指定的变量:var=" + varName + " map=" + context);

		return value;
	}

	@Override
	public String toString() {
		return "VarExp [varName=" + varName + ", defaultValue=" + defaultValue + ", nullable=" + nullable + "]";
	}

}
