package com.ajaxjs.developertools.monitor.jvm;

import com.ajaxjs.developertools.monitor.JmxUtils;
import com.ajaxjs.developertools.monitor.model.jvm.*;
import lombok.Data;
import org.springframework.stereotype.Service;

import javax.management.*;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.openmbean.TabularDataSupport;
import javax.management.remote.JMXConnector;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

/**
 * JmxService
 */
@Service
@Data
public class JmxService {
    /**
     * 获取所有 domain
     */
    public List<Node> getDomains() {
        List<Node> nodes = new ArrayList<>();
        JMXConnector connector = JmxConnectorInstance.INSTANCE.getJmxConnector();

        if (connector != null) {
            try {
                MBeanServerConnection msc = connector.getMBeanServerConnection();
                String[] domains = msc.getDomains();

                for (String domain : domains) {
                    Node node = new Node();
                    node.setKey(domain);
                    node.setTitle(domain);
                    node.setNodeType(NodeType.DOMAIN.getName());
                    List<Node> firstLevelObjectNames = JmxUtils.getObjectNamesByDomain(msc, domain);
                    node.setChildren(firstLevelObjectNames);
                    nodes.add(node);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return nodes;
    }

    public BeanInfo getObjectNameProperties(String fullName) {
        JMXConnector connector = JmxConnectorInstance.INSTANCE.getJmxConnector();
        BeanInfo beanInfo = new BeanInfo();
        List<BeanAttributeInfo> beanAttributeInfoList = new ArrayList<>();

        try {
            MBeanServerConnection msc = connector.getMBeanServerConnection();
            ObjectName queryObjectName = new ObjectName(fullName);
            Set<ObjectName> objectNames = msc.queryNames(queryObjectName, null);

            if (objectNames != null) {
                for (ObjectName o : objectNames) {
                    MBeanInfo mBeanInfo = msc.getMBeanInfo(o);
                    MBeanAttributeInfo[] mBeanAttributeInfoList = mBeanInfo.getAttributes();
                    MBeanOperationInfo[] mBeanOperationInfoList = mBeanInfo.getOperations();
                    MBeanNotificationInfo[] mBeanNotificationInfoList = mBeanInfo.getNotifications();

                    for (MBeanAttributeInfo mBeanAttributeInfo : mBeanAttributeInfoList) {
                        boolean isReadable = mBeanAttributeInfo.isReadable();
                        Object attribute;

                        if (isReadable) {
                            try {
                                attribute = msc.getAttribute(o, mBeanAttributeInfo.getName());
                            } catch (Exception e) {
                                attribute = "不可用";
                            }
                        } else attribute = "不可读";

                        BeanAttributeInfo beanAttributeInfo = new BeanAttributeInfo();
                        beanAttributeInfo.setName(mBeanAttributeInfo.getName());
                        beanAttributeInfo.setAttributeInfo(mBeanAttributeInfo);
                        BeanAttributeValue beanAttributeValue = new BeanAttributeValue();

                        if (mBeanAttributeInfo.getType().equals("javax.management.openmbean.CompositeData") && mBeanAttributeInfo.isReadable()) {
                            SortedMap<String, Object> map = analyzeCompositeData(attribute);
                            beanAttributeValue.setCompositeData(true);
                            beanAttributeValue.setData(map);
                        } else if (mBeanAttributeInfo.getType().equals("[Ljavax.management.openmbean.CompositeData;") && mBeanAttributeInfo.isReadable()) {
                            List<SortedMap<String, Object>> mapList = new ArrayList<>();
                            CompositeData[] compositeDataArray = (CompositeData[]) attribute;

                            for (CompositeData compositeData : compositeDataArray) {
                                SortedMap<String, Object> map = analyzeCompositeData(compositeData);
                                mapList.add(map);
                            }

                            beanAttributeValue.setCompositeData(true);
                            beanAttributeValue.setData(mapList);
                        } else if (mBeanAttributeInfo.getType().equals("javax.management.openmbean.TabularData")) {
                            List<SortedMap<String, Object>> mapList = new ArrayList<>();
                            TabularDataSupport tabularDataSupport = (TabularDataSupport) attribute;

                            for (Map.Entry<Object, Object> entry : tabularDataSupport.entrySet()) {
                                SortedMap<String, Object> map = analyzeCompositeData(entry.getValue());
                                SortedMap<String, Object> normalMap = new TreeMap<>();
                                assert map != null;
                                String key = (String) map.getOrDefault("key", "");
                                Object value = map.getOrDefault("value", "");
                                normalMap.put("name", key);
                                normalMap.put("value", value);
                                mapList.add(normalMap);
                            }

                            beanAttributeValue.setCompositeData(true);
                            beanAttributeValue.setData(mapList);
                        } else {
                            beanAttributeValue.setCompositeData(false);
                            beanAttributeValue.setData(attribute);
                        }

                        beanAttributeInfo.setValue(beanAttributeValue);
                        beanAttributeInfoList.add(beanAttributeInfo);
                    }

                    beanInfo.setOperationInfos(mBeanOperationInfoList);
                    beanInfo.setNotificationInfos(mBeanNotificationInfoList);
                }

                beanInfo.setBeanAttributeInfos(beanAttributeInfoList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return beanInfo;
    }

    @SuppressWarnings("unchecked")
    private static SortedMap<String, Object> analyzeCompositeData(Object compositeData) {
        try {
            if (compositeData instanceof CompositeDataSupport) {
                CompositeDataSupport support = (CompositeDataSupport) compositeData;
                Field field = support.getClass().getDeclaredField("contents");
                field.setAccessible(true);
                return (SortedMap<String, Object>) field.get(support);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
