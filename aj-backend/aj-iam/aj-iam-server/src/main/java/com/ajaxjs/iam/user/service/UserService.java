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

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.function.Function;

import static com.ajaxjs.iam.resource_server.UserConstants.USER_KEY_IN_REQUEST;

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

    /**
     * 在 Request 上下文中获取 User 对象
     *
     * @return User 对象
     */
    public static SimpleUser getUserFromRequestCxt() {
        return getUserFromRequestCxt(Objects.requireNonNull(DiContextUtil.getRequest()));
    }

    /**
     * 在 Request 上下文中获取 User 对象
     *
     * @param req 请求对象
     * @return User 对象
     */
    public static SimpleUser getUserFromRequestCxt(HttpServletRequest req) {
        return (SimpleUser) req.getAttribute(USER_KEY_IN_REQUEST);
    }

    @Override
    public User info() {
        return info(getUserFromRequestCxt().getId());
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
