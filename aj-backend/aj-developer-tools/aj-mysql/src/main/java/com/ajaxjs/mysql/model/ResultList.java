/*
 * Copyright 2015, Yahoo Inc.
 * Copyrights licensed under the Apache License.
 * See the accompanying LICENSE file for terms.
 */
package com.ajaxjs.mysql.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.Data;

/**
 * Store qresultset of a query
 * 
 * @author xrao
 *
 */
@Data
public class ResultList implements java.io.Serializable {
	private static final long serialVersionUID = 1126296256817182221L;
	private ColumnDescriptor columnDescriptor;// meta data
	private List<ResultRow> rows = new ArrayList<>();// results
	private long totalResponseTime = 0L;// total time used in milliseconds
	private long totalExecutionTime = 0L;// total DB execution time used in milliseconds
	private long totalFetchTime = 0L;// total Resultset Fetch time used in milliseconds
	private Map<String, CustomResultObject> customObjects;// The key is the json key name

	public ResultList() {
	}

	public ResultList(int capacity) {
		rows = new ArrayList<>(capacity);
	}

	public int getColumnIndex(String colName) {
		return columnDescriptor.getColumnIndex(colName);
	}

	public void addRow(ResultRow row) {
		rows.add(row);
	}

	public void addCustomeObject(CustomResultObject obj) {
		if (obj == null)
			return;

		if (customObjects == null)
			customObjects = new java.util.HashMap<>();

		customObjects.put(obj.getName(), obj);
	}
}
