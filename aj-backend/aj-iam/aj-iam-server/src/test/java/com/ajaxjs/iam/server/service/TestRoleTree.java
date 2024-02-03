package com.ajaxjs.iam.server.service;

import com.ajaxjs.data.CRUD;
import com.ajaxjs.framework.entity.tree.FlatArrayToTree;
import com.ajaxjs.iam.permission.Permission;
import com.ajaxjs.iam.permission.PermissionService;
import com.ajaxjs.iam.server.BaseTest;
import com.ajaxjs.util.convert.ConvertToJson;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

public class TestRoleTree extends BaseTest {
    @Test
    public void test() {
        List<Map<String, Object>> nodes = CRUD.listMap("SELECT * FROM per_role");
        // 扁平化的列表转换为 tree 结构
        List<Map<String, Object>> tree = new FlatArrayToTree().mapAsTree(Integer.class, nodes);

        System.out.println(ConvertToJson.toJson(tree));
    }

    @Autowired
    PermissionService permissionService;

    @Test
    public void testGetPermissionListByRole() {
        List<Permission> permissionListByRole = permissionService.getPermissionListByRole(28);
        System.out.println(permissionListByRole);
    }
}
