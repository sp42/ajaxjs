package test.com.ajaxjs.util.ioc;

import java.util.Set;
import static org.junit.Assert.*;
import org.junit.Test;

import com.ajaxjs.util.ioc.BeanContext;
import com.ajaxjs.util.ioc.Scanner;

public class TestIoc {
	static class A {

	}

	static class B {
		public B(int i) {

		}
	}

	@Test
	public void foo() {
		try {
			System.out.println(A.class.getConstructor());
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//@Test
	public void test() {
		Set<Class<?>> classes = new Scanner().scanPackage("test.com.ajaxjs.util.ioc");
		System.out.println(classes.size());
		BeanContext.me().init(classes);

		Hi hi = (Hi) BeanContext.me().getBean("hi");

		System.out.println(hi.sayHello());
		assertNotNull(hi);
	}
}
