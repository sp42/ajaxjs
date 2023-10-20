package com.ajaxjs.user.service;

import com.ajaxjs.data.CRUD;
import com.ajaxjs.user.model.po.Client;
import org.springframework.stereotype.Service;

@Service
public class ClientService {
    public Client getClient(String clientId, String clientSecret) {
        return CRUD.info(Client.class, "SELECT * FROM auth_client_details WHERE client_id = ? AND client_secret = ?", clientId, clientSecret);
    }
}
