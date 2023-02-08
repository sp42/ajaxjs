package com.ajaxjs.auth.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ajaxjs.database_meta.BaseMakeDbDocController;

@RestController
@RequestMapping("/make_database_doc") // 这个接口地址不能自定义了，否则前端要跟着改
public class MakeDbDocController extends BaseMakeDbDocController {

}
