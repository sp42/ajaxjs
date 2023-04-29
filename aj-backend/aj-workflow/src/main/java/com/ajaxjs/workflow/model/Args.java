package com.ajaxjs.workflow.model;

import java.util.HashMap;

/**
 * 参数
 * 
 * @author Frank Cheung sp42@qq.com
 *
 */
public class Args extends HashMap<String, Object> {
	private static final long serialVersionUID = -3326234439640990986L;

	/**
	 * 如果为空则返回一个空的 Args，避免 null
	 * 
	 * @param args
	 * @return
	 */
	public static Args getEmpty(Args args) {
		return args == null ? new Args() : args;
	}
}
