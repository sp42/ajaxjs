package com.ajaxjs.developertools.monitor;

import com.ajaxjs.developertools.monitor.model.jvm.Node;
import com.ajaxjs.developertools.monitor.model.jvm.NodeType;
import com.ajaxjs.util.logger.LogHelper;
import org.springframework.util.StringUtils;

import javax.management.*;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.util.*;

public class JmxUtils {
    private static final LogHelper LOGGER = LogHelper.getLog(JmxUtils.class);

    private final MBeanServerConnection msc;

    public JmxUtils(MBeanServerConnection msc) {
        this.msc = msc;
    }

    public MBeanAttributeInfo[] getAttributes(ObjectName objName) {
        try {
            return msc.getMBeanInfo(objName).getAttributes();
        } catch (InstanceNotFoundException | IntrospectionException | ReflectionException | IOException e) {
            LOGGER.warning(e);
            return null;
        }
    }

    /**
     * 列出所有的对象。
     * 下面方法同等效果
     * <code>
     * for (String domain : msc.getDomains())
     * System.out.println(domain);
     * List<Node> tomcat = MonitorUtils.getObjectNamesByDomain(msc, "Tomcat");
     * </code>
     */
    void listObjectInstance() {
        try {
            Set<ObjectInstance> MBeanset = msc.queryMBeans(null, null);

            for (ObjectInstance objectInstance : MBeanset) {
                ObjectName objectName = objectInstance.getObjectName();
                String canonicalName = objectName.getCanonicalName();
                System.out.println("canonicalName : " + canonicalName);

                // 可以查询集群
//                if (canonicalName.equals("Catalina:host=localhost,type=Cluster")) {
//                    // Get details of cluster MBeans
//                    // getMBeansDetails(canonicalName);
//                    String canonicalKeyPropList = objectName.getCanonicalKeyPropertyListString();
//                }
            }
        } catch (IOException e) {
            LOGGER.warning(e);
        }
    }

    public List<Node> getObjectNamesByDomain(String domain) {
        return getObjectNamesByDomain(msc, domain);
    }

    public static JMXConnector connect(String jmxUrl) {
        try {
            return JMXConnectorFactory.connect(new JMXServiceURL(jmxUrl));
        } catch (IOException e) {
            LOGGER.warning(e);
            return null;
        }
    }

    public static JMXConnector connect(String host, String port) {
        String jmxUrl = String.format("service:jmx:rmi:///jndi/rmi://%s:%s/jmxrmi", host, port);

        return connect(jmxUrl);
    }

    /**
     * 根据命名空间获取对象名称列表
     * 可以先打印出所有的命名空间：
     * <code>
     * for (String domain : msc.getDomains())
     * System.out.println(domain);
     * </code>
     *
     * @param msc    MBeanServerConnection
     * @param domain 命名空间
     * @return 对象名称列表
     */
    public static List<Node> getObjectNamesByDomain(MBeanServerConnection msc, String domain) {
        List<Node> nodes = new ArrayList<>();

        try {
            ObjectName queryObjectName = new ObjectName(domain + ":*");
            Set<ObjectName> objectNames = msc.queryNames(queryObjectName, null);
            Map<String, ObjectName> types = new HashMap<>();

            for (ObjectName objectName : objectNames) {
                String type = objectName.getKeyProperty("type");
                types.put(type, objectName);
            }

            for (Map.Entry<String, ObjectName> entry : types.entrySet()) {
                Node node = new Node();
                node.setTitle(StringUtils.hasText(entry.getKey()) ? entry.getKey() : entry.getValue().getCanonicalName());
                node.setKey(entry.getValue().getCanonicalName());
                node.setNodeType(NodeType.OBJECTNAME.getName());
                node.setFullName(entry.getValue().getCanonicalName());
                List<Node> subNodes = getObjectNamesByType(msc, domain, entry.getKey());

                if (subNodes.size() > 1) node.setChildren(subNodes);

                nodes.add(node);
            }
        } catch (IOException | MalformedObjectNameException e) {
            LOGGER.warning(e);
        }

        return nodes;
    }

    private static List<Node> getObjectNamesByType(MBeanServerConnection msc, String domain, String type) {
        List<Node> nodes = new ArrayList<>();

        try {
            ObjectName queryObjectName = new ObjectName(domain + ":type=" + type + ",*");
            Set<ObjectName> objectNames = msc.queryNames(queryObjectName, null);

            for (ObjectName objectName : objectNames) {
                Node node = new Node();
                String fullName = objectName.getCanonicalName();
                String name = objectName.getKeyProperty("name");
                node.setTitle(StringUtils.hasText(name) ? name : objectName.getCanonicalName());
                node.setFullName(fullName);
                node.setKey(fullName);
                node.setNodeType(NodeType.OBJECTNAME.getName());
                nodes.add(node);
            }
        } catch (MalformedObjectNameException | IOException e) {
            LOGGER.warning(e);
        }

        return nodes;
    }
}
