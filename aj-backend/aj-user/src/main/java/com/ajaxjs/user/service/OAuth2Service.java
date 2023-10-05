package com.ajaxjs.user.service;

import com.ajaxjs.user.model.AccessToken;
import com.ajaxjs.user.model.User;
import com.ajaxjs.util.Digest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class OAuth2Service {
    /**
     * 生成授权码（Authorization Code）
     *
     * @param clientId    客户端标识符，表示 OAuth 客户端的唯一标识
     * @param scope       作用域，表示客户端请求的权限范围。可以为 null
     * @param loginedUser 已登录的用户信息
     * @return 授权码
     */
    public String createAuthorizationCode(String clientId, String scope, User loginedUser) {
        String code;

        if (!StringUtils.hasText(scope))
            scope = "DEFAULT_SCOPE";

        String str = clientId + scope + System.currentTimeMillis();

        code = Digest.getSHA1(str);

        return code;
    }

    public AccessToken token(String clientId, String clientSecret, String code) {
        AccessToken accessToken = new AccessToken();

        return accessToken;
    }
}
