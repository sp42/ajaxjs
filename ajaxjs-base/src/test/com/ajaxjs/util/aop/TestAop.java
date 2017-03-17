package test.com.ajaxjs.util.aop;

import static org.junit.Assert.*;

import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ajaxjs.util.aop.Aop;
import com.ajaxjs.util.aop.ReturnBefore;

class TestAopHandler extends Aop<Subject> {
	@Override
	protected Object before(Method method, Object[] args) {
		System.out.println("print TestCls.before");
		return null;
	};

	@Override
	protected void after(Method method, Object[] args, Object returnObj) {
		System.out.println("print TestCls.after");
	}
}

class TestStopAopHandler extends Aop<Subject> {
	@Override
	protected Object before(Method method, Object[] args) {
		System.out.println("print TestCls.before");
		return new ReturnBefore(null);
	}

	@Override
	protected void after(Method method, Object[] args, Object returnObj) {
		System.out.println("print TestCls.after");
	}
}

public class TestAop {
	Subject subject, stopSubject;

	@BeforeClass
	public static void init() {
	}

	@Before
	public void setUp() {
		subject = new TestAopHandler().bind(new BaseTest());
		System.out.println(subject);
		stopSubject = new TestStopAopHandler().bind(new BaseTest());
	}

	@Test
	public void testAop() {
		assertNotNull(subject);
		subject.doIt();
		stopSubject.doIt();
	}
	
	class CaccheHandler extends Aop<Subject> {
		@Override
		protected Object before(Method method, Object[] args) {
			System.out.println("print CaccheHandler.before");
			return null;
		}

		@Override
		protected void after(Method method, Object[] args, Object returnObj) {
			System.out.println("print CaccheHandler.after");
		}
	}
	
	class RightHandler extends Aop<Subject> {
		@Override
		protected Object before(Method method, Object[] args) {
			System.out.println("print RightHandler.before");
			return null;
		}
		
		@Override
		protected void after(Method method, Object[] args, Object returnObj) {
			System.out.println("print RightHandler.after");
		}
	}
	
	@Test
	public void testChain() {
		subject = Aop.chain(new BaseTest(), new TestAopHandler(), new TestStopAopHandler(), new RightHandler());
		assertNotNull(subject);
		subject.doIt();
	}
}