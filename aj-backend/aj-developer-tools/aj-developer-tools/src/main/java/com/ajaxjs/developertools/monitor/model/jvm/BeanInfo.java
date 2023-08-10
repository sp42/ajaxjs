package com.ajaxjs.developertools.monitor.model.jvm;

import com.ajaxjs.framework.IBaseModel;
import lombok.Data;

import javax.management.MBeanNotificationInfo;
import javax.management.MBeanOperationInfo;
import java.util.List;

/**
 * Bean 信息
 */
@Data
public class BeanInfo  implements IBaseModel {
    private List<BeanAttributeInfo> beanAttributeInfos;

    private MBeanOperationInfo[] operationInfos;

    private MBeanNotificationInfo[] notificationInfos;
}
