package test.com.ajaxjs.util.reflect;

import static com.ajaxjs.util.reflect.NewInstance.*;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Test;

public class TestNewInstance {
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
		
		Class<?>[] cs = getDeclaredInterface(ArrayList.class);
		assertNotNull(cs.length);
	}
}
