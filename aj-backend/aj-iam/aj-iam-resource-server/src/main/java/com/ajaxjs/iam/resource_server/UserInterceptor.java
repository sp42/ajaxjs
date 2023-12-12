package com.ajaxjs.iam.resource_server;

import com.ajaxjs.iam.jwt.JWebToken;
import com.ajaxjs.iam.jwt.JWebTokenMgr;
import com.ajaxjs.iam.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Enumeration;
import java.util.function.Function;

/**
 * 资源拦截器
 * 用法：
 * <code>
 *
 * @Bean UserInterceptor authInterceptor() {
 * return new UserInterceptor();
 * }
 * </code>
 */
@Slf4j
public class UserInterceptor implements HandlerInterceptor {
    @Value("${auth.run:true}")
    private String run;

    @Value("${auth.cacheType:jvm_hash}")
    private String cacheType;

    @Autowired(required = false)
    private StringRedisTemplate redis;

    /**
     * 本地可以获取用户信息
     * 这个回调函数决定如何获取
     */
    @Autowired(required = false)
    private Function<String, String> getUserFromJvmHash;

    /**
     * JWT 验证的密钥
     */
    @Value("${auth.jwtSecretKey}")
    private String jwtSecretKey;

    /**
     * JWT 解密
     */
    @Bean
    JWebTokenMgr jWebTokenMgr() {
        JWebTokenMgr mgr = new JWebTokenMgr();
        mgr.setSecretKey(jwtSecretKey);

        return mgr;
    }

    /*
     * 是否调试模式（开发模式）
     * 有两种模式：本地模式和远程模式（自动判断） 返回 true 表示是非 linux 环境，为开发调试的环境，即 isDebug = true； 返回
     * false 表示在部署的 linux 环境下。 Linux 的为远程模式
     */

    public static boolean isDebug;

    static {
        final String OS_NAME = System.getProperty("os.name").toLowerCase();
        isDebug = !(OS_NAME.contains("nix") || OS_NAME.contains("nux") || OS_NAME.indexOf("aix") > 0);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (isDebug && "1".equals(request.getParameter("allow"))) // 方便开发
            return true;
        else if (StringUtils.hasText(run) && Boolean.parseBoolean(run)) {
            String token = extractToken(request);

            if (!StringUtils.hasText(token))
                return returnErrorMsg(401, response);

            String jsonUser;

            switch (cacheType) {
                case "redis":
                    jsonUser = redis.opsForValue().get(UserConstants.REDIS_PREFIX + token);
                    break;
                case "jvm_hash":
                    if (getUserFromJvmHash == null) {
                        serverErr(response, "配置参数 jvm_hash 不正确");

                        return false;
                    } else
                        jsonUser = getUserFromJvmHash.apply(token);
                    break;
                case "jwt":
                    JWebTokenMgr mgr = jWebTokenMgr();
                    JWebToken jwt = mgr.parse(token);

                    if (mgr.isValid(jwt)) {
                        jsonUser = "{\"id\": %s, \"name\": \"%s\"}";
                        jsonUser = String.format(jsonUser, jwt.getPayload().getSub(), jwt.getPayload().getName());
                    } else {
//                        throw new SecurityException("返回非法 JWT Token");
                        returnErrorMsg(403, response);

                        return false;
                    }

                    break;
                default:
                    serverErr(response, "配置参数不正确");

                    return false;
            }
//                LOGGER.info("AuthInterceptor token={0}, jsonUser={1}", token, jsonUser);

            if (StringUtils.hasText(jsonUser)) {
                User user = Utils.jsonStr2Bean(jsonUser, User.class);
                request.setAttribute(UserConstants.USER_KEY_IN_REQUEST, user);

                return true;
            } else
                return returnErrorMsg(401, response);
        } else
            return true; // 关掉了认证
    }

    /**
     * 根据错误代码返回响应的信息
     *
     * @param status   错误代码
     * @param response 响应请求
     */
    private boolean returnErrorMsg(int status, HttpServletResponse response) {
        switch (status) {
            case 401:
                returnMsg(response, HttpStatus.UNAUTHORIZED.value(), "unauthorized", "未认证");
                break;
            case 403:
                returnMsg(response, HttpStatus.FORBIDDEN.value(), "forbidden", "没有权限");
                break;
            case 500:
                returnMsg(response, HttpStatus.INTERNAL_SERVER_ERROR.value(), "error", "认证失败");
                break;
        }

        return false;
    }

    private final static String ERR_JSON = "{\"error\":\"%s\",\"error_description\":\"%s\"}";

    private void serverErr(HttpServletResponse response, String msg) {
        returnMsg(response, HttpStatus.INTERNAL_SERVER_ERROR.value(), "error", msg);
    }

    /**
     * 返回响应信息
     *
     * @param resp        响应请求
     * @param httpErrCode 错误代码
     * @param title       错误标题
     * @param message     错误信息
     */
    private void returnMsg(HttpServletResponse resp, int httpErrCode, String title, String message) {
        returnMsg(resp, httpErrCode, String.format(ERR_JSON, title, message));
    }

    static void returnMsg(HttpServletResponse resp, int httpErrCode, String msg) {
        resp.setStatus(httpErrCode);
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=utf-8");

        try (PrintWriter writer = resp.getWriter()) {
            writer.write(msg);
        } catch (IOException e) {
            log.warn("err::", e);
        }
    }

    /**
     * 获取 expiresIn 与当前时间对比，看是否超时
     *
     * @param expiresIn 超时
     * @return true 表示超时
     */
    static boolean checkIfExpire(long expiresIn) {
        LocalDateTime expiresDateTime = LocalDateTime.ofEpochSecond(expiresIn, 0, ZoneOffset.UTC);// 过期日期

        return expiresDateTime.isBefore(LocalDateTime.now());
    }

    public String extractToken(HttpServletRequest request) {
        String token = extractHeaderToken(request);
        String authorization = request.getHeader("Authorization");
        String authorization2 = request.getHeader("authorization");

        if (token == null) {
            token = request.getHeader("token");

            if (token == null) {
                token = request.getParameter("access_token");

                if (token == null)
                    log.warn("Token not found in request parameters. Not an OAuth2 request.");
            }
        }

        return token;
    }

    private static final String BEARER_TYPE = "Bearer";

    private String extractHeaderToken(HttpServletRequest request) {
        Enumeration<String> headers = request.getHeaders("Authorization");
        String value;

        do {
            if (!headers.hasMoreElements())
                return null;

            value = headers.nextElement();
        } while (!value.toLowerCase().startsWith(BEARER_TYPE.toLowerCase()));

        String authHeaderValue = value.substring(BEARER_TYPE.length()).trim();
        int commaIndex = authHeaderValue.indexOf(44);

        if (commaIndex > 0)
            authHeaderValue = authHeaderValue.substring(0, commaIndex);

        return authHeaderValue;
    }
}
