package com.ajaxjs.iam.server.service;

import com.ajaxjs.data.CRUD;
import com.ajaxjs.framework.BusinessException;
import com.ajaxjs.iam.server.common.IamConstants;
import com.ajaxjs.iam.server.common.IamUtils;
import com.ajaxjs.iam.server.model.AccessToken;
import com.ajaxjs.iam.server.model.po.AccessTokenPo;
import com.ajaxjs.iam.server.model.po.App;
import com.ajaxjs.iam.user.common.session.UserSession;
import com.ajaxjs.iam.user.model.User;
import com.ajaxjs.util.MessageDigestHelper;
import com.ajaxjs.util.StrUtil;
import com.ajaxjs.util.cache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.Date;

public abstract class OAuthCommon implements IamConstants {
    static final String NOT_LOGIN_TEXT = "<meta http-equiv=\"refresh\" content=\"2;url=%s\" /> 用户尚未登录，两秒后跳转到登录页面……";

    @Autowired
    UserSession userSession;

    /**
     * @param webUrl 前端页面地址，用于跳到这里以便获取 Token
     */
    public void sendAuthCode(String responseType, String clientId, String redirectUri, String scope, String state, String webUrl, HttpServletRequest req, HttpServletResponse resp, Cache<String, Object> cache) {
        if (!"code".equals(responseType)) throw new IllegalArgumentException("参数 response_type 只能是 code");

        User user = userSession.getUserFromSession();

        if (user == null) { // 未登录
            // 返回一段 HTML
            String qs = req.getQueryString();
            String html = String.format(NOT_LOGIN_TEXT, "../login?" + qs);
            IamUtils.responseHTML(resp, html);
        } else {// 已登录，发送授权码
            StringBuilder sb = new StringBuilder();
            sb.append("?state=").append(state);
            // 生成授权码（Authorization Code）
            String code = MessageDigestHelper.getSHA1(clientId + StrUtil.getRandomString(6));
            sb.append("&code=").append(code);

            if (StringUtils.hasText(webUrl)) sb.append("&web_url=").append(webUrl);

            if (!StringUtils.hasText(scope)) scope = "DEFAULT_SCOPE";

            if (user.getTenantId() != null)
                scope += ";tenantId=" + user.getTenantId();

            cache.put(code + ":user", user, AUTHORIZATION_CODE_TIMEOUT); // 保存本次请求所属的用户信息
            cache.put(code + ":scope", scope, AUTHORIZATION_CODE_TIMEOUT);// 保存本次请求的授权范围

            IamUtils.send303Redirect(resp, redirectUri + sb); // 跳转到客户机
        }
    }

    /**
     * 根据客户端 ID 和密钥获取应用信息。
     *
     * @param clientId     应用的客户端 ID。
     * @param clientSecret 应用的客户端密钥。
     * @return 返回匹配的 App 对象，如果不存在或密钥不合法，则抛出 BusinessException 异常。
     * @throws BusinessException 如果应用不存在或提供的密钥非法，则抛出此异常。
     */
    public App getApp(String clientId, String clientSecret) {
        // 通过 CRUD 操作，查询应用信息，条件是状态为0、客户端 ID 和密钥匹配
        App app = CRUD.info(App.class, "SELECT * FROM app WHERE stat = 0 AND client_id = ? AND client_secret = ?", clientId, clientSecret);

        if (app == null) throw new BusinessException("应用不存在或非法密钥"); // 如果查询结果为空，表示没有找到对应的应用或密钥不正确，抛出业务异常

        return app;
    }

    /**
     * 根据 HTTP 头的 authorization 获取 App 信息
     */
    public App getAppByAuthHeader(String authorization) {
        authorization = authorization.replaceAll("Basic ", "");
        String base64Str = StrUtil.base64Decode(authorization);

        if (!base64Str.contains(":")) throw new IllegalArgumentException("非法 Token");

        String[] arr = base64Str.split(":");
        String clientId = arr[0], clientSecret = arr[1];

        return getApp(clientId, clientSecret);
    }

    /**
     * Token 的有效期，单位：分钟  默认两天
     */
    @Value("${oauth.token.client_expires: 2880}")
    private Integer clientExpires;

    /**
     * 获取令牌的过期时间（以毫秒为单位）。
     * <p>
     * 根据应用的设置确定令牌的过期时间。如果应用设置了具体的过期分钟数，则使用该值；
     * 否则，使用默认的客户端过期时间。
     *
     * @param app 应用对象，包含令牌过期信息。
     * @return 令牌的过期时间，以毫秒为单位。
     */
    public long getTokenExpires(App app) {
        // 确定令牌的过期时间，优先使用应用设置的过期时间，若无则使用默认的客户端过期时间
        Integer minutes = app.getExpires() == null ? clientExpires : app.getExpires();

        // 将分钟转换为毫秒作为过期时间返回
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
