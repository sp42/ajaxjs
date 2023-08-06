package com.ajaxjs.developertools.monitor.jvm;

import com.ajaxjs.developertools.monitor.JmxUtils;
import com.ajaxjs.developertools.monitor.jvm.JmxConnectorInstance;
import com.ajaxjs.developertools.monitor.model.jvm.BeanInfo;
import com.ajaxjs.developertools.monitor.model.jvm.Node;
import com.ajaxjs.developertools.monitor.model.jvm.Overview;
import com.ajaxjs.developertools.monitor.model.jvm.Vm;
import com.ajaxjs.developertools.monitor.jvm.JmxService;
import com.ajaxjs.developertools.monitor.jvm.MonitorDashboardService;
import com.ajaxjs.developertools.monitor.jvm.VmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.management.*;
import javax.management.remote.JMXConnector;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/jvm")
public class JvmMonitorController {
    @Autowired
    private MonitorDashboardService monitorDashboardService;

    @GetMapping("/dashboard/overview")
    Overview overview() {
        return monitorDashboardService.overview();
    }

    @Autowired
    private VmService vmService;

    @GetMapping("/localJvms")
    List<Vm> getLocalJvmProcessList() {
        return vmService.getLocalJvm();
    }

    @GetMapping("/attachLocalJvm")
    Boolean attachLocalJvm(Integer pid) {
        vmService.attachJvm(pid);

        return true;
    }

    @GetMapping("/attachRemoteJvm")
    Boolean attachRemoteJvm(String host, String port) {
        JmxConnectorInstance.INSTANCE.setJmxConnector(JmxUtils.connect(host, port));

        return true;
    }

    @Autowired
    private JmxService jmxService;

    @GetMapping("/domains")
    List<Node> getDomains() {
        return jmxService.getDomains();
    }

    @GetMapping("/properties")
    BeanInfo getPropertyList(String fullName) {
        return jmxService.getObjectNameProperties(fullName);
    }

    @GetMapping("/run")
    Boolean runMethod(String fullName, String methodName) {
        JMXConnector connector = JmxConnectorInstance.INSTANCE.getJmxConnector();

        try {
            ObjectName queryObjectName = new ObjectName(fullName);
            connector.getMBeanServerConnection().invoke(queryObjectName, methodName, null, null);

            return true;
        } catch (MalformedObjectNameException | IOException | InstanceNotFoundException | MBeanException |
                 ReflectionException e) {
            e.printStackTrace();
            return false;
        }
    }
}
