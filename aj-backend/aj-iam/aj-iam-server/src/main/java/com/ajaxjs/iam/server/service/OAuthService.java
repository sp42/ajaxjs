package com.ajaxjs.iam.server.service;

import com.ajaxjs.data.CRUD;
import com.ajaxjs.framework.BusinessException;
import com.ajaxjs.framework.spring.filter.dbconnection.EnableTransaction;
import com.ajaxjs.iam.server.common.IamConstants;
import com.ajaxjs.iam.server.controller.OAuthController;
import com.ajaxjs.iam.server.model.AccessToken;
import com.ajaxjs.iam.server.model.po.App;
import com.ajaxjs.iam.server.model.po.AccessTokenPo;
import com.ajaxjs.iam.user.model.User;
import com.ajaxjs.util.Digest;
import com.ajaxjs.util.StrUtil;
import com.ajaxjs.util.cache.Cache;
import com.ajaxjs.util.logger.LogHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;

import java.util.Calendar;
import java.util.Date;

@Service
public class OAuthService implements OAuthController, IamConstants {
    private static final LogHelper LOGGER = LogHelper.getLog(OAuthService.class);

    @Autowired(required = false)
    Cache<String, Object> cache;

    /**
     * 授权码超时 5分钟
     */
    private static final int AUTHORIZATION_CODE_TIMEOUT = 5 * 60 * 100;

    @Override
    public ModelAndView agree(String clientId, String redirectUri, String scope, String status) {
        return null;
    }

    @Override
    public ModelAndView authorize(String clientId, String redirectUri, String scope, String status) {
        User user = new User();
        user.setId(1L);
        user.setUsername("admin");
        User loginedUser = user;

        if (!StringUtils.hasText(scope)) scope = "DEFAULT_SCOPE";

        // 生成授权码（Authorization Code）
        String code = Digest.getSHA1(clientId + scope + System.currentTimeMillis());
        String params = "?code=" + code;

        if (StringUtils.hasText(status)) params += "&status=" + status;

        cache.put(code + ":user", loginedUser, AUTHORIZATION_CODE_TIMEOUT); // 保存本次请求所属的用户信息
        cache.put(code + ":scope", scope, AUTHORIZATION_CODE_TIMEOUT);// 保存本次请求的授权范围

        // 检测用户已经登录，如果没跳到登录页面让用户输入帐密
        // 如果已经登录，则提示转到一个页面，询问用户是否同意，授权可访问
        // 若是则生成 code，跳转到 redirectUri，那是一个回调

        return new ModelAndView("redirect:" + redirectUri + params);
    }

    @Autowired
    ClientService clientService;

    @Override
    public AccessToken getToken(String clientId, String clientSecret, String code) {
        App app = clientService.getApp(clientId, clientSecret);

        String scope = cache.get(code + ":scope", String.class);
        User user = cache.get(code + ":user", User.class);

        // 如果能够通过 Authorization Code 获取到对应的用户信息，则说明该 Authorization Code 有效
        if (StringUtils.hasText(scope) && user != null) {
//             生成 Access Token
//            AccessToken accessToken = createAccessToken(user, client, scope);
//            // 查询已经插入到数据库的 Access Token
////			AccessToken accessToken = AcessTokenDAO.setWhereQuery("accessToken", accessTokenStr).findOne();
////			LOGGER.info(accessToken.getExpiresIn());
//            // 生成 Refresh Token
//            accessToken.setRefresh_token(createRefreshToken(user, accessToken));

//            return accessToken;
        }

        return null;
    }

    @Value("${oauth.token.client_expires: 120}")
    private Integer clientExpires;

    @Value("${oauth.token.user_expires: 120}")
    private Integer userExpires;


    @Override
    public AccessToken clientCredentials(String grantType, String clientId, String clientSecret) {
        if (!GrantType.CLIENT_CREDENTIALS.equals(grantType))
            throw new IllegalArgumentException("grantType must be 'clientCredentials'");

        App app = clientService.getApp(clientId, clientSecret);

        AccessToken accessToken = new AccessToken();
        createToken(accessToken, app);

        return accessToken;
    }

    public void createToken(AccessToken accessToken, App app) {
        accessToken.setAccess_token(StrUtil.uuid(false));
        accessToken.setRefresh_token(StrUtil.uuid(false));

        Integer minutes = app.getExpires() == null ? clientExpires : app.getExpires();
        accessToken.setExpires_in(minutes * 60);

        // 保存 token
        AccessTokenPo save = new AccessTokenPo();
        save.setAccessToken(accessToken.getAccess_token());
        save.setRefreshToken(accessToken.getRefresh_token());
        save.setExpiresDate(calculateExpirationDate(minutes));
        save.setGrantType(GrantType.CLIENT_CREDENTIALS);
        save.setClientId(app.getClientId());
        save.setCreateDate(new Date());

        CRUD.create(save);
    }

    public long getTokenExpires(App app) {
        Integer minutes = app.getExpires() == null ? clientExpires : app.getExpires();

        return minutes * 60 * 1000;
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
        String[] arr = getClientInfo(authorization);

        return clientCredentials(grantType, arr[0], arr[1]);
    }

    @Override
    @EnableTransaction
    public AccessToken refreshToken(String grantType, String clientId, String clientSecret, String refreshToken) {
        if (!GrantType.REFRESH_TOKEN.equals(grantType))
            throw new IllegalArgumentException("grantType must be 'refresh_token'");

        AccessTokenPo accessTokenPO = CRUD.info(AccessTokenPo.class, "SELECT * FROM access_token WHERE refresh_token = ?", refreshToken);

        if (accessTokenPO == null) throw new BusinessException("找不到 RefreshToken " + refreshToken);

        AccessToken accessToken = new AccessToken();
        accessToken.setAccess_token(StrUtil.uuid(false));
        accessToken.setRefresh_token(StrUtil.uuid(false));

        // 获取超时
        Integer minutes;

        if (GrantType.CLIENT_CREDENTIALS.equals(accessTokenPO.getGrantType())) {
            // 客户端的 token
            if (!clientId.equals(accessTokenPO.getClientId())) throw new BusinessException("ClientId 不一致");

            minutes = CRUD.queryOne(Integer.class, "SELECT expires FROM app WHERE client_id = ?", clientId);

            if (minutes == null) minutes = 120; // default value
        } else minutes = userExpires;// 用户

        accessToken.setExpires_in(minutes * 60);

        // 修改旧的
        AccessTokenPo updated = new AccessTokenPo();
        updated.setId(accessTokenPO.getId());
        updated.setAccessToken(accessToken.getAccess_token());
        updated.setRefreshToken(accessToken.getRefresh_token());
        updated.setExpiresDate(calculateExpirationDate(minutes));

        CRUD.updateWithIdField(updated);

        return accessToken;
    }

    private static String[] getClientInfo(String authorization) {
        String base64Str = StrUtil.base64Decode(authorization);

        return base64Str.split(":");
    }

    @Override
    @EnableTransaction
    public AccessToken refreshTokenWithBasic(String grantType, String authorization, String refreshToken) {
        String[] arr = getClientInfo(authorization);

        return refreshToken(grantType, arr[0], arr[1], refreshToken);
    }

    @Override
    public String checkToken(String token) {
        return null;
    }

    @Override
    public Boolean revokeToken(String token) {
        return false;
    }

    @Override
    public AccessToken appLogin(String clientId, String clientSecret) {
        return null;
    }

    @Override
    public AccessToken appLogin(String clientId) {
        return null;
    }
}
