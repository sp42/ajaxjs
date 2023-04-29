package com.ajaxjs.auth.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ajaxjs.auth.service.user.IOrgService;

@RestController
@RequestMapping("/org")
public interface OrgController extends IOrgService {

}
