package com.ajaxjs.mysql.model;

/**
 * 表的详情信息
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public class TableDesc {
	private String name;
	private String engine;
	private String version;
	private String rowFormat;
	private String rows;
	private String avgRowLength;
	private String dataLength;
	private String maxDataLength;
	private String indexLength;
	private String dataFree;
	private String autoIncrement;
	private String createTime;
	private String updateTime;
	private String checkTime;
	private String collation;
	private String checksum;
	private String createOptions;
	private String comment;

	private String dataMb;
	private String indexMb;
	private String allMb;
	private String count;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEngine() {
		return engine;
	}

	public void setEngine(String engine) {
		this.engine = engine;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getRowFormat() {
		return rowFormat;
	}

	public void setRowFormat(String rowFormat) {
		this.rowFormat = rowFormat;
	}

	public String getRows() {
		return rows;
	}

	public void setRows(String rows) {
		this.rows = rows;
	}

	public String getAvgRowLength() {
		return avgRowLength;
	}

	public void setAvgRowLength(String avgRowLength) {
		this.avgRowLength = avgRowLength;
	}

	public String getDataLength() {
		return dataLength;
	}

	public void setDataLength(String dataLength) {
		this.dataLength = dataLength;
	}

	public String getMaxDataLength() {
		return maxDataLength;
	}

	public void setMaxDataLength(String maxDataLength) {
		this.maxDataLength = maxDataLength;
	}

	public String getIndexLength() {
		return indexLength;
	}

	public void setIndexLength(String indexLength) {
		this.indexLength = indexLength;
	}

	public String getDataFree() {
		return dataFree;
	}

	public void setDataFree(String dataFree) {
		this.dataFree = dataFree;
	}

	public String getAutoIncrement() {
		return autoIncrement;
	}

	public void setAutoIncrement(String autoIncrement) {
		this.autoIncrement = autoIncrement;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(String checkTime) {
		this.checkTime = checkTime;
	}

	public String getCollation() {
		return collation;
	}

	public void setCollation(String collation) {
		this.collation = collation;
	}

	public String getChecksum() {
		return checksum;
	}

	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}

	public String getCreateOptions() {
		return createOptions;
	}

	public void setCreateOptions(String createOptions) {
		this.createOptions = createOptions;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getDataMb() {
		return dataMb;
	}

	public void setDataMb(String dataMb) {
		this.dataMb = dataMb;
	}

	public String getIndexMb() {
		return indexMb;
	}

	public void setIndexMb(String indexMb) {
		this.indexMb = indexMb;
	}

	public String getAllMb() {
		return allMb;
	}

	public void setAllMb(String allMb) {
		this.allMb = allMb;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}
}
