package com.ajaxjs.user.service;

import com.ajaxjs.data.CRUD;
import com.ajaxjs.framework.entity.BaseEntityConstants;
import com.ajaxjs.user.common.SaasUtils;
import com.ajaxjs.user.model.User;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public interface UserService {
    @GetMapping("/{id}")
    default User info(Long id) {
        String sql = "SELECT * FROM user WHERE stat != 1 AND id = ?";
        sql = SaasUtils.addTenantIdQuery(sql);

        return CRUD.info(User.class, sql, id);
    }

    @PostMapping
    default Long create(@Valid User user) {
        if (checkRepeat("username", user.getUsername()))
            throw new IllegalArgumentException("用户的登录名" + user.getUsername() + "重复");

        return CRUD.create(user);
    }


    default Boolean checkRepeat(String field, Object value) {
        String sql = "SELECT * FROM user WHERE stat != 1 AND " + field + " = ?";
        sql = SaasUtils.addTenantIdQuery(sql);
        sql += "LIMIT 1";

        return CRUD.infoMap(sql, value) != null;
    }

    @PutMapping
    default Boolean update(User user) {
        return CRUD.update(user);
    }

    @DeleteMapping("/{id}")
    default Boolean delete(Long id) {
        User user = new User();
        user.setId(id);
        user.setStat(BaseEntityConstants.STATUS_DELETED);  // 逻辑删除

        return update(user);
    }
}
