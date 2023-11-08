package com.ajaxjs.iam.user.service;

import org.springframework.stereotype.Service;

@Service
public class OrgService {

    public boolean delete(Long id) {
        // @formatter:off
		String sql = "DELETE FROM user_org WHERE id IN (WITH RECURSIVE td AS ("
				+ "    SELECT id FROM user_org WHERE id = ?"
				+ "    UNION ALL"
				+ "    SELECT c.id FROM user_org c , td WHERE c.pid = td.id"
				+ ") SELECT id FROM td ORDER BY td.id);";
		// @formatter:on

        return true;
    }

}
