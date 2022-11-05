package com.ajaxjs.database_meta.model;

/**
 * 列详情
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public class TableColumns {
	private String field;
	private String type;
	private String collation;
	private String nu;
	private String key;
	private String defau;
	private String extra;
	private String privileges;
	private String comment;

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCollation() {
		return collation;
	}

	public void setCollation(String collation) {
		this.collation = collation;
	}

	public String getNu() {
		return nu;
	}

	public void setNu(String nu) {
		this.nu = nu;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getDefau() {
		return defau;
	}

	public void setDefau(String defau) {
		this.defau = defau;
	}

	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}

	public String getPrivileges() {
		return privileges;
	}

	public void setPrivileges(String privileges) {
		this.privileges = privileges;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
}
