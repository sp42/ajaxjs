package com.ajaxjs.iam.user.controller;

import com.ajaxjs.iam.user.model.User;
import org.springframework.web.bind.annotation.*;

/**
 * 用户相关控制器
 */
@RestController
@RequestMapping("/user")
public interface UserController {
    /**
     * 获取用户详情
     *
     * @param id 用户 id
     * @return 用户详情
     */
    @GetMapping("/{id}")
    User info(@PathVariable Long id);

    /**
     * 修改用户
     *
     * @param user 用户详情
     * @return 是否成功
     */
    @PutMapping
    Boolean update(User user);

    /**
     * 注销用户账号
     *
     * @param id 用户 id
     * @return 是否成功
     */
    @DeleteMapping("/{id}")
    Boolean delete(Long id);
}
