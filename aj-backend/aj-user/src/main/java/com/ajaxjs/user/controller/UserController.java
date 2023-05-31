package com.ajaxjs.user.controller;

import com.ajaxjs.user.model.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public interface UserController {
    @GetMapping("/{id}")
    User info(@PathVariable Long id);

    @PostMapping
    Long create(@RequestBody User user);

    /**
     * 检查用户某个值是否已经存在一样的值
     *
     * @param field 字段名，当前只能是 username/email/phone 中的任意一种
     * @param value 字段值，要校验的值
     * @return 是否已经存在一样的值，true 表示存在
     */
    @GetMapping("/checkRepeat")
    Boolean checkRepeat(@RequestParam String field, @RequestParam Object value);

    @PutMapping
    Boolean update(@RequestBody User user);

    @DeleteMapping("/{id}")
    Boolean delete(@PathVariable Long id);
}
