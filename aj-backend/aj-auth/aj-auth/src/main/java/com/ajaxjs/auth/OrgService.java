package com.ajaxjs.auth;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ajaxjs.sql.JdbcHelper;

@Service
public class OrgService {
	@Autowired
	DataSource ds;

	public boolean delete(Long id) {
		// @formatter:off
		String sql = "DELETE FROM user_org WHERE id IN (WITH RECURSIVE td AS ("
				+ "    SELECT id FROM user_org WHERE id = ?"
				+ "    UNION ALL"
				+ "    SELECT c.id FROM user_org c , td WHERE c.pid = td.id"
				+ ") SELECT id FROM td ORDER BY td.id);";
		// @formatter:on

		return JdbcHelper.update(ds, sql, id) > 0;
	}

}
