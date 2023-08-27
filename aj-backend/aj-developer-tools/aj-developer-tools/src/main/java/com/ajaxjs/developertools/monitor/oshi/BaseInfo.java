package com.ajaxjs.developertools.monitor.oshi;

import com.ajaxjs.developertools.monitor.oshi.model.JvmInfo;
import com.ajaxjs.developertools.monitor.oshi.model.SysInfo;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.util.Properties;

/**
 * 获取基础信息，比较简单的获取
 */
public class BaseInfo {
    /**
     * 获取系统信息
     */
    public static SysInfo getSysInfo() {
        SysInfo sysInfo = new SysInfo();

        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            sysInfo.setName(inetAddress.getHostName());
            sysInfo.setIp(inetAddress.getHostAddress());
        } catch (UnknownHostException e) {
            sysInfo.setName("unknown");
            sysInfo.setIp("unknown");
        }

        Properties props = System.getProperties();
        sysInfo.setOsName(props.getProperty("os.name"));
        sysInfo.setOsArch(props.getProperty("os.arch"));
        sysInfo.setUserDir(props.getProperty("user.dir"));

        return sysInfo;
    }

    /**
     * 获取 JVM 信息
     *
     * @return {@link JvmInfo}
     */
    public static JvmInfo getJvmInfo() {
        Properties props = System.getProperties();
        Runtime runtime = Runtime.getRuntime();
        long jvmTotalMemoryByte = runtime.totalMemory(), freeMemoryByte = runtime.freeMemory();

        JvmInfo jvmInfo = new JvmInfo();
        jvmInfo.setJdkVersion(props.getProperty("java.version"));
        jvmInfo.setJdkHome(props.getProperty("java.home"));
        jvmInfo.setVendor(props.getProperty("java.vm.vendor"));
        jvmInfo.setArch(props.getProperty("os.arch"));
        jvmInfo.setJvmTotalMemory(formatByte(jvmTotalMemoryByte));
        jvmInfo.setMaxMemory(formatByte(runtime.maxMemory()));
        jvmInfo.setUsedMemory(formatByte(jvmTotalMemoryByte - freeMemoryByte));
        jvmInfo.setFreeMemory(formatByte(freeMemoryByte));
        jvmInfo.setUsePercent(formatDouble((jvmTotalMemoryByte - freeMemoryByte) * 1.0 / jvmTotalMemoryByte));

        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        jvmInfo.setJdkName(runtimeMXBean.getVmName());
        jvmInfo.setStartTime(runtimeMXBean.getStartTime());
        jvmInfo.setUptime(runtimeMXBean.getUptime());

        return jvmInfo;
    }

    /**
     * 换算单位
     */
    public static String formatByte(long byteNumber) {
        double FORMAT = 1024.0, kbNumber = byteNumber / FORMAT;
        if (kbNumber < FORMAT) return decimalFormat("#.##KB", kbNumber);
        double mbNumber = kbNumber / FORMAT;
        if (mbNumber < FORMAT) return decimalFormat("#.##MB", mbNumber);
        double gbNumber = mbNumber / FORMAT;
        if (gbNumber < FORMAT) return decimalFormat("#.##GB", gbNumber);

        return decimalFormat("#.##TB", gbNumber / FORMAT);
    }

    public static String decimalFormat(String pattern, double number) {
        return new DecimalFormat(pattern).format(number);
    }

    public static double formatDouble(double str) {
        return new BigDecimal(str).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}
