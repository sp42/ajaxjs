package demo.base.aop;

import java.lang.reflect.Method;

import com.ajaxjs.ioc.aop.Aop;
import com.ajaxjs.ioc.aop.AopHandler;
import com.ajaxjs.util.logger.LogHelper;

public class TestAop {
	static class CacheHandler implements AopHandler<Subject> {
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

	static class RightHandler implements AopHandler<Subject> {
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
	public static void main(String[] args) {
		Subject subject = new MyClass();
		subject = new Aop<Subject>().bind(subject, new CacheHandler(), new CacheHandler());
		subject.doIt();
	}
}