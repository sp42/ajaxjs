package com.ajaxjs.iam.user.controller;

import com.ajaxjs.framework.PageResult;
import com.ajaxjs.iam.user.model.LogLogin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/log_login")
public interface LogLoginController {
    /**
     * 查找登录日志的列表
     *
     * @return 登录日志的列表
     */
    @GetMapping
    PageResult<LogLogin> page();

    /**
     * 根据用户 id 查找其登录日志的列表
     *
     * @param userId 用户 id
     * @return 登录日志的列表
     */
    @GetMapping("/user/{userId}")
    List<LogLogin> findListByUserId(@PathVariable Long userId);

    LogLogin getLastUserLoginInfo(long userId);
}
