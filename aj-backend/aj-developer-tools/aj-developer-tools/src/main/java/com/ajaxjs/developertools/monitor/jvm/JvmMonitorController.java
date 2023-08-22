package com.ajaxjs.developertools.monitor.jvm;

import com.ajaxjs.developertools.monitor.JmxHelper;
import com.ajaxjs.developertools.monitor.model.jvm.*;
import com.ajaxjs.framework.spring.validator.custom.IdCard;
import com.sun.tools.attach.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sun.management.ConnectorAddressLink;

import javax.management.*;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.TabularDataSupport;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.*;

@RestController
@RequestMapping("/jvm")
public class JvmMonitorController {
    JmxHelper INSTANCE = new JmxHelper();

    @PostMapping("/test/{id}")
    public boolean test(@PathVariable @IdCard String id) {
        System.out.println(id);
        return true;
    }

    @GetMapping("/jdbc_pool_status")
    public Map<String, Object> jdbcPoolStatus() {
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        Set<ObjectName> objectNames = server.queryNames(null, null);
        Map<String, Object> result = new HashMap<>();

        try {
            for (ObjectName name : objectNames) {
                MBeanInfo info = server.getMBeanInfo(name);

                if (info.getClassName().equals("org.apache.tomcat.jdbc.pool.jmx.ConnectionPool")) {
                    for (MBeanAttributeInfo mf : info.getAttributes()) {
                        Object attributeValue = server.getAttribute(name, mf.getName());

                        if (attributeValue != null)
                            result.put(mf.getName(), attributeValue);
                    }

                    break;
                }
            }
        } catch (InstanceNotFoundException | IntrospectionException | ReflectionException | MBeanException |
                 AttributeNotFoundException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    @Autowired
    private MonitorDashboardService monitorDashboardService;

    @GetMapping("/dashboard/overview")
    Overview overview() {
        return monitorDashboardService.overview();
    }

    @GetMapping("/localJvms")
    List<Vm> getLocalJvmProcessList() {
        List<VirtualMachineDescriptor> vms = VirtualMachine.list();
        List<Vm> list = new ArrayList<>(vms.size());

        for (VirtualMachineDescriptor virtualMachineDescriptor : vms) {
            Vm jvm = new Vm();
            jvm.setPid(Integer.parseInt(virtualMachineDescriptor.id()));
            jvm.setName(virtualMachineDescriptor.displayName().split(" ")[0]);
            list.add(jvm);
        }

        return list;
    }

    /**
     * 根据 pid 加入到分析
     *
     * @param pid 进程 id
     * @return 是否成功
     */
    @GetMapping("/attachLocalJvm")
    Boolean attachLocalJvm(Integer pid) {
        try {
            String address = ConnectorAddressLink.importFrom(pid);

            if (address == null) {
                VirtualMachine vm = VirtualMachine.attach(Integer.toString(pid));
                // 加载 Agent
                String javaHome = vm.getSystemProperties().getProperty("java.home");
                String agentPath = javaHome + File.separator + "jre" + File.separator + "lib" + File.separator + "management-agent.jar";
                File file = new File(agentPath);

                if (!file.exists()) {
                    agentPath = javaHome + File.separator + "lib" + File.separator + "management-agent.jar";
                    file = new File(agentPath);

                    if (!file.exists())
                        throw new IOException("Management agent not found");
                }

                agentPath = file.getCanonicalPath();
                vm.loadAgent(agentPath, "com.sun.management.jmxremote");
                address = (String) vm.getAgentProperties().get("com.sun.management.jmxremote.localConnectorAddress");
            }

            INSTANCE.setConnector(JmxHelper.connect(address));
        } catch (IOException | AttachNotSupportedException | AgentLoadException | AgentInitializationException e) {
            e.printStackTrace();
        }

        return true;
    }

    @GetMapping("/attachRemoteJvm")
    Boolean attachRemoteJvm(String host, String port) {
        INSTANCE.setConnector(JmxHelper.connect(host, port));

        return true;
    }

    /**
     * 获取所有 domain
     */
    @GetMapping("/domains")
    List<Node> getDomains() {
        List<Node> nodes = new ArrayList<>();
        MBeanServerConnection msc = INSTANCE.getMsc();

        try {
            for (String domain : msc.getDomains()) {
                Node node = new Node();
                node.setKey(domain);
                node.setTitle(domain);
                node.setNodeType(NodeType.DOMAIN.getName());
                node.setChildren(JmxHelper.getObjectNamesByDomain(msc, domain)); // 第一层的
                nodes.add(node);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return nodes;
    }

    @GetMapping("/properties")
    BeanInfo getPropertyList(String fullName) {
        BeanInfo beanInfo = new BeanInfo();
        List<BeanAttributeInfo> list = new ArrayList<>();

        try {
            if (INSTANCE.getMsc() == null)
                INSTANCE.setMsc(INSTANCE.getConnector().getMBeanServerConnection());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (ObjectName o : INSTANCE.queryNames(fullName)) {
            MBeanInfo mBeanInfo = INSTANCE.getMBeanInfo(o);
            beanInfo.setOperationInfos(mBeanInfo.getOperations());
            beanInfo.setNotificationInfos(mBeanInfo.getNotifications());

            for (MBeanAttributeInfo mBeanAttributeInfo : mBeanInfo.getAttributes()) {
                Object attribute;

                if (mBeanAttributeInfo.isReadable()) {
                    try {
                        attribute = INSTANCE.getMsc().getAttribute(o, mBeanAttributeInfo.getName());
                    } catch (Exception e) {
                        attribute = "不可用";
                    }
                } else attribute = "不可读";

                BeanAttributeInfo beanAttributeInfo = new BeanAttributeInfo();
                beanAttributeInfo.setName(mBeanAttributeInfo.getName());
                beanAttributeInfo.setAttributeInfo(mBeanAttributeInfo);
                BeanAttributeValue beanAttributeValue = new BeanAttributeValue();
                beanAttributeValue.setCompositeData(true);

                if (mBeanAttributeInfo.getType().equals("javax.management.openmbean.CompositeData") && mBeanAttributeInfo.isReadable())
                    beanAttributeValue.setData(JmxHelper.analyzeCompositeData(attribute));
                else if (mBeanAttributeInfo.getType().equals("[Ljavax.management.openmbean.CompositeData;") && mBeanAttributeInfo.isReadable()) {
                    CompositeData[] compositeDataArray = (CompositeData[]) attribute;
                    List<SortedMap<String, Object>> mapList = new ArrayList<>(compositeDataArray.length);

                    for (CompositeData compositeData : compositeDataArray)
                        mapList.add(JmxHelper.analyzeCompositeData(compositeData));

                    beanAttributeValue.setData(mapList);
                } else if (mBeanAttributeInfo.getType().equals("javax.management.openmbean.TabularData")) {
                    List<SortedMap<String, Object>> mapList = new ArrayList<>();
                    TabularDataSupport tabularDataSupport = (TabularDataSupport) attribute;


                    for (Map.Entry<Object, Object> entry : tabularDataSupport.entrySet()) {
                        SortedMap<String, Object> map = JmxHelper.analyzeCompositeData(entry.getValue());
                        assert map != null;

                        SortedMap<String, Object> normalMap = new TreeMap<>();
                        normalMap.put("name", map.getOrDefault("key", ""));
                        normalMap.put("value", map.getOrDefault("value", ""));
                        mapList.add(normalMap);
                    }

                    beanAttributeValue.setData(mapList);
                } else
                    beanAttributeValue.setData(attribute);

                beanAttributeInfo.setValue(beanAttributeValue);
                list.add(beanAttributeInfo);
            }
        }

        beanInfo.setBeanAttributeInfos(list);

        return beanInfo;
    }

    @GetMapping("/run")
    Boolean runMethod(String fullName, String methodName) {
        try {
            INSTANCE.getMsc().invoke(JmxHelper.objectNameFactory(fullName), methodName, null, null);

            return true;
        } catch (IOException | InstanceNotFoundException | MBeanException | ReflectionException e) {
            e.printStackTrace();
            return false;
        }
    }
}
