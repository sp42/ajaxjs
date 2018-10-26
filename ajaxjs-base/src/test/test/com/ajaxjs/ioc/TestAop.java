package test.com.ajaxjs.ioc;

import static org.junit.Assert.*;

import java.lang.reflect.Method;

import org.junit.Test;

import com.ajaxjs.ioc.Aop;
import com.ajaxjs.ioc.AopException;
import com.ajaxjs.ioc.ReturnAsArg;
import com.ajaxjs.util.logger.LogHelper;

public class TestAop {

	Subject subject, stopSubject;

	class CaccheHandler extends Aop<Subject> {
		private final LogHelper LOGGER = LogHelper.getLog(CaccheHandler.class);

		@Override
		public Object before(Subject target, Method method, String methodName, Object[] args) throws Throwable {
			LOGGER.info("print CaccheHandler.before");
			return null;
		}

		@Override
		public void after(Subject target, Method method, String methodName, Object[] args, Object returnObj) {
			LOGGER.info("print CaccheHandler.after");

		}
	}

	class RightHandler extends Aop<Subject> {
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

	@Test
	public void testChain() {
		subject = Aop.chain(new BaseTest(), new TestHandler.TestAopHandler(), new TestHandler.TestStopAopHandler(), new RightHandler());
		assertNotNull(subject);
		subject.doIt();
	}

	@SuppressWarnings("rawtypes")
	public class DummySub extends Aop {

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
		
		new AopException(null, null);
	}
}