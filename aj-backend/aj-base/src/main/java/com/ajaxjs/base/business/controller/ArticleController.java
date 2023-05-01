package com.ajaxjs.base.business.controller;

import com.ajaxjs.base.business.service.ArticleService;
import com.ajaxjs.base.business.service.ArticleServiceImpl;
import com.ajaxjs.framework.spring.easy_controller.anno.InterfaceBasedController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/article")
@InterfaceBasedController(serviceClass = ArticleServiceImpl.class)
public interface ArticleController extends ArticleService {
}
