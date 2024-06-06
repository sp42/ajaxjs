package com.ajaxjs.iam.server.controller;

import com.ajaxjs.framework.entity.BaseCRUD;
import com.ajaxjs.framework.entity.BaseCRUDController;
import com.ajaxjs.framework.entity.BaseCRUDService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 通用万能型 API 接口
 */
@RestController
@RequestMapping("/simple_api")
public class SimpleApiController extends BaseCRUDService implements BaseCRUDController {
    {
        // 定义表的 CRUD
        BaseCRUD<Map<String, Object>, Long> permission = new BaseCRUD<>();
        permission.setTableName("per_permission");
        permission.setHasIsDeleted(true);
        permission.setDelField("stat");
        namespaces.put("permission", permission);

        BaseCRUD<Map<String, Object>, Long> role = new BaseCRUD<>();
        role.setTableName("per_role");
        role.setHasIsDeleted(true);
        role.setDelField("stat");
        namespaces.put("role", role);
    }
}
