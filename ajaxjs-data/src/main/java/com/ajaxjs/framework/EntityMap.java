package com.ajaxjs.framework;

import java.util.Date;
import java.util.HashMap;

public class EntityMap extends HashMap<String, Object> {
	private static final long serialVersionUID = -6572963539115171049L;

	public Long getId() {
		Object id = get("id");
		if (id instanceof Integer)
			return ((Integer) id).longValue();

		return (Long) id;
	}

	public void setId(Long id) {
		put("id", id);
	}

	public Date getCreateDate() {
		Object d = get("createDate");
		return (Date) d;
	}

	public void setCreateDate(Date createDate) {
		put("createDate", createDate);
	}

	public Date getUpdateDate() {
		Object d = get("updateDate");
		return (Date) d;
	}

	public void setUpdateDate(Date updateDate) {
		put("updateDate", updateDate);
	}

	public Long getUid() {
		Object uid = get("uid");
		return (Long) uid;
	}

	public void setUid(Long uid) {
		put("uid", uid);
	}

}
