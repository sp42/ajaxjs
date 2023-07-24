/*
 * Copyright 2015, Yahoo Inc.
 * Copyrights licensed under the Apache License.
 * See the accompanying LICENSE file for terms.
 */
package com.ajaxjs.mysql.db;

import java.util.Map;

import com.ajaxjs.mysql.config.MyPerfContext;
import com.ajaxjs.mysql.model.DBConnectionWrapper;
import com.ajaxjs.mysql.model.DBInstanceInfo;
import com.ajaxjs.mysql.model.QueryParameters;
import com.ajaxjs.mysql.model.ResultList;

public interface CustomQueryProcessor {
    /**
     * @param context
     * @param dbinfo
     * @param appUser
     * @param connWrapper
     * @param qps         original query parameters
     * @param rListMap    output
     * @throws java.sql.SQLException
     */
    void queryMultiple(MyPerfContext context, DBInstanceInfo dbinfo, String appUser, DBConnectionWrapper connWrapper, QueryParameters qps, Map<String, ResultList> rListMap) throws java.sql.SQLException;

    /**
     * @param context
     * @param dbinfo
     * @param appUser
     * @param connWrapper
     * @param qps         original input
     * @return result
     * @throws java.sql.SQLException
     */
    ResultList querySingle(MyPerfContext context, DBInstanceInfo dbinfo, String appUser, DBConnectionWrapper connWrapper, QueryParameters qps) throws java.sql.SQLException;

    /**
     * indicate if it is multiple
     *
     * @return
     */
    boolean isMultiple();

    /**
     * indicate if require db connection. For example, SNMP query does not need.
     *
     * @return
     */
    boolean requireDBConnection();
}
