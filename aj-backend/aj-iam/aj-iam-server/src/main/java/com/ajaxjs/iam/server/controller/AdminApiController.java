package com.ajaxjs.iam.server.controller;

import com.ajaxjs.framework.entity.BaseCRUDController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 后台接口
 */
@RestController
@RequestMapping("/admin_api")
public interface AdminApiController extends BaseCRUDController {
}
