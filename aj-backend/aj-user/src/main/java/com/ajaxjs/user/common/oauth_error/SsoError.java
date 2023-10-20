package com.ajaxjs.user.common.oauth_error;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * SSO 错误
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SsoError extends SecurityException {
    private static final long serialVersionUID = -5073040457147935345L;

    /**
     * 错误代码
     */
    private String error;

    /**
     * 错误描述
     */
    private String error_description;

    /**
     * @param error            错误代码
     * @param errorDescription 错误描述
     */
    public SsoError(String error, String errorDescription) {
        super(errorDescription);

        this.error = error;
        error_description = errorDescription;
    }

    /**
     * 输出符合 Oauth 规范的异常
     */
    public static SecurityException oauthError(String errCode, String errMsg) {
        return new SsoError(errCode, errMsg);
    }

    /**
     * 输出符合 Oauth 规范的异常
     */
    public static SecurityException oauthError(String errCode, Throwable e) {
        return oauthError(errCode, e.getMessage());
    }

    /**
     * 输出符合 Oauth 规范的异常
     */
    public static SecurityException oauthError(ErrorCodeEnum enumErr) {
        return oauthError(enumErr.getError(), enumErr.getErrorDescription());
    }
}
