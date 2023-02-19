package com.ajaxjs.data_service.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ajaxjs.data_service.model.Project;
import com.ajaxjs.sql.Jdbc;

public abstract class ProjectService {
	/**
	 * 返回数据库连接
	 *
	 * @return
	 */
	protected abstract Connection getConnection();

	/**
	 * 返回数据服务配置所在的表名
	 *
	 * @return
	 */
	protected abstract String getDataServiceTableName();

	String tableName = "aj_base.adp_project";

	@GetMapping("/project")
	public List<Project> getProejctList() {
		String sql = "SELECT * FROM aj_base.adp_project";
		return new Jdbc(getConnection()).queryAsBeanList(sql, Project.class);
	}

	@PostMapping("/project")
	public boolean create(@RequestBody Project project) {
		return (new Jdbc(getConnection()).createBean(project, tableName) != null);
	}

	@PutMapping("/project")
	public boolean update(@RequestBody Project project) {
		return (new Jdbc(getConnection()).updateBean(project, tableName) > 0);
	}

	@DeleteMapping("/project/{id}")
	public boolean delete(@PathVariable Long id) {
		try (Connection conn = getConnection()) {
			String ids = "SELECT id FROM " + getDataServiceTableName() + " WHERE project_id = " + id;
			Jdbc.update(conn, "DELETE FROM " + tableName + " WHERE id IN (?)", ids);
			Jdbc.deleteById(conn, tableName, id);

			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

}
