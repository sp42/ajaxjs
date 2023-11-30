package com.ajaxjs.framework;

/**
 * 业务异常
 */
public class BusinessException extends RuntimeException {
    /**
     * 创建一个业务异常
     */
    public BusinessException() {
    }

    /**
     * 创建一个业务异常
     *
     * @param msg 业务异常的信息
     */
    public BusinessException(String msg) {
        super(msg);
    }
}
