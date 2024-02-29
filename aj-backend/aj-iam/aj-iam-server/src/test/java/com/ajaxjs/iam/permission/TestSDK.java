package com.ajaxjs.iam.permission;

import com.ajaxjs.iam.server.BaseTest;
import org.junit.Test;

public class TestSDK extends BaseTest {
    /**
     * 权限列表
     */
    interface PermissionList {
        PermissionEntity USER_MANAGE = new PermissionEntity("USER_MANAGE");
        PermissionEntity BASE_MANAGE = new PermissionEntity("BASE_MANAGE");
    }

    @Test
    public void test() {
        SDK.init(PermissionList.class);

        boolean check = PermissionList.USER_MANAGE.check(257);
        System.out.println(check);
        check = PermissionList.BASE_MANAGE.check(513);
        System.out.println(check);
    }
}
