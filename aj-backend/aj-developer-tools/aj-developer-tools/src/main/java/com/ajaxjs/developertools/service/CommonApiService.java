package com.ajaxjs.developertools.service;

import com.ajaxjs.developertools.api.CommonApiController;
import com.ajaxjs.framework.entity.BaseCRUD;
import com.ajaxjs.framework.entity.BaseCRUDService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CommonApiService extends BaseCRUDService implements CommonApiController {
    {
        // 定义表的 CRUD
        BaseCRUD<Map<String, Object>, Long> widgetConfig = new BaseCRUD<>();
        widgetConfig.setTableName("widget_config");
        namespaces.put("widget_config", widgetConfig);

        BaseCRUD<Map<String, Object>, Long> user = new BaseCRUD<>();
        user.setTableName("user");

        namespaces.put("user", user);
    }
}