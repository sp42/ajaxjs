package com.ajaxjs.ioc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.ajaxjs.ioc.testcase.LoginAction;
import com.ajaxjs.util.ioc.Bean;
import com.ajaxjs.util.ioc.Hi;
import com.ajaxjs.util.ioc.Person;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtNewMethod;
import javassist.NotFoundException;

public class TestIoc {
//	@BeforeClass
	public static void init() {
		ClassPool pool = ClassPool.getDefault();
		CtClass cc;
		
		try {
			cc = pool.get("com.ajaxjs.ioc.Hi");
//			CtMethod m = CtNewMethod.make("public void setPerson(com.ajaxjs.ioc.Person person) { this.person = person; }", cc);
//			cc.addMethod(m);
			CtField f1 = new CtField(pool.get("com.ajaxjs.ioc.Person"), "person", cc);
			cc.addMethod(CtNewMethod.setter("setPerson", f1));

//			Class<?> c = cc.toClass(this.getClass().getClassLoader());

			cc.toClass();
//			method = c.getDeclaredMethod("setPerson", Person.class);
		} catch (NotFoundException | CannotCompileException e) {
			e.printStackTrace();
		}
	}


	@Test
	public void test2() {
		BeanContext.init("com.ajaxjs.ioc.testcase");
		BeanContext.injectBeans();

		LoginAction loginAction = (LoginAction) ComponentMgr.get("LoginAction");
		loginAction.getCaptchaService().showImage();
		loginAction.captchaService2.showImage();

		assertNotNull(loginAction);
	}
	

	@Test
	public void testFindClassByInterface() {
		BeanContext.init("com.ajaxjs.ioc");

		List<AA> list = BeanContext.findBeanByInterface(AA.class);

		assertEquals(1, list.size());
	}
}
