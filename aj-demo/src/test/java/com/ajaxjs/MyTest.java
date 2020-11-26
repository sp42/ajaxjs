package com.ajaxjs;

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;

public class MyTest {
	class MyClass {
		public int add(int x, int y) {
			return x + y;
		}

		public int substract(int x, int y) {
			return x - y;
		}

		public int multiply(int x, int y) {
			if (x > 999) {
				throw new IllegalArgumentException("X 必须小于 1000");
			}

			return x * y;
		}

		public int divide(int x, int y) {
			return x / y;
		}
	}

	@Test
	public void testAdd() {
		MyClass tester = new MyClass();
		assertEquals("5 + 5 等于 10 ", 10, tester.add(5, 5));
	}

	@Test
	public void testSubstract() {
		MyClass tester = new MyClass();
		assertEquals("10 - 5 等于 5 ", 5, tester.substract(10, 5));
	}

	@Ignore
	public void testMultiply() {
		MyClass tester = new MyClass();
		assertEquals("10 x 5 等于 50 ", 50, tester.multiply(10, 5));
	}

	@Test
	public void testDivide() {
		MyClass tester = new MyClass();
		assertEquals("10 / 5 等于 50 ", 2, tester.divide(10, 0));
	}
}
