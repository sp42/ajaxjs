package com.ajaxjs.framework;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.ajaxjs.sql.JdbcConnection;
import com.ajaxjs.sql.JdbcHelper;

/**
 * 通用的写操作
 */
public abstract class CommonWriter {
	@Autowired
	private DataSource ds;

	public static class NewlyInfo implements IBaseModel {
		public Serializable newlyId;
	}

	@PostMapping
	public NewlyInfo create(@RequestParam String tableName, @RequestBody Map<String, Object> entity) {
		Serializable id = null;
		Connection conn = null;
		NewlyInfo n = new NewlyInfo();

		try {
			conn = ds.getConnection();
			id = JdbcHelper.createMap(conn, entity, tableName);
			n.newlyId = id;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcConnection.closeDb(conn);
		}

		return n;
	}

	@PutMapping
	public Boolean update(@RequestParam String tableName, @RequestBody Map<String, Object> entity) {
		Connection conn = null;
		int rows = 0;

		try {
			conn = ds.getConnection();
			rows = JdbcHelper.updateMap(conn, entity, tableName);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcConnection.closeDb(conn);
		}

		return rows > 0;
	}

	@DeleteMapping("/{id}")
	public Boolean delete(String tableName, @PathVariable Serializable id) {
		Connection conn = null;
		boolean isOk = false;

		try {
			conn = ds.getConnection();
			isOk = JdbcHelper.deleteById(conn, tableName, id);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcConnection.closeDb(conn);
		}

		return isOk;
	}
}
