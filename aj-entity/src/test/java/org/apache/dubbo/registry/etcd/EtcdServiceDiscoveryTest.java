package org.apache.dubbo.registry.etcd;

import static java.lang.String.valueOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.registry.client.DefaultServiceInstance;
import org.apache.dubbo.registry.client.ServiceInstance;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

/**
 * 2019-08-30
 * <p>
 * There is no embedded server. so it works depend on etcd local server.
 */
public class EtcdServiceDiscoveryTest {

	static EtcdServiceDiscovery etcdServiceDiscovery;

	static String urlStr = "etcd3://58.248.254.12:2379/org.apache.dubbo.registry.RegistryService";

	@Before
	public void setUp() throws Exception {
		URL url = URL.valueOf(urlStr);
		etcdServiceDiscovery = new EtcdServiceDiscovery();
		assertNull(etcdServiceDiscovery.etcdClient);
		etcdServiceDiscovery.initialize(url);
		
	}

//    @AfterAll
	public static void destroy() throws Exception {
//        etcdServiceDiscovery.destroy();
	}

	@Test
	public void testLifecycle() throws Exception {
		URL url = URL.valueOf(urlStr);
		EtcdServiceDiscovery etcdServiceDiscoveryTmp = new EtcdServiceDiscovery();
		System.out.println(etcdServiceDiscoveryTmp);
		assertNull(etcdServiceDiscoveryTmp.etcdClient);
		etcdServiceDiscoveryTmp.initialize(url);
		assertNotNull(etcdServiceDiscoveryTmp.etcdClient);
		assertTrue(etcdServiceDiscoveryTmp.etcdClient.isConnected());
		
		System.out.println(etcdServiceDiscoveryTmp.etcdClient.isConnected());
		etcdServiceDiscoveryTmp.destroy();
		assertFalse(etcdServiceDiscoveryTmp.etcdClient.isConnected());

		ServiceInstance serviceInstance = new DefaultServiceInstance(valueOf(System.nanoTime()), "EtcdTestService", "127.0.0.1", 8080);
		assertNull(etcdServiceDiscovery.etcdClient.getKVValue(etcdServiceDiscovery.toPath(serviceInstance)));
		etcdServiceDiscovery.register(serviceInstance);
		assertNotNull(etcdServiceDiscovery.etcdClient.getKVValue(etcdServiceDiscovery.toPath(serviceInstance)));
	}

//    @Test
	public void testRegistry() throws Exception {
	}

//    @Test
	public void testUnRegistry() throws Exception {
		ServiceInstance serviceInstance = new DefaultServiceInstance(valueOf(System.nanoTime()), "EtcdTest2Service", "127.0.0.1", 8080);
		assertNull(etcdServiceDiscovery.etcdClient.getKVValue(etcdServiceDiscovery.toPath(serviceInstance)));
		etcdServiceDiscovery.register(serviceInstance);
		assertNotNull(etcdServiceDiscovery.etcdClient.getKVValue(etcdServiceDiscovery.toPath(serviceInstance)));
		etcdServiceDiscovery.unregister(serviceInstance);
		assertNull(etcdServiceDiscovery.etcdClient.getKVValue(etcdServiceDiscovery.toPath(serviceInstance)));
	}

//    @Test
	public void testUpdate() throws Exception {
		DefaultServiceInstance serviceInstance = new DefaultServiceInstance(valueOf(System.nanoTime()), "EtcdTest34Service", "127.0.0.1", 8080);
		assertNull(etcdServiceDiscovery.etcdClient.getKVValue(etcdServiceDiscovery.toPath(serviceInstance)));
		etcdServiceDiscovery.register(serviceInstance);
		assertNotNull(etcdServiceDiscovery.etcdClient.getKVValue(etcdServiceDiscovery.toPath(serviceInstance)));
		assertEquals(etcdServiceDiscovery.etcdClient.getKVValue(etcdServiceDiscovery.toPath(serviceInstance)), new Gson().toJson(serviceInstance));
		serviceInstance.setPort(9999);
		etcdServiceDiscovery.update(serviceInstance);
		assertNotNull(etcdServiceDiscovery.etcdClient.getKVValue(etcdServiceDiscovery.toPath(serviceInstance)));
		assertEquals(etcdServiceDiscovery.etcdClient.getKVValue(etcdServiceDiscovery.toPath(serviceInstance)), new Gson().toJson(serviceInstance));
	}

//    @Test
	public void testGetInstances() throws Exception {
		String serviceName = "EtcdTest77Service";
		assertTrue(etcdServiceDiscovery.getInstances(serviceName).isEmpty());
		etcdServiceDiscovery.register(new DefaultServiceInstance(valueOf(System.nanoTime()), serviceName, "127.0.0.1", 8080));
		etcdServiceDiscovery.register(new DefaultServiceInstance(valueOf(System.nanoTime()), serviceName, "127.0.0.1", 9809));
		assertFalse(etcdServiceDiscovery.getInstances(serviceName).isEmpty());
		List<String> r = convertToIpPort(etcdServiceDiscovery.getInstances(serviceName));
		assertTrue(r.contains("127.0.0.1:8080"));
		assertTrue(r.contains("127.0.0.1:9809"));
	}

	private List<String> convertToIpPort(List<ServiceInstance> serviceInstances) {
		List<String> result = new ArrayList<>();
		for (ServiceInstance serviceInstance : serviceInstances) {
			result.add(serviceInstance.getHost() + ":" + serviceInstance.getPort());
		}
		return result;
	}

}
