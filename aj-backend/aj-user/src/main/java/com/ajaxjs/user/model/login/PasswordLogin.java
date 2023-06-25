package com.ajaxjs.user.model.login;

import com.ajaxjs.framework.IBaseModel;
import lombok.Data;

/**
 * 普通的用户密码登录
 */
@Data
public class PasswordLogin implements IBaseModel {
    /**
     * 用户 id
     */
    private String loginId;

    /**
     * 密码，明文
     */
    private String password;
}
