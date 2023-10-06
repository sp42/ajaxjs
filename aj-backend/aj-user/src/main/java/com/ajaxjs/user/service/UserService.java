package com.ajaxjs.user.service;

import com.ajaxjs.data.CRUD;
import com.ajaxjs.framework.entity.BaseEntityConstants;
import com.ajaxjs.user.controller.UserController;
import com.ajaxjs.user.model.User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserService implements UserController {
    @Override
    public Boolean register(Map<String, Object> params) {
        return null;
    }

    @Override
    public Boolean checkRepeat(String field, String value) {
        return null;
    }

    @Override
    public User info(Long id) {
        String sql = "SELECT * FROM user WHERE stat != 1 AND id = ?";
        sql = TenantService.addTenantIdQuery(sql);

        return CRUD.info(User.class, sql, id);
    }

//    @PostMapping
//    default Long create(@Valid User user) {
//        if (checkRepeat("username", user.getUsername()))
//            throw new IllegalArgumentException("用户的登录名" + user.getUsername() + "重复");
//
//        return CRUD.create(user);
//    }
//
//
//    default Boolean checkRepeat(String field, Object value) {
//        String sql = "SELECT * FROM user WHERE stat != 1 AND " + field + " = ?";
//        sql = SaasUtils.addTenantIdQuery(sql);
//        sql += "LIMIT 1";
//
//        return CRUD.infoMap(sql, value) != null;
//    }

    @Override
    public Boolean update(User user) {
        return CRUD.update(user);
    }

    @Override
    public Boolean delete(Long id) {
        User user = new User();
        user.setId(id);
        user.setStat(BaseEntityConstants.STATUS_DELETED);  // 逻辑删除

        return update(user);
    }
}
