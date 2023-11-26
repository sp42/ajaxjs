package com.ajaxjs.base.controller;

import com.ajaxjs.framework.entity.BaseCRUDController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 通用万能型 API 接口
 */
@RestController
@RequestMapping("/common_api")
public interface CommonApiController extends BaseCRUDController {
}
