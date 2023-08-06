/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.ajaxjs.developertools.monitor;

import com.ajaxjs.developertools.monitor.model.system.*;
import com.ajaxjs.util.ObjectHelper;
import org.springframework.util.CollectionUtils;
import oshi.SystemInfo;
import oshi.hardware.*;
import oshi.software.os.*;
import oshi.util.FormatUtil;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.util.*;

/**
 * 基于 OShi 服务器信息收集监控
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 <a href="http://aizuda.com">...</a> 出品
 *
 * @author 青苗
 * @since 2022-03-02
 */
public class OshiSystemMonitor {
    public SystemInfo systemInfo = new SystemInfo();

    /**
     * 获取硬件抽象层信息
     *
     * @return {@link HardwareAbstractionLayer}
     */
    public HardwareAbstractionLayer getHardwareAbstractionLayer() {
        return systemInfo.getHardware();
    }

    /**
     * 操作系统
     *
     * @return {@link OperatingSystem}
     */
    public OperatingSystem getOperatingSystem() {
        return systemInfo.getOperatingSystem();
    }

    /**
     * 获取系统信息
     *
     * @return {@link SysInfo}
     */
    public SysInfo getSysInfo() {
        Properties props = System.getProperties();
        SysInfo sysInfo = new SysInfo();
        InetAddress inetAddress;

        try {
            inetAddress = InetAddress.getLocalHost();
            sysInfo.setName(inetAddress.getHostName());
            sysInfo.setIp(inetAddress.getHostAddress());
        } catch (UnknownHostException e) {
            sysInfo.setName("unknown");
            sysInfo.setIp("unknown");
        }

        sysInfo.setOsName(props.getProperty("os.name"));
        sysInfo.setOsArch(props.getProperty("os.arch"));
        sysInfo.setUserDir(props.getProperty("user.dir"));

        return sysInfo;
    }

    /**
     * 获取 cpu 信息
     *
     * @return {@link CpuInfo}
     */
    public CpuInfo getCpuInfo() {
        CentralProcessor centralProcessor = getHardwareAbstractionLayer().getProcessor();
        long[] prevTicks = centralProcessor.getSystemCpuLoadTicks();
        ObjectHelper.sleep(1);

        long[] ticks = centralProcessor.getSystemCpuLoadTicks();
        long nice = ticks[CentralProcessor.TickType.NICE.getIndex()] - prevTicks[CentralProcessor.TickType.NICE.getIndex()];
        long irq = ticks[CentralProcessor.TickType.IRQ.getIndex()] - prevTicks[CentralProcessor.TickType.IRQ.getIndex()];
        long softIRQ = ticks[CentralProcessor.TickType.SOFTIRQ.getIndex()] - prevTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()];
        long steal = ticks[CentralProcessor.TickType.STEAL.getIndex()] - prevTicks[CentralProcessor.TickType.STEAL.getIndex()];
        long sys = ticks[CentralProcessor.TickType.SYSTEM.getIndex()] - prevTicks[CentralProcessor.TickType.SYSTEM.getIndex()];
        long user = ticks[CentralProcessor.TickType.USER.getIndex()] - prevTicks[CentralProcessor.TickType.USER.getIndex()];
        long ioWait = ticks[CentralProcessor.TickType.IOWAIT.getIndex()] - prevTicks[CentralProcessor.TickType.IOWAIT.getIndex()];
        long idle = ticks[CentralProcessor.TickType.IDLE.getIndex()] - prevTicks[CentralProcessor.TickType.IDLE.getIndex()];
        long totalCpu = user + nice + sys + idle + ioWait + irq + softIRQ + steal;

        CpuInfo cpuInfo = new CpuInfo();
        cpuInfo.setPhysicalProcessorCount(centralProcessor.getPhysicalProcessorCount());
        cpuInfo.setLogicalProcessorCount(centralProcessor.getLogicalProcessorCount());
        cpuInfo.setSystemPercent(formatDouble(sys * 1.0 / totalCpu));
        cpuInfo.setUserPercent(formatDouble(user * 1.0 / totalCpu));
        cpuInfo.setWaitPercent(formatDouble(ioWait * 1.0 / totalCpu));
        cpuInfo.setUsePercent(formatDouble(1.0 - (idle * 1.0 / totalCpu)));

        return cpuInfo;
    }

    /**
     * 获取内存使用信息
     *
     * @return {@link MemoryInfo}
     */
    public MemoryInfo getMemoryInfo() {
        GlobalMemory globalMemory = getHardwareAbstractionLayer().getMemory();
        long totalByte = globalMemory.getTotal();
        long availableByte = globalMemory.getAvailable();
        MemoryInfo memoryInfo = new MemoryInfo();
        memoryInfo.setTotal(formatByte(totalByte));
        memoryInfo.setUsed(formatByte(totalByte - availableByte));
        memoryInfo.setFree(formatByte(availableByte));
        memoryInfo.setUsePercent(formatDouble((totalByte - availableByte) * 1.0 / totalByte));

        return memoryInfo;
    }

    /**
     * 获取 JVM 信息
     *
     * @return {@link JvmInfo}
     */
    public JvmInfo getJvmInfo() {
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
     * 获取磁盘使用信息
     */
    public List<DiskInfo> getDiskInfoList() {
        OperatingSystem operatingSystem = getOperatingSystem();
        FileSystem fileSystem = operatingSystem.getFileSystem();
        List<DiskInfo> diskInfoList = new ArrayList<>();
        Iterable<OSFileStore> fsArray = fileSystem.getFileStores();

        for (OSFileStore fs : fsArray) {
            DiskInfo diskInfo = new DiskInfo();
            diskInfo.setName(fs.getName());
            diskInfo.setVolume(fs.getVolume());
            diskInfo.setLabel(fs.getLabel());
            diskInfo.setLogicalVolume(fs.getLogicalVolume());
            diskInfo.setMount(fs.getMount());
            diskInfo.setDescription(fs.getDescription());
            diskInfo.setOptions(fs.getOptions());
            diskInfo.setType(fs.getType());
            diskInfo.setUUID(fs.getUUID());
            long usable = fs.getUsableSpace();
            diskInfo.setUsableSpace(usable);
            long total = fs.getTotalSpace();
            diskInfo.setSize(formatByte(total));
            diskInfo.setTotalSpace(total);
            diskInfo.setAvail(formatByte(usable));
            diskInfo.setUsed(formatByte(total - usable));
            double usedSize = (total - usable);
            double usePercent = 0;

            if (total > 0) usePercent = formatDouble(usedSize / total * 100);

            diskInfo.setUsePercent(usePercent);
            diskInfoList.add(diskInfo);
        }

        return diskInfoList;
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

    public static Map<String, Object> get() {
        OshiSystemMonitor oshiMonitor = new OshiSystemMonitor();
        Map<String, Object> server = new HashMap<>(5);
        server.put("sysInfo", oshiMonitor.getSysInfo());// 系统信息
        server.put("cupInfo", oshiMonitor.getCpuInfo());// CPU 信息
        server.put("memoryInfo", oshiMonitor.getMemoryInfo()); // 内存信息
        server.put("jvmInfo", oshiMonitor.getJvmInfo()); // Jvm 虚拟机信息
        List<DiskInfo> diskInfoList = oshiMonitor.getDiskInfoList();// 磁盘信息
        server.put("diskInfo", diskInfoList);

        if (!CollectionUtils.isEmpty(diskInfoList)) {
            long usableSpace = 0, totalSpace = 0;

            for (DiskInfo diskInfo : diskInfoList) {
                usableSpace += diskInfo.getUsableSpace();
                totalSpace += diskInfo.getTotalSpace();
            }

            double usedSize = (totalSpace - usableSpace);
            server.put("diskUsePercent", formatDouble(usedSize / totalSpace * 100));// 统计所有磁盘的使用率
        }

//		server.put("processList", processMapList);

        return server;
    }

    /**
     * 获取网络带宽信息
     *
     * @return {@link NetIoInfo}
     */
    public NetIoInfo getNetIoInfo() {
        long rxBytesBegin = 0;
        long txBytesBegin = 0;
        long rxPacketsBegin = 0;
        long txPacketsBegin = 0;
        long rxBytesEnd = 0;
        long txBytesEnd = 0;
        long rxPacketsEnd = 0;
        long txPacketsEnd = 0;
        HardwareAbstractionLayer hal = getHardwareAbstractionLayer();
        List<NetworkIF> listBegin = hal.getNetworkIFs();

        for (NetworkIF net : listBegin) {
            rxBytesBegin += net.getBytesRecv();
            txBytesBegin += net.getBytesSent();
            rxPacketsBegin += net.getPacketsRecv();
            txPacketsBegin += net.getPacketsSent();
        }

        // 暂停3秒
        ObjectHelper.sleep(3);

        List<NetworkIF> listEnd = hal.getNetworkIFs();
        for (NetworkIF net : listEnd) {
            rxBytesEnd += net.getBytesRecv();
            txBytesEnd += net.getBytesSent();
            rxPacketsEnd += net.getPacketsRecv();
            txPacketsEnd += net.getPacketsSent();
        }

        long rxBytesAvg = (rxBytesEnd - rxBytesBegin) / 3 / 1024;
        long txBytesAvg = (txBytesEnd - txBytesBegin) / 3 / 1024;
        long rxPacketsAvg = (rxPacketsEnd - rxPacketsBegin) / 3 / 1024;
        long txPacketsAvg = (txPacketsEnd - txPacketsBegin) / 3 / 1024;

        NetIoInfo netIoInfo = new NetIoInfo();
        netIoInfo.setRxbyt(String.valueOf(rxBytesAvg));
        netIoInfo.setTxbyt(String.valueOf(txBytesAvg));
        netIoInfo.setRxpck(String.valueOf(rxPacketsAvg));
        netIoInfo.setTxpck(String.valueOf(txPacketsAvg));

        return netIoInfo;
    }

    /**
     * 获取操作系统信息 <code>
     * System.out.println("manufacturer: " + computerSystem.getManufacturer());
     * System.out.println("model: " + computerSystem.getModel());
     * System.out.println("serialnumber: " + computerSystem.getSerialNumber());
     * final Firmware firmware = computerSystem.getFirmware();
     * System.out.println("firmware:");
     * System.out.println("  manufacturer: " + firmware.getManufacturer());
     * System.out.println("  name: " + firmware.getName());
     * System.out.println("  description: " + firmware.getDescription());
     * System.out.println("  version: " + firmware.getVersion());
     * final Baseboard baseboard = computerSystem.getBaseboard();
     * System.out.println("baseboard:");
     * System.out.println("  manufacturer: " + baseboard.getManufacturer());
     * System.out.println("  model: " + baseboard.getModel());
     * System.out.println("  version: " + baseboard.getVersion());
     * System.out.println("  serialnumber: " + baseboard.getSerialNumber());
     * </code>
     */
    public Map<String, Object> getComputerSystem() {
        ComputerSystem computerSystem = getHardwareAbstractionLayer().getComputerSystem();// 获取硬件抽象层信息

        final Firmware firmware = computerSystem.getFirmware();
        final Baseboard baseboard = computerSystem.getBaseboard();

        return new HashMap<String, Object>() {
            private static final long serialVersionUID = 1L;

            {
                put("manufacturer", computerSystem.getManufacturer());
                put("model", computerSystem.getModel());
                put("serialNumber", computerSystem.getSerialNumber());
                put("firmware-manufacturer", firmware.getManufacturer());
                put("firmware-name", firmware.getName());
                put("firmware-description", firmware.getDescription());
                put("firmware-version", firmware.getVersion());
                put("baseboard-manufacturer", baseboard.getManufacturer());
                put("baseboard-model", baseboard.getModel());
                put("baseboard-version", baseboard.getVersion());
                put("baseboard-serialNumber", baseboard.getSerialNumber());
            }
        };
    }

    /**
     * 不能获取 cpu 温度，没用……
     */
    public void getOtherHardware() {
        HardwareAbstractionLayer hardware = getHardwareAbstractionLayer();
        printNetworkInterfaces(hardware.getNetworkIFs());

        OperatingSystem operatingSystem = getOperatingSystem();
        printNetworkParameters(operatingSystem.getNetworkParams());
        printUsbDevices(hardware.getUsbDevices(true));
        printSensors(hardware.getSensors());
        printPowerSources(hardware.getPowerSources());
        printDisplay(hardware.getDisplays());
    }

    public static void printUsbDevices(List<UsbDevice> usbDeviceList) {
        usbDeviceList.forEach((item) -> System.out.println("Usb驱动：" + item.toString()));
    }

    private static void printNetworkInterfaces(List<NetworkIF> networkIFs) {
        networkIFs.forEach((item) -> System.out.printf("网络设置，名称: %s ，MAC: %s ，MTU: %s ，网速bps: %s ，IPv4: %s ，IPv6: %s%n", item.getName(), item.getMacaddr(), item.getMTU(), FormatUtil.formatValue(item.getSpeed(), "bps"), Arrays.toString(item.getIPv4addr()), Arrays.toString(item.getIPv6addr())));
    }

    private static void printNetworkParameters(NetworkParams networkParams) {
        System.out.printf("网络，Host: %s ，Domain: %s ，DNS: %s ，IPv4: %s ，IPv6: %s%n", networkParams.getHostName(), networkParams.getDomainName(), Arrays.toString(networkParams.getDnsServers()), networkParams.getIpv4DefaultGateway(), networkParams.getIpv6DefaultGateway());
    }

    private static void printSensors(Sensors sensors) {
        System.out.printf("传感器，CPU温度: %s ，风扇: %s ，CPU电压: %s%n", sensors.getCpuTemperature(), Arrays.toString(sensors.getFanSpeeds()), sensors.getCpuVoltage());
    }

    private static void printPowerSources(List<PowerSource> powerSources) {
        powerSources.forEach(item -> System.out.printf("电源，电源名称: %s ，剩余百分比: %s%n", item.getName(), item.getRemainingCapacityPercent() * 100));
    }

    private static void printDisplay(List<Display> displays) {
        displays.forEach(item -> System.out.printf("显示，显示: %s%n", item.toString()));
    }

    /**
     * 系统前 10 个进程 **** 好慢而且返回为空 ****
     */
    public List<OSProcess> getProcessList() {
        List<OSProcess> processList = getOperatingSystem().getProcesses(null, OperatingSystem.ProcessSorting.CPU_DESC, 10);
        List<Map<String, Object>> processMapList = new ArrayList<>();

        for (OSProcess process : processList) {
            Map<String, Object> processMap = new HashMap<>(5);
            processMap.put("name", process.getName());
            processMap.put("pid", process.getProcessID());
            processMap.put("cpu", formatDouble(process.getProcessCpuLoadCumulative()));
            processMapList.add(processMap);
        }

        return processList;
    }

    public List<String> getProcessList2() {
        // 系统前 10 个进程
        List<OSProcess> processList = systemInfo.getOperatingSystem().getProcesses(null, OperatingSystem.ProcessSorting.CPU_DESC, 10);
        List<String> strList = new ArrayList<>();

        for (OSProcess process : processList) {
            // 进程名，进程ID，进程CPU使用率
            strList.add(String.format("name:%s PID: %d CPU:%.3f", process.getName(), process.getProcessID(), process.getProcessCpuLoadCumulative()));
        }

        return strList;
    }
}
