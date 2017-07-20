package com.ajaxjs.json.syntax;

import java.lang.reflect.Method;

/**
 * 状态
 * @author Frank Cheung frank@ajaxjs.com
 */
public class State {
	public State(int index, String description, String expectDescription) {
		this.id = index;
		this.description = description;
		this.expectDescription = expectDescription;
	}
	
	public State(int index, String description, String expectDescription, Method handler) {
		this(index, description, expectDescription);
		this.setHandler(handler);
	}
	
	/**
	 * 索引
	 */
	private int id;
	
	/**
	 * 状态转换操作
	 */
	private Method handler;
	
	/**
	 * 描述
	 */
	private String description;
	
	/**
	 * 下一步期望的描述
	 */
	private String expectDescription;

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the expectDescription
	 */
	public String getExpectDescription() {
		return expectDescription;
	}

	/**
	 * @param expectDescription
	 *            the expectDescription to set
	 */
	public void setExpectDescription(String expectDescription) {
		this.expectDescription = expectDescription;
	}
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the handler
	 */
	public Method getHandler() {
		return handler;
	}
	/**
	 * @param handler the handler to set
	 */
	public void setHandler(Method handler) {
		this.handler = handler;
	}
}
