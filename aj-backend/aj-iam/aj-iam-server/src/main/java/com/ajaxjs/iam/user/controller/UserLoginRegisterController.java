package com.ajaxjs.iam.user.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 登录注册控制器
 */
@RestController
@RequestMapping("/user")
public interface UserLoginRegisterController {
    /**
     * 检测用户是否已经登录
     *
     * @return true 表示已登录
     */
    @GetMapping("/is_login")
    Boolean isLogin();

    /**
     * 普通用户登录
     *
     * @param loginId   登录账号，用户标识，可以是 username/email/phone 中的一种，后台自动判断
     * @param password  密码
     * @param returnUrl 跳转地址
     * @return 若成功登录跳转
     */
    @PostMapping("/login")
    boolean login(@RequestParam String loginId, @RequestParam String password, @RequestParam(required = false) String returnUrl, HttpServletRequest req, HttpServletResponse resp);

    /**
     * 用户登出
     */
    @PostMapping("/logout")
    ModelAndView logout(@RequestParam(required = false) String returnUrl);

    /**
     * 用户注册
     * <p>
     * TODO 数据库事务 和 验证码
     *
     * @param params 用户参数
     * @return 是否成功
     */
    @PostMapping
    Boolean register(@RequestParam Map<String, Object> params);

    /**
     * 检查用户某个值是否已经存在一样的值
     *
     * @param field 字段名，当前只能是 username/email/phone 中的任意一种
     * @param value 字段值，要校验的值
     * @return true=存在
     */
    @GetMapping("/checkRepeat")
    Boolean checkRepeat(@RequestParam String field, @RequestParam String value);
}
