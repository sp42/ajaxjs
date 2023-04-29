/**
 * Copyright sp42 frank@ajaxjs.com
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ajaxjs.sql;

import com.ajaxjs.Version;
import com.ajaxjs.util.logger.LogHelper;
import org.apache.tomcat.jdbc.pool.PoolProperties;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * 连接数据库。 保存线程内的连接对象，还可以保存调用过的 SQL 语句，以便于日志记录
 *
 * @author pp42 frank@ajaxjs.com
 */
public class JdbcConnection {
    private static final LogHelper LOGGER = LogHelper.getLog(JdbcConnection.class);

    public static Connection getConnection(String ipPort, String dbName, String userName, String password) {
        String jdbcUrl = "jdbc:mysql://%s/%s?characterEncoding=utf-8&useSSL=false&autoReconnect=true&zeroDateTimeBehavior=convertToNull&rewriteBatchedStatements=true&serverTimezone=Asia/Shanghai";
        jdbcUrl = String.format(jdbcUrl, ipPort, dbName);

        Connection conn = null;

        try {
            // 有时不能把 user 和 password 写在第一个 jdbc 连接字符串上 那样会连不通
            // 分开 user 和 password 就可以
            conn = DriverManager.getConnection(jdbcUrl, userName, password);
            LOGGER.info("数据库连接成功： " + conn.getMetaData().getURL());
        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.warning("数据库连接失败！", e);
        }

        return conn;
    }

    /**
     * 连接数据库。这种方式最简单，但是没有经过数据库连接池，要采用池化的方式，请使用 getConnectionByJNDI()。
     *
     * @param jdbcUrl 连接字符串
     * @param props   连接属性，可选（可为 null）
     * @return 数据库连接对象
     */
    public static Connection getConnection(String jdbcUrl, Properties props) {
        Connection conn = null;

        try {
            if (props == null)
                conn = DriverManager.getConnection(jdbcUrl);
            else
                conn = DriverManager.getConnection(jdbcUrl, props);

            LOGGER.info("数据库连接成功： " + conn.getMetaData().getURL());
        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.warning("数据库连接失败！", e);
        }

        return conn;
    }

    /**
     * 连接数据库。这种方式最简单，但是没有经过数据库连接池，要采用池化的方式，请使用 getConnectionByJNDI()。
     *
     * @param jdbcUrl 连接字符串
     * @return 数据库连接对象
     */
    public static Connection getConnection(String jdbcUrl) {
        return getConnection(jdbcUrl, null);
    }

    /**
     * 根据数据源对象获得数据库连接对象
     *
     * @param source 数据源对象
     * @return 数据库连接对象
     */
    public static Connection getConnection(DataSource source) {
        try {
            Connection conn = source.getConnection();

            if (conn == null)
                LOGGER.warning("DataSource 不能建立数据库连接");

            return conn;
        } catch (SQLException e) {
            LOGGER.warning(e, "通过数据源对象获得数据库连接对象失败！");
            return null;
        }
    }

    /**
     * 数据库连接对象
     */
    private static final ThreadLocal<Connection> connection = new ThreadLocal<>();

    /**
     * 获取一个数据库连接
     *
     * @return 数据库连接对象
     */
    public static Connection getConnection() {
        return connection.get();
    }

    /**
     * 保存一个数据库连接对象
     *
     * @param conn 数据库连接对象
     */
    public static void setConnection(Connection conn) {
        connection.set(conn);
    }

    /**
     * 关闭数据库连接
     */
    public static void closeDb() {
        closeDb(getConnection());
        connection.set(null);
    }

    public static void closeDb(Connection conn) {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                if (Version.isDebug)
                    LOGGER.info("关闭数据库连接成功！ Close database OK！");
            }
        } catch (SQLException e) {
            LOGGER.warning(e);
        }
    }

    /**
     * 根据 Conn 获取连接字符串，在调试时候非常有用
     *
     * @param conn
     * @return
     */
    public static String getDbUrl(Connection conn) {
        try {
            return conn.getMetaData().getURL();
        } catch (SQLException e) {
            LOGGER.warning(e);
            return null;
        }
    }

    /**
     * 根据 JDBC 连接字符串创建 MySql 数据库连接对象
     *
     * @param jdbcUrl  JDBC 连接字符串。如果连接字符串已经包含用户名和密码，请直接使用 getConnection() 方法即可。
     * @param username 数据库用户名
     * @param password 数据库密码
     * @return 数据库连接对象
     */
    public static Connection getMySqlConnection(String jdbcUrl, String username, String password) {
        if (!jdbcUrl.startsWith("jdbc:mysql://"))
            throw new IllegalArgumentException("参数为非法的 JDBC URL： " + jdbcUrl);

        if (!jdbcUrl.contains("?"))
            jdbcUrl += "?";

        return getConnection(jdbcUrl + "&user=" + username + "&password=" + password);
    }

    /**
     * 手动创建连接池。这里使用了 Tomcat JDBC Pool
     *
     * @param driver
     * @param url
     * @param user
     * @param psw
     * @return 数据源
     */
    public static DataSource setupJdbcPool(String driver, String url, String user, String psw) {
        PoolProperties p = new PoolProperties();
        p.setDriverClassName(driver);
        p.setUrl(url);
        p.setUsername(user);
        p.setPassword(psw);
        p.setMaxActive(100);
        p.setInitialSize(10);
        p.setMaxWait(10000);
        p.setMaxIdle(30);
        p.setMinIdle(5);
        p.setTestOnBorrow(true);
        p.setTestWhileIdle(true);
        p.setTestOnReturn(true);
        p.setValidationInterval(18800);
        p.setDefaultAutoCommit(true);
        org.apache.tomcat.jdbc.pool.DataSource ds = new org.apache.tomcat.jdbc.pool.DataSource();
        ds.setPoolProperties(p);

        return ds;
    }
}
