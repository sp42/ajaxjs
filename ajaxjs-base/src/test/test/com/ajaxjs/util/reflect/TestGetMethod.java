package test.com.ajaxjs.util.reflect;

import static com.ajaxjs.util.reflect.GetMethod.executeMethod;
import static com.ajaxjs.util.reflect.GetMethod.getDeclaredMethodByInterface;
import static com.ajaxjs.util.reflect.GetMethod.getMethod;
import static com.ajaxjs.util.reflect.GetMethod.getMethodByUpCastingSearch;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TestGetMethod {

	class Foo {
		public void m1() {
		}

		public void m1(String arg) {
		}
	}

	class Bar extends Foo {
		public void m2() {
		}
	}

	@Test
	public void testGetMethod() {
		assertNotNull(getMethod(new Foo(), "m1"));// 按实际对象
		assertNotNull(getMethod(Foo.class, "m1"));// 按类引用
		assertNotNull(getMethod(Foo.class, "m1", String.class)); // 按参数类型
		assertNotNull(getMethod(Foo.class, "m1", "foo"));// 按实际参数
		assertNotNull(getMethod(Bar.class, "m1"));
		assertNotNull(getMethod(Bar.class, "m1", String.class));
		assertNotNull(getMethod(Bar.class, "m2"));
	}

	class Foo1 {
		public void foo(Foo1 a) {

		}
	}

	class Bar2 extends Foo1 {

	}

	@Test
	public void testGetMethodByUpCastingSearch() {
		assertTrue(getMethod(Foo1.class, "foo", new Bar2()) == null); // 找不到
		assertNotNull(getMethodByUpCastingSearch(Foo1.class, "foo", new Bar2())); // 找到了
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
		assertTrue(getMethodByUpCastingSearch(A.class, "bar", new D()) == null); // 找不到
		assertNotNull(getDeclaredMethodByInterface(A.class, "bar", new D()));// 找到了
	}
	
	public class Foo2 {
		public void m1() {
		}

		public String m1(String arg) {
			return arg;
		}
	}

	class Bar3 extends Foo2 {
		public void m2() {
		}
	}

	@Test
	public void testExecuteMethod() {
		assertEquals(executeMethod(new Foo2(), "m1"), null);
		assertEquals(executeMethod(new Foo2(), "m1", "fdf"), "fdf");
		assertEquals(executeMethod(new Bar2(), "m1"), null);
		assertEquals(executeMethod(new Bar3(), "m1", "fdf"), "fdf");
		assertEquals(executeMethod(new Bar3(), "m1", String.class, "fdf"), "fdf");
		// assertNotNull(executeMethod(foo, "Bar2"));
		// assertNotNull(executeMethod(foo, "Bar3", "fdf"));
	}
}
