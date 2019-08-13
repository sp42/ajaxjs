package com.ajaxjs.js;

import java.util.Map;
import java.util.function.Consumer;

public class Context {
	private Map<String, Object> map;
	private String key;
	private Object value;
	private Integer indexOfList;
	private Integer level;

	private Context superContext;

	private boolean stop;

	private Consumer<String> newKey;

	public Map<String, Object> getMap() {
		return map;
	}

	public void setMap(Map<String, Object> map) {
		this.map = map;
	}
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public Integer getIndexOfList() {
		return indexOfList;
	}

	public void setIndexOfList(Integer indexOfList) {
		this.indexOfList = indexOfList;
	}

	public boolean isStop() {
		return stop;
	}

	public void setStop(boolean stop) {
		this.stop = stop;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Context getSuperContext() {
		return superContext;
	}

	public void setSuperContext(Context superContext) {
		this.superContext = superContext;
	}

	public Consumer<String> getNewKey() {
		return newKey;
	}

	public void setNewKey(Consumer<String> newKey) {
		this.newKey = newKey;
	}
}
