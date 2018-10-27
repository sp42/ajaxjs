package test.com.ajaxjs.util;

import static com.ajaxjs.util.ReflectUtil.executeMethod;
import static com.ajaxjs.util.ReflectUtil.getClassByName;
import static com.ajaxjs.util.ReflectUtil.getConstructor;
import static com.ajaxjs.util.ReflectUtil.getDeclaredMethodByInterface;
import static com.ajaxjs.util.ReflectUtil.getMethod;
import static com.ajaxjs.util.ReflectUtil.getMethodByUpCastingSearch;
import static com.ajaxjs.util.ReflectUtil.newInstance;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

import com.ajaxjs.util.ReflectUtil;

public class TestReflectUtil {
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
		assertNotNull(newInstance("test.com.ajaxjs.util.reflect.TestNewInstance"));
		assertNotNull(getClassByName("test.com.ajaxjs.util.reflect.TestNewInstance"));
		
		Class<?>[] cs = ReflectUtil.getDeclaredInterface(ArrayList.class);
		assertNotNull(cs.length);
	}
	
	class Foo2 {
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
		assertNotNull(getMethod(Foo2.class, "m1"));// 按类引用
		assertNotNull(getMethod(Foo2.class, "m1", String.class)); // 按参数类型
		assertNotNull(getMethod(Foo2.class, "m1", "foo"));// 按实际参数
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
	
	public class Foo3 {
		public void m1() {
		}

		public String m1(String arg) {
			return arg;
		}
	}

	class Bar3 extends Foo3 {
		public void m2() {
		}
	}

	@Test
	public void testExecuteMethod() {
		assertEquals(executeMethod(new Foo3(), "m1"), null);
		assertEquals(executeMethod(new Foo3(), "m1", "fdf"), "fdf");
		assertEquals(executeMethod(new Bar2(), "m1"), null);
		assertEquals(executeMethod(new Bar3(), "m1", "fdf"), "fdf");
		assertEquals(executeMethod(new Bar3(), "m1", String.class, "fdf"), "fdf");
		// assertNotNull(executeMethod(foo, "Bar2"));
		// assertNotNull(executeMethod(foo, "Bar3", "fdf"));
	}
}
