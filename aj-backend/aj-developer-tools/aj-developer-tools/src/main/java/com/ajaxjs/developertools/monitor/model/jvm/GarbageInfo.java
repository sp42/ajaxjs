package com.ajaxjs.developertools.monitor.model.jvm;

import com.ajaxjs.framework.IBaseModel;
import lombok.Data;

/**
 * 垃圾回收信息
 */
@Data
public class GarbageInfo  implements IBaseModel {
    private String name;

    private long collectionCount;

    private String[] memoryPoolNames;
}
