package com.ajaxjs.user.service;

import com.ajaxjs.data.CRUD;
import com.ajaxjs.framework.entity.BaseEntityConstants;
import com.ajaxjs.sass.SaasUtils;
import com.ajaxjs.user.controller.UserController;
import com.ajaxjs.user.model.User;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

@Service
public class UserService implements UserController {
    @Override
    public User info(Long id) {
        String sql = "SELECT * FROM user WHERE stat != 1 AND id = ?";
        sql = SaasUtils.addTenantIdQuery(sql);

        return CRUD.info(User.class, sql, id);
    }

    @Override
    public Long create(@Valid User user) {
        if (checkRepeat("username", user.getUsername()))
            throw new IllegalArgumentException("用户的登录名" + user.getUsername() + "重复");

        return CRUD.create(user);
    }

    @Override
    public Boolean checkRepeat(String field, Object value) {
        String sql = "SELECT * FROM user WHERE stat != 1 AND " + field + " = ?";
        sql = SaasUtils.addTenantIdQuery(sql);
        sql += "LIMIT 1";

        return CRUD.info(sql, value) != null;
    }

    @Override
    public Boolean update(User user) {
        return CRUD.update(user);
    }

    @Override
    public Boolean delete(Long id) {
        // 逻辑删除
        User user = new User();
        user.setId(id);
        user.setStat(BaseEntityConstants.STATUS_DELETED);

        return update(user);
    }
}
