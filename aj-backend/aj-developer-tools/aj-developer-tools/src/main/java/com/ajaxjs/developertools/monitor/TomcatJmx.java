package com.ajaxjs.developertools.monitor;

import com.ajaxjs.developertools.monitor.model.tomcat.*;
import com.ajaxjs.util.DateUtil;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.util.reflect.BeanUtils;
import org.springframework.util.CollectionUtils;

import javax.management.*;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.remote.JMXConnector;
import java.io.IOException;
import java.lang.management.MemoryUsage;
import java.util.*;

/**
 * 获取 Tomcat 的 JMX 信息
 */
public class TomcatJmx extends JmxHelper {
    private static final LogHelper LOGGER = LogHelper.getLog(TomcatJmx.class);

    public TomcatInfo getInfo(String jmxURL) {
        return getInfo(jmxURL, null);
    }

    public TomcatInfo getInfo(String jmxURL, Integer port) {
        TomcatInfo info = new TomcatInfo();

        try (JMXConnector connect = connect(jmxURL)) {
            assert connect != null;
            MBeanServerConnection msc = connect.getMBeanServerConnection();
            setMsc(msc);

            SystemInfo systemInfo = new SystemInfo();
            ThreadPool threadPool = new ThreadPool();
            info.jvmInfo = jvm();
            info.systemInfo = systemInfo;
            info.session = getSession();
            info.threadPool = threadPool;

            if (port == null) port = getTomcatPort(msc);

            everyAttribute(objectNameFactory("Tomcat:name=\"http-nio-" + port + "\",type=ThreadPool"), (key, value) -> BeanUtils.setBeanValue(threadPool, key, value));
            everyAttribute(objectNameFactory("java.lang:type=Runtime"), (key, value) -> {
                if ("StartTime".equals(key)) {
                    Date startTime = new Date((Long) value);
                    BeanUtils.setBeanValue(systemInfo, key, DateUtil.formatDate(startTime));
                } else if ("Uptime".equals(key)) {
                    Date startTime = new Date((Long) value);
                    BeanUtils.setBeanValue(systemInfo, key, formatTimeSpan((Long) value));
                } else BeanUtils.setBeanValue(systemInfo, key, value);
            });
        } catch (IOException e) {
            LOGGER.warning(e);
        }

        return info;
    }

    /**
     * 获取 tomcat 运行端口
     */
    private static int getTomcatPort(MBeanServerConnection msc) {
        try {
            Set<ObjectName> objectNames = queryNames(msc, "Tomcat:type=Connector,*");

            if (CollectionUtils.isEmpty(objectNames))
                throw new IllegalStateException("没有发现JVM中关联的MBeanServer : " + msc.getDefaultDomain() + " 中的对象名称.");

            for (ObjectName objectName : objectNames) {
                String protocol = (String) msc.getAttribute(objectName, "protocol");

                if (protocol.equals("HTTP/1.1")) return (Integer) msc.getAttribute(objectName, "port");
            }
        } catch (MBeanException | AttributeNotFoundException | ReflectionException | InstanceNotFoundException |
                 IOException e) {
            LOGGER.warning(e);
        }

        return 0;
    }

    private List<Session> getSession() {
        Set<ObjectName> objectNames = queryNames("Tomcat:type=Manager,*");
        List<Session> list = new ArrayList<>(objectNames.size());

        for (ObjectName obj : objectNames) {
//            List<Node> tomcat = JmxUtils.getObjectNamesByDomain(msc, "Tomcat");
//                System.out.println("应用名:" + obj.getKeyProperty("path"));
//                System.out.println("currentThreadCount:" + msc.getAttribute(threadObjName, "currentThreadCount"));// tomcat的线程数对应的属性值

            Session session = new Session();
            everyAttribute(objectNameFactory(obj.getCanonicalName()), (key, value) -> BeanUtils.setBeanValue(session, key, value));
            list.add(session);
        }

        return list;
    }

    private JvmInfo jvm() {
        try {
            // 堆使用率
            ObjectName heapObjName = objectNameFactory("java.lang:type=Memory");
            MemoryUsage heapMemoryUsage = MemoryUsage.from((CompositeDataSupport) getMsc().getAttribute(heapObjName, "HeapMemoryUsage"));

            // 堆当前分配
            long commitMemory = heapMemoryUsage.getCommitted(), usedMemory = heapMemoryUsage.getUsed();
            JvmInfo jvmInfo = new JvmInfo();
            jvmInfo.setMaxMemory(heapMemoryUsage.getMax());
            jvmInfo.setHeap(((Long) (usedMemory * 100 / commitMemory)).intValue());

            MemoryUsage nonheapMemoryUsage = MemoryUsage.from((CompositeDataSupport) getMsc().getAttribute(heapObjName, "NonHeapMemoryUsage"));
            long nonCommitMemory = nonheapMemoryUsage.getCommitted(), nonUsedMemory = heapMemoryUsage.getUsed();
            jvmInfo.setNonCommitMemory(nonCommitMemory);
            jvmInfo.setNonUsedMemory(nonUsedMemory);
            jvmInfo.setNonHeap(((Long) (nonUsedMemory * 100 / nonCommitMemory)).intValue());

//            ObjectName permObjName = new ObjectName("java.lang:type=MemoryPool,name=Perm Gen");
//            MemoryUsage permGenUsage = MemoryUsage.from((CompositeDataSupport) getMsc().getAttribute(permObjName, "Usage"));
//            long committed = permGenUsage.getCommitted();
//            long used = heapMemoryUsage.getUsed();
//
//            jvmInfo.setCommitted(committed);
//            jvmInfo.setUsed(used);
//            jvmInfo.setPermUse(((Long) (used * 100 / committed)).intValue());

            return jvmInfo;
        } catch (ReflectionException | AttributeNotFoundException | InstanceNotFoundException | MBeanException |
                 IOException e) {
            LOGGER.warning(e);
        }

        return null;
    }

    private static String formatTimeSpan(long span) {
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
