package com.ajaxjs.data_service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ajaxjs.data_service.service.AdminService;

/**
 * 数据服务 后台控制器
 */
@RestController
@RequestMapping("/admin/data_service")
public interface AdminController extends AdminService {
}
