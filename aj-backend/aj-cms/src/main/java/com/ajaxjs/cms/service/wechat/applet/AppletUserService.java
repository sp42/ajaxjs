package com.ajaxjs.cms.service.wechat.applet;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

public interface AppletUserService {
    @PostMapping("/login")
    String login(@RequestParam(required = true) String code, HttpServletRequest req);
}
