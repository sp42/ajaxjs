package com.ajaxjs.util.ioc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.function.Function;

import org.junit.Test;

import com.ajaxjs.util.ioc.testcase.LoginAction;
import com.ajaxjs.util.ioc.testcase.Subject;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtNewMethod;
import javassist.NotFoundException;

public class TestIoc {
	@Test
	public void testJavassist() {
		ClassPool pool = ClassPool.getDefault();
		CtClass cc;

		try {
			cc = pool.get("com.ajaxjs.util.ioc.Hi");
//			CtMethod m = CtNewMethod.make("public void setPerson(com.ajaxjs.ioc.Person person) { this.person = person; }", cc);
//			cc.addMethod(m);
			CtField f1 = new CtField(pool.get("com.ajaxjs.util.ioc.Person"), "person", cc);
			cc.addMethod(CtNewMethod.setter("setPerson", f1));

//			Class<?> c = cc.toClass(this.getClass().getClassLoader());

			cc.toClass();
//			method = c.getDeclaredMethod("setPerson", Person.class);
		} catch (NotFoundException | CannotCompileException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void test() {
		ComponentMgr.scan("com.ajaxjs");

		Hi hi = ComponentMgr.get(Hi.class);
		assertNotNull(hi);
		assertEquals("Hello Rose", hi.sayHello());

		Person person = (Person) ComponentMgr.get("Person");
		assertNotNull(person);

		LoginAction loginAction = (LoginAction) ComponentMgr.get("LoginAction");
		loginAction.getCaptchaService().showImage();
		loginAction.captchaService2.showImage();
		assertNotNull(loginAction);

		assertNotNull(ComponentMgr.get("com.ajaxjs.util.ioc.Hi")); // 根据类全名
		assertNotNull(ComponentMgr.get(Base.class)); // 继承关系中某一个类，即传入父类亦可返回组件
		assertNotNull(ComponentMgr.getByInterface(Subject.class));

		List<Subject> list = ComponentMgr.getAllByInterface(Subject.class);
		assertEquals(2, list.size());
	}

	Function<Class<?>, String> giveName = clz -> {
		String[] arr = clz.getName().split("\\.");
		return arr[arr.length - 1];
	};

//	@Test
//	public void testGivename() {
//		BeanContext.simplePut("com.ajaxjs.ioc", giveName);
//	}
}
