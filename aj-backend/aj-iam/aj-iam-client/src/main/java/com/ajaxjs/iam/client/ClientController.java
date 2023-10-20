package com.ajaxjs.iam.client;


import com.ajaxjs.util.StrUtil;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.beans.factory.annotation.Value;

@RestController
@RequestMapping("/iam_client")
public class ClientController {
    @Value("${client.id}")
    private String clientId;

    @Value("${client.secret}")
    private String clientSecret;

    @Value("${oauth.authorizeUrl}")
    private String authorizeUrl;

    @Value("${oauth.tokenUrl}")
    private String tokenUrl;

    /**
     * 跳转到 OP 登录页面。
     * 也可以在前端拼凑着 URL
     *
     * @return 跳转地址
     */
    @GetMapping("/login")
    public RedirectView redirectToLogin() {
        String redirectUri = "http://your-callback-url.com/oauth/callback";
        String scope = "openid profile email address phone"; // 定义需要获取的权限范围

        String authorizationUrl = UriComponentsBuilder.fromHttpUrl(authorizeUrl)
                .queryParam("response_type", "code") // 必选，希望 OP 采用哪种 OAuth 2.0 授权流程来响应，code 代表授权码流程
                .queryParam("client_id", clientId)          // 必选，RP 在 OP 注册的 client_id
                .queryParam("redirect_uri", redirectUri)    // 必选，用户登录成功后，OP 回传授权码等信息给RP的接口
                .queryParam("scope", scope)                 // 必选，申请获取的资源权限，必须包含 openid，表明申请获取 id token
                .queryParam("state", StrUtil.getRandomString(5))   // 推荐，不透明字符串，当OP重定向到redirect_uri时，会原样返回给RP，用于防止 CSRF、 XSRF。
                // 由于OP会原样返回此参数，可将 state 值与用户在RP登录前最后浏览的URI绑定，便于登录完成后将用户重定向回最后浏览的页面
                .queryParam("nonce", StrUtil.getRandomString(5))   // 可选，不透明字符串，当 OP 返回 id token 时，id token 中会原样包含此值，用于减少重播攻击
                .toUriString();

        return new RedirectView(authorizationUrl);
    }

    @RequestMapping("/callback")
    public void token(@RequestParam String code, @RequestParam(required = false) String state) {
        String redirectUri = "http://your-callback-url.com/oauth/callback";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(clientId, clientSecret);

        MultiValueMap<String, String> bodyParams = new LinkedMultiValueMap<>();
        bodyParams.add("grant_type", "authorization_code");
        bodyParams.add("code", code);
        bodyParams.add("redirect_uri", redirectUri);

        ResponseEntity<String> responseEntity = new RestTemplate().exchange(tokenUrl, HttpMethod.POST, new HttpEntity<>(bodyParams, headers), String.class);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            // 处理授权成功的逻辑，例如解析并保存访问令牌和刷新令牌等
            return "success";
        } else {
            // 处理授权失败的逻辑
            return "error";
        }
    }
}
