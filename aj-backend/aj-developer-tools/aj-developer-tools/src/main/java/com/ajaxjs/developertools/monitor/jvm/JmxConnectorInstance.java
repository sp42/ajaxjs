package com.ajaxjs.developertools.monitor.jvm;

import sun.misc.VMSupport;

import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

public enum JmxConnectorInstance {
    INSTANCE;

    private static JMXConnector jmxConnector;

    JmxConnectorInstance() {
    }

    public JMXConnector getJmxConnector() {
        return jmxConnector;
    }

    public void setJmxConnector(JMXConnector jmxConnector) {
        JmxConnectorInstance.jmxConnector = jmxConnector;
    }

    /**
     * 当前 jvm
     */
    private JMXConnector buildJmxConnector() {
        String addr = (String) VMSupport.getAgentProperties().get("com.sun.management.jmxremote.localConnectorAddress");

        if (addr == null) {
            try {
                sun.management.Agent.premain("");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        addr = (String) VMSupport.getAgentProperties().get("com.sun.management.jmxremote.localConnectorAddress");

        try {
            return JMXConnectorFactory.connect(new JMXServiceURL(addr), null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
