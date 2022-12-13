package com.ajaxjs.adp.data_service;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ajaxjs.data_service.controller.BaseDataSourceController;
import com.ajaxjs.spring.DiContextUtil;

@RestController
@RequestMapping("/data_service/datasource")
public class DataSourceController extends BaseDataSourceController {
	final static String TABLE_NAME = "aj_base.adp_datasource";

	@Override
	protected String getTableName() {
		return TABLE_NAME;
	}

	@Override
	protected Connection initDb() {
		DataSource ds = DiContextUtil.getBean(DataSource.class);

		try {
			return ds.getConnection();
		} catch (SQLException e) {
			return null;
		}
	}
}
