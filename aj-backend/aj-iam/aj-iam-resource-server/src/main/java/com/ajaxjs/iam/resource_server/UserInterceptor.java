package com.ajaxjs.iam.resource_server;

import com.ajaxjs.util.StrUtil;
import com.ajaxjs.util.convert.EntityConvert;
import com.ajaxjs.util.logger.LogHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Enumeration;

/**
 * 资源拦截器
 * 用法：
 * <code>
 *
 * @Bean UserInterceptor authInterceptor() {
 * return new UserInterceptor();
 * </code>
 * }
 */
public class UserInterceptor implements HandlerInterceptor {
    @Value("${auth.run:true}")
    private String run;
//
//    @Value("${auth.cacheType:'jvm_hash'}")
//    private String cacheType;
//
//    @Autowired
//    private StringRedisTemplate redis;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (StringUtils.hasText(run) && Boolean.parseBoolean(run)) {
            String token = extractToken(request);

            if (StringUtils.hasText(token)) {
                String jsonUser = null;

//                if("redis".equals(cacheType)){
//                    jsonUser = redis.opsForValue().get(UserConstants.REDIS_PREFIX + token);
//                } else
//
//                LOGGER.info("AuthInterceptor token={0}, jsonUser={1}", token, jsonUser);
//
//                if (StringUtils.hasText(jsonUser)) {
//                    BaseUser user = EntityConvert.json2bean(jsonUser, BaseUser.class);
//                    request.setAttribute(UserConstants.USER_KEY, user);
//
//                    return true;
//                } else
//                    return returnErrorMsg(401, response);
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
            e.printStackTrace();
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

        if (token == null) {
            token = request.getHeader("token");

            if (token == null) {
                token = request.getParameter("access_token");

                if (token == null)
                    System.err.println("Token not found in request parameters. Not an OAuth2 request.");
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
