package com.ajaxjs.iam.server.controller;

import com.ajaxjs.iam.permission.Permission;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/permission")
public interface PermissionController {
    /**
     * 获取角色树
     *
     * @return 角色树
     */
    @GetMapping("/role_tree")
    List<Map<String, Object>> getRoleTree();

    /**
     * 删除角色
     *
     * @param id 角色 id
     * @return 是否成功
     */
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

    /**
     * 为角色添加权限 id 列表
     *
     * @param roleId        角色 id
     * @param permissionIds 权限列表
     * @return 是否成功
     */
    @PostMapping("/add_permissions_to_role")
    boolean addPermissionsToRole(@RequestParam Integer roleId, @RequestParam List<Integer> permissionIds);
}
