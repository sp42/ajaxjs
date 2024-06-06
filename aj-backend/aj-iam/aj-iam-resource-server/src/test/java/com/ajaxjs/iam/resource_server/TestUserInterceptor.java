package com.ajaxjs.iam.resource_server;

import com.ajaxjs.iam.jwt.JWebToken;
import com.ajaxjs.iam.jwt.JWebTokenMgr;
import com.ajaxjs.iam.model.SimpleUser;
import org.junit.Test;

public class TestUserInterceptor {
    String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiIiLCJleHAiOjAsImlhdCI6MTcwMDk0OTEzMSwiaXNzIjoiZm9vQGJhci5uZXQiLCJuYW1lIjoi6JeP57uP6ZiB572R56uZIiwic3ViIjoiNCJ9.m0A-ykfjcZsUYIIYsHqE8vEySisKztN2IhQMvbUfqZI";

    @Test
    public void test() {
        JWebTokenMgr mgr = new JWebTokenMgr();
        mgr.setSecretKey("aEsc65643vb3");
        JWebToken jwt = mgr.parse(token);

        boolean valid = mgr.isValid(jwt);
        System.out.println(valid);

        String jsonUser = "{\"id\": %s, \"name\": \"%s\"}";
        jsonUser = String.format(jsonUser, jwt.getPayload().getSub(), jwt.getPayload().getName());

        System.out.println(jsonUser);
    }
}
