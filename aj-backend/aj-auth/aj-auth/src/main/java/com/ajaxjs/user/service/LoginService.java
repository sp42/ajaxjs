package com.ajaxjs.user.service;

import javax.servlet.http.HttpSession;

import com.ajaxjs.framework.spring.easy_controller.anno.ControllerMethod;
import com.ajaxjs.user.model.UserConstants;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 用户登录业务
 *
 * @author Frank Cheung sp42@qq.com
 */
public interface LoginService extends UserConstants {
    /**
     * 用户登录
     * <p>
     * TODO 是否需要考虑 CaptchaFilter
     *
     * @param userID   用户标识，可以是 username/email/phone 中的一种，后台自动判断
     * @param password 用户密码
     * @return true 表示登录成功
     */
    @PostMapping
    @ControllerMethod("用户登入")
    Boolean login(@RequestParam String userID, @RequestParam String password);

    /**
     * 用户登出
     *
     * @param session 会话对象
     * @return true 表示登出成功
     */
    @PostMapping("logout")
    @ControllerMethod("用户登出")
    Boolean logout(HttpSession session);

    /**
     * 查看登录日志
     *
     * @return
     */
    @GetMapping("/log")
    @ControllerMethod("查看登录日志")
    String listLog();
}
