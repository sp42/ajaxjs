package com.ajaxjs.adp.data_service;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ajaxjs.data_service.controller.BaseDataServiceApiController;

/**
 * 
 */
@RestController
@RequestMapping("${DataService.api_root:/api}/**")
public class DataServiceApiController extends BaseDataServiceApiController {

}
