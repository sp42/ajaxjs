package com.ajaxjs.auth.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ajaxjs.data_service.controller.BaseDataServiceApiController;

/**
 * 数据服务 API 控制器
 */
@RestController
@RequestMapping("${DataService.api_root:/api}/**")
public class DataServiceApiController extends BaseDataServiceApiController {

}
