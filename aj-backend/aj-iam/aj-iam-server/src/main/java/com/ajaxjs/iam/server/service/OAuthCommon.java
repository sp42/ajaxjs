package com.ajaxjs.iam.server.service;

import com.ajaxjs.data.CRUD;
import com.ajaxjs.framework.BusinessException;
import com.ajaxjs.iam.server.common.IamConstants;
import com.ajaxjs.iam.server.model.AccessToken;
import com.ajaxjs.iam.server.model.po.AccessTokenPo;
import com.ajaxjs.iam.server.model.po.App;
import com.ajaxjs.util.StrUtil;
import org.springframework.beans.factory.annotation.Value;

import java.util.Calendar;
import java.util.Date;

public abstract class OAuthCommon implements IamConstants {
    public App getApp(String clientId, String clientSecret) {
        App app = CRUD.info(App.class, "SELECT * FROM app WHERE stat = 1 AND client_id = ? AND client_secret = ?", clientId, clientSecret);

        if (app == null)
            throw new BusinessException("应用不存在或非法密钥");

        return app;
    }

    static String[] getClientInfo(String authorization) {
        authorization = authorization.replaceAll("Basic ", "");
        String base64Str = StrUtil.base64Decode(authorization);

        return base64Str.split(":");
    }

    /**
     * 根据 HTTP 头的 authorization 获取 App 信息
     */
    public App getAppByAuthHeader(String authorization) {
        authorization = authorization.replaceAll("Basic ", "");
        String base64Str = StrUtil.base64Decode(authorization);
        String[] arr = base64Str.split(":");
        String clientId = arr[0], clientSecret = arr[1];

        return getApp(clientId, clientSecret);
    }

    @Value("${oauth.token.client_expires: 120}")
    private Integer clientExpires;

    public long getTokenExpires(App app) {
        Integer minutes = app.getExpires() == null ? clientExpires : app.getExpires();

        return minutes * 60 * 1000;
    }

    /**
     * 创建 Token
     */
    public void createToken(AccessToken accessToken, App app, String grantType) {
        accessToken.setAccess_token(StrUtil.uuid(false));
        accessToken.setRefresh_token(StrUtil.uuid(false));

        Integer minutes = app.getExpires() == null ? clientExpires : app.getExpires();
        accessToken.setExpires_in(minutes * 60);

        // 保存 token
        AccessTokenPo save = new AccessTokenPo();
        save.setAccessToken(accessToken.getAccess_token());
        save.setRefreshToken(accessToken.getRefresh_token());
        save.setExpiresDate(calculateExpirationDate(minutes));
        save.setGrantType(grantType);
        save.setClientId(app.getClientId());
        save.setCreateDate(new Date());

        CRUD.create(save);
    }

    /**
     * 将到期的分钟数转换为到期的时间
     */
    public static Date calculateExpirationDate(int minutesToExpiration) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, minutesToExpiration);

        return calendar.getTime();
    }

}
