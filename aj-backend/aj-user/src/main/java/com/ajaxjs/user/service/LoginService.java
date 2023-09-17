package com.ajaxjs.user.service;

import com.ajaxjs.data.CRUD;
import com.ajaxjs.user.model.Client;
import com.ajaxjs.user.model.TokenResult;
import com.ajaxjs.user.model.login.AppLogin;
import com.ajaxjs.user.model.login.PasswordLogin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录服务
 */
@RestController
@RequestMapping("/login")
public interface LoginService {
    /**
     * 普通的用户密码登录
     *
     * @param login
     * @return
     */
    @PostMapping
    default TokenResult userLogin(PasswordLogin login) {
        return null;
    }

    /**
     * 获取 AppToken
     *
     * @param login 包含应用应用 id 和应用密钥的表单
     * @return AppToken
     */
    @PostMapping("/app")
    default TokenResult appLogin(AppLogin login) {
        String sql = "SELECT * FROM auth_client_details WHERE client_id = ? AND client_secret = ?";
        CRUD.info(Client.class, sql, login.getClientId(), login.getClientSecret());

        return null;
    }
}
