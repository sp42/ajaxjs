package com.ajaxjs.login;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 第三方登录的控制器
 *
 * @author sp42 frank@ajaxjs.com
 */
@Controller
@RequestMapping("/service_public/user/third_login")
public class ThirdLoginController {
    @GetMapping("weibo")
    public String weibo(@RequestParam String code) {
        return "";
    }

    @GetMapping("qq")
    public String qq(@RequestParam String code) {
        return "";
    }
}
