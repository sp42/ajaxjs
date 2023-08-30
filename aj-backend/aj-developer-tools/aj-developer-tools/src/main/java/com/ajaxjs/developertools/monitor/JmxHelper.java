package com.ajaxjs.developertools.monitor;

import com.ajaxjs.developertools.monitor.jvm.model.Node;
import com.ajaxjs.developertools.monitor.jvm.model.NodeType;
import com.ajaxjs.framework.BusinessException;
import com.ajaxjs.util.logger.LogHelper;
import org.springframework.util.StringUtils;

import javax.management.*;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.management.ManagementFactory;
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

    @SuppressWarnings("unchecked")
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

    /**
     * 注册一个 MBean
     * 接口必须以 MBean 结尾，且提供 String getStatus(); 方法。
     * <pre>
     * public interface CustomTomcatThreadPoolMBean {
     *     String getStatus();
     * }
     * </pre>
     *
     * @param mBean MBean 对象，须实现接口方法
     * @param name  ObjectName 须符合一定格式，如 "qww:type=CustomTomcatThreadPool"
     */
    public void registerMBean(Object mBean, String name) {
        try {
            ManagementFactory.getPlatformMBeanServer().registerMBean(mBean, new ObjectName(name));
        } catch (InstanceAlreadyExistsException | MBeanRegistrationException | NotCompliantMBeanException |
                 MalformedObjectNameException e) {
            e.printStackTrace();
        }
    }

    public static void dumpMBean(MBeanServer server, ObjectName objName, MBeanInfo mbi, Writer writer) throws Exception {
        writer.write(String.format("MBeanClassName=%s%n", mbi.getClassName()));
        Map<String, String> props = new HashMap<>();
        int idx = 0;

        for (MBeanAttributeInfo mf : mbi.getAttributes()) {
            idx++;

            try {
                Object attr = server.getAttribute(objName, mf.getName());

                if (attr != null) props.put(mf.getName(), attr.toString());
            } catch (Exception ex) {
                // sun.management.RuntimeImpl: java.lang.UnsupportedOperationException(Boot class path mechanism is not supported)
                props.put("error_" + idx, ex.getClass().getName() + " " + ex.getMessage());
            }
        }

        // sort by hashmap keys
        for (String sKey : new TreeSet<>(props.keySet()))
            writer.write(String.format("%s=%s%n", sKey, props.get(sKey)));
    }

    /**
     * Dump MBean management properties, all beans or named bean {@code dumpMBean.jsp?name=ConnectionPool,ContainerMBean}
     * JSP as below:
     * <pre>{@code
     * <%@ page contentType="text/plain; charset=UTF-8"  pageEncoding="UTF-8" import="com.ajaxjs.developertools.monitor.JmxHelper"%>
     * <%
     *     JmxHelper.printMBean(request, out);
     * %>
     * }</pre>
     */
    public static void printMBean(HttpServletRequest request, JspWriter out) throws Exception {
        String val = request.getParameter("name");
        String[] names = val != null ? val.trim().split(",") : new String[0];
        if (names.length == 1 && names[0].isEmpty()) names = new String[0];

        MBeanServer server = ManagementFactory.getPlatformMBeanServer();

        for (ObjectName objName : server.queryNames(null, null)) {
            MBeanInfo mbi = server.getMBeanInfo(objName);

            boolean match = names.length < 1;
            String name = mbi.getClassName();

            for (String s : names) {
                if (name.endsWith(s)) {
                    match = true;
                    break;
                }
            }

            if (match) {
                JmxHelper.dumpMBean(server, objName, mbi, out);
                out.println("");
            }
        }

        out.flush();
    }
}
