package com.ajaxjs.data.jdbc_helper;

import com.ajaxjs.Version;
import com.ajaxjs.util.logger.LogHelper;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 数据库连接
 */
public class JdbcConn {
    private static final LogHelper LOGGER = LogHelper.getLog(JdbcConn.class);

    /**
     * 数据库连接对象
     */
    Connection conn;

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public Connection getConn() {
        if (conn == null)
            LOGGER.warning("未准备好数据库连接");

        return conn;
    }

    /**
     * 连接数据库。这种方式最简单，但是没有经过数据库连接池。
     * 有时不能把 user 和 password 写在第一个 jdbc 连接字符串上 那样会连不通
     * 分开 user 和 password 就可以
     *
     * @param jdbcUrl  数据库连接字符串，不包含用户名和密码
     * @param userName 用户
     * @param password 密码
     * @return 数据库连接对象
     */
    public Connection getConnection(String jdbcUrl, String userName, String password) {
        try {

            if (StringUtils.hasText(userName) && StringUtils.hasText(password))
                conn = DriverManager.getConnection(jdbcUrl, userName, password);
            else conn = DriverManager.getConnection(jdbcUrl);

            LOGGER.info("数据库连接成功： " + conn.getMetaData().getURL());
        } catch (SQLException e) {
            LOGGER.warning("数据库连接失败！", e);
        }

        return conn;
    }

    /**
     * 连接数据库。这种方式最简单，但是没有经过数据库连接池。
     *
     * @param jdbcUrl 数据库连接字符串，已包含用户名和密码
     * @return 数据库连接对象
     */
    public Connection getConnection(String jdbcUrl) {
        return getConnection(jdbcUrl, null, null);
    }

    /**
     * 一般情况用的数据库连接字符串
     */
    public static final String JDBC_TPL = "jdbc:mysql://%s/%s?characterEncoding=utf-8&useSSL=false&autoReconnect=true&" +
            "allowPublicKeyRetrieval=true&zeroDateTimeBehavior=convertToNull&rewriteBatchedStatements=true&serverTimezone=Asia/Shanghai";

    /**
     * 连接数据库
     *
     * @param ipPort   数据库地址和端口
     * @param dbName   数据库名，可为空字符串
     * @param userName 用户
     * @param password 密码
     * @return 数据库连接对象
     */
    public Connection getConnection(String ipPort, String dbName, String userName, String password) {
        return getConnection(String.format(JDBC_TPL, ipPort, dbName), userName, password);
    }

    /**
     * 根据数据源对象获得数据库连接对象
     *
     * @param source 数据源对象
     * @return 数据库连接对象
     */
    public Connection getConnection(DataSource source) {
        try {
            conn = source.getConnection();

            if (conn == null) LOGGER.warning("DataSource 不能建立数据库连接");

            if (Version.isDebug)
                LOGGER.info("数据库连接成功： " + conn.getMetaData().getURL());
        } catch (SQLException e) {
            LOGGER.warning(e, "通过数据源对象获得数据库连接对象失败！");
        }

        return conn;
    }

    /**
     * 当前进程的数据库连接
     */
    private static final ThreadLocal<Connection> CONNECTION = new ThreadLocal<>();

    /**
     * 获取一个当前进程的数据库连接
     *
     * @return 当前进程的数据库连接对象
     */
    public static Connection getConnection() {
        return CONNECTION.get();
    }

    /**
     * 保存一个数据库连接对象到当前进程
     *
     * @param conn 当前进程的数据库连接对象
     */
    public static void setConnection(Connection conn) {
        CONNECTION.set(conn);
    }

    /**
     * 关闭当前进程的数据库连接
     */
    public static void closeDb() {
        closeDb(getConnection());
        CONNECTION.set(null);
    }

    /**
     * 关闭数据库连接
     *
     * @param conn 数据库连接对象
     */
    public static void closeDb(Connection conn) {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                if (Version.isDebug)
                    LOGGER.info("关闭数据库连接成功！ Closed database OK！");
            }
        } catch (SQLException e) {
            LOGGER.warning(e);
        }
    }

    /**
     * 手动创建连接池。这里使用了 Tomcat JDBC Pool
     *
     * @param driver   驱动程序，如 com.mysql.cj.jdbc.Driver
     * @param url      数据库连接字符串
     * @param userName 用户
     * @param password 密码
     * @return 数据源
     */
    public static DataSource setupJdbcPool(String driver, String url, String userName, String password) {
        PoolProperties p = new PoolProperties();
        p.setDriverClassName(driver);
        p.setUrl(url);
        p.setUsername(userName);
        p.setPassword(password);
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

    /**
     * 对 PS 设置值
     *
     * @param ps     PreparedStatement
     * @param params 插入到 SQL 中的参数，可单个可多个可不填
     * @throws SQLException 异常
     */
    void setParam2Ps(PreparedStatement ps, Object... params) throws SQLException {
        if (!ObjectUtils.isEmpty(params)) {
            /*
             LogHelper.p(params);
             int i = 0;
            for (Object param : params)
               ps.setObject(++i, param);
             */
            for (int i = 0; i < params.length; i++)
                ps.setObject(i + 1, params[i]);
        }
    }

}
