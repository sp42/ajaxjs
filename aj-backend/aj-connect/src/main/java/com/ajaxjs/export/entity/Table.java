package com.ajaxjs.export.entity;

import lombok.Data;

import java.util.List;

@Data
public class Table {
	private Integer expandedColumnCount;

	private Integer expandedRowCount;

	private Integer fullColumns;

	private Integer fullRows;

	private Integer defaultColumnWidth;

	private Integer defaultRowHeight;

	private List<Column> columns;

	private List<Row> rows;

}
