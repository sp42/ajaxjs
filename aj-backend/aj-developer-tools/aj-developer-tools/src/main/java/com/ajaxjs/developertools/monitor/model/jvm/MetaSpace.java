package com.ajaxjs.developertools.monitor.model.jvm;

import com.ajaxjs.framework.IBaseModel;
import lombok.Data;

/**
 * MetaSpace
 */
@Data
public class MetaSpace implements IBaseModel {
    private String unit = "Byte";

    private long committed;

    private long init;

    private long max;

    private long used;
}
