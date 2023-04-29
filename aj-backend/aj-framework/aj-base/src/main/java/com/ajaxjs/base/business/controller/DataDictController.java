package com.ajaxjs.base.business.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ajaxjs.base.business.service.DataDictService;
import com.ajaxjs.base.business.service.DataDictServiceImpl;
import com.ajaxjs.spring.easy_controller.InterfaceBasedController;

@RestController
@InterfaceBasedController(serviceClass = DataDictServiceImpl.class)
@RequestMapping("/data_dict")
public interface DataDictController extends DataDictService {	
}