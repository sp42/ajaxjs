package com.ajaxjs.storage.block;

/**
 * Block 异常
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public class BlockException extends RuntimeException {
	private static final long serialVersionUID = -2028830577421957587L;

	private Long blockId;

	private String uploadId;

	private Integer partNumber;

	public BlockException() {
	}

	public BlockException(String message) {
		super(message);
	}

	public BlockException(String message, Throwable cause) {
		super(message, cause);
	}

	public BlockException(Throwable cause) {
		super(cause);
	}

	public BlockException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
