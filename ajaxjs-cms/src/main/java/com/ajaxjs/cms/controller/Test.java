package com.ajaxjs.cms.controller;

import java.io.Serializable;
import java.lang.reflect.Method;

import javax.ws.rs.Path;

public class Test {

	public static interface inter<T extends Serializable> {
		public void foo(T s);
	}

	public static class Foo implements inter<Long> {

		@Path("/")
		@Override
		public void foo(Long s) {

		}

	}

	public static void main(String[] args) {
		Method[] methods = Foo.class.getDeclaredMethods();
		for (Method m : methods) {
			if(m.getAnnotation(Path.class) != null) {
				System.out.println(m);
			}
//			Class<?>[] cls = m.getParameterTypes();
//			for (Class<?> c : cls) {
//				System.out.println(m+ ":" + c);
//			}
		}
	}
}
