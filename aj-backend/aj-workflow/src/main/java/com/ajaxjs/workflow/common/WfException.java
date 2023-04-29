package com.ajaxjs.workflow.common;

/**
 * 框架抛出的所有异常都是此类（unchecked exception）
 * 
 * @author Frank Cheung
 *
 */
public class WfException extends RuntimeException {
	private static final long serialVersionUID = -5220859421440167454L;

	public WfException() {
		super();
	}

	public WfException(String msg, Throwable cause) {
		super(msg);
		super.initCause(cause);
	}

	public WfException(String msg) {
		super(msg);
	}

	public WfException(Throwable cause) {
		super();
		super.initCause(cause);
	}
}
