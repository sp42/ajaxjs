package com.util.generator.another;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

class JavaGenHandler implements InvocationHandler {
	private JavaGen mTarget;

	public JavaGenHandler(JavaGen javaGen) {
		mTarget = javaGen;
	}

	// 动态注入
	@Override
	public Object invoke(Object obj, Method method, Object[] params) throws Throwable {
		Object ret = null;

		String name = method.getName();
		if (name.equals("getRandomInt")) { // intercept the getRandomInt
			ret = method.invoke(mTarget, params); // call the base method
			System.out.println("Print random int: " + ret);
		} else if (name.equals("printClassInfo")) { // print class info
			System.out.println("Class: " + obj.getClass());
			method.invoke(mTarget, params);
		}

		mTarget.calledMethods.add(name); // change the property

		return ret;
	}

}