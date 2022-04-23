package com.ajaxjs.monitor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ajaxjs.monitor.model.NetIoInfo;

import oshi.hardware.Baseboard;
import oshi.hardware.ComputerSystem;
import oshi.hardware.Display;
import oshi.hardware.Firmware;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;
import oshi.hardware.PowerSource;
import oshi.hardware.Sensors;
import oshi.hardware.UsbDevice;
import oshi.software.os.NetworkParams;
import oshi.software.os.OSProcess;
import oshi.software.os.OperatingSystem;
import oshi.util.FormatUtil;

public class OshiMonitorExtra extends OshiMonitor {

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
		OshiMonitor.sleep(3000);

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
		netIoInfo.setRxbyt(rxBytesAvg + "");
		netIoInfo.setTxbyt(txBytesAvg + "");
		netIoInfo.setRxpck(rxPacketsAvg + "");
		netIoInfo.setTxpck(txPacketsAvg + "");

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
	 *
	 * @return
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
		networkIFs.forEach((item) -> System.out.println(String.format("网络设置，名称: %s ，MAC: %s ，MTU: %s ，网速bps: %s ，IPv4: %s ，IPv6: %s", item.getName(), item.getMacaddr(),
				item.getMTU(), FormatUtil.formatValue(item.getSpeed(), "bps"), Arrays.toString(item.getIPv4addr()), Arrays.toString(item.getIPv6addr()))));
	}

	private static void printNetworkParameters(NetworkParams networkParams) {
		System.out.println(String.format("网络，Host: %s ，Domain: %s ，DNS: %s ，IPv4: %s ，IPv6: %s", networkParams.getHostName(), networkParams.getDomainName(),
				Arrays.toString(networkParams.getDnsServers()), networkParams.getIpv4DefaultGateway(), networkParams.getIpv6DefaultGateway()));
	}

	private static void printSensors(Sensors sensors) {
		System.out.println(String.format("传感器，CPU温度: %s ，风扇: %s ，CPU电压: %s", sensors.getCpuTemperature(), Arrays.toString(sensors.getFanSpeeds()), sensors.getCpuVoltage()));
	}

	private static void printPowerSources(List<PowerSource> powerSources) {
		powerSources.stream().forEach(item -> System.out.println(String.format("电源，电源名称: %s ，剩余百分比: %s", item.getName(), item.getRemainingCapacityPercent() * 100)));
	}

	private static void printDisplay(List<Display> displays) {
		displays.stream().forEach(item -> System.out.println(String.format("显示，显示: %s", item.toString())));
	}

	/**
	 * 系统前 10 个进程 **** 好慢而且返回为空 ****
	 * 
	 * @return
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
