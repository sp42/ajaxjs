package com.ajaxjs.framework;

/**
 * 业务异常
 */
public class BusinessException extends RuntimeException {
    public BusinessException() {
    }

    /**
     * 业务异常
     *
     * @param msg 业务异常
     */
    public BusinessException(String msg) {
        super(msg);
    }
}
