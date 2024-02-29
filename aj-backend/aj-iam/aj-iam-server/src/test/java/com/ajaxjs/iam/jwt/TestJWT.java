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

        JWebTokenMgr mgr = new JWebTokenMgr();
        mgr.setSecretKey("aEsD65643vb3");
        JWebToken jwt = mgr.parse("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJERUZBVUxUX1NDT1BFIiwiZXhwIjoxNzA5MjkwOTM2LCJpYXQiOjE3MDkyMDQ1MzYsImlzcyI6ImZvb0BiYXIubmV0IiwibmFtZSI6ImFkbWluIiwic3ViIjoiMSJ9.HZF89-4j2B5y22AbuB47ID0GCFuMxxbVur5zAdHFrOk");
        System.out.println(mgr.isValid("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJERUZBVUxUX1NDT1BFIiwiZXhwIjoxNzA5MjkwOTM2LCJpYXQiOjE3MDkyMDQ1MzYsImlzcyI6ImZvb0BiYXIubmV0IiwibmFtZSI6ImFkbWluIiwic3ViIjoiMSJ9.HZF89-4j2B5y22AbuB47ID0GCFuMxxbVur5zAdHFrOk"));
//        assertTrue(mgr.isValid("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJhZG1pbiwgZ3Vlc3QiLCJleHAiOjE2ODc3MjE1NTIsImlhdCI6MTY4NzYzNTE1MiwiaXNzIjoiZm9vQGJhci5uZXQiLCJzdWIiOiJ1c2VyMDEifQ.IYommcWgSWAmQnVkkd9-aJ6smeuJ4cFoTBUzUXCltgE"));
    }
}
