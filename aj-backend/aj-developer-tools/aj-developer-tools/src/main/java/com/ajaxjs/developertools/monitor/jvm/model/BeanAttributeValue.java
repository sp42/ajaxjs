package com.ajaxjs.developertools.monitor.jvm.model;

import com.ajaxjs.framework.IBaseModel;
import lombok.Data;

/**
 * BeanAttributeValue
 */
@Data
public class BeanAttributeValue implements IBaseModel {
    private boolean isCompositeData;

    private Object data;
}