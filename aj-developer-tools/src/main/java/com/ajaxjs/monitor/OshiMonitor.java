/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.ajaxjs.monitor;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.util.CollectionUtils;

import com.ajaxjs.monitor.model.CpuInfo;
import com.ajaxjs.monitor.model.DiskInfo;
import com.ajaxjs.monitor.model.JvmInfo;
import com.ajaxjs.monitor.model.MemoryInfo;
import com.ajaxjs.monitor.model.SysInfo;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;

/**
 * 基于 OShi 服务器信息收集监控
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author 青苗
 * @since 2022-03-02
 */
public class OshiMonitor {
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
		InetAddress inetAddress = null;

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
		sleep(600);

		long[] ticks = centralProcessor.getSystemCpuLoadTicks();
		long nice = ticks[CentralProcessor.TickType.NICE.getIndex()] - prevTicks[CentralProcessor.TickType.NICE.getIndex()];
		long irq = ticks[CentralProcessor.TickType.IRQ.getIndex()] - prevTicks[CentralProcessor.TickType.IRQ.getIndex()];
		long softirq = ticks[CentralProcessor.TickType.SOFTIRQ.getIndex()] - prevTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()];
		long steal = ticks[CentralProcessor.TickType.STEAL.getIndex()] - prevTicks[CentralProcessor.TickType.STEAL.getIndex()];
		long sys = ticks[CentralProcessor.TickType.SYSTEM.getIndex()] - prevTicks[CentralProcessor.TickType.SYSTEM.getIndex()];
		long user = ticks[CentralProcessor.TickType.USER.getIndex()] - prevTicks[CentralProcessor.TickType.USER.getIndex()];
		long ioWait = ticks[CentralProcessor.TickType.IOWAIT.getIndex()] - prevTicks[CentralProcessor.TickType.IOWAIT.getIndex()];
		long idle = ticks[CentralProcessor.TickType.IDLE.getIndex()] - prevTicks[CentralProcessor.TickType.IDLE.getIndex()];
		long totalCpu = user + nice + sys + idle + ioWait + irq + softirq + steal;

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
		long jvmTotalMemoryByte = runtime.totalMemory();
		long freeMemoryByte = runtime.freeMemory();

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
	public List<DiskInfo> getDiskInfos() {
		OperatingSystem operatingSystem = getOperatingSystem();
		FileSystem fileSystem = operatingSystem.getFileSystem();
		List<DiskInfo> diskInfos = new ArrayList<>();
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

			if (total > 0)
				usePercent = formatDouble(usedSize / total * 100);

			diskInfo.setUsePercent(usePercent);
			diskInfos.add(diskInfo);
		}

		return diskInfos;
	}

	/**
	 * 换算单位
	 * 
	 * @param byteNumber
	 * @return
	 */
	public static String formatByte(long byteNumber) {
		double FORMAT = 1024.0;
		double kbNumber = byteNumber / FORMAT;
		
		if (kbNumber < FORMAT)
			return decimalFormat("#.##KB", kbNumber);

		double mbNumber = kbNumber / FORMAT;
		if (mbNumber < FORMAT)
			return decimalFormat("#.##MB", mbNumber);

		double gbNumber = mbNumber / FORMAT;
		if (gbNumber < FORMAT)
			return decimalFormat("#.##GB", gbNumber);

		return decimalFormat("#.##TB", gbNumber / FORMAT);
	}

	public static String decimalFormat(String pattern, double number) {
		return new DecimalFormat(pattern).format(number);
	}

	public static double formatDouble(double str) {
		return new BigDecimal(str).setScale(2, RoundingMode.HALF_UP).doubleValue();
	}

	public static void sleep(int time) {
		try {
			Thread.sleep(time);// 当前线程休眠500毫秒
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static Map<String, Object> get() {
		OshiMonitor oshiMonitor = new OshiMonitor();

		Map<String, Object> server = new HashMap<>(5);
		server.put("sysInfo", oshiMonitor.getSysInfo());// 系统信息
		server.put("cupInfo", oshiMonitor.getCpuInfo());// CPU 信息
		server.put("memoryInfo", oshiMonitor.getMemoryInfo()); // 内存信息
		server.put("jvmInfo", oshiMonitor.getJvmInfo()); // Jvm 虚拟机信息
		List<DiskInfo> diskInfos = oshiMonitor.getDiskInfos();// 磁盘信息
		server.put("diskInfos", diskInfos);

		if (!CollectionUtils.isEmpty(diskInfos)) {
			long usableSpace = 0, totalSpace = 0;

			for (DiskInfo diskInfo : diskInfos) {
				usableSpace += diskInfo.getUsableSpace();
				totalSpace += diskInfo.getTotalSpace();
			}

			double usedSize = (totalSpace - usableSpace);
			server.put("diskUsePercent", formatDouble(usedSize / totalSpace * 100));// 统计所有磁盘的使用率
		}

//
//		server.put("processList", processMapList);

		return server;
	}
}
