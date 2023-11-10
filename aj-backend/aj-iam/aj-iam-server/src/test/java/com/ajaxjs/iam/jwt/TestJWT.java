package com.ajaxjs.iam.jwt;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class TestJWT {
    JWebTokenMgr mgr = new JWebTokenMgr();

    @Test
    public void testMakeToken() {
        JWebToken token = mgr.tokenFactory("1000", "user01", "admin, guest", Utils.setExpire(24));
        System.out.println(token.toString());
    }

    @Test
    public void testValid() {
        assertTrue(mgr.isValid("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJhZG1pbiwgZ3Vlc3QiLCJleHAiOjE2ODc3MjE1NTIsImlhdCI6MTY4NzYzNTE1MiwiaXNzIjoiZm9vQGJhci5uZXQiLCJzdWIiOiJ1c2VyMDEifQ.IYommcWgSWAmQnVkkd9-aJ6smeuJ4cFoTBUzUXCltgE"));
    }
}
