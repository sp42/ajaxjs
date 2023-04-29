package com.ajaxjs.adp.data_service;

import java.sql.Connection;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ajaxjs.data_service.DataSerivceUtils;
import com.ajaxjs.data_service.controller.BaseDataServiceAdminController;

/**
 * 数据服务 后台控制器
 * 
 * @author Frank Cheung sp42@qq.com
 *
 */
@RestController
@RequestMapping("/data_service/admin")
public class DataServiceAdminController extends BaseDataServiceAdminController {
	@Override
	protected String getDataSourceTableName() {
		return "aj_base.adp_datasource";
	}

	@Override
	protected String getDataServiceTableName() {
		return "aj_base.adp_data_service";
	}

	@Override
	protected Connection getConnection() {
		return DataSerivceUtils.initDb();
	}
}
