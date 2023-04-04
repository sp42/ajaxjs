package com.ajaxjs.sql.inject_attack;

/**
 * SQL注入攻击异常
 *
 * @author guyadong
 */
public class InjectionAttackException extends RuntimeException {
    private static final long serialVersionUID = -7635324112596781112L;

    public InjectionAttackException() {
        super();
    }

    public InjectionAttackException(String message, Throwable cause) {
        super(message, cause);
    }

    public InjectionAttackException(String message) {
        super(message);
    }

    public InjectionAttackException(Throwable cause) {
        super(cause);
    }
}