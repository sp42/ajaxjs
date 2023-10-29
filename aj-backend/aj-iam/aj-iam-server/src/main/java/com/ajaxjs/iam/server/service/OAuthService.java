package com.ajaxjs.iam.server.service;

import com.ajaxjs.data.CRUD;
import com.ajaxjs.framework.BusinessException;
import com.ajaxjs.iam.server.common.IamConstants;
import com.ajaxjs.iam.server.controller.OAuthController;
import com.ajaxjs.iam.server.model.AccessToken;
import com.ajaxjs.iam.server.model.po.App;
import com.ajaxjs.iam.server.model.po.OauthAccessToken;
import com.ajaxjs.util.StrUtil;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
public class OAuthService implements OAuthController, IamConstants {
    @Override
    public AccessToken clientCredentials(String grantType, String clientId, String clientSecret) {
        if (!GrantType.CLIENT_CREDENTIALS.equals(grantType))
            throw new IllegalArgumentException("grantType must be 'clientCredentials'");

        App token = CRUD.info(App.class, "SELECT * FROM app WHERE stat = 1 AND client_id = ? AND client_secret = ?", clientId, clientSecret);

        if (token == null)
            throw new BusinessException("应用不存在或非法密钥");

        AccessToken accessToken = new AccessToken();
        accessToken.setAccess_token(StrUtil.uuid(false));
        accessToken.setRefresh_token(StrUtil.uuid(false));

        Integer minutes = token.getExpires();
        if (minutes == null)
            minutes = 120; // default value

        accessToken.setExpires_in(minutes * 60);

        // 保存 token
        OauthAccessToken save = new OauthAccessToken();
        save.setAccessToken(accessToken.getAccess_token());
        save.setRefreshToken(accessToken.getRefresh_token());
        save.setExpiresDate(calculateExpirationDate(minutes));
        save.setGrantType(GrantType.CLIENT_CREDENTIALS);
        save.setClientId(clientId);
        save.setCreateDate(new Date());

        CRUD.create(save);

        return accessToken;
    }

    /**
     * 将到期的分钟数转换为到期的时间
     */
    public static Date calculateExpirationDate(int minutesToExpiration) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, minutesToExpiration);

        return calendar.getTime();
    }

    @Override
    public AccessToken clientCredentialsWithBasic(String grantType, String authorization) {
        String base64Str = StrUtil.base64Decode(authorization);
        String[] arr = base64Str.split(":");

        return clientCredentials(grantType, arr[0], arr[1]);
    }

    @Override
    public AccessToken refreshToken(String grantType, String authorization, String refreshToken) {
        if (!GrantType.REFRESH_TOKEN.equals(grantType))
            throw new IllegalArgumentException("grantType must be 'refresh_token'");

        OauthAccessToken accessToken = CRUD.info(OauthAccessToken.class, "SELECT * FROM oauth_access_token WHERE refresh_token = ?", refreshToken);

        if (accessToken == null)
            throw new BusinessException("找不到 RefreshToken " + refreshToken);


        return null;
    }
}
