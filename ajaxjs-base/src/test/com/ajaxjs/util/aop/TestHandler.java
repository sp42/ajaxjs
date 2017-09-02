package test.com.ajaxjs.util.aop;

import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;

import com.ajaxjs.util.aop.Aop;
import com.ajaxjs.util.aop.ReturnBefore;

public class TestHandler {
	Subject subject, stopSubject;
	
	@Before
	public void setUp() {
		subject = new TestAopHandler().bind(new BaseTest());
		stopSubject = new TestStopAopHandler().bind(new BaseTest());
	}

	@Test
	public void testAop() {
		assertNotNull(subject);
		subject.doIt();
		stopSubject.doIt();
	}
}

class TestAopHandler extends Aop<Subject> {
	@Override
	protected Object before(Method method, Object[] args) {
		System.out.println("print TestAopHandler.before");
		return null;
	};

	@Override
	protected void after(Method method, Object[] args, Object returnObj) {
		System.out.println("print TestAopHandler.after");
	}
}

class TestStopAopHandler extends Aop<Subject> {
	@Override
	protected Object before(Method method, Object[] args) {
		System.out.println("print TestStopAopHandler.before");
		return new ReturnBefore(null);
	}

	@Override
	protected void after(Method method, Object[] args, Object returnObj) {
		System.out.println("print TestStopAopHandler.after");
	}
}