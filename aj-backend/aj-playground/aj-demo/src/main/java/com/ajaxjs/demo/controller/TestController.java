package com.ajaxjs.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public interface TestController {
    @GetMapping
    String getFoo();

    @GetMapping("/list")
    List<String> list();

    @GetMapping("/map")
    Map<String, Object> map();

    @GetMapping("/none")
    void none();
}
