/*
 * Copyright 2015, Yahoo Inc.
 * Copyrights licensed under the Apache License.
 * See the accompanying LICENSE file for terms.
 */
package com.ajaxjs.mysql.db;

import com.ajaxjs.mysql.model.DBConnectionWrapper;

public interface DynamicQuery {
	String getQueryString(DBConnectionWrapper conn, boolean gc);
}
