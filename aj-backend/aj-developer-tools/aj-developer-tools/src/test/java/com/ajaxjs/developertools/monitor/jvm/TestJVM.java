package com.ajaxjs.developertools.monitor.jvm;

import com.ajaxjs.developertools.BaseTest;
import com.ajaxjs.developertools.monitor.model.jvm.Node;
import com.ajaxjs.developertools.monitor.model.jvm.Overview;
import com.ajaxjs.developertools.monitor.model.jvm.Vm;
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
        Overview overview = dashboardService.overview();
        TestHelper.printJson(overview);

        assertNotNull(overview);
    }

    @Autowired
    VmService vmService;
    @Autowired
    private JmxService jmxService;

    @Test
    public void testVM() {
        List<Vm> localJvm = vmService.getLocalJvm();
        TestHelper.printJson(localJvm);

        assertNotNull(localJvm);

        vmService.attachJvm(11844);
        testOverview();
    }

    @Test
    public void testJmxService() {
        List<Vm> localJvm = vmService.getLocalJvm();
        TestHelper.printJson(localJvm);

        assertNotNull(localJvm);

        vmService.attachJvm(11844);
        List<Node> domains = jmxService.getDomains();
        TestHelper.printJson(domains);
        assertNotNull(domains);
    }

}
