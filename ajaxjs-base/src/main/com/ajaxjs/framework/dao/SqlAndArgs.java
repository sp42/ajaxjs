package com.ajaxjs.framework.dao;

import java.lang.reflect.Method;

/**
 * Java can't return multiple values, so make a class to hold them.
 * 
 * @author Frank Cheung
 *
 */
public class SqlAndArgs {
	/**
	 * 修改过的 sql
	 */
	public String sql;

	/**
	 * 修改过的参数
	 */
	public Object[] args;

	/**
	 * 
	 */
	public Method method;
}
