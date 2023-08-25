package com.ajaxjs.framework.spring.filter.dbconnection;

import com.ajaxjs.data.jdbc_helper.JdbcConn;
import com.ajaxjs.framework.spring.DiContextUtil;
import com.ajaxjs.framework.spring.filter.GlobalExceptionHandler;
import com.ajaxjs.util.logger.LogHelper;
import org.springframework.lang.Nullable;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

/**
 * 1、数据库连接、关闭连接；2、数据库事务
 * <a href="https://docs.oracle.com/javase/tutorial/jdbc/basics/transactions.html">...</a>
 * <p>
 * Spring MVC 不能只针对控制器方法进行拦截，而是类似 Servlet Filter 那样的 URL 的拦截。于是静态网页的也会拦截到。
 * 这对性能是减分的，多余的。好像
 * DefaultAnnotationHandlerMapping和AnnotationMethodHandlerAdapter
 * 可以对控制器方法进行拦截，但貌似过时和不知道怎么用 AOP 在控制器上不能直接用，因为控制器的方法都是经过代理包装的
 * <a href="http://www.blogjava.net/atealxt/archive/2009/09/20/spring_mvc_annotation_interceptor_gae.html">...</a>
 * <p>
 * 关于springmvc拦截器不拦截jsp页面的折腾
 * <a href="https://blog.csdn.net/qq_21294095/article/details/85019603">...</a>
 *
 * @author Frank Cheung
 */
public class DataBaseConnection implements HandlerInterceptor {
    private static final LogHelper LOGGER = LogHelper.getLog(DataBaseConnection.class);

//	private DataSource ds;

    // TODO 没权限时候还会连接，应该禁止 有时会连接两次
    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();

            if (method != null) {
                Connection connection = null;

                // 默认所有控制器方法，要连接数据库，除了带 IgnoreDataBaseConnect 注解的
                if (method.getAnnotation(IgnoreDataBaseConnect.class) == null)
                    connection = initDb();

                if (connection != null && method.getAnnotation(EnableTransaction.class) != null) {
                    try {
                        connection.setAutoCommit(false);
                    } catch (SQLException e) {
                        LOGGER.warning(e);
                    }
                }
            }
        }

        return true;
    }

    public static Connection initDb() {
        DataSource ds = DiContextUtil.getBean(DataSource.class);
        Objects.requireNonNull(ds, "未配置数据源");
        Connection conn = new JdbcConn().getConnection(ds);
        JdbcConn.setConnection(conn); // 设置连接到库，使其可用

        return conn;
    }

    /**
     * 为方便单测，设一个开关
     */
    public static boolean IS_DB_CONNECTION_AUTO_CLOSE = true;

    @Override
    public void afterCompletion(HttpServletRequest req, HttpServletResponse resp, Object handler, @Nullable Exception ex) {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();

            if (method != null && method.getAnnotation(IgnoreDataBaseConnect.class) == null) {// 有注解
                try {
                    if (method.getAnnotation(EnableTransaction.class) != null) {
                        Object obj = req.getAttribute(GlobalExceptionHandler.EXCEPTION_CXT_KEY);
                        if (ex == null && obj != null)
                            ex = (Exception) obj;

                        doTransaction(ex);
                    }
                } catch (Throwable e) {
                    LOGGER.warning(e);
                } finally {
                    if (IS_DB_CONNECTION_AUTO_CLOSE) // 保证一定关闭，哪怕有异常
                        JdbcConn.closeDb();
                }
            }

        }
    }

    private static void doTransaction(Exception ex) throws SQLException {
        LOGGER.info("正在处理数据库事务……");
        Connection conn = JdbcConn.getConnection();

        if (conn.isClosed())
            throw new SQLException("数据库连接已经关闭");

        if (conn.getAutoCommit())
            throw new SQLException("数据库连接没有关闭自动提交事务");

        if (ex != null)
            conn.rollback();
        else
            conn.commit();

        conn.setAutoCommit(true);
    }
}
