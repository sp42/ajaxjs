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

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.ajaxjs.util.ListUtils;
import com.ajaxjs.util.logger.LogHelper;

/**
 * @author Frank Cheung sp42@qq.com
 */
public class Jdbc extends JdbcHelper {
    private static final LogHelper LOGGER = LogHelper.getLog(JdbcHelper.class);

    private DataSource ds;

    private Connection conn;

    /**
     * @param ds 数据源对象
     */
    public Jdbc(DataSource ds) {
        this.ds = ds;
    }

    /**
     * @param conn 数据库连接对象
     */
    public Jdbc(Connection conn) {
        this.conn = conn;
    }

    public <T> T info(Class<T> beanClz, String sql, Object... params) {
        try (Connection conn = (this.conn == null ? ds.getConnection() : this.conn)) {
            return queryAsBean(beanClz, conn, sql, params);
        } catch (SQLException e) {
            LOGGER.warning(e);
            return null;
        }
    }

    /**
     * @param <T>
     * @param sql
     * @param beanClz Bean 类引用
     * @param params
     * @return
     */
    public <T> List<T> queryAsBeanList(String sql, Class<T> beanClz, Object... params) {
        try {
            if (this.conn == null)
                this.conn = ds.getConnection();

            try (Connection conn = this.conn) {
                return queryAsBeanList2(sql, beanClz, params);
            }
        } catch (SQLException e) {
            LOGGER.warning(e);
            return Collections.emptyList();
        }
    }

    /**
     * 该方法不关闭数据库连接
     *
     * @param <T>
     * @param sql     SQL 语句，支持 ? 的占位符
     * @param beanClz Bean 类引用
     * @param params  SQL 值参数
     * @return
     */
    public <T> List<T> queryAsBeanList2(String sql, Class<T> beanClz, Object... params) {
        return ListUtils.getList(queryAsBeanList(beanClz, conn, sql, params));
    }

    /**
     * 该方法不关闭数据库连接
     *
     * @param sql    SQL 语句，支持 ? 的占位符
     * @param params SQL 值参数
     * @return
     */
    public List<Map<String, Object>> queryAsMapList2(String sql, Object... params) {
        return ListUtils.getList(queryAsMapList(conn, sql, params));
    }

    public List<Map<String, Object>> queryAsMapList(String sql, Object... params) {
        try (Connection conn = (this.conn == null ? ds.getConnection() : this.conn)) {
            List<Map<String, Object>> beanList = queryAsMapList(conn, sql, params);

            return ListUtils.getList(beanList);
        } catch (SQLException e) {
            LOGGER.warning(e);
            return Collections.emptyList();
        }
    }

    public Serializable createBean(Object bean, String tableName) {
        try (Connection conn = (this.conn == null ? ds.getConnection() : this.conn)) {
            return createBean(conn, bean, tableName);
        } catch (SQLException e) {
            LOGGER.warning(e);
            return false;
        }
    }

    public int updateBean(Object bean, String tableName) {
        try (Connection conn = (this.conn == null ? ds.getConnection() : this.conn)) {
            return updateBean(conn, bean, tableName);
        } catch (SQLException e) {
            LOGGER.warning(e);
            return 0;
        }
    }

    public boolean delete(Serializable id, String tableName) {
        try (Connection conn = (this.conn == null ? ds.getConnection() : this.conn)) {
            return deleteById(conn, tableName, id);
        } catch (SQLException e) {
            LOGGER.warning(e);
            return false;
        }
    }

    public boolean deleteInId(String tableName, String ids) {
        try (Connection conn = (this.conn == null ? ds.getConnection() : this.conn)) {
            return update(conn, "DELETE FROM " + tableName + " WHERE id IN ?", ids) == 1;
        } catch (SQLException e) {
            LOGGER.warning(e);
            return false;
        }
    }
}
