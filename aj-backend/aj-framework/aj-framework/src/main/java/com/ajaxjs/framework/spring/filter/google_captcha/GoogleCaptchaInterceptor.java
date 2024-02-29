package com.ajaxjs.framework.spring.filter.google_captcha;

import com.ajaxjs.net.http.Post;
import lombok.Data;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Map;

@Data
public class GoogleCaptchaInterceptor implements HandlerInterceptor {
    private String accessKeyId;

    private String accessSecret;

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            String httpMethod = req.getMethod();

            if (("POST".equals(httpMethod) || "PUT".equals(httpMethod)) && method.getAnnotation(GoogleCaptchaCheck.class) != null) {
                // 有注解，要检测
                System.out.println("开始检测");

                if (check(req))
                    return true;

                return false;
            }
        }

        return true;
    }

    /**
     * 校验表单时候客户端传过来的 token 参数名
     */
    public final static String PARAM_NAME = "grecaptchaToken";

    /**
     * 谷歌校验 API
     */
    private final static String SITE_VERIFY = "https://www.recaptcha.net/recaptcha/api/siteverify";

    /**
     * 校验
     *
     * @param request 请求对象
     * @return 是否通过验证，若为 true 表示通过，否则抛出异常
     */
    public boolean check(HttpServletRequest request) {
        return check(request.getParameter(PARAM_NAME));
    }

    public boolean check(String token) {
        if (!StringUtils.hasText(token))
            throw new SecurityException("非法攻击！客户端缺少必要的参数");

        Map<String, Object> map = Post.api(SITE_VERIFY, String.format("secret=%s&response=%s", getAccessSecret(), token.trim()));

        if (map == null)
            throw new IllegalAccessError("谷歌验证码服务失效，请联系技术人员");

        if ((boolean) map.get("success")) {// 判断用户输入的验证码是否通过
            if (map.get("score") != null) {
                // 评分0 到 1。1：确认为人类，0：确认为机器人
                double score = (double) map.get("score");

                if (score < 0.5)
                    throw new SecurityException("验证码不通过，非法请求");
            }

            return true;
        } else {
            if ("timeout-or-duplicate".equals(map.get("error-codes")))
                throw new NullPointerException("验证码已经过期，请刷新");

            throw new SecurityException("验证码不正确");
        }
    }
}
