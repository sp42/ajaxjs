package com.ajaxjs.developertools.api;

import com.ajaxjs.iam.Utils;
import com.ajaxjs.iam.client.BaseOidcClientUserController;
import com.ajaxjs.iam.client.JWebTokenMgr;
import com.ajaxjs.iam.client.model.JWebToken;
import com.ajaxjs.iam.client.model.JwtAccessToken;
import com.ajaxjs.iam.client.model.User;
import com.ajaxjs.util.StrUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Objects;

@RestController
@RequestMapping("/user")
public class UserController extends BaseOidcClientUserController {
    @Value("${user.loginPage}")
    private String userLoginCode;

    @Value("${user.clientId}")
    private String clientId;

    @Value("${user.clientSecret}")
    private String clientSecret;

    @Value("${website.url}")
    private String websiteUrl;

    /**
     * @param webUrl 前端页面地址，用于跳到这里以便获取 Token
     */
    @GetMapping("/login")
    public RedirectView get(@RequestParam(value = "web_url", required = false) String webUrl) {
        String url = userLoginCode + "?response_type=code&client_id=" + clientId;
        url += "&redirect_uri=" + StrUtil.urlEncode(websiteUrl + "/user/callback");
        url += "&state=" + StrUtil.getRandomString(6);

        if (StringUtils.hasText(webUrl))
            url += "&web_url=" + StrUtil.urlEncode(webUrl);

        return new RedirectView(url);
    }

    /**
     * @param webUrl 前端页面地址，用于跳到这里以便获取 Token
     */
    @RequestMapping("/callback")
    public ModelAndView token(@RequestParam String code, @RequestParam(required = false) String state,
                              @RequestParam(value = "web_url", required = false) String webUrl,
                              HttpSession session) {
//        restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(clientId, clientSecret));// basic 认证

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(clientId, clientSecret);

        MultiValueMap<String, String> bodyParams = new LinkedMultiValueMap<>();
        bodyParams.add("grant_type", "authorization_code");
        bodyParams.add("code", code);
        bodyParams.add("state", state);

        RestTemplate restTemplate = getRestTemplate();

        try {
            ResponseEntity<JwtAccessToken> responseEntity = restTemplate.exchange(getTokenApi(), HttpMethod.POST,
                    new HttpEntity<>(bodyParams, headers), new ParameterizedTypeReference<JwtAccessToken>() {
                    });
//		ResponseEntity<String> responseEntity = getRestTemplate().exchange(getTokenApi(), HttpMethod.POST,
//				new HttpEntity<>(bodyParams, headers), String.class);

            if (responseEntity.getStatusCode().is2xxSuccessful()) {// 处理授权成功的逻辑，例如解析并保存访问令牌和刷新令牌等
                JwtAccessToken jwt = onAccessTokenGot(Objects.requireNonNull(responseEntity.getBody()), session);

                if (StringUtils.hasText(webUrl)) {
                    String jwtJson = Utils.bean2json(jwt);

                    return new ModelAndView(new RedirectView(webUrl + "?token=" + StrUtil.urlEncode(jwtJson)));
                } else
                    return new ModelAndView("redirect:/");
            } else {
                System.out.println(responseEntity);
//			 处理授权失败的逻辑
                throw new SecurityException("获取 JWT Token 是吧");
            }
        } catch (Throwable e) {
            e.printStackTrace();

            return null;
        }
    }

    @Value("${user.jwtSecretKey}")
    private String jwtSecretKey;

    @Bean
    JWebTokenMgr jWebTokenMgr() {
        JWebTokenMgr mgr = new JWebTokenMgr();
        mgr.setSecretKey(jwtSecretKey);

        return mgr;
    }

    @Override
    public JwtAccessToken onAccessTokenGot(JwtAccessToken token, HttpSession session) {
        String idToken = token.getId_token();
        JWebTokenMgr mgr = jWebTokenMgr();
        JWebToken jwt = mgr.parse(idToken);

        if (mgr.isValid(jwt)) {
            User user = new User();
            user.setId(Long.parseLong(jwt.getPayload().getSub()));
            user.setName(jwt.getPayload().getName());

            return token;
//			AccessToken accessToken = new AccessToken();
//			BeanUtils.copyProperties((AccessToken) token, accessToken);
//			user.setAccessToken(accessToken);
//			session.setAttribute(UserLogined.USER_IN_SESSION, user);
        } else
            throw new SecurityException("返回非法 JWT Token");
    }
}
