package com.ajaxjs.framework;

public class BusinessException extends RuntimeException {
    public BusinessException() {

    }

    public BusinessException(String msg) {
        super(msg);
    }
}
