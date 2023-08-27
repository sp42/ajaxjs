package com.ajaxjs.developertools.api;

import com.ajaxjs.developertools.monitor.oshi.BaseInfo;
import com.ajaxjs.developertools.monitor.oshi.OshiSystemMonitor;
import com.ajaxjs.developertools.monitor.oshi.model.DiskInfo;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统信息
 */
@RestController
@RequestMapping("/system_info")
public class SystemInfoController {
    @GetMapping
    public Map<String, Object> get() {
        OshiSystemMonitor oshiMonitor = new OshiSystemMonitor();
        Map<String, Object> server = new HashMap<>();
        server.put("sysInfo", BaseInfo.getSysInfo());// 系统信息
        server.put("cupInfo", oshiMonitor.getCpuInfo());// CPU 信息
        server.put("memoryInfo", oshiMonitor.getMemoryInfo()); // 内存信息
        server.put("jvmInfo", BaseInfo.getJvmInfo()); // Jvm 虚拟机信息
        List<DiskInfo> diskInfoList = oshiMonitor.getDiskInfoList();// 磁盘信息
        server.put("diskInfo", diskInfoList);

        if (!CollectionUtils.isEmpty(diskInfoList)) {
            long usableSpace = 0, totalSpace = 0;

            for (DiskInfo diskInfo : diskInfoList) {
                usableSpace += diskInfo.getUsableSpace();
                totalSpace += diskInfo.getTotalSpace();
            }

            double usedSize = (totalSpace - usableSpace);
            server.put("diskUsePercent", BaseInfo.formatDouble(usedSize / totalSpace * 100));// 统计所有磁盘的使用率
        }

//		server.put("processList", processMapList);

        return server;
    }

    @GetMapping("/live/{which}")
    public Object cpu(@PathVariable String which) {
        OshiSystemMonitor oshiMonitor = new OshiSystemMonitor();

        switch (which) {
            case "cpu":
                return oshiMonitor.getCpuInfo();
            case "memory":
                return oshiMonitor.getMemoryInfo();
        }

        return null;
    }
}
