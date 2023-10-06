package com.ajaxjs.model;

/**
 * 授权方式
 *
 * @author Frank Cheung
 */
public enum GrantTypeEnum {
    /**
     * 授权码模式
     */
    AUTHORIZATION_CODE("authorization_code");

    private final String type;

    /**
     * 授权方式，当前只有 authorization_code
     */
    GrantTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
