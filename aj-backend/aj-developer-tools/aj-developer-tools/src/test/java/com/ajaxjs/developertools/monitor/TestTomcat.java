package com.ajaxjs.developertools.monitor;

import com.ajaxjs.developertools.monitor.tomcat_jmx.model.TomcatInfo;
import com.ajaxjs.developertools.monitor.tomcat_jmx.TomcatJmx;
import com.ajaxjs.util.TestHelper;
import org.junit.Test;

public class TestTomcat {
    String jmxURL = "service:jmx:rmi:///jndi/rmi://127.0.0.1:9011/jmxrmi";

    @Test
    public void testConnectJMX() {
        TomcatInfo info = new TomcatJmx().getInfo(jmxURL);
        TestHelper.printJson(info);
    }
}