package com.ajaxjs.wechat.applet.model;

import lombok.Data;

@Data
public class Code2SessionResult {
    /**
     * 会话密钥
     */
    private String session_key;

    /**
     * 用户在开放平台的唯一标识符，若当前小程序已绑定到微信开放平台帐号下会返回，详见 UnionID 机制说明。
     */
    private String unionid;

    /**
     * 用户唯一标识
     */
    private String openid;

    /**
     * 错误信息
     */
    private String errmsg;

    /**
     * 错误码
     * 调用示例
     */
    private Integer errcode;
}
