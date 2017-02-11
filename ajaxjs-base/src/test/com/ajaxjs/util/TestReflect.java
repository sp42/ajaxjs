package test.com.ajaxjs.util;

import static org.junit.Assert.*;
import java.util.HashMap;
import org.junit.Test;

import com.ajaxjs.util.Reflect;

import java.util.*;
import static com.ajaxjs.util.Reflect.*;

public class TestReflect {

	@Test
	public void testProxy() {
		java.util.Map<String, String> map = new HashMap<String, String>();
		map.put("hihi", "Jack");

		@SuppressWarnings("unchecked")
		Map<String, String> newMap = (Map<String, String>) Reflect.proxy(map, Map.class, new Reflect.ProxyCallback() {
			@Override
			public boolean before(Object instance, String methodName, Object... objects) {
				boolean isGo_ON = true;
				System.out.println(methodName);
				return isGo_ON;
			}

			@Override
			public Object after(Object instance, String methodName, Object returnValue, Object... objects) {
				return returnValue;
			}
		});

		String value = newMap.get("hihi");
		System.out.println(value);
		assertNotNull(value);
	}

	public static class Foo {
		public Foo() {
		}

		public Foo(String str, String str2) {
		}

		public void Bar() {

		}

		public void CC(String cc) {

		}

		public String Bar2() {
			return "bar2";
		}

		public String Bar3(String arg) {
			return arg;
		}
	}

	@Test
	public void testNewInstance() {
		assertNotNull(newInstance(TestReflect.class));
		assertNotNull(newInstance("com.ajaxjs.test.util.TestReflect"));
		assertNotNull(newInstance(getConstructor(Foo.class)));
		assertNotNull(newInstance(getConstructor(Foo.class, String.class, String.class), "a", "b"));
		assertNotNull(getClassByName("com.ajaxjs.test.util.TestReflect"));
	}

	@Test
	public void testGetMethod() {
		assertNotNull(getMethod(Foo.class, "Bar"));
		assertNotNull(getMethod(Foo.class, "CC", String.class));
	}

	@Test
	public void testExecuteMethod() {
		Foo foo = new Foo();
		executeMethod(foo, "Bar");
		executeMethod(foo, "CC", "fdf");
		assertNotNull(executeMethod(foo, "Bar2"));
		assertNotNull(executeMethod(foo, "Bar3", "fdf"));
	}

	public static class A {
		public String foo(A a) {
			return "A.foo";
		}

		public String bar(C c) {
			return "A.bar";
		}
	}

	public static class B extends A {
	}

	public static interface C {
	}

	public static class D implements C {
	}

	@Test
	public void testDeclaredMethod() {
		assertNotNull(getDeclaredMethod(A.class, "foo", new A()));
		assertNotNull(getDeclaredMethod(A.class, "foo", new B()));
		assertNotNull(getDeclaredMethodByInterface(A.class, "bar", new D()));
	}
}
