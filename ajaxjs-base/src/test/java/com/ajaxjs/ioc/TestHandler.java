package com.ajaxjs.ioc;

import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;

import com.ajaxjs.ioc.Aop;
import com.ajaxjs.ioc.annotation.ReturnBefore;
import com.ajaxjs.ioc.testcase.Subject;
import com.ajaxjs.util.logger.LogHelper;

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

	static class TestAopHandler extends Aop<Subject> {
		private final LogHelper LOGGER = LogHelper.getLog(TestAopHandler.class);
		
		@Override
		public Object before(Subject target, Method method, String methodName, Object[] args) throws Throwable {
			LOGGER.info("print TestAopHandler.before");
			return null;
		}
		
		@Override
		public void after(Subject target, Method method, String methodName, Object[] args, Object returnObj) {
			LOGGER.info("print TestAopHandler.after");
		}
	}
	
	static class TestStopAopHandler extends Aop<Subject> {
		private final LogHelper LOGGER = LogHelper.getLog(TestStopAopHandler.class);
		
		@Override
		public Object before(Subject target, Method method, String methodName, Object[] args) throws Throwable {
			LOGGER.info("print TestStopAopHandler.before");
			return new ReturnBefore(null);
		}
		
		@Override
		public void after(Subject target, Method method, String methodName, Object[] args, Object returnObj) {
			LOGGER.info("print TestStopAopHandler.after");
		}
	}
}
