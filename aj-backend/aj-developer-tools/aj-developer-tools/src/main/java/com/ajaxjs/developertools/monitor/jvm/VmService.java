package com.ajaxjs.developertools.monitor.jvm;


import com.ajaxjs.developertools.monitor.model.jvm.Vm;
import com.sun.tools.attach.*;
import org.springframework.stereotype.Service;
import sun.management.ConnectorAddressLink;

import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Service
public class VmService {
    public List<Vm> getLocalJvm() {
        List<Vm> jvmList = new ArrayList<>();
        List<VirtualMachineDescriptor> virtualMachines = VirtualMachine.list();

        for (VirtualMachineDescriptor virtualMachineDescriptor : virtualMachines) {
            Vm jvm = new Vm();
            jvm.setPid(Integer.parseInt(virtualMachineDescriptor.id()));
            jvm.setName(virtualMachineDescriptor.displayName().split(" ")[0]);
            jvmList.add(jvm);
        }

        return jvmList;
    }

    public void attachJvm(int pid) {
        JMXServiceURL jmxServiceURL = null;

        try {
            String address = ConnectorAddressLink.importFrom(pid);

            if (address == null) {
                VirtualMachine virtualMachine = VirtualMachine.attach(Integer.toString(pid));
                //加载Agent
                String javaHome = virtualMachine.getSystemProperties().getProperty("java.home");
                String agentPath = javaHome + File.separator + "jre" + File.separator + "lib" + File.separator + "management-agent.jar";
                File file = new File(agentPath);

                if (!file.exists()) {
                    agentPath = javaHome + File.separator + "lib" + File.separator + "management-agent.jar";
                    file = new File(agentPath);

                    if (!file.exists())
                        throw new IOException("Management agent not found");
                }

                agentPath = file.getCanonicalPath();

                try {
                    virtualMachine.loadAgent(agentPath, "com.sun.management.jmxremote");
                } catch (AgentLoadException | AgentInitializationException e) {
                    throw new IOException(e);
                }

                Properties properties = virtualMachine.getAgentProperties();
                address = (String) properties.get("com.sun.management.jmxremote.localConnectorAddress");

                if (address != null)
                    jmxServiceURL = new JMXServiceURL(address);
            } else
                jmxServiceURL = new JMXServiceURL(address);

            assert jmxServiceURL != null;
            JMXConnector jmxConnector = JMXConnectorFactory.connect(jmxServiceURL, null);
            JmxConnectorInstance.INSTANCE.setJmxConnector(jmxConnector);
        } catch (IOException | AttachNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
