package com.ajaxjs.adp.data_service;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ajaxjs.data_service.controller.BaseDataSourceController;

@RestController
@RequestMapping("/data_service/datasource")
public class DataSourceController extends BaseDataSourceController {
	final static String TABLE_NAME = "aj_base.adp_datasource";

	@Override
	protected String getTableName() {
		return TABLE_NAME;
	}

}
