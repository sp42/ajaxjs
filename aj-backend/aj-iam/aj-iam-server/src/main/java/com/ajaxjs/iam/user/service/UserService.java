package com.ajaxjs.iam.user.service;

import com.ajaxjs.data.CRUD;
import com.ajaxjs.framework.entity.BaseEntityConstants;
import com.ajaxjs.framework.spring.DiContextUtil;
import com.ajaxjs.iam.model.SimpleUser;
import com.ajaxjs.iam.user.common.UserConstants;
import com.ajaxjs.iam.user.controller.UserController;
import com.ajaxjs.iam.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.function.Function;

@Service
public class UserService implements UserController, UserConstants {
    @Autowired
    @Qualifier("passwordEncode")
    Function<String, String> passwordEncode;

    @Override
    public User info(Long id) {
        String sql = "SELECT * FROM user WHERE stat != 1 AND id = ?";
        sql = TenantService.addTenantIdQuery(sql);

        return CRUD.info(User.class, sql, id);
    }

    @Override
    public User info() {
        SimpleUser user = (SimpleUser) Objects.requireNonNull(DiContextUtil.getRequest()).getAttribute(com.ajaxjs.iam.resource_server.UserConstants.USER_KEY_IN_REQUEST);

        return info(user.getId());
    }

    @Override
    public Boolean updateBySession(User user) {
        return null;
    }

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
