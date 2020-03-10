package com.ajaxjs.ioc.aop;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Method;

import org.junit.Test;

import com.ajaxjs.ioc.testcase.Subject;
import com.ajaxjs.util.logger.LogHelper;

public class TestAop {
	Subject subject, stopSubject;

	class CacheHandler implements AopHandler<Subject> {
		private final LogHelper LOGGER = LogHelper.getLog(CacheHandler.class);

		@Override
		public Object before(Subject target, Method method, String methodName, Object[] args) throws Throwable {
			LOGGER.info("print CacheHandler.before");
			return null;
		}

		@Override
		public void after(Subject target, Method method, String methodName, Object[] args, Object returnObj) {
			LOGGER.info("print CacheHandler.after");
		}
	}

	class RightHandler implements AopHandler<Subject>  {
		private final LogHelper LOGGER = LogHelper.getLog(RightHandler.class);

		@Override
		public Object before(Subject target, Method method, String methodName, Object[] args) {
			LOGGER.info("print RightHandler.before");
			return null;
		}

		@Override
		public void after(Subject target, Method method, String methodName, Object[] args, Object returnObj) {
			LOGGER.info("print RightHandler.after");
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testChain() {
		subject = new Aop<Subject>().bind(new BaseTest(), new TestHandler.TestAopHandler(), new TestHandler.TestStopAopHandler(), new RightHandler());
		assertNotNull(subject);
		subject.doIt();
	}

	@SuppressWarnings("rawtypes")
	public class DummySub implements AopHandler {
		@Override
		public Object before(Object target, Method method, String methodName, Object[] args) throws Throwable {
			return null;
		}

		@Override
		public void after(Object target, Method method, String methodName, Object[] args, Object returnObj) {
		}
	}
	
	@Test
	public void testMisc() {
		DummySub d = new DummySub();
		
		try {
			assertEquals(null, d.before(null, null, null, null));
		} catch (Throwable e) {
		}

		d.after(null, null, null, null, null);
		
		ReturnAsArg r = new ReturnAsArg(new Object[]{});
		assertNotNull(r.getArgs());
		
		new ReturnAsArg(new Object());
	}
}