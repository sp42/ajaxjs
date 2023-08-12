package com.ajaxjs.developertools.monitor;

import com.ajaxjs.developertools.monitor.model.jvm.Node;
import com.ajaxjs.developertools.monitor.model.jvm.NodeType;
import com.ajaxjs.util.logger.LogHelper;
import org.springframework.util.StringUtils;

import javax.management.*;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.BiConsumer;

public class JmxHelper {
    private static final LogHelper LOGGER = LogHelper.getLog(JmxHelper.class);

    public JmxHelper() {
    }

    public JmxHelper(MBeanServerConnection msc) {
        this.msc = msc;
    }

    /**
     * 连接实例
     */
    private MBeanServerConnection msc;

    /**
     * 连接对象
     */
    private JMXConnector connector;

    public MBeanAttributeInfo[] getAttributes(ObjectName objName) {
        try {
            return msc.getMBeanInfo(objName).getAttributes();
        } catch (InstanceNotFoundException | IntrospectionException | ReflectionException | IOException e) {
            LOGGER.warning(e);
            return null;
        }
    }

    public void everyAttribute(ObjectName objName, BiConsumer<String, Object> fn) {
        MBeanAttributeInfo[] attributes = getAttributes(objName);

        try {
            for (MBeanAttributeInfo attribute : attributes) {
                String key = attribute.getName();
                Object value = msc.getAttribute(objName, key);
//                System.out.println("private " + attribute.getType() + " " + key + ";");

                fn.accept(key, value);
            }
        } catch (MBeanException | AttributeNotFoundException | InstanceNotFoundException | ReflectionException |
                 IOException e) {
            LOGGER.warning(e);
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
        Map<String, ObjectName> types = new HashMap<>();

        for (ObjectName objectName : Objects.requireNonNull(queryNames(msc, domain + ":*"))) {
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

        return nodes;
    }

    private static List<Node> getObjectNamesByType(MBeanServerConnection msc, String domain, String type) {
        List<Node> nodes = new ArrayList<>();

        for (ObjectName objectName : Objects.requireNonNull(queryNames(msc, domain + ":type=" + type + ",*"))) {
            Node node = new Node();
            String fullName = objectName.getCanonicalName(), name = objectName.getKeyProperty("name");

            node.setTitle(StringUtils.hasText(name) ? name : objectName.getCanonicalName());
            node.setFullName(fullName);
            node.setKey(fullName);
            node.setNodeType(NodeType.OBJECTNAME.getName());
            nodes.add(node);
        }

        return nodes;
    }

    public static SortedMap<String, Object> analyzeCompositeData(Object compositeData) {
        try {
            if (compositeData instanceof CompositeDataSupport) {
                CompositeDataSupport support = (CompositeDataSupport) compositeData;
                Field field = support.getClass().getDeclaredField("contents");
                field.setAccessible(true);

                return (SortedMap<String, Object>) field.get(support);
            }
        } catch (IllegalAccessException | NoSuchFieldException e) {
            LOGGER.warning(e);
        }

        return null;
    }

    /**
     * 实例化都抛出异常，麻烦
     */
    public static ObjectName objectNameFactory(String name) {
        try {
            return new ObjectName(name);
        } catch (MalformedObjectNameException e) {
            LOGGER.warning(e);
            return null;
        }
    }

    public Set<ObjectName> queryNames(String name) {
        return queryNames(msc, name);
    }

    public static Set<ObjectName> queryNames(MBeanServerConnection msc, String name) {
        try {
            return msc.queryNames(objectNameFactory(name), null);
        } catch (IOException e) {
            LOGGER.warning(e);
            return null;
        }
    }

    public MBeanInfo getMBeanInfo(ObjectName o) {
        return getMBeanInfo(msc, o);
    }

    public static MBeanInfo getMBeanInfo(MBeanServerConnection msc, ObjectName o) {
        try {
            return msc.getMBeanInfo(o);
        } catch (InstanceNotFoundException | IntrospectionException | ReflectionException | IOException e) {
            LOGGER.warning(e);
            return null;
        }
    }

    public void setMsc(MBeanServerConnection msc) {
        this.msc = msc;
    }

    public MBeanServerConnection getMsc() {
        return msc;
    }

    public void setConnector(JMXConnector connector) {
        this.connector = connector;
    }

    public JMXConnector getConnector() {
        return connector;
    }
}
