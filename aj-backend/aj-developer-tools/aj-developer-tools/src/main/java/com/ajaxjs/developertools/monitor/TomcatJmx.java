package com.ajaxjs.developertools.monitor;

import com.ajaxjs.developertools.monitor.model.tomcat.Session;
import com.ajaxjs.developertools.monitor.model.tomcat.ThreadPool;
import com.ajaxjs.developertools.monitor.model.tomcat.TomcatInfo;
import com.ajaxjs.util.DateUtil;
import com.ajaxjs.util.logger.LogHelper;
import org.springframework.util.CollectionUtils;

import javax.management.*;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.remote.JMXConnector;
import java.io.IOException;
import java.lang.management.MemoryUsage;
import java.util.*;

/**
 *
 */
public class TomcatJmx {
    private static final LogHelper LOGGER = LogHelper.getLog(TomcatJmx.class);

    public static TomcatInfo getInfo(String jmxURL) {
        return getInfo(jmxURL, null);
    }

    public static TomcatInfo getInfo(String jmxURL, Integer port) {
        TomcatInfo info = new TomcatInfo();

        try (JMXConnector connect = JmxUtils.connect(jmxURL)) {
            assert connect != null;
            MBeanServerConnection msc = connect.getMBeanServerConnection();
//            List<Node> tomcat = JmxUtils.getObjectNamesByDomain(msc, "Tomcat");

            info.session = getSession(msc);
            info.threadPool = getThreadList(msc);
//            system(msc);
//            jvm(msc);

            if (port == null)
                port = getTomcatPort(msc);

            ObjectName threadObjName = new ObjectName("Tomcat:name=\"http-nio-" + port + "\",type=ThreadPool");
            System.out.println("currentThreadCount:" + msc.getAttribute(threadObjName, "currentThreadCount"));// tomcat的线程数对应的属性值
        } catch (ReflectionException | MalformedObjectNameException | AttributeNotFoundException |
                 InstanceNotFoundException | MBeanException | IOException e) {
            e.printStackTrace();
        }

        return info;
    }

    /**
     * 获取 tomcat 运行端口
     */
    private static int getTomcatPort(MBeanServerConnection msc) {
        try {
            Set<ObjectName> objectNames = msc.queryNames(new ObjectName("Tomcat:type=Connector,*"), null);

            if (CollectionUtils.isEmpty(objectNames))
                throw new IllegalStateException("没有发现JVM中关联的MBeanServer : " + msc.getDefaultDomain() + " 中的对象名称.");

            for (ObjectName objectName : objectNames) {
                String protocol = (String) msc.getAttribute(objectName, "protocol");

                if (protocol.equals("HTTP/1.1")) return (Integer) msc.getAttribute(objectName, "port");
            }
        } catch (MalformedObjectNameException | MBeanException | AttributeNotFoundException | ReflectionException |
                 InstanceNotFoundException | IOException e) {
            e.printStackTrace();
        }

        return 0;
    }

    private static void jvm(MBeanServerConnection msc) {
        try {
            // 堆使用率
            ObjectName heapObjName = new ObjectName("java.lang:type=Memory");
            MemoryUsage heapMemoryUsage = MemoryUsage.from((CompositeDataSupport) msc.getAttribute(heapObjName, "HeapMemoryUsage"));
            long maxMemory = heapMemoryUsage.getMax();// 堆最大
            long commitMemory = heapMemoryUsage.getCommitted();// 堆当前分配
            long usedMemory = heapMemoryUsage.getUsed();
            System.out.println("heap:" + (double) usedMemory * 100 / commitMemory + "%");// 堆使用率

            MemoryUsage nonheapMemoryUsage = MemoryUsage.from((CompositeDataSupport) msc.getAttribute(heapObjName, "NonHeapMemoryUsage"));
            long nonCommitMemory = nonheapMemoryUsage.getCommitted();
            long nonUsedMemory = heapMemoryUsage.getUsed();
            System.out.println("nonHeap:" + (double) nonUsedMemory * 100 / nonCommitMemory + "%");

//            ObjectName permObjName = new ObjectName("java.lang:type=MemoryPool,name=Perm Gen");
//            MemoryUsage permGenUsage = MemoryUsage.from((CompositeDataSupport) msc.getAttribute(permObjName, "Usage"));
//            long committed = permGenUsage.getCommitted();// 持久堆大小
//            long used = heapMemoryUsage.getUsed();//
//            System.out.println("perm gen:" + (double) used * 100 / committed + "%");// 持久堆使用率
        } catch (MalformedObjectNameException | MBeanException | AttributeNotFoundException |
                 InstanceNotFoundException | ReflectionException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void system(MBeanServerConnection msc) {
        try {
            ObjectName runtimeObjName = new ObjectName("java.lang:type=Runtime");
            System.out.println("厂商:" + msc.getAttribute(runtimeObjName, "VmVendor"));
            System.out.println("程序:" + msc.getAttribute(runtimeObjName, "VmName"));
            System.out.println("版本:" + msc.getAttribute(runtimeObjName, "VmVersion"));
            Date startTime = new Date((Long) msc.getAttribute(runtimeObjName, "StartTime"));
            System.out.println("启动时间:" + DateUtil.formatDate(startTime));

            Long timeSpan = (Long) msc.getAttribute(runtimeObjName, "Uptime");
            System.out.println("连续工作时间:" + formatTimeSpan(timeSpan));
        } catch (MalformedObjectNameException | MBeanException | AttributeNotFoundException |
                 InstanceNotFoundException | ReflectionException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<Session> getSession(MBeanServerConnection msc) {
        List<Session> list = new ArrayList<>();

        try {
            ObjectName managerObjName = new ObjectName("Catalina:type=Manager,*");
            Set<ObjectName> s = msc.queryNames(managerObjName, null);

            for (ObjectName obj : s) {
                System.out.println("应用名:" + obj.getKeyProperty("path"));
                ObjectName objName = new ObjectName(obj.getCanonicalName());
                Session session = new Session();
                session.maxActiveSessions = (int) msc.getAttribute(objName, "maxActiveSessions");
                session.activeSessions = (int) msc.getAttribute(objName, "activeSessions");
                session.sessionCounter = (int) msc.getAttribute(objName, "sessionCounter");

                list.add(session);
            }
        } catch (MalformedObjectNameException | IOException | AttributeNotFoundException | InstanceNotFoundException |
                 MBeanException | ReflectionException e) {
            e.printStackTrace();
        }

        return list;
    }

    private static List<ThreadPool> getThreadList(MBeanServerConnection msc) {
        List<ThreadPool> list = new ArrayList<>();

        try {
            ObjectName threadPoolObjName = new ObjectName("Tomcat:type=ThreadPool,*");
            Set<ObjectName> s2 = msc.queryNames(threadPoolObjName, null);

            for (ObjectName obj : s2) {
                System.out.println("端口名:" + obj.getKeyProperty("name"));
                ObjectName objName = new ObjectName(obj.getCanonicalName());

                ThreadPool pool = new ThreadPool();
                pool.maxThreads = (int) msc.getAttribute(objName, "maxThreads");
                pool.currentThreadCount = (int) msc.getAttribute(objName, "currentThreadCount");
                pool.currentThreadsBusy = (int) msc.getAttribute(objName, "currentThreadsBusy");
            }
        } catch (MalformedObjectNameException | IOException | AttributeNotFoundException | InstanceNotFoundException |
                 MBeanException | ReflectionException e) {
            e.printStackTrace();
        }

        return list;
    }

    static String formatTimeSpan(long span) {
        long minSeconds = span % 1000;

        span = span / 1000;
        long seconds = span % 60;

        span = span / 60;
        long min = span % 60;

        span = span / 60;
        long hours = span % 24;

        span = span / 24;
        long days = span;

        try (Formatter formatter = new Formatter()) {
            return formatter.format("%1$d天 %2$02d:%3$02d:%4$02d.%5$03d", days, hours, min, seconds, minSeconds).toString();
        }
    }
}
