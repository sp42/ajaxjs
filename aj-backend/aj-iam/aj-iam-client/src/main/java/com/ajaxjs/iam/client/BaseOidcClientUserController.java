package com.ajaxjs.iam.client;

import javax.servlet.http.HttpSession;

import com.ajaxjs.iam.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.ajaxjs.iam.jwt.JwtAccessToken;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;
import org.springframework.web.servlet.view.RedirectView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Data
public abstract class BaseOidcClientUserController {
    @Value("${user.tokenApi}")
    private String tokenApi;

    @Autowired(required = false)
    RestTemplate restTemplate;

    public RestTemplate getRestTemplate() {
        if (restTemplate == null) {
            SkipSSL.init(); // 忽略 SSL 证书
            restTemplate = new RestTemplate();

            MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
            converter.setObjectMapper(new ObjectMapper());
            restTemplate.getMessageConverters().add(converter);
        }

        return restTemplate;
    }

    /**
     * 获取重定向视图，用于认证流程中的第一步：跳转到登录页面。
     *
     * @param session       HttpSession对象，用于保存和管理用户会话信息。
     * @param userLoginCode 用户登录代码，通常是登录页面的URL后缀。
     * @param clientId      客户端ID，用于识别请求OAuth服务的应用。
     * @param websiteUrl    应用的网站URL，授权服务器完成授权后会重定向到该URL的回调接口。
     * @return RedirectView 返回一个重定向视图对象，包含了构造的重定向URL。
     */
    public RedirectView loginPageUrl(HttpSession session, String userLoginCode, String clientId, String websiteUrl) {
        String state = JwtUtils.getRandomString(5);
        session.setAttribute(ClientUtils.OAUTH_STATE, state);// 将 state 值保存到会话中

        String url = userLoginCode + "?response_type=code&client_id=" + clientId;
        url += "&redirect_uri=" + urlEncode(websiteUrl + "/user/callback");
        url += "&state=" + state;

        return new RedirectView(url);
    }

    /**
     * UTF-8 字符串而已
     */
    public static final String UTF8_SYMBOL = "UTF-8";

    /**
     * URL 编码
     *
     * @param str 输入的字符串
     * @return URL 编码后的字符串
     */
    public static String urlEncode(String str) {
        try {
            return URLEncoder.encode(str, UTF8_SYMBOL);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * 用 AccessToken 可用的时候
     */
    public abstract JwtAccessToken onAccessTokenGot(JwtAccessToken token, HttpSession session);
}
