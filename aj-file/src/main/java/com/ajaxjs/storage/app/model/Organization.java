package com.ajaxjs.storage.app.model;

public class Organization {
	private String id;

	private String name;

	private String parentId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	@Override
	public String toString() {
		return "Organization{" + "id='" + id + '\'' + ", name='" + name + '\'' + ", parentId='" + parentId + '\'' + '}';
	}
}
