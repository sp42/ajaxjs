package test.com.ajaxjs.util.reflect;

import static com.ajaxjs.util.reflect.ReflectNewInstance.*;
import static com.ajaxjs.util.reflect.Reflect.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class TestReflect {
	public TestReflect(){}
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
		assertNotNull(newInstance(Foo.class));
		assertNotNull(newInstance(Foo.class, "a", "b"));
		assertNotNull(newInstance(getConstructor(Foo.class)));
		assertNotNull(newInstance(getConstructor(Foo.class, String.class, String.class), "a", "b"));
		assertNotNull(newInstance("test.com.ajaxjs.util.reflect.TestReflect")); 
		assertNotNull(getClassByName("test.com.ajaxjs.util.reflect.TestReflect"));
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
//		assertNotNull(getDeclaredMethodByInterface(A.class, "bar", new D()));
	}
}
