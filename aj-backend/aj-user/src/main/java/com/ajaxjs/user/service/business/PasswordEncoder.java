package com.ajaxjs.user.service.business;

import com.ajaxjs.util.Digest;

/**
 * 密码加密规则
 */
public class PasswordEncoder {
    /**
     * 盐值
     */
    private static final String SALT = "@#D2s!As12";

    /**
     * 基本的密码加密
     *
     * @param psw 明文
     * @return 密文
     */
    public static String md5salt(String psw) {
        return Digest.md5(psw + SALT).toLowerCase();
    }
}
