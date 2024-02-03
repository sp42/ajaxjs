package com.ajaxjs.iam.server.controller;

import com.ajaxjs.iam.permission.Permission;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/permission")
public interface PermissionController {
    @GetMapping("/role_tree")
    List<Map<String, Object>> getRoleTree();

    @DeleteMapping("/role/{id}")
    boolean deleteRole(@PathVariable Integer id);

    /**
     * 根据角色 id 获取其权限列表
     *
     * @param roleId 角色 id
     * @return 权限列表
     */
    @GetMapping("/permission_list_by_role/{roleId}")
    List<Permission> getPermissionListByRole(@PathVariable Integer roleId);
}
