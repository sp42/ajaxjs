package com.ajaxjs.iam.permission;

import com.ajaxjs.data.CRUD;

import java.lang.reflect.Field;
import java.util.List;

public class SDK {
    public static void init(Class<?> permissionListClz) {
        List<String> allPermissionIIdList = CRUD.list(String.class, "SELECT code FROM per_permission WHERE stat = 0 ORDER BY id ASC");
        Field[] fields = permissionListClz.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);

            // 获取字段的值
            try {
                Object value = field.get(null);  // 由于常量是静态的，所以传入null作为对象实

                if (value instanceof PermissionEntity) {
                    PermissionEntity permissionEntity = (PermissionEntity) value;
                    int index = allPermissionIIdList.indexOf(permissionEntity.getName());

                    if (index == -1)
                        throw new IllegalStateException("找不到权限");

                    permissionEntity.setIndex(index);
                    System.out.println("Field: " + permissionEntity);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }
}