package com.ajaxjs.developertools.monitor.model.jvm;

import com.ajaxjs.framework.IBaseModel;
import lombok.Data;

import java.lang.management.MemoryUsage;
import java.util.List;

/**
 * Overview
 */
@Data
public class Overview implements IBaseModel {
    private SystemInfo systemInfo;

    private JvmInfo jvmInfo;

    private MemoryUsage heapMemoryUsage;

    private MemoryUsage nonHeapMemoryUsage;

    private MetaSpace metaSpace;

    private ThreadInfo threadInfo;

    private ClassLoadingInfo classLoadingInfo;

    private List<GarbageInfo> garbageCollectorInfo;
}
