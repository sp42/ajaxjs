package com.ajaxjs.user;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.ajaxjs.model.AccessToken;
import com.ajaxjs.model.ErrorCodeEnum;
import com.ajaxjs.util.date.LocalDateUtils;

@Service
public class StateServiceImpl {
    public Boolean verify(String access_token) {
        AccessToken token = findByAccessToken(access_token);// 查询数据库中的 Access Token

        if (token == null)
            throw SsoError.oauthError(ErrorCodeEnum.INVALID_GRANT);

        long savedExpiresAt = token.getExpiresIn();// 过期日期
        LocalDateTime expiresDateTime = LocalDateUtils.ofEpochSecond(savedExpiresAt);
        System.out.println(expiresDateTime);

        // 如果 Access Token 已经失效，则返回错误提示
        if (LocalDateUtils.isTimeout(expiresDateTime)) {
            // TODO 是否要删除过期 token？
            throw SsoError.oauthError(ErrorCodeEnum.EXPIRED_TOKEN);
        }

        return true;
    }
}
