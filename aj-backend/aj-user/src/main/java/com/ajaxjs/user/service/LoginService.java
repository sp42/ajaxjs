package com.ajaxjs.user.service;

import com.ajaxjs.data.CRUD;
import com.ajaxjs.user.controller.LoginController;
import com.ajaxjs.user.model.AppLogin;
import com.ajaxjs.user.model.Client;
import com.ajaxjs.user.model.TokenResult;
import org.springframework.stereotype.Service;

/**
 * 登录服务
 */
@Service
public class LoginService implements LoginController {
    @Override
    public TokenResult appLogin(AppLogin login) {
        String sql = "SELECT * FROM auth_client_details WHERE client_id = ? AND client_secret = ?";
        CRUD.info(Client.class, sql, login.getClientId(), login.getClientSecret());
        return null;
    }
}
