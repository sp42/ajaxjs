package com.ajaxjs.adp.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ajaxjs.adp.DoService;
import com.ajaxjs.adp.IDoService;
import com.ajaxjs.framework.spring.easy_controller.anno.Info;
import com.ajaxjs.framework.spring.easy_controller.anno.InterfaceBasedController;

@RestController
@RequestMapping("/do_task")
@Info("探障任务")
@InterfaceBasedController(serviceClass = DoService.class)
public interface DoController extends IDoService {
}