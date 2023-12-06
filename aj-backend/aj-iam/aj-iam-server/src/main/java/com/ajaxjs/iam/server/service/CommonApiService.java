package com.ajaxjs.iam.server.service;

import com.ajaxjs.framework.entity.BaseCRUDService;
import com.ajaxjs.iam.server.controller.CommonApiController;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class CommonApiService extends BaseCRUDService implements CommonApiController {
    @EventListener
    public void handleContextRefresh(ContextRefreshedEvent event) {
        // 在Spring初始化完成后执行的操作
        loadConfigFromDatabase();
    }
}