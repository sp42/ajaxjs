package com.ajaxjs.developertools.monitor.model.jvm;

import com.ajaxjs.framework.IBaseModel;
import lombok.Data;

/**
 * 线程信息
 */
@Data
public class ThreadInfo implements IBaseModel {
    private int liveThreadCount;

    private int livePeakThreadCount;

    private int daemonThreadCount;

    private long totalStartedThreadCount;
}
