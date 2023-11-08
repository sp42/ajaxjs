package com.ajaxjs.iam.user.model;

import com.ajaxjs.framework.BaseModel;

/**
 * 租户
 */
public class Tenant extends BaseModel {
    /**
     * 租户编码
     */
    public String code;

    /**
     * 1、账号密码 2、二维码 3、手机验证码 4、微信授权 5、企业微信授权，一个租户类型可以选择多个登录方式，采用逗号隔开的方式
     */
    public String loginModes;
}
