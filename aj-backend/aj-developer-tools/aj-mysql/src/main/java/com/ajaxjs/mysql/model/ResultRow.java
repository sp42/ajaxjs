/*
 * Copyright 2015, Yahoo Inc.
 * Copyrights licensed under the Apache License.
 * See the accompanying LICENSE file for terms.
 */
package com.ajaxjs.mysql.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class ResultRow {
	private ColumnDescriptor columnDescriptor;

	/**
	 * we only need string value for display
	 */
	private List<String> columns;

	/**
	 * The user can build columns by adding column one by one. The user should not
	 * use setColumns when this is used. This is convenient when there are only a
	 * few columns to build
	 */
	public void addColumn(String s) {
		if (columns == null)
			columns = new ArrayList<>();

		columns.add(s);
	}
}
