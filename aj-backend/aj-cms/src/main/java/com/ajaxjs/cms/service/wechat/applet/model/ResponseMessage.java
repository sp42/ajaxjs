package com.ajaxjs.cms.service.wechat.applet.model;

import lombok.Data;

/**
 * 通用的异常信息，微信返回
 */
@Data
public abstract class ResponseMessage {
    private String code;

    private String message;

    /**
     * 是否成功
     */
    private Boolean isOk;
}