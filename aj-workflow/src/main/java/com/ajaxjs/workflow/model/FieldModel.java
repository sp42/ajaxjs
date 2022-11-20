package com.ajaxjs.workflow.model;

import java.util.HashMap;
import java.util.Map;

/**
 * 字段模型类
 */
public class FieldModel extends BaseWfModel {
	private static final long serialVersionUID = -3439646328045814844L;

	/**
	 * 字段类型
	 */
	private String type;

	/**
	 * 字段模型对应的属性 key/value 数据
	 */
	private Map<String, String> attrMap = new HashMap<>();

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	/**
	 * 向属性集合添加 key/value 数据
	 * 
	 * @param name  属性名称
	 * @param value 属性值
	 */
	public void addAttr(String name, String value) {
		attrMap.put(name, value);
	}

	/**
	 * 获取属性集合
	 * 
	 * @return 属性集合
	 */
	public Map<String, String> getAttrMap() {
		return attrMap;
	}

	/**
	 * 设置属性集合
	 * 
	 * @param attrMap 属性集合
	 */
	public void setAttrMap(Map<String, String> attrMap) {
		this.attrMap = attrMap;
	}
}
