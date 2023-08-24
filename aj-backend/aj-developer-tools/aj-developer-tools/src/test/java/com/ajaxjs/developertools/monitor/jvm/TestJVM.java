package com.ajaxjs.developertools.monitor.jvm;

import com.ajaxjs.developertools.BaseTest;
import com.ajaxjs.developertools.monitor.jvm.model.Node;
import com.ajaxjs.developertools.monitor.jvm.model.Overview;
import com.ajaxjs.developertools.monitor.jvm.model.Vm;
import com.ajaxjs.util.TestHelper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertNotNull;

public class TestJVM extends BaseTest {
    @Autowired
    MonitorDashboardService dashboardService;

    @Test
    public void testOverview() {
        jvmMonitorController.attachLocalJvm(17168);
        Overview overview = dashboardService.overview();
        TestHelper.printJson(overview);

        assertNotNull(overview);
    }

    @Autowired
    JvmMonitorController jvmMonitorController;

    @Test
    public void testVM() {
        List<Vm> localJvm = jvmMonitorController.getLocalJvmProcessList();
//        TestHelper.printJson(localJvm);
        assertNotNull(localJvm);
        jvmMonitorController.attachLocalJvm(17168);
        testOverview();
    }

    @Test
    public void testJmxService() {
        List<Vm> localJvm = jvmMonitorController.getLocalJvmProcessList();
        TestHelper.printJson(localJvm);

        assertNotNull(localJvm);

        jvmMonitorController.attachLocalJvm(11844);
        List<Node> domains = jvmMonitorController.getDomains();
        TestHelper.printJson(domains);
        assertNotNull(domains);
    }

}
