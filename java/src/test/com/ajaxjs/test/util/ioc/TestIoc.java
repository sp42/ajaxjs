package com.ajaxjs.test.util.ioc;

import java.util.Set;
import static org.junit.Assert.*;
import org.junit.Test;

import com.ajaxjs.util.ioc.BeanContext;
import com.ajaxjs.util.ioc.Scanner;

public class TestIoc {

	@Test
	public void test(){
		Set<Class<?>> classes = new Scanner().scanPackage("com.ajaxjs.test.util.ioc");
		
		BeanContext.me().init(classes);
		
		Hi hi = (Hi) BeanContext.me().getBean("hi");
		hi.sayHello();
		
		assertNotNull(hi);
	}
}
