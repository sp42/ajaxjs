package com.ajaxjs.developertools.monitor.jvm.model;

import com.ajaxjs.framework.IBaseModel;
import lombok.Data;

/**
 * 类加载信息
 */
@Data
public class ClassLoadingInfo  implements IBaseModel {
    private long totalLoadedClassCount;

    private int loadedClassCount;

    private long unloadedClassCount;

}