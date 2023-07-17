/*
 * Copyright 2015, Yahoo Inc.
 * Copyrights licensed under the Apache License.
 * See the accompanying LICENSE file for terms.
 */
package com.ajaxjs.mysql.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * Information about the columns from a query return results
 *
 * @author xrao
 */
@Data
public class ColumnDescriptor implements java.io.Serializable {
	private static final long serialVersionUID = 6923318258330634209L;

	private List<ColumnInfo> columns = new ArrayList<>();

	public ColumnInfo getColumn(String colName) {
		for (int i = columns.size() - 1; i >= 0; i--)
			if (colName.equalsIgnoreCase(columns.get(i).getName()))
				return columns.get(i);

		return null;
	}

	public int getColumnIndex(String colName) {
		for (int i = columns.size() - 1; i >= 0; i--)
			if (colName.equalsIgnoreCase(columns.get(i).getName()))
				return i;

		return -1;
	}

	public void addColumn(String colName, boolean isNumber, int pos) {
		ColumnInfo col = new ColumnInfo();
		col.setName(colName);
		col.setNumberType(isNumber);
		col.setPosition(pos);

		columns.add(col);
	}
}
