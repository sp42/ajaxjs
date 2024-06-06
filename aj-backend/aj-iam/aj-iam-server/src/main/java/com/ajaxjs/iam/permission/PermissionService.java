package com.ajaxjs.iam.permission;

import com.ajaxjs.data.CRUD;
import com.ajaxjs.framework.entity.tree.FlatArrayToTree;
import com.ajaxjs.iam.server.controller.PermissionController;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PermissionService implements PermissionController {
    @Override
    public List<Map<String, Object>> getRoleTree() {
        List<Map<String, Object>> nodes = CRUD.listMap("SELECT * FROM per_role WHERE stat != 1");

        // 扁平化的列表转换为 tree 结构
        List<Map<String, Object>> data = new FlatArrayToTree().mapAsTree(Integer.class, nodes);

        return transformToTreeStructure(data);
    }

    @Override
    public boolean deleteRole(Integer id) {
        String sql = "WITH RECURSIVE sub_roles AS (" +
                "  SELECT id, parent_id, name FROM per_role WHERE id = ?" +
                "  UNION ALL" +
                "  SELECT r.id, r.parent_id, r.name FROM per_role r INNER JOIN sub_roles sr ON sr.id = r.parent_id" +
                ")" +
                "UPDATE per_role SET stat = 1 WHERE id IN (SELECT id FROM sub_roles);";

        return CRUD.jdbcWriterFactory().write(sql, id) > 0;
    }

    @Override
    public List<Permission> getPermissionListByRole(Integer roleId) {
        Role role = CRUD.info(Role.class, "SELECT * FROM per_role WHERE id = ?", roleId);
        Long permissionValue = role.getPermissionValue();

        if (permissionValue == null || permissionValue == 0)
            throw new NullPointerException("没有 permissionValue");

        // get all permission list
        List<Permission> allPermissionList = CRUD.list(Permission.class, "SELECT * FROM per_permission WHERE stat = 0 ORDER BY id ASC");

        List<Permission> result = new ArrayList<>();
        getPermissionList(result, allPermissionList, permissionValue, false, null);

        // find parents
        if (role.getIsInheritedParent()) {
            List<Role> parentRoles = CRUD.list(Role.class, "WITH RECURSIVE parent_cte AS (\n" +
                    "  SELECT id, name, parent_id, permission_value FROM per_role\n" +
                    "  WHERE id = ?  -- 用您要查询的节点的ID替换 <your_node_id>\n" +
                    "  UNION ALL\n" +
                    "  SELECT pr.id, pr.name, pr.parent_id, pr.permission_value FROM per_role pr\n" +
                    "  INNER JOIN parent_cte pc ON pr.id = pc.parent_id\n" +
                    ")\n" +
                    "SELECT id, name, parent_id, permission_value\n" +
                    "FROM parent_cte WHERE id != ? -- 不包含自己", roleId, roleId);

            if (!CollectionUtils.isEmpty(parentRoles)) {
                for (Role r : parentRoles)
                    getPermissionList(result, allPermissionList, r.getPermissionValue(), true, r.getName());
            }
        }

        return removeDuplicates(result);
    }

    // 去重
    private static List<Permission> removeDuplicates(List<Permission> list) {
        return list.stream().collect(Collectors.collectingAndThen(
                Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Permission::getId))),
                ArrayList::new
        ));
    }

    private void getPermissionList(List<Permission> result, List<Permission> allPermissionList, Long permissionValue, boolean isInherited, String roleName) {
        int i = 0;

        for (Permission p : allPermissionList) {
            if (PermissionControl.check(permissionValue, i++)) {
                if (isInherited) {
                    p.setIsInherit(true);
                    p.setRoleName(roleName);
                }

                result.add(p);
            }
        }
    }

    // a stupid method
//    public List<Permission> getPermissionListByRole(Integer roleId) {
//        Map<String, Object> role = CRUD.infoMap("SELECT * FROM per_role WHERE id = ?", roleId);
//        Long permissionValue = (Long) role.get("permissionValue");
//
//        if (permissionValue == null || permissionValue == 0)
//            return null;
//
//        // get all permission list
//        List<Permission> allPermissionList = CRUD.list(Permission.class, "SELECT * FROM per_permission WHERE stat = 0 ORDER BY id ASC");
//        String binaryString = Long.toBinaryString(permissionValue);
//
//        if (!StringUtils.hasText(binaryString))
//            throw new NullPointerException("没有 permissionValue");
//
//        System.out.println(binaryString);
//
//        char[] arr = binaryString.toCharArray();
//
//        if (arr.length > allPermissionList.size())
//            throw new IllegalStateException("不足够的权限");
//
//        int j = 0;
//        List<Permission> result = new ArrayList<>();
//        for (int i = arr.length - 1; i >= 0; i--) {
//            if (arr[i] == '1') {
//                Permission permission = allPermissionList.get(j);
//                result.add(permission);
//            }
//
//            j++;
//        }
//
//        return result;
//    }

    /**
     * 将扁平的列表转换为 iView 的 tree 结构
     */
    private List<Map<String, Object>> transformToTreeStructure(List<Map<String, Object>> data) {
        List<Map<String, Object>> iView = new ArrayList<>();

        for (Map<String, Object> map : data) {
            Map<String, Object> iViewMap = new HashMap<>();
            iViewMap.put("id", map.get("id"));
            iViewMap.put("title", map.get("name"));
            iViewMap.put("parentId", map.get("parentId"));
            iViewMap.put("expand", true);
            iViewMap.put("contextmenu", true);

            Object _children = map.get("children");

            if (_children != null) {
                List<Map<String, Object>> children = (List<Map<String, Object>>) _children;

                if (children.size() > 0) {
                    iViewMap.put("children", transformToTreeStructure(children));
                }
            }

            iView.add(iViewMap);
        }

        return iView;
    }
}
