package com.ajaxjs.workflow;

/**
 * 框架抛出的所有异常都是此类（unchecked exception）
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class WorkflowException extends RuntimeException {
	private static final long serialVersionUID = -5220859421440167454L;

	public WorkflowException() {
		super();
	}

	public WorkflowException(String msg, Throwable cause) {
		super(msg);
		initCause(cause);
	}

	public WorkflowException(String msg) {
		super(msg);
	}

	public WorkflowException(Throwable cause) {
		super();
		initCause(cause);
	}

	public WorkflowException(String tpl, String... args) {
		super(String.format(tpl, args));
	}

}
