/*
 * Copyright 2015, Yahoo Inc.
 * Copyrights licensed under the Apache License.
 * See the accompanying LICENSE file for terms.
 */
package com.ajaxjs.mysql.model;

import lombok.Data;

/**
 * Result column descriptor
 *
 * @author xrao
 */
@Data
public class ColumnInfo implements java.io.Serializable {
	private static final long serialVersionUID = 2467837325706019159L;

	private String name;// name of the column
	private int position;// position of the column
	private boolean numberType;// no need to quote number type
	private int maxLength = 0;// used for formatting

	// support repository
	private String dbDataType;// data type of the database column
	private String javaType;// data type in java
	private String javaName;// java bean member name
	private String srcName;// column name from source Oracle database

	public ColumnInfo() {
	}

	public ColumnInfo(String name) {
		this.name = name;
	}

	public ColumnInfo copy() {
		ColumnInfo colInfo = new ColumnInfo();
		colInfo.setName(getName());
		colInfo.setNumberType(numberType);

		return colInfo;
	}

}
