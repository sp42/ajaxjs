package com.ajaxjs.util.ioc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class TestIoc {
	@Test
	public void test() {
		ComponentMgr.scan();

		Hi hi = ComponentMgr.get(Hi.class);
		assertNotNull(hi);
		assertEquals("Hello Rose", hi.sayHello());

		Person person = (Person) ComponentMgr.get("Person");
		assertNotNull(person);
	}
}
