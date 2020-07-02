package com.ajaxjs.framework;

import java.util.function.Consumer;

import javax.servlet.ServletContextEvent;

import org.junit.Test;

import com.ajaxjs.util.ReflectUtil;
import com.ajaxjs.util.ioc.EveryClass;

public class TestComponent {
	public class Foo implements IComponent {
		Consumer<ServletContextEvent> onServletStartUp = e -> {

		};
	}

	@Test
	public void test() {
		EveryClass scan = new EveryClass();
		scan.scan("com", resource -> {
			if (resource.contains("SetStartupCtx"))
				ReflectUtil.getClassByName(resource);
		});
	}
}
