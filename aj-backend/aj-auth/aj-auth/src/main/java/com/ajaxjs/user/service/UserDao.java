package com.ajaxjs.user.service;

import com.ajaxjs.data.CRUD;
import com.ajaxjs.framework.entity.BaseEntityConstants;
import com.ajaxjs.user.model.User;

public class UserDao implements BaseEntityConstants {
    public static User findUserBy(String type, String userID, int tenantId) {
        String sql = "SELECT * FROM user WHERE " + type + " = ? AND tenant_id = ? AND stat != ?";

        return CRUD.info(User.class, sql, userID, tenantId, STATUS_DELETED);
    }
}
