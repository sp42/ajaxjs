package com.ajaxjs.util.aop;

/**
 * 
 * @author xinzhang
 *
 */
public class AopException extends Exception {
	private static final long serialVersionUID = 5423010532958020537L;

	/**
	 * 
	 * @param msg
	 * @param type
	 *            before|after
	 */
	public AopException(String msg, String type) {
		super(msg);
	}

}
