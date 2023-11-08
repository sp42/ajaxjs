package com.ajaxjs.iam.server.service;

import com.ajaxjs.framework.entity.BaseCRUD;
import com.ajaxjs.framework.entity.BaseCRUDService;
import com.ajaxjs.iam.server.controller.AdminApiController;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AdminApiService extends BaseCRUDService implements AdminApiController {
    {
        // 定义表的 CRUD
        BaseCRUD<Map<String, Object>, Long> app = new BaseCRUD<>();
        app.setTableName("app");
        namespaces.put("app", app);

        BaseCRUD<Map<String, Object>, Long> user = new BaseCRUD<>();
        user.setTableName("user");

        namespaces.put("user", user);
    }

//    public static void getMenuGroup(HttpServletRequest request) {
//        DataBaseConnection.initDb();
//
//        try {
//            AdminService adminService = DiContextUtil.getBean(AdminService.class);
//
//            List<Map<String, Object>> groups = CRUD.list("SELECT * FROM et_resoure_group WHERE et_id = 1687014453675167744");
////        List<Map<String, Object>> mapList = CRUD.list("SELECT * FROM et_menu_group");
//            assert adminService != null;
//            List<Map<String, Object>> mapList = adminService.list("menuGroup");
//
//            request.setAttribute("groups", JsonHelper.toJson(groups));
//            request.setAttribute("mapList", JsonHelper.toJson(mapList));
//        } finally {
//            JdbcConn.closeDb();
//        }
//
//    }
}