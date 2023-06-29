package com.ajaxjs.jwt;

import com.ajaxjs.framework.IBaseModel;
import lombok.Data;

/**
 * JWT 基础载荷
 */
@Data
public class Payload implements IBaseModel {
    /**
     * 主题
     */
    private String sub;

    /**
     * 受众
     */
    private String aud;

    /**
     * 过期时间
     */
    private Long exp;

    /**
     * 签发人
     */
    private String iss;

    /**
     * 签发时间
     */
    private Long iat;

//    /**
//     * 编号，似乎不需要
//     */
//    private String jti;
}
