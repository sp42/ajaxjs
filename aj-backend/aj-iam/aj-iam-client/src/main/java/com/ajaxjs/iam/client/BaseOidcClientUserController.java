package com.ajaxjs.iam.client;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ajaxjs.iam.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.ajaxjs.iam.jwt.JwtAccessToken;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Objects;

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
     * @param session       HttpSession 对象，用于保存和管理用户会话信息。
     * @param userLoginCode 用户登录代码，通常是登录页面的 URL 后缀。
     * @param clientId      客户端 ID，用于识别请求 OAuth 服务的应用。
     * @param websiteUrl    应用的网站 URL，授权服务器完成授权后会重定向到该 URL 的回调接口。
     * @param webUrl        前端页面地址，用于跳到这里以便获取 Token。
     * @return RedirectView 返回一个重定向视图对象，包含了构造的重定向URL。
     */
    public RedirectView loginPageUrl(HttpSession session, String userLoginCode, String clientId, String websiteUrl, String webUrl) {
        String state = JwtUtils.getRandomString(5);
        session.setAttribute(ClientUtils.OAUTH_STATE, state);// 将 state 值保存到会话中

        String url = userLoginCode + "?response_type=code&client_id=" + clientId;
        url += "&redirect_uri=" + urlEncode(websiteUrl + "/user/callback");
        url += "&state=" + state;

        if (StringUtils.hasText(webUrl))
            url += "&web_url=" + urlEncode(webUrl);

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

    public ModelAndView callbackToken(String clientId, String clientSecret, String code, String state, String webUrl, HttpSession session, HttpServletResponse resp) {
        // 从会话中获取之前保存的 state 值
        String savedState = (String) session.getAttribute(ClientUtils.OAUTH_STATE);

        if (!state.equals(savedState)) { // 检查返回的 state 值是否与之前保存的值匹配
            ClientUtils.returnForbidden(resp);

            return null;
        } else
            session.removeAttribute(ClientUtils.OAUTH_STATE);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(clientId, clientSecret);

        MultiValueMap<String, String> bodyParams = new LinkedMultiValueMap<>();
        bodyParams.add("grant_type", "authorization_code");
        bodyParams.add("code", code);
        bodyParams.add("state", state);

        RestTemplate restTemplate = getRestTemplate();

        ResponseEntity<JwtAccessToken> responseEntity = restTemplate.exchange(getTokenApi(), HttpMethod.POST,
                new HttpEntity<>(bodyParams, headers), new ParameterizedTypeReference<JwtAccessToken>() {
                });

        if (responseEntity.getStatusCode().is2xxSuccessful()) {// 处理授权成功的逻辑，例如解析并保存访问令牌和刷新令牌等
            JwtAccessToken jwt = onAccessTokenGot(Objects.requireNonNull(responseEntity.getBody()), session);

            if (StringUtils.hasText(webUrl)) {
//                String jwtJson = Utils.bean2json(jwt);

                return new ModelAndView(new RedirectView(webUrl + "?token=" + urlEncode(jwt.getId_token())));
            } else
                return new ModelAndView("redirect:/");
        } else {
            System.out.println(responseEntity);
//			 处理授权失败的逻辑
            throw new SecurityException("获取 JWT Token 是吧");
        }
    }
}
