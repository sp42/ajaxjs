package com.ajaxjs.developertools.monitor.jvm.model;

import com.ajaxjs.framework.IBaseModel;
import lombok.Data;

/**
 * Unit: Byte
 */
@Data
public class MemoryUsage implements IBaseModel {
    private long init;
    private long used;
    private long committed;
    private long max;
}
