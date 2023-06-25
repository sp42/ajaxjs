package com.ajaxjs.user.service;

import com.ajaxjs.data.CRUD;
import com.ajaxjs.user.controller.LoginController;
import com.ajaxjs.user.model.login.AppLogin;
import com.ajaxjs.user.model.Client;
import com.ajaxjs.user.model.TokenResult;
import com.ajaxjs.user.model.login.PasswordLogin;
import org.springframework.stereotype.Service;

/**
 * 登录服务
 */
@Service
public class LoginService implements LoginController {
    @Override
    public TokenResult userLogin(PasswordLogin login) {
        return null;
    }

    @Override
    public TokenResult appLogin(AppLogin login) {
        String sql = "SELECT * FROM auth_client_details WHERE client_id = ? AND client_secret = ?";
        CRUD.info(Client.class, sql, login.getClientId(), login.getClientSecret());

        return null;
    }
}
