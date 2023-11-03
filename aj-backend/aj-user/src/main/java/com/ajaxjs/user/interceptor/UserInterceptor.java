package com.ajaxjs.user.interceptor;

import com.ajaxjs.user.common.UserConstants;
import com.ajaxjs.user.model.BaseUser;
import com.ajaxjs.util.StrUtil;
import com.ajaxjs.util.convert.EntityConvert;
import com.ajaxjs.util.logger.LogHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

/**
 * 资源拦截器
 * 用法：
 *
 * @Bean UserInterceptor authInterceptor() {
 * return new UserInterceptor();
 * }
 */
public class UserInterceptor implements HandlerInterceptor {
    private static final LogHelper LOGGER = LogHelper.getLog(UserInterceptor.class);

    @Value("${auth.run:true}")
    private String run;

    @Value("${auth.cacheType:'jvm_hash'}")
    private String cacheType;

    @Autowired
    private StringRedisTemplate redis;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (StringUtils.hasText(run) && Boolean.parseBoolean(run)) {
            String token = extractToken(request);

            if (StringUtils.hasText(token)) {
                String jsonUser = null;

                if("redis".equals(cacheType)){
                    jsonUser = redis.opsForValue().get(UserConstants.REDIS_PREFIX + token);
                } else

                LOGGER.info("AuthInterceptor token={0}, jsonUser={1}", token, jsonUser);

                if (StringUtils.hasText(jsonUser)) {
                    BaseUser user = EntityConvert.json2bean(jsonUser, BaseUser.class);
                    request.setAttribute(UserConstants.USER_KEY, user);

                    return true;
                } else
                    return returnErrorMsg(401, response);
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
                returnMsg(response, "401", "unauthorized", "未认证");
                break;
            case 403:
                returnMsg(response, "403", "forbidden", "没有权限");
                break;
            case 500:
                returnMsg(response, "500", "error", "认证失败");
                break;
        }

        return false;
    }

    private final static String ERR_JSON = "{\"error\":\"%s\",\"error_description\":\"%s\"}";

    /**
     * 返回响应信息
     *
     * @param response  响应请求
     * @param errorCode 错误代码
     * @param title     错误标题
     * @param message   错误信息
     */
    private void returnMsg(HttpServletResponse response, String errorCode, String title, String message) {
        byte[] bytes = String.format(ERR_JSON, title, message).getBytes();

        response.setCharacterEncoding(StrUtil.UTF8_SYMBOL);
        response.setContentType("application/json;charset=utf-8");
        response.setContentLength(bytes.length);
        response.setStatus(Integer.parseInt(errorCode));

        try (ServletOutputStream outputStream = response.getOutputStream()) {
            outputStream.write(bytes);
            outputStream.flush();
        } catch (IOException e) {
            LOGGER.warning(e);
        }
    }

    public String extractToken(HttpServletRequest request) {
        String token = extractHeaderToken(request);

        if (token == null) {
            token = request.getHeader("token");

            if (token == null) {
                token = request.getParameter("access_token");

                if (token == null)
                    LOGGER.warning("Token not found in request parameters. Not an OAuth2 request.");
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
