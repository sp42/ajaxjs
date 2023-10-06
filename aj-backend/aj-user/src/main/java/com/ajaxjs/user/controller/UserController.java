package com.ajaxjs.user.controller;

import com.ajaxjs.user.model.User;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 用户相关控制器
 */
@RestController
@RequestMapping("/user")
public interface UserController {
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

    /**
     * 获取用户详情
     *
     * @param id 用户 id
     * @return 用户详情
     */
    @GetMapping("/{id}")
    User info(Long id);

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
