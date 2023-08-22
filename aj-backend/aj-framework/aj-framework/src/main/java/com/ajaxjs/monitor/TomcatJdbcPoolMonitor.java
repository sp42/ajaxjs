package com.ajaxjs.monitor;

import javax.management.*;
import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * 通过 JMX 获取 Tomcat JDBC Pool 监控情况
 */
public class TomcatJdbcPoolMonitor {
    /**
     * 一般来说 Tomcat 会自动注册但是我们现在手动使用 Pool，于是也得手动地注册到 MBean
     */
    public static void registerMBean(org.apache.tomcat.jdbc.pool.DataSource ds) {
        try {
            MBeanServer server = ManagementFactory.getPlatformMBeanServer();
            ObjectName on = new ObjectName("org.apache.tomcat.jdbc.pool.jmx.ConnectionPool:type=Logging2");
            server.registerMBean(ds.getPool().getJmxPool(), on);
        } catch (Throwable e) {
            System.err.println(e);
        }
    }

    /**
     * 获取 Tomcat JDBC Pool 监控情况
     */
    public static Map<String, Object> jdbcPoolStatus() {
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        Set<ObjectName> objectNames = server.queryNames(null, null);
        Map<String, Object> result = new HashMap<>();

        try {
            for (ObjectName name : objectNames) {
                MBeanInfo info = server.getMBeanInfo(name);

                if (info.getClassName().equals("org.apache.tomcat.jdbc.pool.jmx.ConnectionPool")) {
                    for (MBeanAttributeInfo mf : info.getAttributes()) {
                        Object attributeValue = server.getAttribute(name, mf.getName());

                        if (attributeValue != null)
                            result.put(mf.getName(), attributeValue);
                    }

                    break;
                }
            }
        } catch (InstanceNotFoundException | IntrospectionException | ReflectionException | MBeanException |
                 AttributeNotFoundException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    public final static Map<String, String> fields = new LinkedHashMap<>();

    static {
        fields.put("Name", "连接池的名称");
        fields.put("PoolName", "连接池的名称");
        fields.put("InitialSize", "初始连接数");
        fields.put("Size", "当前连接池大小");
        fields.put("Active", "当前正在使用的连接数");
        fields.put("CreatedCount", "创建的连接数");
        fields.put("BorrowedCount", "已借用的连接数");
        fields.put("Idle", "空闲的连接数");
        fields.put("MaxActive", "最大活跃连接数");
        fields.put("MaxAge", "连接的最大存活时间");
        fields.put("MaxIdle", "最大空闲连接数");
        fields.put("MaxWait", "获取连接的最大等待时间");
        fields.put("MinIdle", "最小空闲连接数");
        fields.put("NumActive", "当前活跃的连接数");
        fields.put("NumIdle", "当前空闲的连接数");
        fields.put("ReconnectedCount", "重连的次数");
        fields.put("ReleasedCount", "已释放的连接数");
        fields.put("ReleasedIdleCount", "已释放但仍然处于空闲状态的连接数");
        fields.put("RemoveAbandoned", "是否移除被遗弃的连接");
        fields.put("RemoveAbandonedCount", "移除的被遗弃连接数");
        fields.put("RemoveAbandonedTimeout", "被遗弃连接的超时时间");
        fields.put("ReturnedCount", "已返回的连接数");
        fields.put("AbandonWhenPercentageFull", "当连接池使用率达到指定百分比时，是否放弃获取连接，默认为 0，表示不放弃");
        fields.put("AccessToUnderlyingConnectionAllowed", "是否允许直接访问底层连接对象，默认为 true");
        fields.put("AlternateUsernameAllowed", "是否允许使用替代用户名（alternative username）");
        fields.put("CommitOnReturn", "返回连接时是否自动提交事务，默认为 false");
        fields.put("DbProperties", "数据库连接属性");
        fields.put("DefaultAutoCommit", "默认是否自动提交事务");
        fields.put("DefaultTransactionIsolation", "默认事务隔离级别");
        fields.put("DriverClassName", "数据库驱动类名");
        fields.put("FairQueue", "是否使用公平队列");
        fields.put("IgnoreExceptionOnPreLoad", "在预加载时是否忽略异常");
        fields.put("JdbcInterceptorsAsArray", "连接池拦截器定义的数组");
        fields.put("LogAbandoned", "是否记录被遗弃的连接");
        fields.put("LogValidationErrors", "是否记录验证错误");
        fields.put("MinEvictableIdleTimeMillis", "连接在池中最小空闲时间，超过该时间将被清除");
        fields.put("NumTestsPerEvictionRun", "每次空闲连接回收执行的测试数量");
        fields.put("PoolSweeperEnabled", "是否开启连接池扫描器");
        fields.put("PropagateInterruptState", "是否传播中断状态");
        fields.put("RollbackOnReturn", "返回连接时是否回滚事务，默认为 false");
        fields.put("SuspectTimeout", "连接的可疑超时时间，用于检测连接是否超时");
        fields.put("TestOnBorrow", "借用连接时是否执行测试");
        fields.put("TestOnConnect", "连接时是否执行测试");
        fields.put("TestOnReturn", "归还连接时是否执行测试");
        fields.put("TestWhileIdle", "连接处于空闲状态时是否定期执行测试");
        fields.put("TimeBetweenEvictionRunsMillis", "空闲连接检测线程运行的时间间隔");
        fields.put("UseDisposableConnectionFacade", "是否使用可释放连接外观类");
        fields.put("UseLock", "是否使用锁");
        fields.put("UseStatementFacade", "是否使用可释放语句外观类");
        fields.put("ValidationInterval", "验证连接的间隔时间");
        fields.put("ValidationQueryTimeout", "验证连接的超时时间");
        fields.put("WaitCount", "等待获取连接的数量");
    }
}
