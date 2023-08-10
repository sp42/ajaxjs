package com.ajaxjs.developertools.monitor.jvm;

import com.ajaxjs.developertools.monitor.model.jvm.*;
import com.ajaxjs.developertools.monitor.model.jvm.ThreadInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import java.lang.management.*;
import java.lang.reflect.Field;
import java.util.*;

/**
 * MonitorDashboardService
 * 采集如下几个指标：
 * 1. 内存占用
 * 2. CPU 使用率
 * 3. 线程数量
 * 4. 类加载
 * 5. 操作系统信息 通过 OperatingSystemMXBean 或者直接查询 java.lang:type=OperatingSystem
 * 6. jvm 信息 通过 RuntimeMXBean 或者直接查询 java.lang:type=Runtime
 * 7. 垃圾收集器
 *
 * @author fengzheng
 */
@Service
public class MonitorDashboardService {
    @Autowired
    private JmxService jmxService;

    public Overview overview() {
        OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        ObjectName systemBeanObjectName = operatingSystemMXBean.getObjectName();
        BeanInfo systemBean = jmxService.getObjectNameProperties(systemBeanObjectName.getCanonicalName());
        BeanInfo jvmBean = jmxService.getObjectNameProperties(runtimeMXBean.getObjectName().getCanonicalName());

        Overview overview = new Overview();
        overview.setSystemInfo(buildSystemInfo(systemBean));
        overview.setJvmInfo(buildJvmInfo(jvmBean));
        buildMemoryInfo(overview);
        overview.setMetaSpace(buildMetaspace());
        overview.setThreadInfo(buildThreadInfo());
        overview.setClassLoadingInfo(buildClassInfo());

        try {
            overview.setGarbageCollectorInfo(buildGarbageCollectorInfo());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return overview;

    }

    /**
     * 系统参数
     */
    private SystemInfo buildSystemInfo(BeanInfo systemBean) {
        List<BeanAttributeInfo> attributeInfoList = systemBean.getBeanAttributeInfos();
        SystemInfo systemInfo = new SystemInfo();

        if (CollectionUtils.isEmpty(attributeInfoList))
            System.out.println("SystemInfo attributeInfoList is EMPTY");
        else
            for (BeanAttributeInfo beanAttributeInfo : attributeInfoList) {
                String name = beanAttributeInfo.getName();
                String firstCharLowerCase = name.substring(0, 1).toLowerCase();
                name = name.replaceFirst("[A-Z]", firstCharLowerCase);

                if ("objectName".equals(name))// 忽略这个 会报错
                    continue;

                setValue(systemInfo, name, beanAttributeInfo.getValue().getData());
            }

        return systemInfo;
    }

    /**
     * jvm 参数
     */
    private JvmInfo buildJvmInfo(BeanInfo runtimeBean) {
        List<BeanAttributeInfo> attributeInfoList = runtimeBean.getBeanAttributeInfos();
        JvmInfo jvmInfo = new JvmInfo();

        if (CollectionUtils.isEmpty(attributeInfoList))
            System.out.println("JvmInfo attributeInfoList is EMPTY");
        else
            for (BeanAttributeInfo beanAttributeInfo : attributeInfoList) {
                String name = beanAttributeInfo.getName();
                String firstCharLowerCase = name.substring(0, 1).toLowerCase();
                name = name.replaceFirst("[A-Z]", firstCharLowerCase);

                if ("objectName".equals(name))// 忽略这个 会报错
                    continue;

                if (!name.equals("systemProperties"))
                    setValue(jvmInfo, name, beanAttributeInfo.getValue().getData());
                else {
                    /*
                     * systemProperties 格式特殊 需要单独处理
                     */
                    List<TreeMap<String, Object>> properties = (ArrayList) beanAttributeInfo.getValue().getData();
                    jvmInfo.setSystemProperties(properties);
                }
            }

        return jvmInfo;
    }

    private static final String HEAP_MEMORY = "HeapMemoryUsage";

    private static final String NON_HEAP_MEMORY = "NonHeapMemoryUsage";

    /**
     * jvm 内存信息 堆和非堆
     */
    private void buildMemoryInfo(Overview overview) {
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        BeanInfo beanInfo = jmxService.getObjectNameProperties(memoryMXBean.getObjectName().getCanonicalName());
        List<BeanAttributeInfo> beanAttributeInfoList = beanInfo.getBeanAttributeInfos();

        if (CollectionUtils.isEmpty(beanAttributeInfoList))
            System.out.println("BeanAttributeInfoList is EMPTY");
        else
            for (BeanAttributeInfo beanAttributeInfo : beanAttributeInfoList) {
                if (beanAttributeInfo.getName().equals(HEAP_MEMORY))
                    overview.setHeapMemoryUsage(buildMemoryUsage(beanAttributeInfo));
                else if (beanAttributeInfo.getName().equals(NON_HEAP_MEMORY))
                    overview.setNonHeapMemoryUsage(buildMemoryUsage(beanAttributeInfo));
            }
    }

    private MemoryUsage buildMemoryUsage(BeanAttributeInfo beanAttributeInfo) {
        @SuppressWarnings("unchecked")
        SortedMap<String, Object> map = (TreeMap<String, Object>) beanAttributeInfo.getValue().getData();
        long init = Long.parseLong(map.get("init").toString());
        long used = Long.parseLong(map.get("used").toString());
        long committed = Long.parseLong(map.get("committed").toString());
        long max = Long.parseLong(map.get("max").toString());

        return new MemoryUsage(init, used, committed, max);
    }

    /**
     * Metaspace 元空间信息
     */
    private MetaSpace buildMetaspace() {
        MetaSpace metaSpace = new MetaSpace();
        BeanInfo beanInfo = jmxService.getObjectNameProperties("java.lang:name=Metaspace,type=MemoryPool");
        List<BeanAttributeInfo> beanAttributeInfoList = beanInfo.getBeanAttributeInfos();

        if (CollectionUtils.isEmpty(beanAttributeInfoList))
            System.out.println("MetaSpace AttributeInfoList is EMPTY");
        else
            for (BeanAttributeInfo attributeInfo : beanAttributeInfoList) {
                if (attributeInfo.getName().equals("Usage")) {
                    @SuppressWarnings("unchecked")
                    TreeMap<String, Object> usageMap = (TreeMap<String, Object>) attributeInfo.getValue().getData();
                    metaSpace.setCommitted((long) usageMap.get("committed"));
                    metaSpace.setInit((long) usageMap.get("init"));
                    metaSpace.setMax((long) usageMap.get("max"));
                    metaSpace.setUsed((long) usageMap.get("used"));

                    return metaSpace;
                }
            }

        return null;
    }

    /**
     * 线程信息
     */
    private ThreadInfo buildThreadInfo() {
        ThreadInfo threadInfo = new ThreadInfo();
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        BeanInfo beanInfo = jmxService.getObjectNameProperties(threadMXBean.getObjectName().getCanonicalName());
        List<BeanAttributeInfo> beanAttributeInfoList = beanInfo.getBeanAttributeInfos();

        if (CollectionUtils.isEmpty(beanAttributeInfoList))
            System.out.println("ThreadInfo AttributeInfoList is EMPTY");
        else
            for (BeanAttributeInfo beanAttributeInfo : beanAttributeInfoList) {
                switch (beanAttributeInfo.getName()) {
                    case "ThreadCount":
                        threadInfo.setLiveThreadCount((int) beanAttributeInfo.getValue().getData());
                        break;
                    case "DaemonThreadCount":
                        threadInfo.setDaemonThreadCount((int) beanAttributeInfo.getValue().getData());
                        break;
                    case "TotalStartedThreadCount":
                        threadInfo.setTotalStartedThreadCount((long) beanAttributeInfo.getValue().getData());
                        break;
                    case "PeakThreadCount":
                        threadInfo.setLivePeakThreadCount((int) beanAttributeInfo.getValue().getData());
                        break;
                }
            }

        return threadInfo;
    }

    /**
     * class 信息
     */
    private ClassLoadingInfo buildClassInfo() {
        ClassLoadingInfo classLoadingInfo = new ClassLoadingInfo();
        ClassLoadingMXBean classLoadingMXBean = ManagementFactory.getClassLoadingMXBean();
        BeanInfo beanInfo = jmxService.getObjectNameProperties(classLoadingMXBean.getObjectName().getCanonicalName());
        List<BeanAttributeInfo> beanAttributeInfoList = beanInfo.getBeanAttributeInfos();

        if (CollectionUtils.isEmpty(beanAttributeInfoList))
            System.out.println("ClassLoadingInfo AttributeInfoList is EMPTY");
        else
            for (BeanAttributeInfo beanAttributeInfo : beanAttributeInfoList) {
                switch (beanAttributeInfo.getName()) {
                    case "TotalLoadedClassCount":
                        classLoadingInfo.setTotalLoadedClassCount((long) beanAttributeInfo.getValue().getData());
                        break;
                    case "LoadedClassCount":
                        classLoadingInfo.setLoadedClassCount((int) beanAttributeInfo.getValue().getData());
                        break;
                    case "UnloadedClassCount":
                        classLoadingInfo.setUnloadedClassCount((long) beanAttributeInfo.getValue().getData());
                        break;
                }
            }

        return classLoadingInfo;
    }

    /**
     * 垃圾收集器信息
     */
    private List<GarbageInfo> buildGarbageCollectorInfo() throws Exception {
        List<GarbageInfo> garbageInfoList = new ArrayList<>();
        JmxConnectorInstance commonConfig = JmxConnectorInstance.INSTANCE;
        JMXConnector connector = commonConfig.getJmxConnector();
        MBeanServerConnection msc = connector.getMBeanServerConnection();
        ObjectName queryObjectName = new ObjectName("java.lang:name=*,type=GarbageCollector");
        Set<ObjectName> objectNames = msc.queryNames(queryObjectName, null);

        if (CollectionUtils.isEmpty(objectNames))
            System.out.println("GarbageInfo objectNames is EMPTY");
        else
            for (ObjectName objectName : objectNames) {
                BeanInfo beanInfo = jmxService.getObjectNameProperties(objectName.getCanonicalName());
                List<BeanAttributeInfo> beanAttributeInfoList = beanInfo.getBeanAttributeInfos();
                GarbageInfo garbageInfo = new GarbageInfo();

                for (BeanAttributeInfo beanAttributeInfo : beanAttributeInfoList) {
                    switch (beanAttributeInfo.getName()) {
                        case "Name":
                            garbageInfo.setName(beanAttributeInfo.getValue().getData().toString());
                            break;
                        case "CollectionCount":
                            garbageInfo.setCollectionCount((long) beanAttributeInfo.getValue().getData());
                            break;
                        case "MemoryPoolNames":
                            String[] pools = (String[]) beanAttributeInfo.getValue().getData();
                            garbageInfo.setMemoryPoolNames(pools);
                            break;
                    }
                }

                garbageInfoList.add(garbageInfo);
            }

        return garbageInfoList;
    }

    public static <T> void setValue(T t, String fieldName, Object value) {
        try {
            Field field = t.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(t, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
