package com.ajaxjs.user.service;

import com.ajaxjs.data.CRUD;
import com.ajaxjs.sass.SaasUtils;
import com.ajaxjs.user.model.User;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public User info(Long id) {
        String sql = "SELECT * FROM user WHERE stat != 1 AND id = ?";
        sql = SaasUtils.addTenantIdQuery(sql);

        return CRUD.info(User.class, sql, id);
    }

    @Override
    public Long create(@Valid User user) {
        return CRUD.create(user);
    }

    @Override
    public Boolean checkRepeat(String field, String value) {
        String sql = "SELECT * FROM user WHERE stat != 1 AND id = ?";
        sql = SaasUtils.addTenantIdQuery(sql);
        sql += "LIMIT 1";

        return ;
    }

    @Override
    public Boolean update(User user) {
        return null;
    }

    @Override
    public Boolean delete(Long id) {
        return null;
    }
}
