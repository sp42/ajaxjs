package com.ajaxjs.database_meta.model;

public class TableIndex {
	private String table;
	private String nonUnique;
	private String keyName;
	private String seqInIndex;
	private String columnName;
	private String collation;
	private String cardinality;
	private String subPart;
	private String packed;
	private String nullValue;
	private String indexType;
	
	public String getTable() {
		return table;
	}
	public void setTable(String table) {
		this.table = table;
	}
	public String getNonUnique() {
		return nonUnique;
	}
	public void setNonUnique(String nonUnique) {
		this.nonUnique = nonUnique;
	}
	public String getKeyName() {
		return keyName;
	}
	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}
	public String getSeqInIndex() {
		return seqInIndex;
	}
	public void setSeqInIndex(String seqInIndex) {
		this.seqInIndex = seqInIndex;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public String getCollation() {
		return collation;
	}
	public void setCollation(String collation) {
		this.collation = collation;
	}
	public String getCardinality() {
		return cardinality;
	}
	public void setCardinality(String cardinality) {
		this.cardinality = cardinality;
	}
	public String getSubPart() {
		return subPart;
	}
	public void setSubPart(String subPart) {
		this.subPart = subPart;
	}
	public String getPacked() {
		return packed;
	}
	public void setPacked(String packed) {
		this.packed = packed;
	}
	public String getNullValue() {
		return nullValue;
	}
	public void setNullValue(String nullValue) {
		this.nullValue = nullValue;
	}
	public String getIndexType() {
		return indexType;
	}
	public void setIndexType(String indexType) {
		this.indexType = indexType;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getIndexComment() {
		return indexComment;
	}
	public void setIndexComment(String indexComment) {
		this.indexComment = indexComment;
	}
	public String getVisible() {
		return visible;
	}
	public void setVisible(String visible) {
		this.visible = visible;
	}
	public String getExpression() {
		return expression;
	}
	public void setExpression(String expression) {
		this.expression = expression;
	}
	private String comment;
	private String indexComment;
	private String visible;
	private String expression;
}
