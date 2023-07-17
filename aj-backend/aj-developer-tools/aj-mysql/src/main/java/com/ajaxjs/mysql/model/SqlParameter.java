/*
 * Copyright 2015, Yahoo Inc.
 * Copyrights licensed under the Apache License.
 * See the accompanying LICENSE file for terms.
 */
package com.ajaxjs.mysql.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SqlParameter {
	public final static String TYPE_NUMERIC = "numeric";
	public final static String TYPE_NONBREAKABLE_STRING = "non_breakable_string";// no space, quote, , ; \r, \n allowed
	public final static String TYPE_BREAK_STRING = "breakable_string";
	public final static String TYPE_QUOTED_STRING = "single_quoted_string";// so we only need escape single quote

	private String name;// actual parameter meaningful name
	private String dataType = TYPE_NONBREAKABLE_STRING;// value type, one of above defined value

	public SqlParameter(String name, String dataType) {
		this.name = name;
		if (dataType != null)
			this.dataType = dataType;
	}

	public void setDataType(String dataType) {
		if (dataType != null)
			this.dataType = dataType;
	}

	public boolean needEscape() {
		return TYPE_BREAK_STRING.equalsIgnoreCase(this.dataType) || TYPE_QUOTED_STRING.equalsIgnoreCase(this.dataType);
	}

	public boolean isValidValue(String input) {
		// check if numeric type
		if (TYPE_NUMERIC.equalsIgnoreCase(this.dataType)) {
			BigDecimal dec = new BigDecimal(input);
			return true;
		} else if (TYPE_NONBREAKABLE_STRING.equalsIgnoreCase(this.dataType)) {
			if (input == null)
				return true;
			else if (input.indexOf(' ') >= 0)
				return false;
			else if (input.indexOf('\t') >= 0)
				return false;
			else if (input.indexOf('\n') >= 0)
				return false;
			else if (input.indexOf('\r') >= 0)
				return false;
			else if (input.indexOf(';') >= 0)
				return false;
			else
				return input.indexOf(',') < 0;
		}

		return true;
	}

	public static String escapeSingleQuote(String input) {
		if (input == null)
			return null;

		return input.replaceAll("'", "''");
	}
}
