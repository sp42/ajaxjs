package com.ajaxjs.developertools.monitor.model.jvm;

import com.ajaxjs.framework.IBaseModel;
//import com.ajaxjs.framework.spring.validator.model.NotBank;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.TreeMap;

/**
 * Jvm 信息
 */
@Data
public class JvmInfo implements IBaseModel {

    private String name;

    @NotNull
    private String classPath;

    private Long startTime;

    private String specName;

    private String specVendor;

    private String specVersion;

    private String managementSpecVersion;

    private String[] inputArguments;

    /**
     * 67 个系统参数
     */
    private List<TreeMap<String, Object>> systemProperties;

    private String vmName;

    private String vmVendor;

    private String vmVersion;

    private String libraryPath;

    private Boolean bootClassPathSupported;

    private String bootClassPath;

    private Long uptime;
}
