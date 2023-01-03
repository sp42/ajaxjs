package com.ajaxjs.database_meta;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ajaxjs.data_service.model.DataSourceInfo;
import com.ajaxjs.spring.DiContextUtil;
import com.ajaxjs.spring.response.ResponseResult;
import com.ajaxjs.sql.JdbcConnection;

/**
 * 生成数据库信息的 JSON，用于显示数据库文档
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public abstract class BaseMakeDbDocController {
	private String jsonPath = "D:\\code\\ajaxjs\\aj-framework\\aj-ui-widget\\database-doc\\";

	@PostMapping
	public Boolean genJsonFile(@RequestBody DataSourceInfo ds) throws SQLException {
		try (Connection conn = JdbcConnection.getMySqlConnection(ds.getUrl(), ds.getUsername(), ds.getPassword())) {
//			DataBaseQuery.saveToDiskJson(conn, getJsonPath() + "json.js");
			DB_DOC_JSON = "DOC_DATA = " + DataBaseQuery.getDoc(conn, null);

			return true;
		}
	}

	public static String DB_DOC_JSON = "DOC_DATA =[];";

	@GetMapping
	public String getJson() {
		getSingleDataSource();
		return ResponseResult.PLAIN_TEXT_OUTPUT + DB_DOC_JSON;
	}

	void getSingleDataSource() {
		DataSource ds = DiContextUtil.getBean(DataSource.class);

		try (Connection conn = JdbcConnection.getConnection(ds)) {
			DB_DOC_JSON = "DOC_DATA = " + DataBaseQuery.getDoc(conn, conn.getCatalog());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public String getJsonPath() {
		return jsonPath;
	}

	public void setJsonPath(String jsonPath) {
		this.jsonPath = jsonPath;
	}

}
