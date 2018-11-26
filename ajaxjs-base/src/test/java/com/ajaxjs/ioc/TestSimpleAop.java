package com.ajaxjs.ioc;

import java.lang.reflect.Method;

import org.junit.Test;

import com.ajaxjs.util.ReflectUtil;

public class TestSimpleAop {
	static class Foo {
		@Before(value = Bar.class, methodName = "bar")
		@Before(value = Bar.class, methodName = "bar2")
		public static void foo() {
			System.out.println("foo");
		}
	}

	static class Bar {
		public void bar() {
			System.out.println("bar");
		}

		public static void bar2() {
			System.out.println("bar");
		}
	}

	@Test
	public void aop() throws NoSuchMethodException, SecurityException {
		Method method = Foo.class.getMethod("foo");

		Before[] befores = method.getAnnotationsByType(Before.class);

		for (Before before : befores) {
			Class<?> beforeActor = before.value();
			String methodName = before.methodName();

			Method beforeMethod = beforeActor.getMethod(methodName);
			
			if(ReflectUtil.isStaticMethod(beforeMethod)) {
//				ReflectUtil.executeMethod(beforeMethod, args); // 静态方法
			}
			System.out.println(beforeMethod);
		}
	}
}
