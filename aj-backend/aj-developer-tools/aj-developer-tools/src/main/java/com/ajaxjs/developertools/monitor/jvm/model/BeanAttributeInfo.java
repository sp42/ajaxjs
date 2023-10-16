package com.ajaxjs.developertools.monitor.jvm.model;

import com.ajaxjs.framework.IBaseModel;
import lombok.Data;

import javax.management.MBeanAttributeInfo;

/**
 * BeanAttributeInfo
 */
@Data
public class BeanAttributeInfo implements IBaseModel {
    private String name;

    private BeanAttributeValue value;

    private MBeanAttributeInfo attributeInfo;
}