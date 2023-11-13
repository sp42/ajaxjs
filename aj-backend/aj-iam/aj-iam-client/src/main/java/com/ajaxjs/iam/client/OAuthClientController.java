package com.ajaxjs.iam.client;

import com.ajaxjs.iam.Utils;
import com.ajaxjs.iam.client.model.User;
import org.springframework.beans.factory.annotation.Value;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * OAuth 授权客户端控制器
 */
@RestController
@RequestMapping("/oauth_client")
public class OAuthClientController {
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
                .queryParam("state", Utils.getRandomString(5))   // 推荐，不透明字符串，当OP重定向到redirect_uri时，会原样返回给RP，用于防止 CSRF、 XSRF。
                // 由于OP会原样返回此参数，可将 state 值与用户在RP登录前最后浏览的URI绑定，便于登录完成后将用户重定向回最后浏览的页面
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
//        if (responseEntity.getStatusCode().is2xxSuccessful()) {
//            // 处理授权成功的逻辑，例如解析并保存访问令牌和刷新令牌等
//            return "success";
//        } else {
//            // 处理授权失败的逻辑
//            return "error";
//        }
    }


    @Value("${User.home}")
    private String userHome;

    static final String GRANT_TYPE = "authorization_code";

    /**
     * 发起客户端认证
     *
     * @param code
     * @param req
     * @return
     */
//    @GetMapping("clientLogin")
//	public Map<String, Object> clientLogin(@RequestParam String code, HttpServletRequest req) {
//		Map<String, Object> params = new HashMap<>();
//		params.put("code", code);
//		params.put("grant_type", GRANT_TYPE);
//		params.put("client_id", clientId);
//		params.put("client_secret", clientSecret);
//
//		Map<String, Object> result = Post.api(api + "/sso/authorize", params);
//
//		UserSession saveSession = saveSession(result);
//		// 存入 session
//		req.getSession().setAttribute(saveSession.accessToken.getAccessToken(), saveSession);
//
//		if ("${User.home}".equals(userHome))
//			return result;
//		else
//			return null;
//
////		return "${User.home}".equals(userHome) ?result : "redirect:/" + userHome;
//	}

    /**
     * JSON 结果转换为 Session 存储，形成本地登录状态
     *
     * @param result
     * @return
     */
//	static UserSession saveSession(Map<String, Object> result) {
//		AccessToken accessToken = new AccessToken();
//		accessToken.setAccessToken(result.get("access_token").toString());
//		accessToken.setRefreshToken(result.get("refresh_token").toString());
//		accessToken.setScope(result.get("scope").toString());
//		accessToken.setExpiresIn(((Integer) result.get("expires_in")).longValue());
//
//		@SuppressWarnings("unchecked")
//		Map<String, Object> userJson = (Map<String, Object>) result.get("user");
//		SimpleUser user = MapTool.map2Bean(userJson, SimpleUser.class, true);
//
//		UserSession userSession = new UserSession();
//		userSession.accessToken = accessToken;
//		userSession.user = user;
//
//		return userSession;
//	}

    /**
     * Session 中获取 user 的 key
     */
    public static final String USER_SESSION_KEY = "USER";

    public static User getLoginedUser(HttpServletRequest req) {
        return getLoginedUser(req.getSession());
    }

    /**
     * 获取用户
     */
    public static User getLoginedUser(HttpSession session) {
        Object obj = session.getAttribute(USER_SESSION_KEY);

        if (obj == null)
            throw new IllegalAccessError("用户未登录，非法访问");

        if (!(obj instanceof User))
            throw new IllegalStateException("用户不是 User 类型");

        return (User) obj;
    }

    /**
     * 用户是否已登录
     *
     * @return true=已登录
     */
    public static boolean isLogin(HttpServletRequest req) {
        return isLogin(req.getSession());
    }

    /**
     * 用户是否已登录
     *
     * @return true=已登录
     */
    public static boolean isLogin(HttpSession session) {
        return session.getAttribute(USER_SESSION_KEY) != null;
    }
}
