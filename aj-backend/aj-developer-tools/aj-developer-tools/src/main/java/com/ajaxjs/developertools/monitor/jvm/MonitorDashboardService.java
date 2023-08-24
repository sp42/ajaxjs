package com.ajaxjs.developertools.monitor.jvm;

import com.ajaxjs.developertools.monitor.JmxHelper;
import com.ajaxjs.developertools.monitor.jvm.model.*;
import com.ajaxjs.developertools.monitor.jvm.model.MemoryUsage;
import com.ajaxjs.developertools.monitor.jvm.model.ThreadInfo;
import com.ajaxjs.util.reflect.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.ObjectName;
import java.lang.management.*;
import java.util.*;

/**
 * MonitorDashboardService 采集如下几个指标：
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
    private JvmMonitorController jmMonitorController;

    public Overview overview() {
        Overview overview = new Overview();
        overview.setSystemInfo(buildSystemInfo());
        overview.setJvmInfo(buildJvmInfo());
        overview.setMetaSpace(buildMetaspace());
        overview.setThreadInfo(buildThreadInfo());
        overview.setClassLoadingInfo(buildClassInfo());
        overview.setGarbageCollectorInfo(buildGarbageCollectorInfo());
        buildMemoryInfo(overview);

        return overview;
    }

    /**
     * 系统参数
     */
    private SystemInfo buildSystemInfo() {
        SystemInfo info = new SystemInfo();

        for (BeanAttributeInfo beanInfo : getBeanAttributeInfoList(ManagementFactory.getOperatingSystemMXBean())) {
            String name = parseName(beanInfo);

            if ("objectName".equals(name))// 忽略这个 会报错
                continue;

            BeanUtils.setBeanValue(info, name, getData(beanInfo));
        }

        return info;
    }

    /**
     * jvm 参数
     */
    @SuppressWarnings("unchecked")
    private JvmInfo buildJvmInfo() {
        JvmInfo info = new JvmInfo();

        for (BeanAttributeInfo beanInfo : getBeanAttributeInfoList(ManagementFactory.getRuntimeMXBean())) {
            String name = parseName(beanInfo);

            if ("objectName".equals(name))// 忽略这个 会报错
                continue;

            if (!name.equals("systemProperties"))
                BeanUtils.setBeanValue(info, name, getData(beanInfo));
            else
                info.setSystemProperties((List<TreeMap<String, Object>>) getData(beanInfo)); // systemProperties 格式特殊 需要单独处理
        }

        return info;
    }

    /**
     * jvm 内存信息 堆和非堆
     */
    private void buildMemoryInfo(Overview overview) {
        for (BeanAttributeInfo beanInfo : getBeanAttributeInfoList(ManagementFactory.getMemoryMXBean())) {
            if (beanInfo.getName().equals("HeapMemoryUsage"))
                overview.setHeapMemoryUsage(buildMemoryUsage(beanInfo));
            else if (beanInfo.getName().equals("NonHeapMemoryUsage"))
                overview.setNonHeapMemoryUsage(buildMemoryUsage(beanInfo));
        }
    }

    private MemoryUsage buildMemoryUsage(BeanAttributeInfo beanInfo) {
        MemoryUsage info = new MemoryUsage();

        @SuppressWarnings("unchecked")
        Map<String, Object> map = (Map<String, Object>) getData(beanInfo);

        BeanUtils.setBeanValue(info, "init", map.get("init"));
        BeanUtils.setBeanValue(info, "used", map.get("used"));
        BeanUtils.setBeanValue(info, "committed", map.get("committed"));
        BeanUtils.setBeanValue(info, "max", map.get("max"));

        return info;
    }

    /**
     * MetaSpace 元空间信息
     */
    private MemoryUsage buildMetaspace() {
        for (BeanAttributeInfo beanInfo : jmMonitorController.getPropertyList("java.lang:name=Metaspace,type=MemoryPool").getBeanAttributeInfos()) {
            if (beanInfo.getName().equals("Usage"))
                return buildMemoryUsage(beanInfo);
        }

        return null;
    }

    /**
     * 线程信息
     */
    private ThreadInfo buildThreadInfo() {
        ThreadInfo info = new ThreadInfo();

        for (BeanAttributeInfo beanInfo : getBeanAttributeInfoList(ManagementFactory.getThreadMXBean())) {
            switch (beanInfo.getName()) {
                case "ThreadCount":
                    info.setLiveThreadCount(getData(beanInfo, int.class));
                    break;
                case "DaemonThreadCount":
                    info.setDaemonThreadCount(getData(beanInfo, int.class));
                    break;
                case "TotalStartedThreadCount":
                    info.setTotalStartedThreadCount(getData(beanInfo, long.class));
                    break;
                case "PeakThreadCount":
                    info.setLivePeakThreadCount(getData(beanInfo, int.class));
                    break;
            }
        }

        return info;
    }

    /**
     * class 信息
     */
    private ClassLoadingInfo buildClassInfo() {
        ClassLoadingInfo info = new ClassLoadingInfo();

        for (BeanAttributeInfo beanInfo : getBeanAttributeInfoList(ManagementFactory.getClassLoadingMXBean())) {
            String name = beanInfo.getName();

            if (ignore(name))
                continue;

            BeanUtils.setBeanValue(info, toLowerCaseFirstChar(name), getData(beanInfo));
        }

        return info;
    }

    /**
     * 垃圾收集器信息
     */
    private List<GarbageInfo> buildGarbageCollectorInfo() {
        List<GarbageInfo> garbageInfoList = new ArrayList<>();
        Set<ObjectName> objectNames = JmxHelper.queryNames(jmMonitorController.INSTANCE.getMsc(), "java.lang:name=*,type=GarbageCollector");
        assert objectNames != null;

        for (ObjectName objectName : objectNames) {
            GarbageInfo info = new GarbageInfo();

            for (BeanAttributeInfo beanInfo : jmMonitorController.getPropertyList(objectName.getCanonicalName()).getBeanAttributeInfos()) {
                switch (beanInfo.getName()) {
                    case "Name":
                        info.setName(getData(beanInfo, String.class));
                        break;
                    case "CollectionCount":
                        info.setCollectionCount(getData(beanInfo, long.class));
                        break;
                    case "MemoryPoolNames":
                        info.setMemoryPoolNames(getData(beanInfo, String[].class));
                        break;
                }
            }

            garbageInfoList.add(info);
        }

        return garbageInfoList;
    }

    private List<BeanAttributeInfo> getBeanAttributeInfoList(PlatformManagedObject obj) {
        BeanInfo beanInfo = jmMonitorController.getPropertyList(obj.getObjectName().getCanonicalName());

        return beanInfo.getBeanAttributeInfos();
    }

    private static boolean ignore(String str) {
        return "Verbose".equals(str) || "ObjectName".equals(str);
    }

    private static String toLowerCaseFirstChar(String input) {
        return Character.toLowerCase(input.charAt(0)) + input.substring(1);
    }

    private static Object getData(BeanAttributeInfo info) {
        return info.getValue().getData();
    }

    @SuppressWarnings("unchecked")
    private static <T> T getData(BeanAttributeInfo info, Class<T> clz) {
        return (T) getData(info);
    }

    private static String parseName(BeanAttributeInfo info) {
        String name = info.getName(), firstCharLowerCase = name.substring(0, 1).toLowerCase();

        return name.replaceFirst("[A-Z]", firstCharLowerCase);
    }

}
