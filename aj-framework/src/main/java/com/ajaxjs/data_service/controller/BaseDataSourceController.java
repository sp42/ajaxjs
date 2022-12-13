package com.ajaxjs.data_service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ajaxjs.data_service.service.DataSourceService;

@RestController
@RequestMapping("/admin/datasource")
public interface DataSourceController extends DataSourceService {
}
