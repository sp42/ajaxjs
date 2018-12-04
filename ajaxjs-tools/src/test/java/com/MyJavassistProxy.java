package com;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

/**
 * javassist动态代理
 * Created by windwant on 2016/9/18.
 */
public class MyJavassistProxy {
	public Object getProxySelf(String clazz, String pClazz, String methodName, String methodBefore, String methodAfter) {
		ClassPool cp = ClassPool.getDefault();
		CtClass ct;
		
		try {
			ct = cp.get(clazz);
			if (pClazz != null) {
				ct.setSuperclass(cp.get(pClazz));
			}
			ct.writeFile();
			ct.defrost();
			CtMethod m = ct.getDeclaredMethod(methodName);
			if (methodBefore != null) {
				m.insertBefore(methodBefore);
			}
			if (methodAfter != null) {
				m.insertAfter(methodAfter);
			}
			
			Class<?> c = ct.toClass();
			return c.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void testJavassist(){
		String clazz = "org.windwant.spring.core.proxy.Hello";
		String methodBefore = "{ System.out.println(\"method before...:\"); }";
		String methodAfter = "{ System.out.println(\"method after...:\"); }";
		String pClazz = "org.windwant.spring.core.proxy.HelloP";
		
//		Hello hello = (Hello) new MyJavassistProxy().getProxySelf(clazz, null, "say", methodBefore, methodAfter);
//		hello.say();
	}
}
