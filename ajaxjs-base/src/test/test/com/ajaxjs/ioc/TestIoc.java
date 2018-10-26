package test.com.ajaxjs.ioc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.ajaxjs.ioc.Bean;
import com.ajaxjs.ioc.BeanContext;
import com.ajaxjs.util.io.resource.ScanClass;

public class TestIoc {
	@Test
	public void test() {
		Set<Class<Object>> classes = ScanClass.scanClass("test.com.ajaxjs.ioc");
		BeanContext.init(classes);
		BeanContext.injectBeans();

		Hi hi = (Hi) BeanContext.getBean("hi");
		System.out.println(hi);

		assertNotNull(hi);
		
		Person person = (Person)BeanContext.getBean("person");
		System.out.println(person.getName());
		assertEquals("Hello Rose", hi.sayHello());
	}
	
	@Test
	public void testFindBeanById() {
		BeanContext.init("test.com.ajaxjs.ioc");
		Object bean = BeanContext.getBean("foo");

		assertTrue(bean instanceof AA);
	}
	
	public static interface AA {
	}

	@Bean("foo")
	public static class AObj implements AA {
	}


	@Test
	public void testFindClassByInterface() {
		BeanContext.init("test.com.ajaxjs.ioc");

		List<AA> list = BeanContext.findBeanByInterface(AA.class);

		assertEquals(1, list.size());
	}
}
