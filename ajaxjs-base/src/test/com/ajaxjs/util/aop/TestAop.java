package test.com.ajaxjs.util.aop;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.ajaxjs.util.aop.Aop;

public class TestAop {
	@Test
	public void testProxy() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("hihi", "Jack");

		@SuppressWarnings("unchecked")
		Map<String, String> newMap = (Map<String, String>) Aop.proxy(map, Map.class, new Aop.ProxyCallback() {
			@Override
			public boolean before(Object instance, String methodName, Object... objects) {
				boolean isGo_ON = true;
				System.out.println(methodName);
				return isGo_ON;
			}

			@Override
			public Object after(Object instance, String methodName, Object returnValue, Object... objects) {
				return returnValue;
			}
		});

		String value = newMap.get("hihi");
		System.out.println(value);
		assertNotNull(value);
	}
	
	Subject subject, stopSubject;
	
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