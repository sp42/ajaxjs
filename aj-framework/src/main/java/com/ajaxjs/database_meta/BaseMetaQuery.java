package com.ajaxjs.database_meta;

import java.sql.Connection;

public abstract class BaseMetaQuery {
	Connection conn;

	public BaseMetaQuery(Connection conn) {
		this.conn = conn;
	}
}
