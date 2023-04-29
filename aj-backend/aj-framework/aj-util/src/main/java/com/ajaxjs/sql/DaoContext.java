/**
 * Copyright Sp42 frank@ajaxjs.com Licensed under the Apache License, Version
 * 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package com.ajaxjs.sql;

import lombok.Data;

import java.sql.Connection;
import java.util.List;

/**
 * DAO 的上下文对象
 *
 * @author sp42 frank@ajaxjs.com
 */
@Data
public class DaoContext {
    /**
     * 当前的数据库连接对象
     */
    private Connection connection;

    /**
     * 查询参数列表
     */
    private Object[] params;

    /**
     * 所有执行的 SQL 语句
     */
    private List<String> sql;

    private DataBaseType dbType;

    public void setConnection(Connection conn) {
        if (DataBaseType.isMySql(conn))
            dbType = DataBaseType.MYSQL;
        else if (DataBaseType.isSqlite(conn))
            dbType = DataBaseType.SQLITE;

        this.connection = conn;
    }
}
