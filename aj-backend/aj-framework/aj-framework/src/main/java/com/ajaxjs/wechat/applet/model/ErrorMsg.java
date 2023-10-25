package com.ajaxjs.wechat.applet.model;

import lombok.Data;

/**
 * 异常信息
 *
 * @author Frank Cheung
 */
@Data
public class ErrorMsg {
    /**
     * 错误码
     */
    private Integer errcode;

    /**
     * 错误信息
     */
    private String errmsg;
}
